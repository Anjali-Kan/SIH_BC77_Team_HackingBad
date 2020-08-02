import * as admin from "firebase-admin";
import * as functions from "firebase-functions";
import {DocumentSnapshot} from "firebase-functions/lib/providers/firestore";
import {EventContext} from "firebase-functions";
import {getDistanceFromLatLonInKm} from "./emergency";
import * as sendMessage from "./twilio"
import * as req from "axios"

const apiKey = "AIzaSyAZoBGG3s6nE2dh2kEgpGt4Cw93undUwE4";

let assignAmbulance = (dataSnap:DocumentSnapshot,cont:EventContext)=>{
    let data = dataSnap.data();
    let emergencyId = cont.params.docId;

    if(data===undefined)
        return Promise.resolve();

    let fcmData: any = {
        'type': 'requestAmbulance',
        'emergencyId': emergencyId,
        'case': (data.type || "unknown"),
        'lat': data.location.latitude.toString(),
        'lon': data.location.longitude.toString(),
    };
    const lat = data.location.latitude;
    const long = data.location.longitude;

    let govHospital = false;
    let types:String[] = [];

    if(data.govAmbulance !==undefined && data.govAmbulance)
        govHospital = true;
    types.push(data["type"]);

    let ref =  admin.firestore().collection("ambulance");

    ref.where("isAvailable","==",true);
    if(govHospital)
        ref.where("isAvailable","==",true);
    types.forEach(t=>{
       ref.where("type","array-contains",t);
    });

    return ref.get().then(async ambSnap => {
        let ambulanceId = "";

        if (ambSnap.size===0){
            ambulanceId = await getAllAmbulance(lat,long)
        }else{
            ambulanceId = computeNearestAmbulanceId(ambSnap,lat,long);
        }



        return Promise.all([
            //add to db
            admin.firestore().collection("emergencies").doc(emergencyId).update({
                'ambulanceID': ambulanceId
            }),
            //notify ambulance driver
            admin.messaging().sendToTopic(ambulanceId, {
                data: fcmData
            }),
            //send twilio msg
            sendMessage(`You have a new Patient... Please open the app or click the link https://www.google.com/maps/search/?api=1&query=${lat},${long}`)
        ]);
    });
};

let computeNearestAmbulanceId = (ambulanceIter:FirebaseFirestore.QuerySnapshot,lat:number,long:number)=>{
    let initialDistance = -1;
    let ambulanceId = "";
    ambulanceIter.forEach(amb => {
        let documentData = amb.data();
        // @ts-ignore
        const distance = getDistanceFromLatLonInKm(lat, long,
            documentData.location.latitude, documentData.location.longitude);
        if (distance < initialDistance || initialDistance == -1) {
            ambulanceId = amb.id;
            initialDistance = distance;
        }
    });
    return ambulanceId
};

let getAllAmbulance = (lat:number,long:number)=>{

    let ref =  admin.firestore().collection("ambulance");
    ref.where("isAvailable","==",true);

    return ref.get().then(amb =>{
        return computeNearestAmbulanceId(amb,lat,long)
    })

};

let updateLocationAmbulance = functions.firestore.document("ambulance/{ambulanceId}").onUpdate((dataSnap,context)=>{
    let data = dataSnap.after.data();
    let isFirst = false;
    if(data===undefined)
        return Promise.resolve();
    if(data.isAvailable)
        return Promise.resolve();

    if(dataSnap.before===undefined || dataSnap.before.data()===undefined)
        return Promise.resolve();

    //@ts-ignore
    if(dataSnap.before.data().isAvailable)
        isFirst = true;

    let ambulanceId = context.params.ambulanceId;
    return admin.firestore().collection("emergencies").where("ambulanceID","==",ambulanceId).get().then(async emergencySnap=>{
       if(emergencySnap.size == 0)
           return dataSnap.after.ref.update({"isAvailable":true});

       let promise:Promise<any>[] = [];

       let eme :DocumentSnapshot = emergencySnap.docs[0];


       const emergency = eme.data();
       if(emergency === undefined)
           return Promise.resolve();
       let updates:any = {};
        if(emergency.toPick !== -1)
            //@ts-ignore
            updates["toPick"] = await getTime(data.location.latitude,data.location.longitude,emergency.location.latitude,emergency.location.longitude);
        else
            //@ts-ignore
            updates["toHospital"] = await getTime(data.location.latitude,data.location.longitude,emergency.hospitalLocation.latitude,emergency.hospitalLocation.longitude);
        if(isFirst)
            updates["toHospital"] =await getTime(emergency.location.latitude,emergency.location.longitude,emergency.hospitalLocation.latitude,emergency.hospitalLocation.longitude);

        promise.push(
            eme.ref.update(updates)
        );

        updates["emergency"]=eme.id;
        updates["type"]="ambulanceUpdate";
        updates["ambulance"]=ambulanceId;
        // @ts-ignore
        updates["locLat"]=data.location.latitude.toString();
        // @ts-ignore
        updates["locLong"]=data.location.longitude.toString();


        promise.push(
            admin.messaging().sendToTopic(eme.id,{
                data:updates
            })
        );


       return Promise.all(promise);

    });


});

const getTime  = async function(oLat:number,oLong:number,dLat:number,dLong:number){
    const url = `https://maps.googleapis.com/maps/api/directions/json?origin=${oLat},${oLong}&destination=${dLat},${dLong}&key=${apiKey}&mode=driving`.replace(" ","");
    const res = await req.default.get(url);
    if(res.data.status === "OK"){
        return res.data.routes[0].legs[0].duration.value;
    }else{
        return -2;
    }

};

export { updateLocationAmbulance, assignAmbulance}
