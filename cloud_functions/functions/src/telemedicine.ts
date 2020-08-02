import * as admin from "firebase-admin";
import * as functions from 'firebase-functions';
import {EventContext} from "firebase-functions";

const callTeleMedicineDoc =(dataSnap:FirebaseFirestore.DocumentSnapshot,context:EventContext)=>{
    let data = dataSnap.data();
    let emergencyId = context.params.docId;

    if(data===undefined)
        return Promise.resolve();

    return admin.firestore().collection("doctors").where("telemedicineAvail","==",true)
        .get().then(qSnap=>{
            let promises:Promise<any>[]=[];
            let fcmData = {
                "type":"telemedicine",
                "emergencyId":emergencyId,
                // @ts-ignore
                "case":data.type||"Unknown"
            };

            qSnap.forEach(docSnap=>{
                promises.push(
                  admin.messaging().sendToTopic(docSnap.id,{
                      data:fcmData
                  })
                );
            });
            return Promise.all(promises);
        });

};

const ackTelemedicine = functions.https.onRequest(async (req,res)=>{
    const emergencyId = req.body.emergencyId;
    const docId = req.body.docId;
    if(emergencyId===undefined || docId===undefined){
        res.send({
            "status":"error",
            "error":"invalid params"
        });
        return null;
    }

    const documentSnapshot = await admin.firestore().doc(`emergencies/${emergencyId}`).get();
    if(documentSnapshot===undefined || documentSnapshot.data() ===undefined){
        res.send({
            "status":"error",
            "error":"invalid emergency"
        });
        return null;
    }

    //@ts-ignore
    let telemedicineDoc = documentSnapshot.data().telemedicine;

    if(telemedicineDoc==undefined){
        await documentSnapshot.ref.update({"telemedicine":docId});
        res.send({
            "status":"success",
            "shouldGo":true,
            // @ts-ignore
            "token":documentSnapshot.data().teleToken||""
        })
    }else{
        res.send({
            "status":"success",
            "shouldGo":false
        })
    }
    return null;
});



export {callTeleMedicineDoc,ackTelemedicine}
