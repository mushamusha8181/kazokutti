# 家族っち Android starter — recovered

保存されていた「家族っち Android starter」の仕様から復元したAndroid Studio用スタータープロジェクトです。

実装済み:
- Compose project skeleton
- 4ページ初回オンボーディング
- マイク権限要求
- バックグラウンド会話選択時のみ通知権限要求
- 初回完了後の音声ホーム画面
- 仕様書分割

未接続:
- AI network clients/back-end
- Speech recognition/TTS engines
- background microphone foreground service
- Bluetooth routing
- chat database/history UI
- mascot production assets/animation
- Wear OS module

保存資料のビルド基準:
- Android Gradle Plugin 9.4.0
- Compose BOM 2026.09.00
- compileSdk 37 / targetSdk 36

Android Studioでこのフォルダを開き、Gradle Syncしてください。
