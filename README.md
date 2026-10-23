# 家族っち (Kazokucchi) Android alpha 0.3.0

ハリネズミの「家族っち」と、音声中心で長く話せるAndroid向けDeepSeekクライアントです。スマホ側アプリに加えて簡易Wear OS companionも含みます。

## 0.3.0で入っている主な機能

- メス / オスのハリネズミ切替とランチャーアイコン連動
- Android SpeechRecognizerによる連続音声入力
- Android TTSによる返答読み上げ
- Foreground Serviceによるバックグラウンド会話
- マイク自動OFF、1日ライブ上限、ランダム自発会話
- Bluetooth / 本体スピーカーの音声ルート切替
- テキストチャット、カメラ、画像添付（任意ファイル添付は誤動作防止のため廃止）
- 会話履歴の複数選択 / 全選択 / 一括削除 / 自動削除
- 性格、方言/語尾、家族情報、本人プロフィール
- **確認式の記憶カード** — 「覚えて」と言われた内容を保存前に確認
- **履歴を残さないモード** — 履歴保存なし、家族/本人プロフィール/記憶カードをAPIへ送らない。質問本文自体はDeepSeekへHTTPS送信
- **DeepSeek API使用量** — 月間リクエスト / token / 料金上限目安、月額上限設定
- **スマホ操作3段階** — 提案だけ / 毎回確認 / 許可済み操作は自動
- 朝のひとこと + 当日のカレンダー予定 / 夜のひとこと
- 「今は話しかけないで」時間帯
- ドライブモード + 通知アクセス（「通知読んで」の明示操作時のみ）
- 緊急連絡先ダイヤル / 現在地共有
- パスワード付きAES-GCM暗号化バックアップ / 復元（APIキーは除外）
- ハリネズミの listening / thinking / speaking / mic-off 状態アニメーション
- Wear OS簡易 companion（音声質問、返答表示・TTS、スマホ会話開始は通知タップ経由）
- 初回起動の権限・前提サービスセットアップ


## 小さなスマホ向けUI最適化

- 画面幅360dp以下、または画面高640dp以下をコンパクト端末として自動判定
- タップ領域は原則48dpを維持しつつ、余白・タイトル・ボタン文字を圧縮
- ライブ画面のハリネズミを固定220dpから端末幅連動の132〜196dpへ変更
- チャット入力欄は最大3〜4行に制限し、会話一覧を圧迫しにくくした
- 設定の複数行入力欄はコンパクト端末では初期高さを縮小し、必要時だけスクロール
- 4分割の下部ナビ、2〜3分割の操作列は横幅320〜360dpでも収まりやすい文字サイズに調整
- 権限セットアップの長いボタン文言は2行まで許可し、縦方向の占有を抑制

## 初回権限セットアップ

初回起動で、実装済み機能に必要な標準権限をまとめて要求します。その後、通常のランタイム権限では扱えない以下の特殊アクセスを順番に案内します。

- 通知へのアクセス
- バッテリー最適化の解除
- 音声入力サービス確認
- TTS確認
- アプリ詳細設定（Galaxy等のOEM省電力設定への入口）

Android側のシステム画面そのものはアプリから点滅・自動許可できないため、**家族っち側の「ここだよ」ボタンを点滅 → 正式なAndroid設定画面を開く**方式です。詳細は `docs/PERMISSIONS.md`。

## DeepSeek APIキーの安全設計

- APIキーをソースコード、APK、BuildConfig、リソース、Gitへ埋め込まない
- 設定画面で入力したAPIキーをAndroid Keystoreの非エクスポートAES-256鍵でAES-GCM暗号化して端末内保存
- 平文APIキーをSharedPreferencesや通常ファイルへ保存しない
- 保存済みキーは設定画面へ再表示しない
- Androidバックアップを無効化
- 暗号化バックアップにもAPIキーを含めない
- 平文HTTP通信を無効化
- DeepSeek公式 `https://api.deepseek.com/chat/completions` にのみ送信
- HTTPリダイレクト自動追従を無効化
- APIキーをログ、通知、クリップボードへ出さない

API利用時には認証のため、キーがDeepSeek公式APIへHTTPSで送信されます。第三者プロキシへは送らない設計です。

## API使用量

0.2.1は `deepseek-flash` を使用し、APIレスポンスの `usage` を端末内で集計します。ライブ会話ではThinkingを無効化し、通常チャットは低い推論強度に抑えて待ち時間と費用を制御します。料金表示は2026-10-22時点のDeepSeek Flashピーク料金を使った上限寄りの概算です。実際の料金はDeepSeek側の料金改定・キャッシュ・時間帯で変わるため、最終的な請求はDeepSeek側を確認してください。

## Wear OS

`wear` モジュールは Google Play services Wearable Data Layer (`play-services-wearable:20.0.1`) を使います。電話側と時計側を同じ署名・同じapplicationIdでビルドして利用します。

## 現在の制限

- 外部Whisper / VOICEVOX / AquesTalkのモデル管理UIはまだ未接続
- 本格フルデュプレックスAEC / 高度なバージインは未実装
- 音声ルートはAndroidの通信デバイス単位で切替。入力/出力を別々に指定するUIは廃止
- Android 14+のバックグラウンドマイク起動制限を避けるため、Wear OSからの会話開始はスマホ通知を表示し、ユーザーのタップから開始
- Galaxy等の独自「自動起動」設定はAndroid標準APIがないため、アプリ詳細設定まで案内し、最終操作はユーザーが行う

## ビルド

Android SDK 35 + JDK 17。Gradle Wrapperは同梱していないため、Android Studioからのビルドを推奨します。ローカルに互換Gradleがある場合は `gradle :app:assembleDebug` / `gradle :wear:assembleDebug` を利用してください。


## 0.3.0 健康診断で修正した点

- 新規会話で別セッションの履歴がDeepSeekへ混入する問題を修正
- ライブ会話のAI多重リクエストを抑止し、停止時は通信をキャンセル
- TTSの文字数タイマーによる早すぎるマイク復帰を廃止し、onDone中心へ統一
- SpeechRecognizerの異常時に無限高速再試行しないよう指数バックオフと停止上限を追加
- 無音/NO_MATCHは異常回数に数えず、通常の連続待受として再開
- ServiceのOS再生成で意図せずマイク開始しないよう `START_NOT_STICKY` 化
- マイク自動OFF後に通知から再開できる操作を追加
- Wear OSからバックグラウンドでマイクFGSを直接起動する処理を廃止
- 朝夕通知からExact Alarm依存を除去し、特殊権限を削減
- 添付を画像だけに制限し、未対応ファイル送信を削除
- 暗号化バックアップは10文字以上のパスワード、50MB入力上限、PBKDF2 310,000回に強化
- 音声会話終了時にAudioManagerの通信モード/通信デバイスを復元
- SOS現在地共有はAndroid 11+で可能なら現在位置を新規取得してから共有
- Wear UIの生px指定をdpへ修正

### 0.3.3-alpha: image attachment optimization
Image attachments are now processed on-device before API submission. The long edge is capped at 800 px, JPEG quality is reduced only as needed, transparent images are flattened onto white, and oversized source images are decoded with sampling to reduce memory pressure. This keeps screenshots/photos readable while avoiding needless image upload/vision cost. The original user file is not modified.
