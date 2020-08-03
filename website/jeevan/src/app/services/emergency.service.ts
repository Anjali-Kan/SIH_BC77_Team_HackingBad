import { Injectable } from '@angular/core';
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from 'angularfire2/firestore';
import { Emergency } from '../models/Emergency';
import { Appointment } from '../models/appointment';
import { Observable } from 'rxjs';
import { map } from "rxjs/operators";
import { Patient } from '../models/patient';
import { Doctor } from '../models/doctor';
import * as firebase from 'firebase/app';



@Injectable({
  providedIn: 'root'
})
export class EmergencyService {
  emergenciesCollection: AngularFirestoreCollection<Emergency>;
  emergencies: Observable<Emergency[]>;
  appointments: Observable<Appointment[]>;
  upcomingAppointments: Observable<Appointment[]>;
  doctors: Observable<Doctor[]>;
  userDoc: AngularFirestoreDocument<Patient>;
  prescriptions;
  prescriptionArray = [];
  public patientId : String;
  public currEmergency: Emergency;
  af: AngularFirestore;

  constructor(public afs: AngularFirestore) { 
      //this.emergencies = this.afs.collection('emergencies',ref => ref.where('assignedHospital', '==', 'A6wOXwqpt4xiG68PZ6Qq')).valueChanges();
      this.af = afs;
      this.emergencies = this.afs.collection(('emergencies'),ref => ref.where('assignedHospital', '==', 'A6wOXwqpt4xiG68PZ6Qq').where("inProgress", "==", true)).snapshotChanges().pipe(map(changes => {
        return changes.map(a=>{
          const data = a.payload.doc.data() as Emergency;
          data.id = a.payload.doc.id;
          return data;
        });
      }));

      this.appointments = this.afs.collection(('appointments'),ref => ref.where('hospitalId', '==', 'A6wOXwqpt4xiG68PZ6Qq').where("docId", "==", null)).snapshotChanges().pipe(map(changes => {
        return changes.map(a=>{
          const data = a.payload.doc.data() as Appointment;
          data.id = a.payload.doc.id;
          return data;
        });
      }));

      this.upcomingAppointments = this.afs.collection(('appointments'),ref => ref.where('hospitalId', '==', 'A6wOXwqpt4xiG68PZ6Qq')).snapshotChanges().pipe(map(changes => {
        return changes.map(a=>{
          const data = a.payload.doc.data() as Appointment;
          data.id = a.payload.doc.id;
          return data;
        });
      }));

      this.doctors = this.afs.collection(('doctors'),ref => ref.where("hospitalsLinked", "array-contains", "A6wOXwqpt4xiG68PZ6Qq")).snapshotChanges().pipe(map(changes => {
        return changes.map(a=>{
          const data = a.payload.doc.data() as Doctor;
          data.id = a.payload.doc.id;
          return data;
        });
      }));

      
  }
  getEmergencies(){
    return this.emergencies;
  }
  
  
  updateEmergency(_id: string, _value: boolean) {
    var emergencyRef = this.af.collection("emergencies").doc(`${_id}`);
    console.log(emergencyRef);
    emergencyRef.update({
      inProgress: false
    })
  }

  getPrescriptions(_id: string){
    this.prescriptions = this.afs.collection(('appointments'),ref => ref.where('patientId', '==', '9byVuEIVgpfPOHSqVDyTn1YQ8Am2').where("state", "==", 10)).snapshotChanges().pipe(map(changes => {
      return changes.map(a=>{
        const data = a.payload.doc.data() as Appointment;
        data.id = a.payload.doc.id;
        return data;
      });
    }));
    // this.prescriptions = this.afs.doc<Appointment>(`appointments/`)
    
    // this.prescriptions.forEach(doc => {
    //   console.log(doc.id, '=>', doc.data());
    // });
    
    return this.prescriptions;
  }

  getUser(){
    //this.userDoc = this.afs.doc<Patient>(`users/bVB1J8aZoMebwqZ5FWBrT7YW7n63`);
    this.userDoc = this.afs.doc<Patient>(`users/${this.patientId}`);
    console.log(this.patientId);
    return this.userDoc.valueChanges();
  }
  

  sendId(id: String, emergency: Emergency){
    this.patientId = id;
    this.currEmergency = emergency; 
  }

  setOnlyId(id:String){
    this.patientId = id;
  }

  getAppointments(){
    return this.appointments;
  }

  getUpcomingAppointments(){
    return this.upcomingAppointments;
  }

  updateAppointments(_id: string, _docId: string) {
    var appointmentRef = this.af.collection("appointments").doc(`${_id}`);
    console.log(appointmentRef);
    appointmentRef.update({
      docId: _docId,
    })
  }

  getDOctors(){
    return this.doctors;
  }

  addEmergency(emergency: Emergency, patientId:string, emergencyType:string, ambulanceType:boolean, hospitalType:boolean, lat, long) {
    const locationData = new firebase.firestore.GeoPoint(lat, long)
    this.af.collection("emergencies").add({
      patientID: patientId,
      type: emergencyType,
      govHospital : hospitalType,
      govAmbulance: ambulanceType,
      location: locationData,
      forSelf: false,
      ambulanceId: null,
      assignedHospital: null,
      hospitalLocation: null,
      raisedBy: 'fromWebsite',
      teleToken: null,
      telemedicine: null,
      timestamp: firebase.firestore.FieldValue.serverTimestamp(),
      toHospital: null,
      toPick: null,
      inProgress: true,
  })
  .then(function(docRef) {
      console.log("Document written with ID: ", docRef.id);
  })
  .catch(function(error) {
      console.error("Error adding document: ", error);
  });
  
  }


  getPosition(): Promise<any>
  {
    return new Promise((resolve, reject) => {

      navigator.geolocation.getCurrentPosition(resp => {

          resolve({lng: resp.coords.longitude, lat: resp.coords.latitude});
        },
        err => {
          reject(err);
        });
    });

  }
}
