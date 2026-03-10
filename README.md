# Strategy Bot — Grand Unified Auto-Bot

![Build APK](https://github.com/YOUR_USERNAME/YOUR_REPO/actions/workflows/build-apk.yml/badge.svg)

Deriv/Binary.com trading bot — Android WebView app with Grand Unified Analysis Engine (10 rules), DeepSeek AI, live digit stream, and auto-bot mode.

---

## ⬇️ Get the APK (No setup needed)

1. Go to **[Actions](../../actions)** tab
2. Click **Build APK** → **Run workflow** → **Run workflow**
3. Wait ~3 min → click the finished run → download **StrategyBot-Debug-APK**

---

## 🔧 Build locally

```bash
git clone https://github.com/YOUR_USERNAME/YOUR_REPO
cd YOUR_REPO
chmod +x gradlew

# Download wrapper jar first
curl -fsSL "https://github.com/gradle/gradle/raw/v8.4.0/gradle/wrapper/gradle-wrapper.jar" \
  -o gradle/wrapper/gradle-wrapper.jar

./gradlew assembleDebug
# → app/build/outputs/apk/debug/app-debug.apk
```

---

## 🔑 Signed release (optional)

Add 4 secrets in **Settings → Secrets → Actions**:

| Secret | Value |
|--------|-------|
| `KEYSTORE_BASE64` | `base64 -w0 your.keystore` |
| `KEY_ALIAS` | your key alias |
| `KEY_PASSWORD` | key password |
| `STORE_PASSWORD` | store password |

Generate keystore:
```bash
keytool -genkey -v -keystore strategybot.keystore \
  -alias strategybot -keyalg RSA -keysize 2048 -validity 10000
base64 -w0 strategybot.keystore   # paste this as KEYSTORE_BASE64
```

---

## 📋 Info

| | |
|-|-|
| Package | `com.strategybot.app` |
| Min Android | 5.1 (API 22) |
| Target | Android 14 (API 34) |
| Version | 1.0.0 |
