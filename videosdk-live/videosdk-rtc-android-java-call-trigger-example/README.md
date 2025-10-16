# Android Call Trigger with VideoSDK

In this guide, we’ll walk you through setting up and running a native Android video calling app. Follow these steps to get the project up and running smoothly.

## Client

### Step 1: Clone the sample project

Clone the repository to your local environment.

```js
https://github.com/videosdk-live/videosdk-rtc-android-java-call-trigger-example.git
```

## Android Setup

### Step 1: Add VideoSdkAuthToken to MainApplication class

- Add your VideoSDK authentication token to your MainApplication class. You can obtain your token from the [ VideoSDK dashboard](https://app.videosdk.live/dashboard/MCAYBP2ZEH26)

### Step 2: Setup Firebase

#### FCM setup

- Replace your firebase app `google-services.json` file in the app folder.

![plot](https://cdn.videosdk.live/docs/images/android/call_keep/sevice.png)


### Step 3: Allow calling and overlay permissions


### 1. Access phone accounts permission

You need to grant call account permissions to enable VideoSDK to make and receive calls.

#### calling accounts

<p>
  <img src="https://cdn.videosdk.live/docs/images/android/call_keep/calling_accounts.jpg" width="180" />
  </p>

Click on `All calling accounts` and allow the app to receive call.

<p>
  <img src="https://cdn.videosdk.live/docs/images/android/call_keep/calling_accounts_allowed.jpg" width="180" />
</p>

_**NOTE : It is necesary to setup local server before run the project.**_

## Server Setup

### Step 1: Go to server folder

### Step 2: Setup Firebase Admin SDK for managing FCM

Download private Key from firebase and add the `.json` file in the server folder.
![plot](https://cdn.videosdk.live/docs/images/android/call_keep/firebase_server_sdk.jpeg)

### Step 3: Configure Firebase Admin in Server Code

```js
// Path to your service account key file for Firebase Admin SDK
var serviceAccount = require("add_path_here");
```

```js
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "database_url" // Replace with your database URL
});
```


### Step 4: Install Package and start server

```js
npm install
```

```js
npm run start
```

### Step 4: Add local server url in client

Add Local server ip address in `Network/ApiClient.java` file, eg. "http://192.168.1.10:9000".

```js title="api.js"
const FCM_SERVER_URL = "BASE_URL";
```

## Issue

You can generate the issue on [Github](https://github.com/videosdk-live/videosdk-rtc-react-native-call-trigger-example/issues) or ping us on [Discord](https://discord.gg/bsEukaNhrD)

## Other Information

### Tested on Devices

- Nothing device
