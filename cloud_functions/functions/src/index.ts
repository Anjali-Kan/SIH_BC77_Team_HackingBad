// import * as functions from 'firebase-functions';
import * as admin from 'firebase-admin';
import * as functions from 'firebase-functions';
import { assignHospital,acknowledementVolunteer } from './emergency'
import {assignAmbulance,updateLocationAmbulance} from "./ambulance";
import {callTeleMedicineDoc,ackTelemedicine} from "./telemedicine";
import {newAppointMent} from "./appointment";

admin.initializeApp();


const onNewEmergency =functions.firestore.document('emergencies/{docId}').onCreate((dataSnap,context)=>{

    let promises:Promise<any>[] =  [];
    promises.push(
        assignHospital(dataSnap,context)
    );

    promises.push(
        assignAmbulance(dataSnap,context)
    );

    promises.push(
        callTeleMedicineDoc(dataSnap,context)
    );

    return Promise.all(promises);
});

let webHookIfttt = functions.https.onRequest((req,res)=>{

    const data = {
        "forSelf":true,
        "govAmbulance":true,
        "govHospital":true,
        "inProgress":true,
        "patientID":null,
        "raisedBy":"ifttt",
        "location": new admin.firestore.GeoPoint(19.20816,72.851395),
        "type":"heart_attack",
        "timestamp": admin.firestore.FieldValue.serverTimestamp()
    }
    admin.firestore().collection("emergencies").add(data).then((_)=>{
        res.status(200).send(JSON.stringify(_))
    }).catch((err)=>res.status(500).send(JSON.stringify(err)))


})


export  = {onNewEmergency,acknowledementVolunteer,updateLocationAmbulance,ackTelemedicine,newAppointMent,webHookIfttt}
