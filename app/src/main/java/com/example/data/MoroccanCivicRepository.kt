package com.example.data

import com.example.model.BilingualText
import com.example.model.CivicRightTip
import com.example.model.DocumentItem
import com.example.model.EmergencyCategory
import com.example.model.EmergencyService
import com.example.model.LetterTemplate
import com.example.model.MoroccanCityInfo
import com.example.model.OfficialSource
import com.example.model.ProcedureCategory
import com.example.model.ProcedureGuide
import com.example.model.TemplateCategory

class MoroccanCivicRepository {

    val lastUpdated = BilingualText(
        ar = "مراجعة تقنية: 17 نونبر 2026 — ليست تحققاً قانونياً مستقلاً لكل معلومة",
        fr = "Revue technique : 17 novembre 2026 — pas une validation juridique individuelle"
    )

    val procedures: List<ProcedureGuide> = listOf(
        ProcedureGuide(
            id = "cnie",
            category = ProcedureCategory.IDENTITY,
            title = BilingualText(
                ar = "البطاقة الوطنية للتعريف الإلكترونية (CNIE)",
                fr = "Carte Nationale d'Identité Électronique (CNIE)"
            ),
            subtitle = BilingualText(
                ar = "أول طلب، تجديد، تغيير العنوان، أو ضياع",
                fr = "Première demande, renouvellement, adresse ou perte"
            ),
            feeLabel = BilingualText(
                ar = "75 درهم للبالغين (50 درهم لأقل من 12 سنة) — تمبر إلكتروني",
                fr = "75 DH adultes (50 DH < 12 ans) — E-timbre officiel"
            ),
            processingTime = BilingualText(
                ar = "حوالي 1 إلى 3 أسابيع بعد إيداع الملف (حسب المركز)",
                fr = "Environ 1 à 3 semaines après dépôt physique"
            ),
            officeLocation = BilingualText(
                ar = "مركز تسجيل المعطيات التعريفية (الشرطة أو الدرك حسب محل السكنى)",
                fr = "Centre d'enregistrement des données identitaires (Police ou Gendarmerie)"
            ),
            onlinePortal = "cnie.ma",
            sourceName = BilingualText(
                ar = "بوابة البطاقة الوطنية للأمن الوطني (cnie.ma)",
                fr = "Portail officiel CNIE — DGSN (cnie.ma)"
            ),
            sourceUrl = "https://www.cnie.ma/",
            legalReference = BilingualText(
                ar = "القانون رقم 04.20 المتعلق بالبطاقة الوطنية للتعريف الإلكترونية",
                fr = "Loi n° 04-20 relative à la CNIE"
            ),
            verifiedAt = BilingualText("لم يُنجز بعد تحقق مستقل من الرسوم والآجال والوثائق؛ المرجع الرسمي مرفق للمراجعة", "Frais, délais et pièces non encore vérifiés indépendamment ; source officielle fournie pour contrôle"),
            documents = listOf(
                DocumentItem("cnie_1", BilingualText("استمارة الطلب المسبق مطبوعة من بوابة cnie.ma", "Formulaire de pré-demande imprimé depuis cnie.ma")),
                DocumentItem("cnie_2", BilingualText("شهادة السكنى من السلطة المحلية (المقدم/الباشا) أو الدرك الملكي", "Certificat de résidence (Moqaddem / Pacha / Gendarmerie)")),
                DocumentItem("cnie_3", BilingualText("صور فوتوغرافية حديثة للتعريف بخلفية فاتحة موحدة (حسب معايير الأمن الوطني)", "Photos d'identité récentes sur fond clair uni (normes DGSN)")),
                DocumentItem("cnie_4", BilingualText("وصل التمبر الإلكتروني الرسمي (75 درهم؛ أو 50 درهم للقاصر < 12 سنة)", "Quittance e-timbre (75 DH ; ou 50 DH pour mineur < 12 ans)")),
                DocumentItem("cnie_5", BilingualText("البطاقة القديمة (تجديد) أو تصريح بالضياع، أو نسخة عقد الازدياد/الحالة المدنية (أول مرة)", "Ancienne CNIE (ou déclaration de perte), ou extrait de naissance/livret (1ère demande)"))
            ),
            tips = listOf(
                BilingualText(
                    ar = "عبئ الطلب المسبق واحجز موعدك عبر البوابة الرسمية cnie.ma ثم تتبع جاهزية بطاقتك عن بُعد.",
                    fr = "Remplissez la pré-demande, prenez rendez-vous sur cnie.ma et suivez l'avancement en ligne."
                ),
                BilingualText(
                    ar = "البطاقة صالحة لمدة 10 سنوات للبالغين، ويجب تحيينها عند تغيير العنوان أو الحالة العائلية.",
                    fr = "Valable 10 ans pour les adultes ; à renouveler en cas de changement d'adresse ou d'état civil."
                )
            )
        ),
        ProcedureGuide(
            id = "passport",
            category = ProcedureCategory.IDENTITY,
            title = BilingualText(
                ar = "جواز السفر البيومتري",
                fr = "Passeport Biométrique"
            ),
            subtitle = BilingualText(
                ar = "إعداد ملف جواز السفر (البالغين والقاصرين)",
                fr = "Constitution du dossier (majeurs et mineurs)"
            ),
            feeLabel = BilingualText(
                ar = "500 درهم (تمبر إلكتروني) — معفى مجاناً لأبناء الجالية المقيمين بالخارج دون 18 سنة",
                fr = "500 DH (e-timbre) — Gratuit pour mineurs MRE résidant à l'étranger (< 18 ans)"
            ),
            processingTime = BilingualText(
                ar = "حوالي 1 إلى 2 أسبوع بعد إيداع الملف (حسب العمالة/الولاية)",
                fr = "Environ 1 à 2 semaines après dépôt (selon préfecture)"
            ),
            officeLocation = BilingualText(
                ar = "الملحقة الإدارية (المقاطعة) أو القيادة، أو القنصلية لمغاربة الخارج",
                fr = "Annexe administrative (Moqataa), Caïdat ou Consulat (MRE)"
            ),
            onlinePortal = "passeport.ma/Web/",
            sourceName = BilingualText(
                ar = "خدمة طلب جواز السفر البيومتري (passeport.ma)",
                fr = "Portail Passeport Biométrique (passeport.ma/Web)"
            ),
            sourceUrl = "https://www.passeport.ma/Web/",
            legalReference = BilingualText(
                ar = "المرسوم 2.08.310 والمدونة العامة للضرائب (واجبات التمبر وإعفاءات القاصرين)",
                fr = "Décret n° 2-08-310 & Code Général des Impôts (timbre & exonérations mineurs)"
            ),
            verifiedAt = BilingualText("لم يُنجز بعد تحقق مستقل من الرسوم والآجال والوثائق؛ المرجع الرسمي مرفق للمراجعة", "Frais, délais et pièces non encore vérifiés indépendamment ; source officielle fournie pour contrôle"),
            documents = listOf(
                DocumentItem("pass_1", BilingualText("استمارة الطلب معبأة ومطبوعة من موقع passeport.ma (ورق A4)", "Formulaire rempli en ligne et imprimé depuis passeport.ma (A4)")),
                DocumentItem("pass_2", BilingualText("البطاقة الوطنية سارية الصلاحية للبالغين، أو عقد الازدياد/الدفتر العائلي للقاصر + هوية النائب الشرعي", "CNIE valide (majeur) ou acte de naissance/livret de famille (mineur) + pièce tuteur")),
                DocumentItem("pass_3", BilingualText("صورة فوتوغرافية واحدة حديثة (35×45 ملم، خلفية فاتحة موحدة: أبيض أو أزرق سماوي أو رمادي فاتح)", "1 photo d'identité récente (35×45 mm, fond clair uni : blanc, bleu ciel ou gris clair)")),
                DocumentItem("pass_4", BilingualText("رمز التمبر الإلكتروني 500 درهم (16 رقماً)، أو اختيار خانة الإعفاء القانوني عند انطباق شروطه", "Code e-timbre de 500 DH (16 chiffres), ou option d'exonération légale si éligible")),
                DocumentItem("pass_5", BilingualText("جواز السفر القديم (تجديد) أو تصريح بالضياع/السرقة عند الاقتضاء", "Ancien passeport (renouvellement) ou déclaration de perte/vol le cas échéant"))
            ),
            tips = listOf(
                BilingualText(
                    ar = "بموجب المدونة العامة للضرائب، يُعفى القاصرون المغاربة المقيمون بالخارج (أقل من 18 سنة) من رسم التمبر؛ أما المقيمون داخل المغرب فيطبق عليهم رسم 500 درهم ما لم يصدر إعفاء قانوني صريح في الجريدة الرسمية أو بوابة passeport.ma.",
                    fr = "Selon le CGI, les mineurs marocains résidant à l'étranger (< 18 ans) sont exonérés du timbre ; au Maroc, le tarif de 500 DH reste applicable sauf exonération légale publiée sur passeport.ma."
                ),
                BilingualText(
                    ar = "وفق passeport.ma، يُشترط تقديم صورة واحدة مطابقة للمعايير للطلبات داخل المغرب، ويتوفر النموذج الإلكتروني على خانة الإعفاء للحالات المشمولة قانوناً.",
                    fr = "Selon passeport.ma, une seule photo conforme est demandée au Maroc, et le formulaire en ligne propose la mention d'exonération pour les catégories éligibles."
                )
            )
        ),
        ProcedureGuide(
            id = "casier",
            category = ProcedureCategory.IDENTITY,
            title = BilingualText(
                ar = "السجل العدلي (البطاقة رقم 3) وحسن السيرة",
                fr = "Casier Judiciaire (Bulletin N°3) & Fiche Anthropométrique"
            ),
            subtitle = BilingualText(
                ar = "مطلوب للتوظيف، المباريات، أو الملفات الإدارية",
                fr = "Requis pour emploi, concours ou dossier administratif"
            ),
            feeLabel = BilingualText(
                ar = "10 دراهم (سحب من المحكمة)؛ 25 درهم بالبريد بالمغرب (40 درهم بالخارج)",
                fr = "10 DH (retrait tribunal) ; 25 DH courrier Maroc (40 DH étranger)"
            ),
            processingTime = BilingualText(
                ar = "48 إلى 72 ساعة عمل عادةً (حسب المحكمة وطريقة الاستلام)",
                fr = "48 à 72 heures ouvrables en moyenne (selon tribunal et mode)"
            ),
            officeLocation = BilingualText(
                ar = "المحكمة الابتدائية (السجل العدلي) أو ولاية الأمن (لبطاقة حسن السيرة 30 درهم)",
                fr = "Tribunal de 1ère Instance (Casier) / Sûreté Nationale (Fiche 30 DH)"
            ),
            onlinePortal = "casierjudiciaire.justice.gov.ma",
            sourceName = BilingualText(
                ar = "خدمة السجل العدلي — وزارة العدل",
                fr = "E-service Casier Judiciaire — Ministère de la Justice"
            ),
            sourceUrl = "https://casierjudiciaire.justice.gov.ma",
            legalReference = BilingualText(
                ar = "قانون المسطرة الجنائية (المواد المنظمة للبطاقة رقم 3 من السجل العدلي)",
                fr = "Code de Procédure Pénale (dispositions relatives au Bulletin n° 3)"
            ),
            verifiedAt = BilingualText("لم يُنجز بعد تحقق مستقل من الرسوم والآجال والوثائق؛ المرجع الرسمي مرفق للمراجعة", "Frais, délais et pièces non encore vérifiés indépendamment ; source officielle fournie pour contrôle"),
            documents = listOf(
                DocumentItem("cas_1", BilingualText("البطاقة الوطنية للتعريف الإلكترونية سارية الصلاحية (+ نسخة عند الإيداع الحضوري)", "CNIE originale en cours de validité (+ copie si dépôt sur place)")),
                DocumentItem("cas_2", BilingualText("تحديد سبب الطلب (التوظيف، مباراة...) عبر البوابة أو الاستمارة", "Motif de la demande précisé sur le portail ou formulaire")),
                DocumentItem("cas_3", BilingualText("رسوم التمبر (10 دراهم بالمحكمة، أو 25 درهم للبريد المضمون؛ و30 درهم لحسن السيرة)", "Frais de timbre (10 DH au tribunal ou 25 DH par poste ; 30 DH fiche police)"))
            ),
            tips = listOf(
                BilingualText(
                    ar = "الطلب الإلكتروني عبر بوابة وزارة العدل يتيح الاستلام من المحكمة أو بالبريد المضمون، أو عبر البريد الإلكتروني لحاملي الجيل الجديد من CNIE وهاتف يدعم NFC.",
                    fr = "Le portail du Ministère de la Justice permet le retrait au tribunal, l'envoi postal, ou l'envoi par e-mail avec CNIE nouvelle génération et smartphone NFC."
                )
            )
        ),
        ProcedureGuide(
            id = "auto_entrepreneur",
            category = ProcedureCategory.WORK_BUSINESS,
            title = BilingualText(
                ar = "بطاقة المقاول الذاتي (Auto-Entrepreneur)",
                fr = "Statut Auto-Entrepreneur (Carte officielle)"
            ),
            subtitle = BilingualText(
                ar = "ملف التسجيل الرسمي في السجل الوطني للمقاول الذاتي",
                fr = "Dossier d'inscription au Registre National (RNAE)"
            ),
            feeLabel = BilingualText(
                ar = "التسجيل مجاني في البوابة الوطنية",
                fr = "Inscription gratuite sur le portail national"
            ),
            processingTime = BilingualText(
                ar = "حوالي 7 إلى 15 يوم عمل بعد إيداع الملف بالبنك الشريك",
                fr = "Environ 7 à 15 jours ouvrables après dépôt en agence bancaire"
            ),
            officeLocation = BilingualText(
                ar = "التسجيل الأولي عبر البوابة ثم إيداع الملف في وكالة بنكية شريكة",
                fr = "Pré-inscription en ligne puis dépôt en agence bancaire partenaire"
            ),
            onlinePortal = "rn.ae.gov.ma",
            sourceName = BilingualText(
                ar = "بوابة السجل الوطني للمقاول الذاتي (RNAE)",
                fr = "Portail Registre National Auto-Entrepreneur (RNAE)"
            ),
            sourceUrl = "https://rn.ae.gov.ma",
            legalReference = BilingualText(
                ar = "القانون رقم 114.13 المتعلق بنظام المقاول الذاتي",
                fr = "Loi n° 114-13 relative au statut de l'auto-entrepreneur"
            ),
            verifiedAt = BilingualText("لم يُنجز بعد تحقق مستقل من الرسوم والآجال والوثائق؛ المرجع الرسمي مرفق للمراجعة", "Frais, délais et pièces non encore vérifiés indépendamment ; source officielle fournie pour contrôle"),
            documents = listOf(
                DocumentItem("ae_1", BilingualText("استمارة التسجيل مطبوعة وموقعة من بوابة rn.ae.gov.ma", "Formulaire imprimé et signé depuis rn.ae.gov.ma")),
                DocumentItem("ae_2", BilingualText("نسخة من البطاقة الوطنية للتعريف (CNIE) مطابقة للأصل", "Photocopie de la CNIE en cours de validité")),
                DocumentItem("ae_3", BilingualText("صورة شمسية حديثة (خلفية واضحة)", "1 photo d'identité récente"))
            ),
            tips = listOf(
                BilingualText(
                    ar = "راجع دائماً الآجال الفصلية الرسمية للتصريح برقم المعاملات على البوابة الرسمية لتفادي ذعائر التأخير.",
                    fr = "Consultez régulièrement les échéances trimestrielles officielles sur rn.ae.gov.ma pour éviter les pénalités de retard."
                )
            )
        ),
        ProcedureGuide(
            id = "cnss_amo",
            category = ProcedureCategory.WORK_BUSINESS,
            title = BilingualText(
                ar = "ملف استرجاع مصاريف العلاج (AMO / CNSS)",
                fr = "Dossier de remboursement soins (AMO / CNSS)"
            ),
            subtitle = BilingualText(
                ar = "الوثائق اللازمة لملف المرض والتغطية الصحية الإجبارية",
                fr = "Pièces requises pour la feuille de soins AMO"
            ),
            feeLabel = BilingualText(
                ar = "مجاني",
                fr = "Gratuit"
            ),
            processingTime = BilingualText(
                ar = "حسب دراسة الملف (أجل الإيداع الأقصى 60 يوماً من أول علاج)",
                fr = "Selon traitement CNSS (dépôt sous 60 jours max du 1er acte)"
            ),
            officeLocation = BilingualText(
                ar = "وكالة الضمان الاجتماعي (CNSS) أو مراكز القرب المعتمدة",
                fr = "Agence CNSS ou guichet de proximité agréé"
            ),
            onlinePortal = "cnss.ma",
            sourceName = BilingualText(
                ar = "البوابة الرسمية للصندوق الوطني للضمان الاجتماعي (CNSS)",
                fr = "Portail officiel CNSS / MaCNSS"
            ),
            sourceUrl = "https://www.cnss.ma",
            legalReference = BilingualText(
                ar = "القانون رقم 65.00 بمثابة مدونة التغطية الصحية الأساسية (AMO)",
                fr = "Loi n° 65-00 portant Code de la Couverture Médicale de Base"
            ),
            verifiedAt = BilingualText("لم يُنجز بعد تحقق مستقل من الرسوم والآجال والوثائق؛ المرجع الرسمي مرفق للمراجعة", "Frais, délais et pièces non encore vérifiés indépendamment ; source officielle fournie pour contrôle"),
            documents = listOf(
                DocumentItem("amo_1", BilingualText("ورقة العلاجات (Feuille de soins) مملوءة ومختومة وموقعة من الطبيب والصيدلي", "Feuille de soins dûment remplie, cachetée et signée")),
                DocumentItem("amo_2", BilingualText("الوصفة الطبية الأصلية + علب الأدوية (أكواد PPM)", "Ordonnance originale + boîtes de médicaments (codes à barres PPM)")),
                DocumentItem("amo_3", BilingualText("الفواتير المفصلة ونتائج التحاليل/الأشعة الأصلية (إن وجدت)", "Factures détaillées et justificatifs d'analyses ou radiologie")),
                DocumentItem("amo_4", BilingualText("كشف الهوية البنكية (RIB) مفعّل في حسابك على MaCNSS", "RIB bancaire enregistré sur votre compte MaCNSS"))
            ),
            tips = listOf(
                BilingualText(
                    ar = "يجب إيداع ملف استرجاع المصاريف داخل أجل أقصاه 60 يوماً من تاريخ أول علاج طبي.",
                    fr = "Déposez votre dossier de soins dans un délai maximum de 60 jours à compter du premier acte médical."
                )
            )
        ),
        ProcedureGuide(
            id = "carte_grise",
            category = ProcedureCategory.TRANSPORT_HEALTH,
            title = BilingualText(
                ar = "نقل ملكية سيارة (البطاقة الرمادية - Carte Grise)",
                fr = "Mutation de Véhicule (Carte Grise)"
            ),
            subtitle = BilingualText(
                ar = "إجراءات بيع وشراء سيارة مستعملة بالمغرب",
                fr = "Démarches d'achat et vente d'un véhicule d'occasion"
            ),
            feeLabel = BilingualText(
                ar = "تُحدد الرسوم رسمياً ببوابة NARSA حسب نوع وقوة المركبة",
                fr = "Frais officiels fixés sur NARSA selon type et puissance"
            ),
            processingTime = BilingualText(
                ar = "وصل مؤقت فوري عند إيداع الملف المكتمل",
                fr = "Récépissé provisoire immédiat au dépôt du dossier complet"
            ),
            officeLocation = BilingualText(
                ar = "بوابة نقل الملكية NARSA ووكالات التسهيلات/البريد المعتمدة",
                fr = "Portail Mutation Véhicule NARSA & agences agréées"
            ),
            onlinePortal = "mutationvehicule.ma",
            sourceName = BilingualText(
                ar = "البوابة الرسمية لنقل ملكية المركبات (NARSA)",
                fr = "Portail officiel Mutation Véhicule — NARSA"
            ),
            sourceUrl = "https://www.mutationvehicule.ma",
            legalReference = BilingualText(
                ar = "القانون 52.05 المتعلق بمدونة السير على الطرق — مساطر نقل الملكية",
                fr = "Code de la Route (Loi n° 52-05) — Procédures d'immatriculation"
            ),
            verifiedAt = BilingualText("لم يُنجز بعد تحقق مستقل من الرسوم والآجال والوثائق؛ المرجع الرسمي مرفق للمراجعة", "Frais, délais et pièces non encore vérifiés indépendamment ; source officielle fournie pour contrôle"),
            documents = listOf(
                DocumentItem("cg_1", BilingualText("عقد البيع مصادق عليه بين البائع والمشتري (أو مسجل عبر بوابة mutationvehicule.ma)", "Contrat de vente légalisé (ou validé sur mutationvehicule.ma)")),
                DocumentItem("cg_2", BilingualText("البطاقة الرمادية الأصلية مشطوب عليها عبارة 'بيعت يوم...'", "Carte grise originale barrée avec mention 'Vendue le...'")),
                DocumentItem("cg_3", BilingualText("شهادة الفحص التقني لمركز معتمد خاصة بنقل الملكية (أقل من 3 أشهر)", "Procès-verbal de visite technique spéciale mutation (< 3 mois)")),
                DocumentItem("cg_4", BilingualText("شهادة رفع اليد (Mainlevée) إن كانت السيارة ممولة بقرض", "Mainlevée si le véhicule était sous crédit")),
                DocumentItem("cg_5", BilingualText("نسخة من البطاقة الوطنية للمشتري والبائع + وصل الضريبة السنوية (لافيينت)", "CNIE acheteur & vendeur + quittance de la vignette à jour"))
            ),
            tips = listOf(
                BilingualText(
                    ar = "تحقق دائماً من عدم وجود تعرض (Opposition) أو مخالفات غير مؤداة قبل إتمام الشراء.",
                    fr = "Vérifiez toujours l'absence d'opposition ou d'amendes impayées avant de finaliser l'achat."
                )
            )
        ),
        ProcedureGuide(
            id = "mariage",
            category = ProcedureCategory.FAMILY_CIVIL,
            title = BilingualText(
                ar = "ملف عقد الزواج (قاضي الأسرة والعدول)",
                fr = "Dossier d'Acte de Mariage (Tribunal & Adoul)"
            ),
            subtitle = BilingualText(
                ar = "الوثائق المطلوبة لإبرام عقد الزواج حسب مدونة الأسرة",
                fr = "Pièces requises selon le Code de la Famille"
            ),
            feeLabel = BilingualText(
                ar = "واجبات قضائية لملف الإذن بالزواج + أتعاب العدول (تُراجع بالمحكمة)",
                fr = "Taxes judiciaires d'autorisation + honoraires des Adoul (selon tarif légal)"
            ),
            processingTime = BilingualText(
                ar = "لا يوجد أجل قانوني موحد (حسب استكمال الملف؛ يُرسل ملخص العقد للحالة المدنية خلال 15 يوماً بعد التوثيق)",
                fr = "Pas de délai fixe (selon dossier ; avis transmis à l'état civil sous 15 jours après rédaction)"
            ),
            officeLocation = BilingualText(
                ar = "قسم قضاء الأسرة بالمحكمة الابتدائية ثم مكتب العدول",
                fr = "Section des Affaires Familiales (Tribunal) puis bureau des Adoul"
            ),
            onlinePortal = "adala.justice.gov.ma",
            sourceName = BilingualText(
                ar = "البوابة القانونية والقضائية لوزارة العدل (عدالة)",
                fr = "Portail juridique Adala — Ministère de la Justice"
            ),
            sourceUrl = "https://adala.justice.gov.ma/api/uploads/2024/07/22/%D9%85%D8%AF%D9%88%D9%86%D8%A9%20%D8%A7%D9%84%D8%A3%D8%B3%D8%B1%D8%A9-1721646687560.pdf",
            legalReference = BilingualText(
                ar = "القانون رقم 70.03 بمثابة مدونة الأسرة (المواد 65 إلى 68)",
                fr = "Loi n° 70-03 portant Code de la Famille (Articles 65 à 68)"
            ),
            verifiedAt = BilingualText("لم يُنجز بعد تحقق مستقل من الرسوم والآجال والوثائق؛ المرجع الرسمي مرفق للمراجعة", "Frais, délais et pièces non encore vérifiés indépendamment ; source officielle fournie pour contrôle"),
            documents = listOf(
                DocumentItem("mar_1", BilingualText("نسخة كاملة من رسم الولادة لكل من الخطيبين (صلاحية أقل من 3 أشهر)", "Copie intégrale de l'acte de naissance pour chacun des futurs époux (< 3 mois)")),
                DocumentItem("mar_2", BilingualText("نسخة مصادق عليها من البطاقة الوطنية للتعريف (CNIE) + صور شمسية لكل طرف", "Copie certifiée conforme de la CNIE + photos d'identité pour chaque époux")),
                DocumentItem("mar_3", BilingualText("شهادة إدارية للعزوبة / الخطوبة مسلمة من السلطة المحلية", "Certificat administratif de célibat / fiançailles")),
                DocumentItem("mar_4", BilingualText("شهادة طبية تثبت الخلو من الأمراض المعدية (لكل طرف، صلاحية 3 أشهر)", "Certificat médical prénuptial pour chacun des époux (validité 3 mois)")),
                DocumentItem("mar_5", BilingualText("إذن قاضي الأسرة بالزواج + نسخة من رسم الطلاق أو الوفاة (في حالة سبق الزواج)", "Autorisation du juge de la famille + acte de divorce ou décès (si mariage antérieur)"))
            ),
            tips = listOf(
                BilingualText(
                    ar = "صلاحية الشهادة الطبية والنسخة الكاملة لرسم الولادة والشهادة الإدارية غالباً لا تتجاوز 3 أشهر؛ اجمع الوثائق في فترة متقاربة.",
                    fr = "Le certificat médical, la copie intégrale de naissance et le certificat administratif ont une validité de 3 mois."
                )
            )
        ),
        ProcedureGuide(
            id = "residence_naissance",
            category = ProcedureCategory.FAMILY_CIVIL,
            title = BilingualText(
                ar = "شهادة السكنى ووثائق الحالة المدنية",
                fr = "Certificat de Résidence & Actes d'État Civil"
            ),
            subtitle = BilingualText(
                ar = "وثائق يومية للملفات الإدارية والمنح والتسجيل",
                fr = "Documents courants pour dossiers administratifs et scolaires"
            ),
            feeLabel = BilingualText(
                ar = "مجاني بالإدارة؛ رسوم بريدية + تمبر عند الطلب عبر watiqa.ma",
                fr = "Gratuit sur place ; frais postaux + timbre via watiqa.ma"
            ),
            processingTime = BilingualText(
                ar = "فوري إلى 24 ساعة بالإدارة (عدة أيام عبر البريد المضمون)",
                fr = "Immédiat à 24h sur place (quelques jours par courrier recommandé)"
            ),
            officeLocation = BilingualText(
                ar = "مكتب الحالة المدنية بالجماعة أو الملحقة الإدارية + بوابة وثيقة",
                fr = "Bureau d'État Civil (Commune / Moqataa) ou portail Watiqa"
            ),
            onlinePortal = "watiqa.ma",
            sourceName = BilingualText(
                ar = "الشباك الإلكتروني لطلب وثائق الحالة المدنية (watiqa.ma)",
                fr = "Guichet électronique État Civil (watiqa.ma)"
            ),
            sourceUrl = "https://www.watiqa.ma/",
            legalReference = BilingualText(
                ar = "القانون رقم 36.21 المتعلق بالحالة المدنية",
                fr = "Loi n° 36-21 relative à l'État Civil"
            ),
            verifiedAt = BilingualText("لم يُنجز بعد تحقق مستقل من الرسوم والآجال والوثائق؛ المرجع الرسمي مرفق للمراجعة", "Frais, délais et pièces non encore vérifiés indépendamment ; source officielle fournie pour contrôle"),
            documents = listOf(
                DocumentItem("res_1", BilingualText("البطاقة الوطنية للتعريف الإلكترونية سارية الصلاحية", "CNIE originale en cours de validité")),
                DocumentItem("res_2", BilingualText("إثبات السكنى: فاتورة ماء/كهرباء أو عقد كراء أو شهادة إيواء (لشهادة السكنى)", "Justificatif de domicile : facture eau/élec, bail ou attestation d'hébergement")),
                DocumentItem("res_3", BilingualText("الدفتر العائلي (الحالة المدنية) لطلب رسم الولادة", "Livret de famille pour les actes d'état civil"))
            ),
            tips = listOf(
                BilingualText(
                    ar = "يمكنك طلب وثائق الحالة المدنية عبر موقع watiqa.ma لتصلك عبر البريد المضمون أينما كنت في المغرب.",
                    fr = "Commandez vos actes d'état civil sur watiqa.ma avec livraison par courrier recommandé partout au Maroc."
                )
            )
        )
    )

    val letterTemplates: List<LetterTemplate> = listOf(
        LetterTemplate(
            id = "demande_emploi",
            category = TemplateCategory.WORK_STUDY,
            title = BilingualText("طلب خطي للتوظيف أو مباراة", "Demande manuscrite d'emploi / concours"),
            summary = BilingualText("نموذج رسمي جاهز للترشيح في القطاع العام أو الخاص", "Modèle formel pour candidature publique ou privée"),
            needsTargetEntity = true,
            bodyTemplateAr = """
{CITY} في: {DATE}

من: {FULL_NAME}
رقم البطاقة الوطنية: {CIN}
رقم الهاتف: {PHONE}

إلى السيد(ة): {TARGET_ENTITY}

الموضوع: طلب المشاركة / التوظيف

سلام تام بوجود مولانا الإمام،
وبعد، يشرفني أن أتقدم إلى سيادتكم بطلبي هذا قصد الترشح لمنصب {DETAILS}.
وأحيطكم علماً أنني أتوفر على المؤهلات والمهارات اللازمة للقيام بالمهام المطلوبة بكل جدية ومسؤولية، وتجدون رفقة هذا الطلب نسخاً من السيرة الذاتية والوثائق المطلوبة.

وفي انتظار ردكم الإيجابي، تقبلوا فائق عبارات التقدير والاحترام.

والسلام.
الإمضاء: {FULL_NAME}
            """.trimIndent(),
            bodyTemplateFr = """
{CITY}, le {DATE}

Nom & Prénom : {FULL_NAME}
CNIE : {CIN}
Tél : {PHONE}

À l'attention de : {TARGET_ENTITY}

Objet : Candidature au poste / concours de {DETAILS}

Madame, Monsieur,

J'ai l'honneur de soumettre à votre haute bienveillance ma candidature pour le poste de {DETAILS} au sein de votre établissement.

Sérieux(se), motivé(e) et prêt(e) à m'investir pleinement, je vous prie de trouver ci-joint mon curriculum vitae ainsi que les pièces justificatives requises.

Dans l'attente d'une réponse favorable, je vous prie d'agréer, Madame, Monsieur, l'expression de mes salutations distinguées.

Signature : {FULL_NAME}
            """.trimIndent()
        ),
        LetterTemplate(
            id = "demande_stage",
            category = TemplateCategory.WORK_STUDY,
            title = BilingualText("طلب تدريب مهني (Stage)", "Demande de stage professionnel"),
            summary = BilingualText("موجه للشركات والمؤسسات للطلبة والمتدربين", "Destiné aux étudiants et lauréats OFPPT / Université"),
            needsTargetEntity = true,
            bodyTemplateAr = """
{CITY} في: {DATE}

من: {FULL_NAME}
رقم البطاقة الوطنية: {CIN}
الهاتف: {PHONE}

إلى السيد(ة) مدير(ة): {TARGET_ENTITY}

الموضوع: طلب إجراء تدريب (Stage)

سلام تام بوجود مولانا الإمام،
وبعد، يشرفني أن ألتمس من سيادتكم قبولي لإجراء تدريب ميداني داخل مؤسستكم في مجال {DETAILS}.
سيمكنني هذا التدريب من تطبيق معارفي النظرية واكتساب خبرة عملية في بيئة مهنية متميزة.

وتفضلوا بقبول فائق الاحترام والتقدير.

الإمضاء: {FULL_NAME}
            """.trimIndent(),
            bodyTemplateFr = """
{CITY}, le {DATE}

{FULL_NAME}
CNIE : {CIN}
Tél : {PHONE}

À Monsieur/Madame le Directeur de {TARGET_ENTITY}

Objet : Demande de stage

Madame, Monsieur,

Actuellement en formation, je souhaite effectuer un stage pratique au sein de votre entreprise/organisme concernant : {DETAILS}.

Ce stage me permettra de mettre en pratique mes connaissances et de renforcer mon expérience professionnelle auprès de vos équipes.

Je reste à votre disposition pour un entretien et vous prie d'agréer mes salutations distinguées.

Signature : {FULL_NAME}
            """.trimIndent()
        ),
        LetterTemplate(
            id = "hebergement",
            category = TemplateCategory.ATTESTATION,
            title = BilingualText("التزام بالإيواء والسكنى", "Attestation d'hébergement"),
            summary = BilingualText("لإثبات سكنى شخص معك لدى السلطات أو الإدارة", "Pour justifier la résidence d'un proche hébergé"),
            needsTargetEntity = true,
            bodyTemplateAr = """
تصريح والتزام بالإيواء

أنا الموقع(ة) أسفله:
الاسم الكامل: {FULL_NAME}
الحامل(ة) للبطاقة الوطنية رقم: {CIN}
الهاتف: {PHONE}
الساكن(ة) بمدينة: {CITY}

أشهد وأصرح بشرفي أنني أؤوي بمحل سكناي الكائن بالمدينة المذكورة السيد(ة): {TARGET_ENTITY}، بخصوص: {DETAILS}، وذلك بصفة دائمة ومستمرة.

وقد سُلم هذا الالتزام للإدلاء به عند الحاجة وفق القوانين الجاري بها العمل.

حرر بـ {CITY} في: {DATE}
الإمضاء: {FULL_NAME}
            """.trimIndent(),
            bodyTemplateFr = """
ATTESTATION D'HÉBERGEMENT

Je soussigné(e) {FULL_NAME}, titulaire de la CNIE n° {CIN}, joignable au {PHONE}, résidant à {CITY},

Déclare sur l'honneur héberger à mon domicile situé à {CITY} : {TARGET_ENTITY} ({DETAILS}).

La présente attestation est établie pour servir et valoir ce que de droit.

Fait à {CITY}, le {DATE}
Signature : {FULL_NAME}
            """.trimIndent()
        ),
        LetterTemplate(
            id = "declaration_honneur",
            category = TemplateCategory.ATTESTATION,
            title = BilingualText("تصريح بالشرف", "Déclaration sur l'honneur"),
            summary = BilingualText("تصريح شخصي بصحة المعطيات للملفات الإدارية", "Déclaration officielle de véracité pour dossiers"),
            needsTargetEntity = false,
            bodyTemplateAr = """
تصريح بالشرف

أنا الموقع(ة) أسفله: {FULL_NAME}
الحامل(ة) للبطاقة الوطنية رقم: {CIN}
الساكن(ة) بمدينة: {CITY}
الهاتف: {PHONE}

أصرح بشرفي وبكامل قواي العقلية والقانونية، وتحت مسؤوليتي الكاملة، بأن:
{DETAILS}

وأتحمل كامل المسؤولية القانونية في حالة ثبوت خلاف ذلك.

حرر بـ {CITY} في: {DATE}
الإمضاء: {FULL_NAME}
            """.trimIndent(),
            bodyTemplateFr = """
DÉCLARATION SUR L'HONNEUR

Je soussigné(e) {FULL_NAME}, titulaire de la CNIE n° {CIN}, résidant à {CITY}, tél : {PHONE},

Déclare sur l'honneur que :
{DETAILS}

Je certifie l'exactitude des informations fournies et assume toute responsabilité légale en cas de fausse déclaration.

Fait à {CITY}, le {DATE}
Signature : {FULL_NAME}
            """.trimIndent()
        ),
        LetterTemplate(
            id = "procuration",
            category = TemplateCategory.ADMIN_REQUEST,
            title = BilingualText("توكيل إداري خاص", "Procuration administrative"),
            summary = BilingualText("لتفويض شخص بسحب وثيقة أو تمثيلك إدارياً", "Pour autoriser un proche à retirer un document"),
            needsTargetEntity = true,
            bodyTemplateAr = """
توكيل إداري

أنا الموقع(ة) أسفله: {FULL_NAME}
رقم البطاقة الوطنية: {CIN}
الساكن(ة) بمدينة: {CITY} — الهاتف: {PHONE}

أوكّل بموجب هذا التوكيل السيد(ة): {TARGET_ENTITY}
للقيام نيابةً عني بـ: {DETAILS}، والتوقيع والسحب لدى الجهات المختصة.

حرر بـ {CITY} في: {DATE}
إمضاء الموكِّل: {FULL_NAME}
            """.trimIndent(),
            bodyTemplateFr = """
PROCURATION ADMINISTRATIVE

Je soussigné(e) {FULL_NAME}, titulaire de la CNIE n° {CIN}, domicilié(e) à {CITY} (Tél : {PHONE}),

Donne par la présente procuration à : {TARGET_ENTITY}
à l'effet d'accomplir en mon nom la démarche suivante : {DETAILS}.

Fait à {CITY}, le {DATE}
Signature du mandant : {FULL_NAME}
            """.trimIndent()
        ),
        LetterTemplate(
            id = "demission",
            category = TemplateCategory.WORK_STUDY,
            title = BilingualText("رسالة استقالة من العمل", "Lettre de démission"),
            summary = BilingualText("نموذج محترم لإنهاء عقد العمل مع احترام الإشعار", "Modèle formel avec préavis légal"),
            needsTargetEntity = true,
            bodyTemplateAr = """
{CITY} في: {DATE}

من: {FULL_NAME}
البطاقة الوطنية: {CIN} — الهاتف: {PHONE}

إلى السيد(ة) مدير(ة): {TARGET_ENTITY}

الموضوع: تقديم استقالة من العمل

سلام تام،
يشرفني أن أنهي إلى علمكم قراري بتقديم استقالتي من منصبي ({DETAILS}) وذلك ابتداءً من تاريخ اليوم مع التزامي بفترة الإخطار القانونية المعمول بها.
أشكركم على فترة العمل التي قضيتها داخل المؤسسة وأتمنى لكم دوام التوفيق.

وتقبلوا فائق الاحترام.
الإمضاء: {FULL_NAME}
            """.trimIndent(),
            bodyTemplateFr = """
{CITY}, le {DATE}

{FULL_NAME}
CNIE : {CIN} — Tél : {PHONE}

À Monsieur/Madame le Directeur de {TARGET_ENTITY}

Objet : Démission

Madame, Monsieur,

Par la présente, je vous informe de ma décision de démissionner de mes fonctions ({DETAILS}), tout en respectant la durée du préavis légal en vigueur.

Je vous remercie pour l'expérience acquise au sein de votre entreprise et vous prie d'agréer mes salutations respectueuses.

Signature : {FULL_NAME}
            """.trimIndent()
        ),
        LetterTemplate(
            id = "declaration_perte",
            category = TemplateCategory.ADMIN_REQUEST,
            title = BilingualText("تصريح بضياع وثيقة", "Déclaration de perte de document"),
            summary = BilingualText("نموذج جاهز للتصريح بالضياع لدى الأمن أو الإدارة", "Modèle de déclaration en cas de perte d'une pièce"),
            needsTargetEntity = true,
            bodyTemplateAr = """
تصريح بضياع

{CITY} في: {DATE}

أنا الموقع(ة) أسفله: {FULL_NAME}
الحامل(ة) للبطاقة الوطنية رقم: {CIN}
الهاتف: {PHONE}

إلى السيد(ة): {TARGET_ENTITY}

أصرح بشرفي أنني قد أضعت وثيقتي المتعلقة بـ: {DETAILS}، وذلك بمدينة {CITY}.
أرجو منكم تسجيل هذا التصريح وتسليمي وصلاً للإدلاء به عند الاقتضاء.

الإمضاء: {FULL_NAME}
            """.trimIndent(),
            bodyTemplateFr = """
DÉCLARATION DE PERTE

{CITY}, le {DATE}

Je soussigné(e) {FULL_NAME}, CNIE n° {CIN}, tél : {PHONE},

À l'attention de : {TARGET_ENTITY}

Déclare avoir perdu la pièce suivante : {DETAILS}, à {CITY}.
Je sollicite l'enregistrement de cette déclaration pour servir et valoir ce que de droit.

Signature : {FULL_NAME}
            """.trimIndent()
        ),
        LetterTemplate(
            id = "reclamation",
            category = TemplateCategory.COMPLAINT,
            title = BilingualText("شكاية مستهلك أو تظلم إداري", "Réclamation consommateur / administrative"),
            summary = BilingualText("للمطالبة بحقك لدى شركة أو مصلحة عمومية", "Pour adresser une réclamation formelle"),
            needsTargetEntity = true,
            bodyTemplateAr = """
{CITY} في: {DATE}

من: {FULL_NAME}
البطاقة الوطنية: {CIN}
الهاتف: {PHONE}

إلى السيد(ة) المسؤول(ة) عن: {TARGET_ENTITY}

الموضوع: شكاية رسمية

سلام تام،
أتقدم إليكم بهذه الشكاية بخصوص: {DETAILS}.
ألتمس منكم التدخل العاجل لمعالجة هذا الإشكال وإنصافي طبقاً للقوانين والضوابط المعمول بها، وموافاتي بردكم في أقرب الآجال.

وتقبلوا أسمى عبارات التقدير.
الإمضاء: {FULL_NAME}
            """.trimIndent(),
            bodyTemplateFr = """
{CITY}, le {DATE}

{FULL_NAME}
CNIE : {CIN} — Tél : {PHONE}

À l'attention du responsable de : {TARGET_ENTITY}

Objet : Réclamation officielle

Madame, Monsieur,

Je me permets de vous saisir concernant l'incident suivant : {DETAILS}.

Je vous prie de bien vouloir examiner cette situation et d'y apporter une solution dans les plus brefs délais, conformément à la réglementation en vigueur.

Dans l'attente de votre retour, veuillez agréer mes salutations distinguées.

Signature : {FULL_NAME}
            """.trimIndent()
        )
    )

    val moroccanCities: List<MoroccanCityInfo> = listOf(
        MoroccanCityInfo("rabat", BilingualText("الرباط", "Rabat"), BilingualText("الرباط-سلا-القنيطرة", "Rabat-Salé-Kénitra"), "1", "10000"),
        MoroccanCityInfo("sale", BilingualText("سلا", "Salé"), BilingualText("الرباط-سلا-القنيطرة", "Rabat-Salé-Kénitra"), "2", "11000"),
        MoroccanCityInfo("temara", BilingualText("الصخيرات - تمارة", "Skhirat-Témara"), BilingualText("الرباط-سلا-القنيطرة", "Rabat-Salé-Kénitra"), "4", "12000"),
        MoroccanCityInfo("khemisset", BilingualText("الخميسات", "Khémisset"), BilingualText("الرباط-سلا-القنيطرة", "Rabat-Salé-Kénitra"), "5", "15000"),
        MoroccanCityInfo("casa_anfa", BilingualText("الدار البيضاء - أنفا", "Casablanca-Anfa"), BilingualText("الدار البيضاء-سطات", "Casablanca-Settat"), "6", "20000"),
        MoroccanCityInfo("casa_ain_sebaa", BilingualText("الدار البيضاء - عين السبع", "Casablanca Aïn Sebaâ"), BilingualText("الدار البيضاء-سطات", "Casablanca-Settat"), "7", "20250"),
        MoroccanCityInfo("casa_hay_hassani", BilingualText("الدار البيضاء - الحي الحسني", "Casablanca Hay Hassani"), BilingualText("الدار البيضاء-سطات", "Casablanca-Settat"), "8", "20230"),
        MoroccanCityInfo("casa_ben_msick", BilingualText("الدار البيضاء - بن مسيك", "Casablanca Ben M'sick"), BilingualText("الدار البيضاء-سطات", "Casablanca-Settat"), "9", "20700"),
        MoroccanCityInfo("casa_moulay_rachid", BilingualText("الدار البيضاء - مولاي رشيد", "Casablanca Moulay Rachid"), BilingualText("الدار البيضاء-سطات", "Casablanca-Settat"), "10", "20702"),
        MoroccanCityInfo("casa_bernoussi", BilingualText("الدار البيضاء - سيدي البرنوصي", "Casablanca Sidi Bernoussi"), BilingualText("الدار البيضاء-سطات", "Casablanca-Settat"), "13", "20600"),
        MoroccanCityInfo("mohammedia", BilingualText("المحمدية", "Mohammédia"), BilingualText("الدار البيضاء-سطات", "Casablanca-Settat"), "14", "28800"),
        MoroccanCityInfo("fes_jdid", BilingualText("فاس الجديد - دار الدبيبغ", "Fès-Jdid"), BilingualText("فاس-مكناس", "Fès-Meknès"), "15", "30000"),
        MoroccanCityInfo("fes_medina", BilingualText("فاس المدينة", "Fès-Médina"), BilingualText("فاس-مكناس", "Fès-Meknès"), "16", "30110"),
        MoroccanCityInfo("sefrou", BilingualText("صفرو", "Sefrou"), BilingualText("فاس-مكناس", "Fès-Meknès"), "18", "31000"),
        MoroccanCityInfo("meknes", BilingualText("مكناس", "Meknès"), BilingualText("فاس-مكناس", "Fès-Meknès"), "20 / 21", "50000"),
        MoroccanCityInfo("ifrane", BilingualText("إفران", "Ifrane"), BilingualText("فاس-مكناس", "Fès-Meknès"), "23", "53000"),
        MoroccanCityInfo("khenifra", BilingualText("خنيفرة", "Khénifra"), BilingualText("بني ملال-خنيفرة", "Béni Mellal-Khénifra"), "24", "54000"),
        MoroccanCityInfo("errachidia", BilingualText("الرشيدية", "Errachidia"), BilingualText("درعة-تافيلالت", "Drâa-Tafilalet"), "25", "52000"),
        MoroccanCityInfo("marrakech_menara", BilingualText("مراكش - المنارة", "Marrakech-Ménara"), BilingualText("مراكش-آسفي", "Marrakech-Safi"), "26", "40000"),
        MoroccanCityInfo("marrakech_medina", BilingualText("مراكش - المدينة", "Marrakech-Médina"), BilingualText("مراكش-آسفي", "Marrakech-Safi"), "27", "40034"),
        MoroccanCityInfo("kelaa", BilingualText("قلعة السراغنة", "Kelaat des Sraghna"), BilingualText("مراكش-آسفي", "Marrakech-Safi"), "31", "43000"),
        MoroccanCityInfo("essaouira", BilingualText("الصويرة", "Essaouira"), BilingualText("مراكش-آسفي", "Marrakech-Safi"), "32", "44000"),
        MoroccanCityInfo("agadir", BilingualText("أكادير إداوتنان", "Agadir Ida-Outanane"), BilingualText("سوس-ماسة", "Souss-Massa"), "33", "80000"),
        MoroccanCityInfo("inezgane", BilingualText("إنزكان - أيت ملول", "Inezgane-Aït Melloul"), BilingualText("سوس-ماسة", "Souss-Massa"), "34", "80100 / 86350"),
        MoroccanCityInfo("taroudant", BilingualText("تارودانت", "Taroudant"), BilingualText("سوس-ماسة", "Souss-Massa"), "36", "83000"),
        MoroccanCityInfo("tiznit", BilingualText("تيزنيت", "Tiznit"), BilingualText("سوس-ماسة", "Souss-Massa"), "37", "85000"),
        MoroccanCityInfo("ouarzazate", BilingualText("ورزازات", "Ouarzazate"), BilingualText("درعة-تافيلالت", "Drâa-Tafilalet"), "38", "45000"),
        MoroccanCityInfo("zagora", BilingualText("زاكورة", "Zagora"), BilingualText("درعة-تافيلالت", "Drâa-Tafilalet"), "39", "47900"),
        MoroccanCityInfo("tanger", BilingualText("طنجة - أصيلة", "Tanger-Assilah"), BilingualText("طنجة-تطوان-الحسيمة", "Tanger-Tétouan-Al Hoceïma"), "40", "90000"),
        MoroccanCityInfo("larache", BilingualText("العرائش", "Larache"), BilingualText("طنجة-تطوان-الحسيمة", "Tanger-Tétouan-Al Hoceïma"), "42", "92000"),
        MoroccanCityInfo("chefchaouen", BilingualText("شفشاون", "Chefchaouen"), BilingualText("طنجة-تطوان-الحسيمة", "Tanger-Tétouan-Al Hoceïma"), "43", "91000"),
        MoroccanCityInfo("tetouan", BilingualText("تطوان", "Tétouan"), BilingualText("طنجة-تطوان-الحسيمة", "Tanger-Tétouan-Al Hoceïma"), "44", "93000"),
        MoroccanCityInfo("hoceima", BilingualText("الحسيمة", "Al Hoceïma"), BilingualText("طنجة-تطوان-الحسيمة", "Tanger-Tétouan-Al Hoceïma"), "45", "32000"),
        MoroccanCityInfo("taza", BilingualText("تازة", "Taza"), BilingualText("فاس-مكناس", "Fès-Meknès"), "46", "35000"),
        MoroccanCityInfo("oujda", BilingualText("وجدة - أنجاد", "Oujda-Angad"), BilingualText("الشرق", "L'Oriental"), "48", "60000"),
        MoroccanCityInfo("berkane", BilingualText("بركان", "Berkane"), BilingualText("الشرق", "L'Oriental"), "49", "63300"),
        MoroccanCityInfo("nador", BilingualText("الناظور", "Nador"), BilingualText("الشرق", "L'Oriental"), "50", "62000"),
        MoroccanCityInfo("safi", BilingualText("آسفي", "Safi"), BilingualText("مراكش-آسفي", "Marrakech-Safi"), "54", "46000"),
        MoroccanCityInfo("el_jadida", BilingualText("الجديدة", "El Jadida"), BilingualText("الدار البيضاء-سطات", "Casablanca-Settat"), "55", "24000"),
        MoroccanCityInfo("settat", BilingualText("سطات", "Settat"), BilingualText("الدار البيضاء-سطات", "Casablanca-Settat"), "56", "26000"),
        MoroccanCityInfo("khouribga", BilingualText("خريبكة", "Khouribga"), BilingualText("بني ملال-خنيفرة", "Béni Mellal-Khénifra"), "57", "25000"),
        MoroccanCityInfo("kenitra", BilingualText("القنيطرة", "Kénitra"), BilingualText("الرباط-سلا-القنيطرة", "Rabat-Salé-Kénitra"), "59", "14000"),
        MoroccanCityInfo("beni_mellal", BilingualText("بني ملال", "Béni Mellal"), BilingualText("بني ملال-خنيفرة", "Béni Mellal-Khénifra"), "61", "23000"),
        MoroccanCityInfo("guelmim", BilingualText("كلميم", "Guelmim"), BilingualText("كلميم-واد نون", "Guelmim-Oued Noun"), "64", "81000"),
        MoroccanCityInfo("laayoune", BilingualText("العيون", "Laâyoune"), BilingualText("العيون-الساقية الحمراء", "Laâyoune-Sakia El Hamra"), "68", "70000"),
        MoroccanCityInfo("dakhla", BilingualText("الداخلة", "Dakhla"), BilingualText("الداخلة-وادي الذهب", "Dakhla-Oued Eddahab"), "70", "73000"),
        MoroccanCityInfo("mdiq_fnideq", BilingualText("المضيق - الفنيدق", "M'diq-Fnideq"), BilingualText("طنجة-تطوان-الحسيمة", "Tanger-Tétouan-Al Hoceïma"), "75", "93200 / 93100"),
        MoroccanCityInfo("berrechid", BilingualText("برشيد", "Berrechid"), BilingualText("الدار البيضاء-سطات", "Casablanca-Settat"), "81", "26100"),
        MoroccanCityInfo("benguerir", BilingualText("الرحامنة (ابن جرير)", "Rehamna (Benguerir)"), BilingualText("مراكش-آسفي", "Marrakech-Safi"), "83", "43150"),
        MoroccanCityInfo("tinghir", BilingualText("تنغير", "Tinghir"), BilingualText("درعة-تافيلالت", "Drâa-Tafilalet"), "86", "45800"),
        MoroccanCityInfo("sidi_ifni", BilingualText("سيدي إفني", "Sidi Ifni"), BilingualText("كلميم-واد نون", "Guelmim-Oued Noun"), "87", "85200")
    )

    val emergencyServices: List<EmergencyService> = listOf(
        EmergencyService(
            id = "police",
            number = "19",
            name = BilingualText("الشرطة النجدة (داخل المدن)", "Police Secours (Zone urbaine)"),
            description = BilingualText("التدخل الأمني السريع داخل المجال الحضري 24/24", "Intervention d'urgence de la Sûreté Nationale 24h/24"),
            category = EmergencyCategory.SECURITY_RESCUE,
            sourceName = BilingualText("المديرية العامة للأمن الوطني (DGSN) / إدارتي", "DGSN / Idarati.ma"),
            sourceUrl = "https://www.anrt.ma/infos-pratiques/que-sont-les-numeros-speciaux",
            verifiedAt = BilingualText("أكدت ANRT الرقم 19 للشرطة، وهو متاح من الهاتف الثابت والمحمول.", "L’ANRT confirme le 19 pour la Police, accessible depuis les lignes fixes et mobiles."),
            lastIndependentlyVerified = "2026-11-17"
        ),
        EmergencyService(
            id = "gendarmerie",
            number = "177",
            name = BilingualText("الدرك الملكي (الطرق والقرى)", "Gendarmerie Royale (Routes & Rural)"),
            description = BilingualText("الطوارئ الأمنية وحوادث السير خارج المدار الحضري", "Urgences et accidents hors périmètre urbain"),
            category = EmergencyCategory.SECURITY_RESCUE,
            sourceName = BilingualText("الدرك الملكي / إدارتي", "Gendarmerie Royale / Idarati.ma"),
            sourceUrl = "https://www.anrt.ma/infos-pratiques/que-sont-les-numeros-speciaux",
            verifiedAt = BilingualText("أكدت ANRT الرقم 177 للدرك الملكي، وهو متاح من الهاتف الثابت والمحمول.", "L’ANRT confirme le 177 pour la Gendarmerie Royale, accessible depuis les lignes fixes et mobiles."),
            lastIndependentlyVerified = "2026-11-17"
        ),
        EmergencyService(
            id = "protection_civile",
            number = "15",
            name = BilingualText("الوقاية المدنية / الحرائق والإنقاذ", "Protection Civile / Pompiers"),
            description = BilingualText("الحرائق، الإنقاذ، وحوادث السير والإسعاف الأولي", "Incendies, secours, sauvetage et premiers secours"),
            category = EmergencyCategory.SECURITY_RESCUE,
            sourceName = BilingualText("المديرية العامة للوقاية المدنية / إدارتي", "Protection Civile / Idarati.ma"),
            sourceUrl = "https://www.anrt.ma/infos-pratiques/que-sont-les-numeros-speciaux",
            verifiedAt = BilingualText("أكدت ANRT الرقم 15 للإطفائيين والوقاية المدنية، وهو متاح من الهاتف الثابت والمحمول.", "L’ANRT confirme le 15 pour les pompiers, accessible depuis les lignes fixes et mobiles."),
            lastIndependentlyVerified = "2026-11-17"
        ),
        EmergencyService(
            id = "mobile_police",
            number = "112",
            name = BilingualText("الشرطة من الهاتف المحمول", "Police depuis un téléphone mobile"),
            description = BilingualText("رقم شرطة للطوارئ من الهاتف المحمول؛ عند تعذر الاتصال استعمل 19 أو 177 حسب المنطقة.", "Numéro Police d'urgence depuis un mobile ; si l'appel échoue, composez le 19 ou le 177 selon la zone."),
            category = EmergencyCategory.SECURITY_RESCUE,
            sourceName = BilingualText("اتصالات المغرب — كتالوج عروض الهاتف المحمول 2026", "Maroc Telecom — Catalogue des offres Mobile B2C 2026"),
            sourceUrl = "https://www.iam.ma/documents/66341/0/Catalogue%2Bdes%2Boffres%2BMobile%2BB2C%2B-%2BJuin%2B22-2026.pdf/2b18ea63-7178-51bf-ea4e-3df27b76bfa6?t=1782141863456",
            verifiedAt = BilingualText("كتالوج اتصالات المغرب 2026 يدرج 112 ضمن طوارئ الشرطة ومجاناً؛ لا نعمم ذلك على باقي المشغلين.", "Le catalogue Maroc Telecom 2026 classe le 112 comme urgence Police et gratuit ; ne pas extrapoler aux autres opérateurs."),
            lastIndependentlyVerified = "2026-11-17"
        )
    )

    val civicRightsTips: List<CivicRightTip> = listOf(
        CivicRightTip(
            id = "tip_legalisation",
            category = BilingualText("تبسيط المساطر", "Simplification Admin."),
            title = BilingualText(
                ar = "القانون 55.19: لا حاجة لتصحيح الإمضاء في كل وثيقة",
                fr = "Loi 55-19 : Fin de la légalisation systématique"
            ),
            summary = BilingualText(
                ar = "في نطاق القرارات الإدارية الخاضعة للقانون 55.19، لا تطلب الإدارة أكثر من نسخة واحدة، ولا تصحيح الإمضاء، ولا نسخة مطابقة للأصل كقاعدة؛ ويمكنها طلب الأصل أو نسخة مطابقة للاطلاع مرة واحدة مع تعليل الشك في صحة النسخة. كما لا تطلب وثيقة إدارية متاحة للعموم ولا تعني المرتفق شخصياً.",
                fr = "Pour les décisions administratives couvertes par la loi 55-19, l'administration ne demande en principe qu'un seul exemplaire, sans légalisation de signature ni copie certifiée conforme ; en cas de doute motivé, elle peut demander une fois l'original ou une copie conforme pour consultation. Elle ne demande pas un document administratif public qui ne concerne pas personnellement l'usager."
            ),
            sourceName = BilingualText("الجريدة الرسمية / القانون 55.19 (الأمانة العامة للحكومة)", "Bulletin Officiel / Loi n° 55-19 (SGG)"),
            sourceUrl = "https://www.sgg.gov.ma/BO/bo_ar/2020/BO_6866_Ar.pdf",
            verifiedAt = BilingualText("لم يُنجز بعد تحقق مستقل من الرسوم والآجال والوثائق؛ المرجع الرسمي مرفق للمراجعة", "Frais, délais et pièces non encore vérifiés indépendamment ; source officielle fournie pour contrôle")
        ),
        CivicRightTip(
            id = "tip_consumer_price",
            category = BilingualText("حقوق المستهلك", "Droits Consommateur"),
            title = BilingualText(
                ar = "إجبارية إشهار الأثمان وتسليم الفاتورة",
                fr = "Affichage obligatoire des prix & facture"
            ),
            summary = BilingualText(
                ar = "ينظم القانون 31.08 إعلام المستهلك وإشهار الأسعار والفاتورة، ويقرر حق التراجع في بعض العقود المبرمة عن بعد وفق شروط واستثناءات قانونية؛ تحقق من انطباقه على نوع العقد قبل استعماله.",
                fr = "La loi 31-08 encadre l'information du consommateur, l'affichage des prix et la facture. Le droit de rétractation s'applique à certains contrats à distance, sous conditions et exceptions légales."
            ),
            sourceName = BilingualText("القانون 31.08 — بوابة حماية المستهلك", "Loi 31-08 — Portail du Consommateur"),
            sourceUrl = "https://adala.justice.gov.ma/api/uploads/2024/10/29/%D8%B8%D9%87%D9%8A%D8%B1%20%D8%B4%D8%B1%D9%8A%D9%81%20%D8%B1%D9%82%D9%85%20%20%D8%A8%D8%AA%D8%AD%D8%AF%D9%8A%D8%AF%20%D8%AA%D8%AF%D8%A7%D8%A8%D9%8A%D8%B1%20%D9%84%D8%AD%D9%85%D8%A7%D9%8A%D8%A9%20%D8%A7%D9%84%D9%85%D8%B3%D8%AA%D9%87%D9%84%D9%83-1730189983480.pdf",
            verifiedAt = BilingualText("لم يُنجز بعد تحقق مستقل من الرسوم والآجال والوثائق؛ المرجع الرسمي مرفق للمراجعة", "Frais, délais et pièces non encore vérifiés indépendamment ; source officielle fournie pour contrôle")
        ),
        CivicRightTip(
            id = "tip_leave",
            category = BilingualText("قانون الشغل", "Code du Travail"),
            title = BilingualText(
                ar = "حقك في العطلة السنوية وورقة الأداء",
                fr = "Congé payé & bulletin de paie"
            ),
            summary = BilingualText(
                ar = "لكل أجير اشتغل 6 أشهر متصلة الحق في عطلة سنوية مؤدى عنها، كما يلتزم المشغل بتسليم ورقة الأداء (Bulletin de paie) وشهادة الشغل عند المغادرة.",
                fr = "Tout salarié ayant 6 mois de service continu a droit au congé annuel payé, ainsi qu'au bulletin de paie et au certificat de travail en fin de contrat."
            ),
            sourceName = BilingualText("القانون 65.99 (مدونة الشغل) — بوابة عدالة", "Loi n° 65-99 (Code du Travail) — Adala"),
            sourceUrl = "https://adala.justice.gov.ma/api/uploads/2024/09/05/%D9%85%D8%AF%D9%88%D9%86%D8%A9%20%D8%A7%D9%84%D8%B4%D8%BA%D9%84%20%D8%B5%D9%8A%D8%BA%D8%A9%202021-1725527879588.pdf",
            verifiedAt = BilingualText("لم يُنجز بعد تحقق مستقل من الرسوم والآجال والوثائق؛ المرجع الرسمي مرفق للمراجعة", "Frais, délais et pièces non encore vérifiés indépendamment ; source officielle fournie pour contrôle")
        ),
        CivicRightTip(
            id = "tip_cnss_check",
            category = BilingualText("الضمان الاجتماعي", "MaCNSS"),
            title = BilingualText(
                ar = "راقب تصريحاتك على بوابة MaCNSS",
                fr = "Contrôlez vos jours déclarés sur MaCNSS"
            ),
            summary = BilingualText(
                ar = "تأكد دورياً عبر حسابك في MaCNSS من تصريح المشغل بأيام العمل الفعلية للحفاظ على حقوق التغطية الصحية AMO والتقاعد.",
                fr = "Vérifiez régulièrement sur MaCNSS que votre employeur déclare bien vos jours travaillés afin de préserver votre couverture AMO et retraite."
            ),
            sourceName = BilingualText("الصندوق الوطني للضمان الاجتماعي (CNSS)", "Portail officiel CNSS / MaCNSS"),
            sourceUrl = "https://www.cnss.ma",
            verifiedAt = BilingualText("لم يُنجز بعد تحقق مستقل من الرسوم والآجال والوثائق؛ المرجع الرسمي مرفق للمراجعة", "Frais, délais et pièces non encore vérifiés indépendamment ; source officielle fournie pour contrôle")
        ),
        CivicRightTip(
            id = "tip_plate_info",
            category = BilingualText("الترقيم والرموز البريدية", "Immatriculation & Poste"),
            title = BilingualText(
                ar = "قراءة ترقيم السيارات والرموز البريدية بالمغرب",
                fr = "Comprendre matricules auto et codes postaux"
            ),
            summary = BilingualText(
                ar = "الرقم في أقصى يمين اللوحة يشير لعمالة أو إقليم التسجيل الأول (مثلاً 1 الرباط، 26 مراكش، 33 أكادير)، والرمز البريدي يخص المكتب الرئيسي ويمكن تدقيق الحي عبر codepostal.ma.",
                fr = "Le nombre à droite de la plaque indique la préfecture d'immatriculation (ex. 1 Rabat, 26 Marrakech, 33 Agadir). Pour un quartier précis, vérifiez sur codepostal.ma."
            ),
            sourceName = BilingualText("NARSA وبريد المغرب (codepostal.ma)", "NARSA & Barid Al-Maghrib (codepostal.ma)"),
            sourceUrl = "https://www.codepostal.ma",
            verifiedAt = BilingualText("لم يُنجز بعد تحقق مستقل من الرسوم والآجال والوثائق؛ المرجع الرسمي مرفق للمراجعة", "Frais, délais et pièces non encore vérifiés indépendamment ; source officielle fournie pour contrôle")
        )
    )

    val officialSources: List<OfficialSource> = listOf(
        OfficialSource(
            id = "idarati",
            title = BilingualText("البوابة الوطنية للمساطر والإدارات (إدارتي)", "Portail National des Procédures (Idarati)"),
            domain = "idarati.ma",
            url = "https://www.idarati.ma",
            scope = BilingualText("الوثائق المطلوبة والآجال وفق القانون 55.19", "Référentiel officiel des démarches (Loi 55-19)")
        ),
        OfficialSource(
            id = "sgg_bo",
            title = BilingualText("الجريدة الرسمية — الأمانة العامة للحكومة", "Bulletin Officiel — Secrétariat Général du Gouvernement"),
            domain = "sgg.gov.ma/BO",
            url = "https://www.sgg.gov.ma/BO/",
            scope = BilingualText("القوانين، المراسيم، وقوانين المالية المنشورة رسمياً", "Textes législatifs, décrets et lois de finances")
        ),
        OfficialSource(
            id = "adala",
            title = BilingualText("بوابة عدالة للتشريع — وزارة العدل", "Portail Juridique Adala — Ministère de la Justice"),
            domain = "adala.justice.gov.ma",
            url = "https://adala.justice.gov.ma/",
            scope = BilingualText("مدونة الأسرة، مدونة الشغل، والنصوص القانونية", "Code de la Famille, Code du Travail et textes de lois")
        ),
        OfficialSource(
            id = "watiqa",
            title = BilingualText("بوابة وثيقة لطلب الحالة المدنية", "Portail Watiqa (État Civil)"),
            domain = "watiqa.ma",
            url = "https://www.watiqa.ma/",
            scope = BilingualText("طلب عقود الازدياد عن بعد عبر البريد المضمون", "Commande d'actes de naissance par courrier")
        ),
        OfficialSource(
            id = "cnie",
            title = BilingualText("بوابة البطاقة الوطنية للتعريف الإلكترونية", "Portail officiel CNIE"),
            domain = "cnie.ma",
            url = "https://www.cnie.ma/",
            scope = BilingualText("حجز المواعيد وتتبع طلب البطاقة الوطنية", "Prise de rendez-vous et suivi CNIE")
        ),
        OfficialSource(
            id = "passeport",
            title = BilingualText("خدمة جواز السفر البيومتري", "Service Passeport Biométrique"),
            domain = "passeport.ma/Web/",
            url = "https://www.passeport.ma/Web/",
            scope = BilingualText("تعبئة الاستمارة، الإعفاءات، وتتبع طلب جواز السفر", "Formulaire en ligne, exonérations et suivi passeport")
        ),
        OfficialSource(
            id = "narsa",
            title = BilingualText("بوابة نقل ملكية المركبات (NARSA)", "Portail Mutation Véhicule (NARSA)"),
            domain = "mutationvehicule.ma",
            url = "https://www.mutationvehicule.ma",
            scope = BilingualText("نقل ملكية المركبات والبطاقة الرمادية", "Mutation des véhicules et carte grise")
        ),
        OfficialSource(
            id = "justice",
            title = BilingualText("بوابة السجل العدلي (وزارة العدل)", "Portail Casier Judiciaire"),
            domain = "casierjudiciaire.justice.gov.ma",
            url = "https://casierjudiciaire.justice.gov.ma",
            scope = BilingualText("طلب السجل العدلي عبر الأنترنت", "Demande en ligne du casier judiciaire")
        ),
        OfficialSource(
            id = "cnss",
            title = BilingualText("الصندوق الوطني للضمان الاجتماعي (MaCNSS)", "Portail MaCNSS"),
            domain = "cnss.ma",
            url = "https://www.cnss.ma",
            scope = BilingualText("التغطية الصحية AMO وتتبع ملفات المرض", "Couverture AMO et suivi des dossiers de soins")
        ),
        OfficialSource(
            id = "ae",
            title = BilingualText("السجل الوطني للمقاول الذاتي", "Registre National Auto-Entrepreneur"),
            domain = "rn.ae.gov.ma",
            url = "https://rn.ae.gov.ma",
            scope = BilingualText("التسجيل الرسمي وبطاقة المقاول الذاتي", "Inscription officielle au registre")
        ),
        OfficialSource(
            id = "barid",
            title = BilingualText("بريد المغرب — دليل الرموز البريدية", "Barid Al-Maghrib — Codes Postaux"),
            domain = "codepostal.ma",
            url = "https://www.codepostal.ma",
            scope = BilingualText("التحقق من الرموز البريدية حسب الأحياء والمدن", "Recherche officielle des codes postaux par quartier")
        ),
        OfficialSource(
            id = "consumer_law",
            title = BilingualText("القانون 31.08 لحماية المستهلك", "Loi 31-08 relative à la protection du consommateur"),
            domain = "adala.justice.gov.ma",
            url = "https://adala.justice.gov.ma/api/uploads/2024/10/29/%D8%B8%D9%87%D9%8A%D8%B1%20%D8%B4%D8%B1%D9%8A%D9%81%20%D8%B1%D9%82%D9%85%20%20%D8%A8%D8%AA%D8%AD%D8%AF%D9%8A%D8%AF%20%D8%AA%D8%AF%D8%A7%D8%A8%D9%8A%D8%B1%20%D9%84%D8%AD%D9%85%D8%A7%D9%8A%D8%A9%20%D8%A7%D9%84%D9%85%D8%B3%D8%AA%D9%87%D9%84%D9%83-1730189983480.pdf",
            scope = BilingualText("النص القانوني لحماية المستهلك", "Texte légal relatif à la protection du consommateur")
        ),
        OfficialSource(
            id = "labour_code",
            title = BilingualText("مدونة الشغل — القانون 65.99", "Code du travail — loi 65-99"),
            domain = "adala.justice.gov.ma",
            url = "https://adala.justice.gov.ma/api/uploads/2024/09/05/%D9%85%D8%AF%D9%88%D9%86%D8%A9%20%D8%A7%D9%84%D8%B4%D8%BA%D9%84%20%D8%B5%D9%8A%D8%BA%D8%A9%202021-1725527879588.pdf",
            scope = BilingualText("النص المحين المتاح لمدونة الشغل", "Version disponible du Code du travail")
        ),
        OfficialSource(
            id = "auto_entrepreneur_law",
            title = BilingualText("القانون 114.13 المتعلق بنظام المقاول الذاتي", "Loi 114-13 relative au statut de l'auto-entrepreneur"),
            domain = "adala.justice.gov.ma",
            url = "https://adala.justice.gov.ma/api/uploads/2024/07/09/%D8%B8%D9%87%D9%8A%D8%B1%20%D8%B4%D8%B1%D9%8A%D9%81%20%D8%A8%D8%AA%D9%86%D9%81%D9%8A%D8%B0%20%D8%A7%D9%84%D9%82%D8%A7%D9%86%D9%88%D9%86%20%D8%B1%D9%82%D9%85%20114.13%20%D8%A7%D9%84%D9%85%D8%AA%D8%B9%D9%84%D9%82%20%D8%A8%D9%86%D8%B8%D8%A7%D9%85%20%D8%A7%D9%84%D9%85%D9%82%D8%A7%D9%88%D9%84%20%D8%A7%D9%84%D8%B0%D8%A7%D8%AA%D9%8A-1720535300284.pdf",
            scope = BilingualText("النص القانوني لنظام المقاول الذاتي", "Texte légal du statut auto-entrepreneur")
        ),
        OfficialSource(
            id = "civil_status_law",
            title = BilingualText("القانون 36.21 المتعلق بالحالة المدنية", "Loi 36-21 relative à l'état civil"),
            domain = "adala.justice.gov.ma",
            url = "https://adala.justice.gov.ma/api/uploads/2026/04/16/%D8%B8%D9%87%D9%8A%D8%B1%20%D8%B4%D8%B1%D9%8A%D9%81%20%D8%B1%D9%82%D9%85%201.21.81%20%D8%A8%D8%AA%D9%86%D9%81%D9%8A%D8%B0%20%D8%A7%D9%84%D9%82%D8%A7%D9%86%D9%88%D9%86%20%D8%B1%D9%82%D9%85%2036.21%20%D8%A7%D9%84%D9%85%D8%AA%D8%B9%D9%84%D9%82%20%D8%A8%D8%A7%D9%84%D8%AD%D8%A7%D9%84%D8%A9%20%D8%A7%D9%84%D9%85%D8%AF%D9%86%D9%8A%D8%A9%20%281%29-1776328405081.pdf",
            scope = BilingualText("النص القانوني للحالة المدنية", "Texte légal de l'état civil")
        ),
        OfficialSource(
            id = "anrt_emergency",
            title = BilingualText("الوكالة الوطنية لتقنين المواصلات — الأرقام الخاصة", "ANRT — Numéros spéciaux"),
            domain = "anrt.ma",
            url = "https://www.anrt.ma/infos-pratiques/que-sont-les-numeros-speciaux",
            scope = BilingualText("تأكيد الأرقام القصيرة متعددة المشغلين: 19 الشرطة و177 الدرك و15 الوقاية المدنية", "Confirmation des numéros courts multi-opérateurs : 19 Police, 177 Gendarmerie, 15 Pompiers")
        ),
        OfficialSource(
            id = "law_5519",
            title = BilingualText("القانون 55.19 — النص المنشور بالجريدة الرسمية", "Loi 55-19 — Bulletin officiel"),
            domain = "sgg.gov.ma",
            url = "https://www.sgg.gov.ma/BO/bo_ar/2020/BO_6866_Ar.pdf",
            scope = BilingualText("النص الأصلي للقانون رقم 55.19 المنشور في الجريدة الرسمية عدد 6866", "Texte original de la loi 55-19, Bulletin officiel n° 6866")
        ),
        OfficialSource(
            id = "sante",
            title = BilingualText("وزارة الصحة والحماية الاجتماعية", "Ministère de la Santé"),
            domain = "sante.gov.ma",
            url = "https://www.sante.gov.ma/urgences/Pages/default.aspx",
            scope = BilingualText("صفحة الوزارة الرسمية لأرقام استعجالات المستشفيات حسب المدن", "Page officielle des urgences hospitalières par ville")
        )
    )
}
