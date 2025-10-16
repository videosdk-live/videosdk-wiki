# 🧠 Vision-Based AI Character with VideoSDK

A quickstart React Native example to showcase **real-time, voice-only interactions** with AI Characters using VideoSDK’s **Character SDK**.

> Talk to lifelike AI Characters through audio in a low-bandwidth, real-time setting — ideal for call centers, telephony, voice bots, and support use cases.

---

## Setup Guide

- Sign up on [VideoSDK](https://app.videosdk.live/) and visit [API Keys](https://app.videosdk.live/api-keys) section to get your API key and Secret key.

- Get familiarized with [Authentication and tokens](https://docs.videosdk.live/react-native/guide/video-and-audio-calling-api-sdk/authentication-and-token)

---

## ✨ Features

- 🎥 Real-time WebRTC-based video chat with AI
- 🧍 Vision-powered, 1-on-1 character interaction
- 🧠 Supports persona, memory, multilingual input
- 🧩 Easily customizable UI & behavior

---

## 🚀 Getting Started

### 📦 Prerequisites

- Node.js v12+
- NPM v6+ (comes installed with newer Node versions)
- Android Studio or Xcode installed
- Valid [Video SDK Account](https://app.videosdk.live/signup)

## Run the Sample App

### Step 1: Clone the sample project

Clone the repository to your local environment.

```js
git clone https://github.com/videosdk-live/character-sdk-react-native-quickstart
cd character-sdk-react-native-quickstart
cd videosdk-react-character-voice
```

### Step 2: Update the `api.js` file.

Update the `api.js` file with your Authentication Token generated from [VideoSDK Dashboard](https://app.videosdk.live/api-keys).

### Step 3: Install the dependecies

Install all the dependecies to run the project.

```js
npm install
```

for iOS

```js
cd ios && pod install
```

### Step 4: Start Metro Server

```js
npm run start
```

### Step 5: Run the App

Bingo, it's time to push the launch button.

```js
npm run android
npm run ios
```

---

## 📁 Project Structure

```
videosdk-react-character-voice/
├── android/                  # Native Android project (Java/Kotlin)
├── ios/                      # Native iOS project (Swift/Obj-C)
├── .gitignore                # Git ignore rules
├── App.js                    # Main App component
├── Gemfile                   # iOS dependency manager (via Bundler for CocoaPods)
├── README.md                 # Project documentation
├── api.js                    # Likely handles REST/WebSocket API communication
├── app.json                  # App metadata and configuration
├── babel.config.js           # Babel config for JS/TS transpilation
├── index.js                  # Entry point of the app
├── metro.config.js           # Metro bundler config (custom resolver, assets, etc.)
├── package-lock.json         # Lock file for npm dependencies
├── package.json              # Project dependencies, scripts, metadata
├── tsconfig.json             # TypeScript configuration (optional TS support)
└── yarn.lock                 # Lock file for yarn dependencies
```

---

## ❓ Need Help?

- 💬 [Join Discord](https://discord.com/invite/f2WsNDN9S5)
- 📧 [Email Support](mailto:support@videosdk.live)

---

> Made with ❤️ by [VideoSDK](https://videosdk.live)
