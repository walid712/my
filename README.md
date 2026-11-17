# Mizan Maroc — Android natif (العربية / Français)

Application Android native construite avec Kotlin et Jetpack Compose. Application ID retenu pour cette version : `ma.mizanmaroc.app` (à conserver pour toutes les mises à jour futures). L'interface propose l'arabe (RTL) et le français (LTR), un choix de langue mémorisé, et suit la langue du téléphone au premier démarrage.

## Fonctionnalités présentes

- Accueil et recherche des démarches.
- Guides bilingues des procédures et listes de pièces à cocher, sauvegardées localement.
- Modèles de lettres bilingues avec remplissage, copie et partage.
- Répertoire des villes, repères administratifs et numéros utiles.
- Liens directs vers les sources institutionnelles.
- Thème clair/sombre/système et navigation adaptative téléphone/tablette.

## Environnement

| Élément | Version |
|---|---:|
| Android Gradle Plugin | 9.1.1 |
| Gradle | 9.3.1 |
| JDK pour Gradle | 17 minimum (17 recommandé) |
| Android SDK | API 36 |
| Build Tools | 36.0.0 |
| minSdk | 24 (Android 7.0) |
| targetSdk | 36 |
| Bytecode Java/Kotlin | 17 |

La matrice Android indique Gradle 9.3.1 et JDK 17 pour AGP 9.1.1. Consultez les liens officiels à la fin de ce fichier.

## Générer APK et AAB sur Windows

1. Installer Android Studio récent, JDK 17 et Android SDK Platform 36.
2. Dans SDK Manager, installer Android SDK Build-Tools 36.0.0 et Android SDK Platform-Tools.
3. Décompresser le projet et ouvrir le dossier qui contient `settings.gradle.kts`.
4. Attendre la synchronisation Gradle et accepter les licences SDK.
5. Ouvrir PowerShell à la racine et lancer :

```powershell
.\scripts\build-and-test.ps1
```

Le script exécute les tests unitaires, lint, construit l'APK debug et le bundle AAB debug, puis copie les fichiers dans `artifacts/`. Au premier lancement, Gradle est téléchargé depuis le serveur officiel et son SHA-256 est vérifié.

## Générer APK et AAB sur Linux/macOS

```bash
chmod +x gradlew scripts/build-and-test.sh
./scripts/build-and-test.sh
```

## Fichiers générés

- `artifacts/mizan-maroc-debug.apk` : installable directement sur un téléphone pour les tests.
- `artifacts/mizan-maroc-debug.aab` : bundle de test signé avec la clé debug ; ce n'est pas le fichier à publier sur Google Play.

Un AAB destiné à Google Play doit être signé avec une clé de publication privée. Pour produire les variantes release signées, configurer localement les variables suivantes (ne jamais committer le keystore ni ses mots de passe) :

- `KEYSTORE_PATH` : chemin du keystore.
- `STORE_PASSWORD` : mot de passe du keystore.
- `KEY_PASSWORD` : mot de passe de la clé.
- `KEY_ALIAS` : alias (par défaut `upload`).

Puis relancer le même script. Il produira également `artifacts/mizan-maroc-release.apk` et `artifacts/mizan-maroc-release.aab`. Conservez une sauvegarde sécurisée du keystore : sans lui, les mises à jour signées avec cette clé ne pourront pas être publiées.

## Test sur un vrai téléphone Android

1. Activer les options développeur et le débogage USB.
2. Brancher le téléphone et accepter l'empreinte RSA.
3. Vérifier `adb devices -l` et que le téléphone affiche l'état `device`.
4. Définir `MIZAN_RUN_DEVICE_TESTS=1`, puis relancer le script.

PowerShell :

```powershell
$env:MIZAN_RUN_DEVICE_TESTS='1'
.\scripts\build-and-test.ps1
```

Linux/macOS :

```bash
MIZAN_RUN_DEVICE_TESTS=1 ./scripts/build-and-test.sh
```

Le script installe l'APK debug et exécute les tests UI instrumentés. Aucun test ne doit déclencher un appel d'urgence réel.

## Données personnelles et prudence juridique

Les brouillons des lettres sont gardés en mémoire pendant l'utilisation et ne sont pas synchronisés avec un serveur. Les préférences et cases à cocher sont stockées localement avec DataStore. La sauvegarde Android automatique est désactivée dans cette version afin de limiter la sortie de données de l’appareil. Les liens web s'ouvrent dans une application externe. Les fiches juridiques sont des guides indicatifs : chaque tarif, délai, document et numéro doit être revérifié auprès de l'administration compétente avant publication. Un lien institutionnel ne prouve pas automatiquement tous les détails de la fiche.

## Documentation officielle

- AGP 9.1.1 et compatibilité : https://developer.android.com/build/releases/agp-9-1-0-release-notes
- Matrice AGP / Gradle : https://developer.android.com/build/releases/about-agp
- JDK dans Android builds : https://developer.android.com/build/jdks
- Localisation Android : https://developer.android.com/guide/topics/resources/localization
- Construire APK/AAB : https://developer.android.com/build/building-cmdline
- Tester un App Bundle : https://developer.android.com/guide/app-bundle/test

---

# ميزان المغرب — تعليمات مختصرة بالعربية

يدعم التطبيق العربية والفرنسية. عند أول تشغيل يحاول احترام لغة الهاتف (العربية أو الفرنسية)، ويمكن تغيير اللغة من داخل التطبيق، ويُحفظ اختيارك محلياً.

## بناء APK وAAB

على Windows افتح PowerShell في مجلد المشروع وشغّل:

```powershell
.\scripts\build-and-test.ps1
```

على Linux/macOS:

```bash
./scripts/build-and-test.sh
```

بعد نجاح البناء ستجد: `artifacts/mizan-maroc-debug.apk` للتثبيت المباشر، و`artifacts/mizan-maroc-debug.aab` لحزمة الاختبار. نسخة Debug ليست نسخة نشر Google Play. للنشر يجب إعداد مفتاح توقيع Release خاص وآمن عبر متغيرات البيئة الموضحة أعلاه.

## تجربة الهاتف الحقيقي

فعّل USB debugging، وصل الهاتف بالحاسوب، وافق على رسالة RSA، ثم شغّل السكريبت مع `MIZAN_RUN_DEVICE_TESTS=1`. لا تنفّذ أي اتصال حقيقي بأرقام الطوارئ أثناء الاختبار.

**تنبيه:** التطبيق دليل إرشادي وليس استشارة قانونية. يجب التأكد من الرسوم والآجال والوثائق وأرقام الهاتف من الجهة الرسمية قبل الاعتماد عليها.

## CI GitHub Actions

Le workflow `.github/workflows/android.yml` exécute `lintDebug`, `testDebugUnitTest`, `assembleDebug` et `bundleDebug`, publie les deux artefacts et exécute les tests instrumentés sur un émulateur Android. Les builds Release signés sont réservés aux tags `v*` et nécessitent les secrets GitHub `ANDROID_KEYSTORE_BASE64`, `ANDROID_STORE_PASSWORD`, `ANDROID_KEY_PASSWORD`, `ANDROID_KEY_ALIAS`.

**Gradle Wrapper :** le dépôt inclut les scripts `gradlew`/`gradlew.bat`, `gradle-wrapper.properties`, le SHA-256 officiel de la distribution Gradle et le SHA-256 officiel attendu du Wrapper JAR. Le binaire `gradle-wrapper.jar` ne peut pas être incorporé depuis cet environnement hors-ligne. Le workflow CI génère le Wrapper depuis Gradle 9.3.1 officiel, puis vérifie son JAR avec `gradle/actions/wrapper-validation@v4`. Le script de build local génère automatiquement le Wrapper manquant après avoir amorcé Gradle 9.3.1, puis compare le JAR au SHA-256 officiel dans `gradle/wrapper/gradle-wrapper.jar.sha256`.

## Revue juridique et confidentialité

Voir `docs/LEGAL_REVIEW_REGISTER.md`, `docs/PRIVACY_POLICY_AR_FR.md` et `docs/TERMS_OF_USE_AR_FR.md`. Les champs de date de vérification indépendante restent vides tant que chaque tarif, délai et pièce n'a pas été contrôlé individuellement. Le nom légal de l'éditeur et son contact doivent être fournis avant publication publique.

### Clé de publication Android

Créez votre clé de téléversement localement et gardez le fichier `.jks` hors du dépôt :

```bash
keytool -genkeypair -v -keystore mizan-upload.jks -keyalg RSA -keysize 4096 -validity 10000 -alias upload
base64 -w 0 mizan-upload.jks > mizan-upload.jks.base64
```

Dans GitHub Settings → Secrets and variables → Actions, ajoutez `ANDROID_KEYSTORE_BASE64`, `ANDROID_STORE_PASSWORD`, `ANDROID_KEY_PASSWORD` et `ANDROID_KEY_ALIAS`. Sur macOS, utilisez `base64 < mizan-upload.jks | tr -d '\n'`. Ne transmettez jamais la clé ou les mots de passe dans un chat, un commit ou un artefact public. Activez Play App Signing dans Play Console ; le keystore ci-dessus est la clé d’upload.
