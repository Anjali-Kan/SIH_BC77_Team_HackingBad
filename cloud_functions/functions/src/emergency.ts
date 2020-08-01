import * as functions from 'firebase-functions';
import * as admin from "firebase-admin";
import * as twilioHelper from './twilio'


const maxVolunteer = 5;

let raisedEmergency = functions.firestore.document('emergencies/{docId}').onCreate((dataSnap,context)=>{
    let data = dataSnap.data();
    let emergencyId = context.params.docId;

    if(data === undefined)
        return Promise.resolve();

    let promises = Array();

    let fcmDataPayload: any = {
        'type':'request',
        'emergencyId':emergencyId,
        'case':(data.type || "unknown"),
        'lat':data.location.latitude.toString(),
        'lon':data.location.longitude.toString(),
    };

    promises.push(
            admin.messaging().sendToTopic("volunteer",{
            'data':fcmDataPayload
        })
    );

    if(data.forSelf !== undefined && data.forSelf){
        promises.push(
            twilioHelper("Help needed for user and add his location as well here")
        )
    }

    let wantGovtHospital = false;
    if(data.govHospital !== undefined && data.govHospital)
        wantGovtHospital = true;
    let ref = admin.firestore().collection("hospitals").where("emergency","==",true);
    if(wantGovtHospital)
        ref.where("government","==",true);

    promises.push(
        ref.get().then((results)=>{
            let initialDistance = -1;
            let hospitalId = "";
            results.forEach((eachDoc)=>{
                let documentData = eachDoc.data();
                // @ts-ignore
                const distance = getDistanceFromLatLonInKm(data.location.latitude,data.location.longitude,
                    documentData.location.latitude,documentData.location.longitude);
                if(distance < initialDistance || initialDistance == -1){
                    hospitalId = eachDoc.id;
                    initialDistance = distance;
                }
            });
            return admin.firestore().collection("emergencies").doc(emergencyId).update({
                'assignedHospital':hospitalId
            })
        })
    );


    return Promise.all(promises);

});


let acknowledementVolunteer = functions.https.onRequest(async (req,res)=>{

    let emergencyId = req.body.emergencyId;
    let userId = req.body.userID;

    if(emergencyId === undefined || userId === undefined){
        res.send({
            'status':'error',
            'error':'Invalid Params'
        });
        return;
    }

    let responsePayload:any = {
        'status':'success'
    };

    const ref = admin.firestore().doc(`emergencies/${emergencyId}`);

    await admin.firestore().runTransaction((trans)=>{
        return trans.get(ref).then((dataSnap)=>{
            if(dataSnap !== undefined && dataSnap.exists){
                // @ts-ignore
                let volunteerCount = dataSnap.data().volunteer.length;
                console.log(volunteerCount);
                if(volunteerCount < maxVolunteer){
                    responsePayload["shouldGo"] = true;
                    trans.update(ref,{
                        'volunteer':admin.firestore.FieldValue.arrayUnion(userId)
                    })
                }else{
                    responsePayload["shouldGo"] = false;
                }
            }else{
                responsePayload["status"]= "error"
            }
        })
    });


    res.send(responsePayload)

});


// @ts-ignore
function getDistanceFromLatLonInKm(lat1,lon1,lat2,lon2) {
    var R = 6371; // Radius of the earth in km
    var dLat = deg2rad(lat2-lat1);  // deg2rad below
    var dLon = deg2rad(lon2-lon1);
    var a =
        Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
        Math.sin(dLon/2) * Math.sin(dLon/2)
    ;
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    var d = R * c; // Distance in km
    return d;
}

// @ts-ignore
function deg2rad(deg) {
    return deg * (Math.PI/180)
}

module.exports = {
    'raise':raisedEmergency,
    'ackVol':acknowledementVolunteer,
};