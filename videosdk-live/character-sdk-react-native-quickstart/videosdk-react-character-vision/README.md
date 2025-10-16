# 🧠 Vision-Based AI Character with VideoSDK

A quickstart React Native example to showcase **real-time, face-to-face interactions** with AI Characters using VideoSDK’s **Character SDK**.

> Talk to AI Characters that can see, listen, and respond in real-time — perfect for healthcare, edtech, KYC, customer support, and more.

<br/>

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
cd videosdk-react-character-vision
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
videosdk-react-character-vision/
├── android/                  # Native Android project
├── ios/                      # Native iOS project
├── .gitignore                # Git ignore rules
├── App.js                    # Main React Native App component
├── Gemfile                   # Ruby dependencies (used by iOS, e.g., CocoaPods)
├── README.md                 # Project documentation
├── api.js                    # Likely handles API interactions
├── app.json                  # App configuration
├── babel.config.js           # Babel configuration for transpiling JS
├── index.js                  # Entry point of the app
├── metro.config.js           # Metro bundler config for React Native
├── package-lock.json         # NPM dependency lock file
├── package.json              # Project dependencies and scripts
├── tsconfig.json             # TypeScript configuration (even if TS isn't fully used)
└── yarn.lock                 # Yarn dependency lock file
```

---

## ❓ Need Help?

- 💬 [Join Discord](https://discord.com/invite/f2WsNDN9S5)
- 📧 [Email Support](mailto:support@videosdk.live)

---

> Made with ❤️ by [VideoSDK](https://videosdk.live)
