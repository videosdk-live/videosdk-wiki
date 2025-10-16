# videosdk-react-native-pip-mode-example

## Overview

Picture-in-Picture (PiP) mode allows users to continue a video call while using other apps. This feature is useful for multitasking, such as taking notes or browsing the web during a call.

## Setup Guide

- Sign up on [VideoSDK](https://www.videosdk.live/) and visit the [API Keys](https://app.videosdk.live/api-keys) section to get your API key and Secret key.
- Get familiarized with [Token](https://docs.videosdk.live/flutter/guide/video-and-audio-calling-api-sdk/authentication-and-tokens).

## Prerequisites

Before running the project, ensure you have:

- Node.js ≥ 14.x
- React Native CLI
- Valid [VideoSDK Account](https://app.videosdk.live/signup)

## Installation & Setup

- Clone the Repository

```sh
git clone https://github.com/videosdk-live/videosdk-react-native-pip-mode-example.git
cd videosdk-react-native-pip-mode-example
```

- Install Dependencies

```sh
npm install
```

- Add VideoSDK Token

Update the token in the api.js file:

```sh
String token = "Your VideoSDK token";
```

- Run the App

**for android**

```sh
npx react-native run-android
```

**for ios**

```sh
cd ios
pod install
cd ..
npx react-native run-ios
```

## PiP Mode Usage

1. Launch the app on your device.
2. Start a video call using the built-in UI.
3. PiP mode activates automatically when you press the PiP button or send the app to the background.
4. The video will shrink into a floating window, allowing you to multitask while keeping the call visible.
5. Returning to the app will exit PiP mode and restore the full-screen view.

## Resources
- [Official VideoSDK Documentation](https://docs.videosdk.live/react-native/guide/video-and-audio-calling-api-sdk/concept-and-architecture)
- [PiP Documentation](https://docs.videosdk.live/react/guide/video-and-audio-calling-api-sdk/render-media/picture-in-picture)
