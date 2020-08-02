import * as functions from 'firebase-functions';
import * as admin from "firebase-admin";

let newAppointMent = functions.firestore.document("appointments/{appointId}").onWrite((dataSnapshot,context)=>{
    const appointment = context.params.appointId;
    let data = dataSnapshot.after.data();

    if(data===undefined)
        return Promise.resolve();

    if(appointment === undefined)
        return Promise.reject("Invalid params");
    const docIdAfter = data.docId;
    const patientId = data.patiendId;


    const promises:Promise<any>[] = [];

    const state = data.state;
    if(state !==0){
        if(state==-1)
            promises.push(
                admin.messaging().sendToTopic(patientId,{
                    data:{
                        "type":"appointmentAck",
                        "subType":"rejAppointment",
                        "title":"We are sorry",
                        "msg": "We are extremely sorry that doctor has declined your request\nWe are there to connect you with some other doctor",
                        "appointId":appointment
                    }
                })
            );
        else
            promises.push(
                admin.messaging().sendToTopic(patientId,{
                    data:{
                        "type":"appointmentAck",
                        "subType":"accAppointment",
                        "title":"Appointment Finalized",
                        "msg": "Your appointment has been finalized\nPlease see appointments page for more details",
                        "appointId":appointment
                    }
                })
            );

    }else{
        if(docIdAfter!==undefined) {
            promises.push(
                admin.messaging().sendToTopic(docIdAfter,{
                    data:{
                        "type":"appointmentAck",
                        "subType":"newAppointment",
                        "title":"New Appointment for you",
                        "msg": "You have a new request from an appointment",
                        "appointId":appointment,
                        "patientId":patientId
                    }
                })
            );
        }
    }

    return Promise.all(promises);
});

export {newAppointMent};