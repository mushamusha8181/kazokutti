package jp.mushamusha.kazokucchi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import jp.mushamusha.mushinglass.MushinGlass

class MainActivity : ComponentActivity() {
    private val micPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { }
    private val notificationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences("kazokucchi", Context.MODE_PRIVATE)
        setContent {
            MushinTheme {
                var onboardingDone by remember { mutableStateOf(prefs.getBoolean("onboarding_done", false)) }
                if (onboardingDone) {
                    VoiceHome()
                } else {
                    Onboarding(
                        requestMic = {
                            if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                                micPermission.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        },
                        requestNotification = {
                            if (Build.VERSION.SDK_INT >= 33 && ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
                            }
                        },
                        finish = {
                            prefs.edit().putBoolean("onboarding_done", true).apply()
                            onboardingDone = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun Onboarding(
    requestMic: () -> Unit,
    requestNotification: () -> Unit,
    finish: () -> Unit
) {
    var page by remember { mutableIntStateOf(0) }
    var backgroundConversation by remember { mutableStateOf(false) }
    val titles = listOf("家族っちへようこそ", "声で話そう", "バックグラウンド会話", "準備完了")
    val bodies = listOf(
        "家族っちは、自然なライブ音声会話を中心にした話し相手AIアプリです。",
        "マイク権限を許可すると、声で会話できるようになります。",
        "バックグラウンド会話を使う場合だけ通知権限を利用します。",
        "最初は音声モードから始まります。設定はあとから変更できます。"
    )

    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.74f),
                shape = MaterialTheme.shapes.large,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.34f)),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("${page + 1} / 4", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(titles[page], style = MaterialTheme.typography.headlineMedium)
                    Text(bodies[page], style = MaterialTheme.typography.bodyLarge)

                    if (page == 1) {
                        GlassButton("マイクを許可", requestMic)
                    }
                    if (page == 2) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Switch(checked = backgroundConversation, onCheckedChange = {
                                backgroundConversation = it
                                if (it) requestNotification()
                            })
                            Spacer(Modifier.width(12.dp))
                            Text("バックグラウンド会話を使う")
                        }
                    }
                }
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = { if (page > 0) page-- }, enabled = page > 0) { Text("戻る") }
                GlassButton(if (page < 3) "次へ" else "はじめる") {
                    if (page < 3) page++ else finish()
                }
            }
        }
    }
}

@Composable
private fun VoiceHome() {
    Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.74f),
                shape = MaterialTheme.shapes.large,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.34f)),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 22.dp, vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🦔", style = MaterialTheme.typography.displayLarge)
                    Spacer(Modifier.height(12.dp))
                    Text("家族っち", style = MaterialTheme.typography.headlineMedium)
                    Spacer(Modifier.height(8.dp))
                    Text("音声モード", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(24.dp))
                    GlassButton("🎤 話しかける") { }
                    Spacer(Modifier.height(12.dp))
                    Text("AI接続・音声認識/TTS・履歴・Bluetooth等は次段階で接続するスターターです。", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun GlassButton(label: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.80f),
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.38f)),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(label)
    }
}

@Composable
private fun MushinTheme(content: @Composable () -> Unit) {
    val context = LocalContext.current
    val cfg = MushinGlass.config(context)
    val fontId = remember { context.resources.getIdentifier("mushin", "font", context.packageName) }
    val family = remember(fontId) { if (fontId != 0) FontFamily(Font(fontId)) else FontFamily.Default }
    val alpha = (cfg.glassOpacity / 100f).coerceIn(0.35f, 0.95f)
    val colors = lightColorScheme(
        primary = Color(cfg.palette.primary),
        onPrimary = Color(cfg.textColor),
        primaryContainer = Color(cfg.band).copy(alpha = alpha),
        onPrimaryContainer = Color(cfg.textColor),
        background = Color(cfg.background),
        onBackground = Color(cfg.textColor),
        surface = Color(cfg.rowBackground).copy(alpha = alpha),
        onSurface = Color(cfg.textColor),
        surfaceVariant = Color(cfg.listBackground).copy(alpha = alpha),
        onSurfaceVariant = Color(cfg.auxiliary),
        outline = Color(cfg.middle).copy(alpha = 0.55f)
    )
    MaterialTheme(
        colorScheme = colors,
        typography = mushinTypography(family),
        shapes = Shapes(
            extraSmall = RoundedCornerShape(8.dp),
            small = RoundedCornerShape(10.dp),
            medium = RoundedCornerShape(14.dp),
            large = RoundedCornerShape(18.dp),
            extraLarge = RoundedCornerShape(24.dp)
        ),
        content = content
    )
}

private fun TextStyle.mushin(family: FontFamily) = copy(fontFamily = family)
private fun mushinTypography(family: FontFamily): Typography {
    val t = Typography()
    return Typography(
        displayLarge = t.displayLarge.mushin(family), displayMedium = t.displayMedium.mushin(family), displaySmall = t.displaySmall.mushin(family),
        headlineLarge = t.headlineLarge.mushin(family), headlineMedium = t.headlineMedium.mushin(family), headlineSmall = t.headlineSmall.mushin(family),
        titleLarge = t.titleLarge.mushin(family), titleMedium = t.titleMedium.mushin(family), titleSmall = t.titleSmall.mushin(family),
        bodyLarge = t.bodyLarge.mushin(family), bodyMedium = t.bodyMedium.mushin(family), bodySmall = t.bodySmall.mushin(family),
        labelLarge = t.labelLarge.mushin(family), labelMedium = t.labelMedium.mushin(family), labelSmall = t.labelSmall.mushin(family)
    )
}
