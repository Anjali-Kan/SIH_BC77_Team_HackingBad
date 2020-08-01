import * as functions from 'firebase-functions';
import * as admin from "firebase-admin";

const maxVolunteer = 5;

let raisedEmergency = functions.firestore.document('emergencies/{docId}').onCreate((dataSnap,context)=>{
    let data = dataSnap.data();
    let emergencyId = context.params.docId;

    if(data === undefined)
        return Promise.resolve();

    let fcmDataPayload: any = {
        'type':'request',
        'emergencyId':emergencyId,
        'lat':data.location.latitude.toString(),
        'lon':data.location.longitude.toString(),
    };

    return admin.messaging().sendToTopic("volunteer",{
        'data':fcmDataPayload
    })

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


module.exports = {
    'raise':raisedEmergency,
    'ackVol':acknowledementVolunteer
};