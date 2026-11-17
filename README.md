# TrustCraft PWA — cross-platform web edition

This folder contains a responsive Progressive Web App edition of the supplied TrustCraft Android project. It is designed for Android phones, iPhones, tablets and desktop browsers.

## Implemented in this web edition

- Responsive mobile-first layout and Arabic RTL, English, French and Spanish language switching.
- Firebase Google sign-in (requires Firebase Web app / OAuth domain setup).
- Firestore provider discovery, text search and service/category/continent filters.
- Provider profiles, public reviews, favourites and provider listing publication.
- Booking requests, customer/provider booking lists and the original booking status flow.
- Private profile contact information stored under `users/{uid}/private/info`.
- OpenStreetMap / Leaflet provider map and browser geolocation.
- PWA manifest, install icons, standalone display and service-worker app-shell caching.
- Browser speech synthesis for reading provider descriptions.

## Important: connect the Firebase Web app before use

The supplied archive contains an Android Firebase registration, not a complete Web app registration. The Firebase project ID, public API key, database ID and Firestore rules were carried over, but a web app registration and its allowed domain must be configured.

1. Open the Firebase Console for project `gen-lang-client-0782344491`.
2. Add/register a **Web app** in Project settings.
3. Copy the Web app config and replace the `firebaseConfig` object near the top of `app.js` (especially `appId`; add it from the Web config).
4. In Authentication → Sign-in method, enable Google.
5. In Authentication → Settings → Authorized domains, add the domain where you will host this PWA (and `localhost` for local development).
6. Confirm Firestore database `ai-studio-android-trustcra-af6ee663-7792-465c-abb8-86f9a5cbb138` is available and deploy the included `firestore.rules` to that database.
7. Test Google sign-in, provider listing, favourites and bookings with separate customer and provider accounts before launch.

The Firebase API key in a web app config is a public client identifier, not a server secret. Keep Firestore rules restrictive and configure API restrictions / quotas in Google Cloud.

## Run locally

A PWA must be served from HTTPS or localhost; opening `index.html` directly with `file://` will not enable service workers or installation.

With Python installed:

```bash
python -m http.server 8080
```

Then open `http://localhost:8080` in a browser. Firebase Auth may require `localhost` to be in Authorized domains.

## Deploy

Upload the contents of this folder to any static host with HTTPS (for example Firebase Hosting, Cloudflare Pages, Netlify or another HTTPS static host). The `manifest.webmanifest`, `sw.js`, `index.html`, `app.js`, `styles.css` and `icons/` directory must remain at the same site root. Configure SPA/static fallback to `index.html` if your host requires it.

### Install on Android

Open the HTTPS site in Chrome, then choose **Install app** / **Add to Home screen** from the browser menu (or use the in-page install prompt when available).

### Install on iPhone

Open the HTTPS site in Safari → Share → **Add to Home Screen**. iOS does not expose the same JavaScript install prompt as Chromium browsers.

## Current parity limits — read before production

This is a functional web port of the main marketplace workflows, not a byte-for-byte or feature-complete replacement of the native Android app. The supplied Android project contains additional native integrations that need separate web configuration and real-device testing:

- The original Gemini Search grounding, Gemini Maps grounding and Gemini TTS pipeline are not enabled in this web build. The Concierge panel currently explains the limitation and provides basic planning guidance; provider-description reading uses the browser's built-in speech synthesis.
- The map uses OpenStreetMap/Leaflet and approximate provider coordinates, rather than the native Google Maps SDK and Gemini Maps grounding.
- Firebase Phone OTP was not implemented in the supplied Android app either.
- Offline support caches the app shell only. Authentication, Firestore data, maps and external services still require a connection.
- The PWA has not been deployed to a public HTTPS domain or tested on physical Android/iPhone devices from this environment.

The original Android source project is included separately in `original-android/` so the existing native app is preserved. Do not remove the original Android Firestore rules or replace them with permissive rules.

## Source project

`original-android/` is the original uploaded Android project, retained for reference and future parity work. The PWA uses the same Firestore collection/document structure and includes the project's `firestore.rules`.
