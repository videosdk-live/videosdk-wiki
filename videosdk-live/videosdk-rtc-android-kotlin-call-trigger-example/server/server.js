const functions = require("firebase-functions");
const express = require("express");
const cors = require("cors");
const morgan = require("morgan");
var admin = require("firebase-admin");
const { v4: uuidv4 } = require("uuid");

const app = express();
app.use(cors());
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(morgan("dev"));

// Path to your service account key file for Firebase Admin SDK
var serviceAccount = require("YOUR_FILE_PATH");

// Initialize Firebase Admin SDK
admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "YOUR_DATABASE_URL" // Replace with your database URL
});

// Home Route
app.get("/", (req, res) => {
  res.send("Hello World!");
});

// Initiate call notification (for Android)
app.post("/initiate-call", (req, res) => {
  const { calleeInfo, callerInfo, videoSDKInfo } = req.body;

  var FCMtoken = calleeInfo.token;
    const info = JSON.stringify({
      callerInfo,
      videoSDKInfo,
      type: "CALL_INITIATED",
    });
    var message = {
      data: {
        info,
      },
      android: {
        priority: "high",
      },
      token: FCMtoken,
    };
    
  // Send the FCM message using firebase-admin
  admin.messaging().send(message)
    .then((response) => {
      console.log("Successfully sent FCM message:", response);
      res.status(200).send(response);
    })
    .catch((error) => {
      console.log("Error sending FCM message:", error);
      res.status(400).send("Error sending FCM message: " + error);
    });
});

// Update call notification (for Android)
app.post("/update-call", (req, res) => {
  const { callerInfo, type } = req.body;
  const info = JSON.stringify({
    callerInfo,
    type,
  });

  var message = {
    data: {
      info,
    },
    token: callerInfo.token,
  };
  var message = {
    data: { info },
    token: callerInfo.token, // Token for the target device
    android: {
      priority: "high",
      notification: {
        title: "Call Updated",
        body: "Your call has been updated by " + callerInfo.name,
      },
    },
  };

  // Send the update message through firebase-admin
  admin.messaging().send(message)
    .then((response) => {
      console.log("Successfully updated call:", response);
      res.status(200).send(response);
    })
    .catch((error) => {
      console.log("Error updating call:", error);
      res.status(400).send("Error updating call: " + error);
    });
});

// Start the Express server
app.listen(9000, () => {
  console.log(`API server listening at http://localhost:9000`);
});

// Export app as a Firebase Cloud Function
exports.app = functions.https.onRequest(app);
