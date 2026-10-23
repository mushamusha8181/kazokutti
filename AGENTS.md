# 家族っち AGENTS.md

このファイルは開発エージェントの入口。詳細は用途ごとの仕様書を読む。

## Product goal
Android向けの「話し相手」AIアプリ。チャット機能も持つが、主役は自然なライブ音声会話。
アプリ名は「家族っち」。初期キャラクターは太めの中年ハリネズミ男性。メガネあり。初期語尾は「ハリー」。

## Read these specs before touching related code
- Product / screen behavior: `docs/PRODUCT.md`
- First launch and permissions: `docs/ONBOARDING.md`, `docs/PERMISSIONS.md`
- Voice input/output and Bluetooth: `docs/VOICE.md`
- AI providers and API keys: `docs/AI_PROVIDERS.md`
- Character behavior / male-female switching: `docs/CHARACTERS.md`
- User/family profile and memory: `docs/MEMORY.md`
- History retention and bulk deletion: `docs/HISTORY.md`
- Wear OS companion: `docs/WEAR_OS.md`

## Non-negotiable defaults
- Start in voice mode.
- Proactive AI speaking interval: random.
- Daily live-chat limit: configurable 1-24 hours, reset at 00:00 local time.
- Conversation auto-delete default: 30 days; pinned conversations survive.
- Smartphone-control mode default: OFF.
- Adult mascot behaviors (beer/smoking etc.) default: ON, but user can disable.
- Standard hosted AI key is the default. Personal API keys are optional.
- Minimum personal providers supported: OpenAI, Anthropic Claude, Google Gemini, DeepSeek.
