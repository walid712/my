/**
 * Removes only the ten demo provider documents created by the old app seeder.
 *
 * Usage:
 *   GOOGLE_APPLICATION_CREDENTIALS=/path/to/service-account.json node scripts/remove_demo_seed_providers.js
 *   ... add --apply to actually delete (default is dry-run).
 *
 * Run from the project root. Back up Firestore before applying.
 */
const admin = require("firebase-admin");

const DEMO_PROVIDER_IDS = [
  "seed_marrakech_zellige",
  "seed_casablanca_plumbing",
  "seed_madrid_carpentry",
  "seed_paris_electrical",
  "seed_dubai_hvac",
  "seed_nyc_appliance",
  "seed_cdmx_metalwork",
  "seed_tokyo_joinery",
  "seed_bogota_painting",
  "seed_sydney_garden",
];

async function main() {
  if (!admin.apps.length) admin.initializeApp();
  const db = admin.firestore();
  const apply = process.argv.includes("--apply");

  for (const id of DEMO_PROVIDER_IDS) {
    const providerRef = db.collection("providers").doc(id);
    const snapshot = await providerRef.get();
    if (!snapshot.exists) {
      console.log(`[skip] ${id}: not found`);
      continue;
    }

    const reviews = await providerRef.collection("reviews").get();
    console.log(`[${apply ? "delete" : "dry-run"}] ${id} and ${reviews.size} nested review(s)`);

    if (apply) {
      const batch = db.batch();
      reviews.docs.forEach((review) => batch.delete(review.ref));
      batch.delete(providerRef);
      await batch.commit();
    }
  }

  console.log(apply ? "Demo seed cleanup complete." : "Dry-run only. Add --apply after backing up Firestore.");
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
