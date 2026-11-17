const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (
  process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085"
).split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

function validProviderPayload(ownerId, overrides = {}) {
  const now = new Date(Date.now() - 2000);
  return {
    ownerId,
    businessName: "Atelier Atlas Zellige",
    tradeCategory: "zellige",
    serviceKind: "artisan",
    bio: "Traditional hand-cut Moroccan tilework and fountain restoration worldwide.",
    city: "Marrakech",
    country: "Morocco",
    continent: "africa",
    latitude: 31.6295,
    longitude: -7.9811,
    hourlyRate: 45.0,
    currency: "USD",
    languages: ["ar", "fr", "en"],
    rating: 0,
    reviewCount: 0,
    yearsExperience: 14,
    verificationTier: "unverified",
    availableToday: true,
    published: true,
    createdAt: now,
    updatedAt: now,
    ...overrides,
  };
}

test("Unauthenticated users cannot read or write any collection", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(
    unauthDb.collection("providers").where("published", "==", true).get()
  );
  await assertFails(
    unauthDb.collection("providers").doc("prov_1").set(validProviderPayload(ALICE_UID))
  );
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
  await assertFails(
    unauthDb.collection("bookings").where("clientId", "==", ALICE_UID).get()
  );
});

test("User summary and PII private isolation", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const now = new Date(Date.now() - 2000);

  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      displayName: "Alice",
      preferredLanguage: "ar",
      preferredCity: "Casablanca",
      preferredCountry: "Morocco",
      createdAt: now,
      updatedAt: now,
    })
  );

  await assertSucceeds(
    aliceDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("private")
      .doc("info")
      .set({
        userId: ALICE_UID,
        email: "alice@example.com",
        phone: "+212600000000",
        homeAddress: "Anfa, Casablanca",
        createdAt: now,
        updatedAt: now,
      })
  );

  // Bob cannot read Alice's summary or PII
  await assertFails(bobDb.collection("users").doc(ALICE_UID).get());
  await assertFails(
    bobDb
      .collection("users")
      .doc(ALICE_UID)
      .collection("private")
      .doc("info")
      .get()
  );
});

test("Provider listing: published filtered query succeeds, unfiltered query and shadow keys fail", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();

  // Valid create
  await assertSucceeds(
    aliceDb
      .collection("providers")
      .doc(ALICE_UID)
      .set(validProviderPayload(ALICE_UID))
  );

  // Reject ghost @DocumentId `id` field
  await assertFails(
    aliceDb
      .collection("providers")
      .doc("prov_ghost")
      .set(validProviderPayload(ALICE_UID, { id: "prov_ghost" }))
  );

  // Filtered published query succeeds for Bob
  await assertSucceeds(
    bobDb.collection("providers").where("published", "==", true).get()
  );

  // Unfiltered query on top-level collection fails
  await assertFails(bobDb.collection("providers").get());

  // One profile per account: a second listing under another ID is rejected.
  await assertFails(
    aliceDb.collection("providers").doc("prov_spam").set(validProviderPayload(ALICE_UID))
  );

  // Bob cannot modify Alice's provider listing
  await assertFails(
    bobDb.collection("providers").doc(ALICE_UID).update({
      hourlyRate: 1.0,
      updatedAt: new Date(Date.now() - 1000),
    })
  );
});

test("Provider ratings cannot be self-declared or modified by clients", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(
    aliceDb.collection("providers").doc(ALICE_UID).set(
      validProviderPayload(ALICE_UID, { rating: 5, reviewCount: 100 })
    )
  );
  await assertFails(
    aliceDb.collection("providers").doc(ALICE_UID).set(
      validProviderPayload(ALICE_UID, { verificationTier: "master_artisan" })
    )
  );
  await assertSucceeds(
    aliceDb.collection("providers").doc(ALICE_UID).set(
      validProviderPayload(ALICE_UID, { rating: 0, reviewCount: 0 })
    )
  );
  await assertFails(
    aliceDb.collection("providers").doc(ALICE_UID).update({
      rating: 5,
      reviewCount: 100,
      updatedAt: new Date(Date.now() - 1000),
    })
  );
});

test("Only a privileged admin claim can change provider verification tier", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const adminDb = testEnv.authenticatedContext("moderator_1", { admin: true }).firestore();
  await assertSucceeds(
    aliceDb.collection("providers").doc(ALICE_UID).set(validProviderPayload(ALICE_UID))
  );
  await assertFails(
    aliceDb.collection("providers").doc(ALICE_UID).update({
      verificationTier: "id_verified",
      updatedAt: new Date(Date.now() - 1000),
    })
  );
  await assertSucceeds(
    adminDb.collection("providers").doc(ALICE_UID).update({
      verificationTier: "id_verified",
      updatedAt: new Date(Date.now() - 1000),
    })
  );
});

test("Reviews are one-per-user, non-self-authored and ratings are bounded", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const now = new Date(Date.now() - 2000);
  await assertSucceeds(
    aliceDb.collection("providers").doc(ALICE_UID).set(validProviderPayload(ALICE_UID))
  );
  await assertSucceeds(
    bobDb.collection("bookings").doc("completed_booking").set({
      clientId: BOB_UID,
      clientName: "Bob",
      providerId: ALICE_UID,
      providerName: "Atelier Atlas Zellige",
      providerOwnerId: ALICE_UID,
      tradeCategory: "zellige",
      serviceTitle: "Tile restoration",
      scheduledDate: "2026-11-25 10:00",
      scheduledAt: new Date(Date.now() + 5 * 24 * 60 * 60 * 1000),
      city: "Marrakech",
      estimatedPrice: 180,
      currency: "USD",
      status: "pending",
      createdAt: now,
      updatedAt: now,
    })
  );
  await assertSucceeds(
    aliceDb.collection("bookings").doc("completed_booking").update({
      status: "confirmed",
      updatedAt: new Date(Date.now() - 1000),
    })
  );
  await assertSucceeds(
    aliceDb.collection("bookings").doc("completed_booking").update({
      status: "completion_pending",
      updatedAt: new Date(Date.now() - 500),
    })
  );
  await assertSucceeds(
    bobDb.collection("bookings").doc("completed_booking").update({
      status: "completed",
      updatedAt: new Date(Date.now() - 250),
    })
  );
  await assertFails(
    bobDb.collection("providers").doc(ALICE_UID).collection("reviews").doc(BOB_UID).set({
      providerId: ALICE_UID,
      bookingId: "missing_booking",
      authorId: BOB_UID,
      authorName: "Bob",
      rating: 5,
      comment: "Unverified review",
      language: "en",
      published: true,
      createdAt: now,
      updatedAt: now,
    })
  );
  const review = {
    providerId: ALICE_UID,
    bookingId: "completed_booking",
    authorId: BOB_UID,
    authorName: "Bob",
    rating: 5,
    comment: "Excellent work",
    language: "en",
    published: true,
    createdAt: now,
    updatedAt: now,
  };
  await assertSucceeds(
    bobDb.collection("providers").doc(ALICE_UID).collection("reviews").doc(BOB_UID).set(review)
  );
  await assertFails(
    bobDb.collection("providers").doc(ALICE_UID).collection("reviews").doc("second_review").set(review)
  );
  await assertFails(
    aliceDb.collection("providers").doc(ALICE_UID).collection("reviews").doc(ALICE_UID).set({
      ...review, authorId: ALICE_UID
    })
  );
});

test("Booking lifecycle and terminal state locking", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobDb = testEnv.authenticatedContext(BOB_UID).firestore();
  const now = new Date(Date.now() - 2000);

  await assertSucceeds(
    aliceDb
      .collection("providers")
      .doc(ALICE_UID)
      .set(validProviderPayload(ALICE_UID))
  );

  // A client cannot nominate a fake provider owner.
  await assertFails(
    bobDb.collection("bookings").doc("forged_booking").set({
      clientId: BOB_UID,
      clientName: "Bob",
      providerId: ALICE_UID,
      providerName: "Atelier Atlas Zellige",
      providerOwnerId: BOB_UID,
      tradeCategory: "zellige",
      serviceTitle: "Courtyard tile restoration",
      scheduledDate: "2026-11-20 10:00",
      scheduledAt: new Date(Date.now() + 3 * 24 * 60 * 60 * 1000),
      city: "Marrakech",
      estimatedPrice: 180.0,
      currency: "USD",
      status: "pending",
      createdAt: now,
      updatedAt: now,
    })
  );

  // Bob books Alice's provider
  await assertSucceeds(
    bobDb.collection("bookings").doc("book_1").set({
      clientId: BOB_UID,
      clientName: "Bob",
      providerId: ALICE_UID,
      providerName: "Atelier Atlas Zellige",
      providerOwnerId: ALICE_UID,
      tradeCategory: "zellige",
      serviceTitle: "Courtyard tile restoration",
      scheduledDate: "2026-11-20 10:00",
      scheduledAt: new Date(Date.now() + 3 * 24 * 60 * 60 * 1000),
      city: "Marrakech",
      notes: "Need traditional mosaic quote",
      clientPhone: "+212600000000",
      serviceAddress: "Quartier Hassan, Rabat",
      estimatedPrice: 180.0,
      currency: "USD",
      status: "pending",
      createdAt: now,
      updatedAt: now,
    })
  );
  await assertSucceeds(aliceDb.collection("bookings").doc("book_1").get());
  await assertSucceeds(bobDb.collection("bookings").doc("book_1").get());
  const eveDb = testEnv.authenticatedContext("eve_789").firestore();
  await assertFails(eveDb.collection("bookings").doc("book_1").get());

  // Provider confirms the job, but cannot unilaterally mark it completed.
  await assertSucceeds(
    aliceDb.collection("bookings").doc("book_1").update({
      status: "confirmed",
      updatedAt: new Date(Date.now() - 1000),
    })
  );
  await assertFails(
    aliceDb.collection("bookings").doc("book_1").update({
      status: "completed",
      updatedAt: new Date(Date.now() - 750),
    })
  );
  await assertSucceeds(
    aliceDb.collection("bookings").doc("book_1").update({
      status: "completion_pending",
      updatedAt: new Date(Date.now() - 500),
    })
  );
  await assertSucceeds(
    bobDb.collection("bookings").doc("book_1").update({
      status: "completed",
      updatedAt: new Date(Date.now() - 250),
    })
  );

  // Terminal state lock: cannot update after completed
  await assertFails(
    bobDb.collection("bookings").doc("book_1").update({
      status: "cancelled",
      updatedAt: new Date(Date.now() - 100),
    })
  );
});
