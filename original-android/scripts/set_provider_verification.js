#!/usr/bin/env node
/**
 * Privileged operator tool. Run only on a trusted workstation/server.
 * Requires GOOGLE_APPLICATION_CREDENTIALS for a Firebase service account.
 * Usage: node scripts/set_provider_verification.js <providerUid> <tier>
 */
const admin = require("firebase-admin");
const { getFirestore } = require("firebase-admin/firestore");

const [providerId, tier] = process.argv.slice(2);
const allowed = new Set(["unverified", "id_verified", "master_artisan", "insured_pro"]);
if (!providerId || !allowed.has(tier)) {
  console.error("Usage: node scripts/set_provider_verification.js <providerUid> <unverified|id_verified|master_artisan|insured_pro>");
  process.exit(2);
}

const app = admin.initializeApp({ credential: admin.credential.applicationDefault() });
const databaseId = process.env.FIRESTORE_DATABASE_ID;
const db = databaseId ? getFirestore(app, databaseId) : getFirestore(app);

(async () => {
  const ref = db.collection("providers").doc(providerId);
  const snapshot = await ref.get();
  if (!snapshot.exists) throw new Error("Provider profile not found.");
  await ref.update({
    verificationTier: tier,
    updatedAt: admin.firestore.FieldValue.serverTimestamp(),
  });
  console.log(`Updated ${providerId} verificationTier to ${tier}.`);
  process.exit(0);
})().catch((error) => {
  console.error(error.message || error);
  process.exit(1);
});
