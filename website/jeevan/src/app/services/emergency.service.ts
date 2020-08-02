import { Injectable } from '@angular/core';
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from 'angularfire2/firestore';
import { Emergency } from '../models/Emergency';
import { Observable } from 'rxjs';
import { map } from "rxjs/operators";
import { Patient } from '../models/patient-details';


@Injectable({
  providedIn: 'root'
})
export class EmergencyService {
  emergenciesCollection: AngularFirestoreCollection<Emergency>;
  emergencies: Observable<Emergency[]>;
  userDoc: AngularFirestoreDocument<Patient>;
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

  }
  getEmergencies(){
    return this.emergencies;
  }
  
  
  updateDoc(_id: string, _value: boolean) {
    var emergencyRef = this.af.collection("emergencies").doc(`${_id}`);
    console.log(emergencyRef);
    emergencyRef.update({
      inProgress: false
    })
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
}
