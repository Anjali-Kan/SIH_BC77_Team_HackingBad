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




export  = {onNewEmergency,acknowledementVolunteer,updateLocationAmbulance,ackTelemedicine,newAppointMent}


