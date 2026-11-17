package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AppLanguage

private const val CNDP_LAW_URL = "https://www.cndp.ma/textes-et-lois/"
private const val CNDP_FORMALITIES_URL = "https://www.cndp.ma/formalites/"

@Composable
fun LegalInfoScreen(language: AppLanguage) {
    val isAr = language == AppLanguage.AR
    val context = LocalContext.current
    val scroll = rememberScrollState()

    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(scroll).padding(horizontal = 20.dp, vertical = 18.dp).testTag("legal_info_content"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(if (isAr) "الخصوصية والشروط" else "Confidentialité et conditions", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text(if (isAr) "ميزان المغرب — الإصدار 1.1.0" else "Mizan Maroc — version 1.1.0", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)

        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(if (isAr) "سياسة الخصوصية" else "Politique de confidentialité", modifier = Modifier.testTag("legal_privacy_title"), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text(if (isAr) "في هذه النسخة، لا يطلب التطبيق إنشاء حساب ولا يرسل بياناتك إلى خادم تابع له. تفضيلات اللغة والمظهر وقوائم الوثائق المحفوظة تُخزّن محلياً على جهازك. مسودات الرسائل تبقى في ذاكرة التطبيق أثناء الاستعمال ولا تُرسل تلقائياً." else "Dans cette version, l’application ne demande pas de créer un compte et n’envoie pas vos données à un serveur exploité par l’application. Les préférences de langue et de thème ainsi que les listes de pièces cochées sont stockées localement sur votre appareil. Les brouillons restent en mémoire pendant l’utilisation et ne sont pas transmis automatiquement.", style = MaterialTheme.typography.bodyMedium)
                Text(if (isAr) "عند فتح رابط رسمي، ينتقل الرابط إلى تطبيق أو متصفح خارجي؛ وتخضع أي بيانات تقدمها هناك لسياسة الجهة الخارجية. لا تضع أرقام بطاقتك أو بيانات حساسة في رسالة قبل التأكد من الجهة المستقبلة." else "Lorsqu’un lien officiel est ouvert, il est transmis à une application ou un navigateur externe ; les données que vous y fournissez relèvent alors de la politique de ce tiers. N’insérez pas de numéro d’identité ni de donnée sensible dans un message sans vérifier le destinataire.", style = MaterialTheme.typography.bodyMedium)
                Text(if (isAr) "يمكن حذف التفضيلات والبيانات المحلية بإزالة التطبيق من الجهاز. إذا أُضيف مستقبلاً جمع بيانات أو تحليلات أو مزامنة سحابية، يجب تحديث هذه السياسة وإجراء إجراءات الامتثال اللازمة قبل تفعيلها." else "Vous pouvez supprimer les préférences et données locales en désinstallant l’application. Si une collecte, des outils d’analyse ou une synchronisation cloud sont ajoutés ultérieurement, cette politique et les formalités de conformité devront être mises à jour avant leur activation.", style = MaterialTheme.typography.bodyMedium)
                OutlinedButton(onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(CNDP_LAW_URL))) }) {
                    Text(if (isAr) "قانون 09-08 — CNDP" else "Loi 09-08 — CNDP")
                    Spacer(Modifier.width(8.dp)); Icon(Icons.Default.OpenInNew, contentDescription = null)
                }
                OutlinedButton(onClick = { context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(CNDP_FORMALITIES_URL))) }) {
                    Text(if (isAr) "إجراءات التصريح لدى CNDP" else "Formalités CNDP")
                    Icon(Icons.Default.OpenInNew, contentDescription = null)
                }
            }
        }

        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow), modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.Default.Gavel, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Text(if (isAr) "شروط الاستخدام" else "Conditions d’utilisation", modifier = Modifier.testTag("legal_terms_title"), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                Text(if (isAr) "التطبيق دليل معلوماتي عام، وليس استشارة قانونية أو إدارية ولا يمثل أي مؤسسة حكومية مغربية. قد تتغير القوانين والرسوم والآجال والوثائق والأرقام. يجب التأكد من الجهة المختصة والمصدر الرسمي قبل اتخاذ قرار أو دفع أي مبلغ." else "L’application est un guide d’information générale. Elle ne constitue pas un conseil juridique ou administratif et ne représente aucune administration marocaine. Les lois, frais, délais, pièces et numéros peuvent changer. Vérifiez auprès du service compétent et de la source officielle avant toute décision ou paiement.", style = MaterialTheme.typography.bodyMedium)
                Text(if (isAr) "الروابط الخارجية مقدمة لتسهيل الوصول إلى المصادر؛ لا نضمن توفر مواقع الجهات الخارجية أو استمرار محتواها. استخدم النماذج كمسودات وعدّلها بما يناسب وضعك." else "Les liens externes facilitent l’accès aux sources ; la disponibilité et le contenu des sites tiers ne sont pas garantis. Utilisez les modèles comme brouillons et adaptez-les à votre situation.", style = MaterialTheme.typography.bodyMedium)
                Text(if (isAr) "لا يتحمل التطبيق مسؤولية رفض ملف أو تأخر إجراء بسبب اختلاف الشروط المحلية أو تغير القواعد بعد آخر مراجعة. لا تستخدم التطبيق للطوارئ؛ اتصل مباشرة بالخدمات المختصة." else "L’application ne peut garantir l’acceptation d’un dossier ni le délai d’une procédure, notamment si les règles locales ont changé. En cas d’urgence, contactez directement les services compétents.", style = MaterialTheme.typography.bodyMedium)
            }
        }
        Text(if (isAr) "تواصل / Contact : لم يُحدد ناشر التطبيق عنوان دعم رسمي بعد. يجب إضافته قبل النشر العام." else "Contact : aucune adresse officielle de support n’a encore été fournie par l’éditeur. Elle devra être ajoutée avant la publication publique.", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(12.dp))
    }
}
