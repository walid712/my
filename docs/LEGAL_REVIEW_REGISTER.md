# سجل مراجعة المساطر / Registre de vérification juridique

**Date de l’audit technique : 2026-11-17**

> هذا تاريخ مراجعة ملفات المصدر فقط، وليست تاريخ تحقق قانوني من الرسوم أو الآجال أو الوثائق. لا توجد حالياً تواريخ تحقق قانوني مستقلة معتمدة. يبقى `lastIndependentlyVerified = null` إلى حين مراجعة كل معلومة وتوثيق المادة أو الصفحة الرسمية.

| ID | Lien de référence actuellement renseigné (à revalider) | État des frais, délais et pièces |
|---|---|---|
| `cnie` | https://www.cnie.ma/ | Non vérifiés indépendamment ; confirmer auprès de la source et du service compétent. |
| `passport` | https://www.passeport.ma/Web/ | Non vérifiés indépendamment ; confirmer auprès de la source et du service compétent. |
| `casier` | https://casierjudiciaire.justice.gov.ma | Non vérifiés indépendamment ; confirmer auprès de la source et du service compétent. |
| `auto_entrepreneur` | https://rn.ae.gov.ma | Non vérifiés indépendamment ; confirmer auprès de la source et du service compétent. |
| `cnss_amo` | https://www.cnss.ma | Non vérifiés indépendamment ; confirmer auprès de la source et du service compétent. |
| `carte_grise` | https://www.mutationvehicule.ma | Non vérifiés indépendamment ; confirmer auprès de la source et du service compétent. |
| `mariage` | https://adala.justice.gov.ma/api/uploads/2024/07/22/%D9%85%D8%AF%D9%88%D9%86%D8%A9%20%D8%A7%D9%84%D8%A3%D8%B3%D8%B1%D8%A9-1721646687560.pdf | Non vérifiés indépendamment ; confirmer auprès de la source et du service compétent. |
| `residence_naissance` | https://www.watiqa.ma/ | Non vérifiés indépendamment ; confirmer auprès de la source et du service compétent. |

## Règles de validation avant publication
- Pour chaque tarif : référence fiscale ou administrative officielle en vigueur, rubrique précise, date de consultation.
- Pour chaque délai : distinguer délai légal, délai indicatif annoncé par le service et délai constaté.
- Pour chaque document : vérifier la catégorie de demande (première demande, renouvellement, perte, mineur, majeur, résidence).
- Ne jamais extrapoler une règle générale à toutes les communes ou tous les cas.
- Si la source ne donne pas le détail, afficher « à confirmer auprès du service » plutôt qu’une valeur présentée comme certaine.

## Références transversales officielles
- CNDP, textes et lois : https://www.cndp.ma/textes-et-lois/
- CNDP, formalités : https://www.cndp.ma/formalites/
- Bulletin officiel, loi 09-08 : https://www.sgg.gov.ma/BO/bo_fr/2009/bo_5714_fr.pdf
- Portail Adala : https://adala.justice.gov.ma/


## Sources institutionnelles générales

Les entrées `OfficialSource` dans l'application disposent également d'un champ `lastIndependentlyVerified`. Il reste `null` tant que l'adresse, le contenu et le champ d'application de chaque portail n'ont pas été testés et documentés individuellement. Les liens CNDP sont consultés comme référence officielle pour la loi 09-08, les formalités de déclaration et les obligations de transparence ; ils ne signifient pas que l'éditeur a déjà déposé une déclaration.


## أرقام الطوارئ — تحقق مستقل بتاريخ 2026-11-17

| الرقم | الجهة | ما يثبته المصدر | النطاق / التحفظ |
|---|---|---|---|
| 19 | الشرطة | ANRT تسمي 19 للشرطة وتذكر إتاحته من الثابت والمحمول | موثق لدى ANRT |
| 177 | الدرك الملكي | ANRT تسمي 177 للدرك الملكي وتذكر إتاحته من الثابت والمحمول | موثق لدى ANRT |
| 15 | الإطفائيون / الوقاية المدنية | ANRT تسمي 15 للإطفائيين وتذكر إتاحته من الثابت والمحمول | موثق لدى ANRT |
| 112 | طوارئ الشرطة عبر المحمول | كتالوج Maroc Telecom B2C 2026 يدرجه كـ Urgences Police ويظهره مجانياً | مصدر المشغل Maroc Telecom؛ لا نعمم التوفر أو التعرفة على باقي المشغلين دون مصدرهم |

Sources :
- ANRT, numéros spéciaux : https://www.anrt.ma/infos-pratiques/que-sont-les-numeros-speciaux
- Maroc Telecom, catalogue B2C 2026 : https://www.iam.ma/documents/66341/0/Catalogue%2Bdes%2Boffres%2BMobile%2BB2C%2B-%2BJuin%2B22-2026.pdf/2b18ea63-7178-51bf-ea4e-3df27b76bfa6?t=1782141863456
