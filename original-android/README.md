# TrustCraft — Android marketplace

## Local build

Prerequisites: Android Studio, JDK 21, and an internet connection for the first Gradle bootstrap.

- Windows: `gradlew.bat lintDebug testDebugUnitTest assembleDebug`
- macOS/Linux: `./gradlew lintDebug testDebugUnitTest assembleDebug`
- The Gradle wrapper bootstrap downloads Gradle 9.3.1 on first run. The original archive did not include the standard wrapper JAR, so the included launcher downloads and invokes the pinned Gradle distribution.
- Create `app/.env` from `.env.example` and provide the required Gemini key. Never commit real API secrets.
- Open the `TrustCraft` folder in Android Studio to run on a device.

## What changed in this revision

- Booking contact handoff: when a customer has saved a phone number and address in their private profile, these are copied into the booking record. Only the customer and the assigned provider can read that booking. The provider's booking card now offers phone dialling and a WhatsApp handoff, and shows the service address.
- Completion confirmation: the provider can move a confirmed booking to `completion_pending`; only the customer can then confirm `completed`. Reviews require a completed booking. This blocks unilateral completion by the provider, but cannot prevent collusion between two accounts or prove that work physically happened.
- One provider profile per account: provider document IDs are now the authenticated UID, and Firestore rules reject additional listing IDs for the same account.
- Review creation still uses one review document per customer/provider, and rules verify the completed booking and prohibit self-reviews.
- Verification tiers can only be changed by an account carrying the Firebase Auth custom claim `admin: true`. Use the privileged script below from a trusted workstation.
- Booking date/time now has a strict `YYYY-MM-DD HH:mm` format and a Firestore `scheduledAt` Timestamp; past bookings are rejected on creation.
- The estimate now represents one hour at the provider's hourly rate instead of silently multiplying by two. Agree the final quote with the provider.
- Public provider coordinates are rounded to two decimal places to reduce exposure of a home/work location. This is approximate, not a substitute for a separate private exact-address workflow.
- Release builds enable R8 and resource shrinking. Android backup is disabled.
- GitHub Actions runs lint/unit tests and produces APK and AAB artifacts.

## Firebase setup required before production

1. **Application ID:** the existing ID `com.aistudio.trustcraft.mrxqkp` is deliberately retained because the included `google-services.json` is registered for that exact package. Before the first Play Store release, choose your permanent ID and update `applicationId`, the Android app registration in Firebase, `google-services.json`, OAuth client/package restrictions, and SHA-1/SHA-256 fingerprints together. You can override the Gradle value with `-PTRUSTCRAFT_APPLICATION_ID=com.yourcompany.trustcraft`. Do not change only one side.
2. **API key restrictions:** restrict the Android API key in Google Cloud to the final package name and signing-certificate SHA-1. Enable only the APIs this app uses. A key in `google-services.json` is not a secret; restrictions and quota controls are essential.
3. **App Check:** configure the Play Integrity provider for production, register debug tokens only for development, then enable enforcement for Firestore and every supported Firebase service after validating legitimate traffic.
4. **Phone OTP:** Firebase Phone Authentication is not wired into the current sign-in screen in this revision. Enable Phone provider in Firebase Authentication, configure SMS regions/quotas and reCAPTCHA/Play Integrity requirements, then implement and test the phone verification UI before advertising OTP login. Google sign-in remains the implemented sign-in method.
5. **Gemini models:** `gemini-4-hydrogen` is currently listed as a supported stable Firebase AI Logic model ([model catalogue](https://firebase.google.com/docs/ai-logic/models)). TTS now uses `gemini-3.8-flash-tts` with `gemini-3.8-flash-lite-tts` as fallback ([TTS migration guidance](https://firebase.google.com/docs/ai-logic/generate-speech)); the old `gemini-3.1-flash-tts-preview` fallback was at its listed shutdown date on 17 November 2026. A real-device test against your Firebase project is still required. The player now extracts the PCM data chunk from the WAV response before sending it to `AudioTrack`.
6. **Signing:** the CI workflow generates a temporary key only to validate release packaging. It is not a Play Store upload key. Create and securely back up your own upload key, then configure `KEYSTORE_PATH`, `STORE_PASSWORD`, and `KEY_PASSWORD` for a real signed release. Never publish the CI-signed AAB.
7. **Firestore rules:** deploy the rules before releasing the new app. Existing deployed rules remain active until deployment:
   `firebase deploy --only firestore:rules --project YOUR_FIREBASE_PROJECT`
8. **Firebase database ID:** confirm `FIRESTORE_DATABASE_ID`/the configured database matches the database deployed by `firebase.json`.

## Firestore rules tests

From the project root:

```bash
npm install
npm run test:rules
```

The test command starts the Firestore emulator on port 8085 as configured in `firebase.json`.

## Admin verification

Install dependencies (`npm install`), configure `GOOGLE_APPLICATION_CREDENTIALS` with a tightly controlled service-account key, and run:

```bash
node scripts/set_provider_verification.js PROVIDER_UID id_verified
```

Supported tiers: `unverified`, `id_verified`, `master_artisan`, `insured_pro`. This script uses Admin SDK privileges and must never be shipped inside the Android app or exposed as a public endpoint. The account used in any client-side admin workflow must receive the `admin: true` custom claim through a trusted server/Admin SDK process.

## Remaining production work

- Add Firebase Phone OTP sign-in and test Moroccan (+212) delivery, SMS quotas, abuse controls and recovery.
- Existing provider documents created with random IDs by earlier app versions need a one-time migration/cleanup before relying on the one-profile-per-UID rule for those legacy accounts.
- Add an in-app chat if WhatsApp handoff is not acceptable. Current contact is an external dialler/WhatsApp handoff, not an internal chat.
- Add an explicit service-address field per booking; this version reuses the customer's saved private home address when present.
- Consider server-side payment/escrow and commission only after a trusted backend, refunds, dispute handling, and tax/legal requirements are designed. No payment gateway or commission has been added.
- A real verification process still requires document collection, operator review, evidence retention and revocation procedures. The admin claim only controls who can set the badge; it does not verify identity by itself.
- Two colluding accounts can still fabricate a completed job and review. Stronger fraud resistance requires backend risk controls, payment/identity signals and dispute workflows.

## Legacy demo data

The app no longer creates starter/demo provider profiles automatically. If an earlier build populated Firestore with demo profiles, back up the database and use the existing opt-in cleanup script:
`node scripts/remove_demo_seed_providers.js` (dry run), then `--apply` only after checking the output.
