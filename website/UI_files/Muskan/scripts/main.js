const firebase = require(["firebase"]);
// Required for side-effects
  require(["firebase/firestore"]);
  // Your web app's Firebase configuration

// Initialize Cloud Firestore through Firebase
firebase.initializeApp({
  apiKey: "AIzaSyAOB1_s4mJ1B1VdaziCSMMFQDiKJqLRPgM",
  authDomain: "jeevan-b9882.firebaseapp.com",
  projectId: "jeevan-b9882.firebaseapp.com",
});

var db = firebase.firestore();

db.collection("users").get().then((querySnapshot) => {
    querySnapshot.forEach((doc) => {
        console.log(`${doc.id} => ${doc.data()}`);
    });
});

  var firebaseConfig = {
    apiKey: "AIzaSyAOB1_s4mJ1B1VdaziCSMMFQDiKJqLRPgM",
    authDomain: "jeevan-b9882.firebaseapp.com",
    databaseURL: "https://jeevan-b9882.firebaseio.com",
    projectId: "jeevan-b9882",
    storageBucket: "jeevan-b9882.appspot.com",
    messagingSenderId: "5237821815",
    appId: "1:5237821815:web:f80976b42ef6b458681172",
    measurementId: "G-XKPQK9HRWW"
  };
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);
  firebase.analytics();