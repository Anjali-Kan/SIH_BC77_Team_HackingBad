import { Injectable } from '@angular/core';
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from 'angularfire2/firestore';
import { Emergency } from '../models/Emergency';
import { Observable } from 'rxjs';
import { map } from "rxjs/operators";


@Injectable({
  providedIn: 'root'
})
export class EmergencyService {
  emergenciesCollection: AngularFirestoreCollection<Emergency>;
  emergencies: Observable<Emergency[]>;

  constructor(public afs: AngularFirestore) { 
      //this.emergencies = this.afs.collection('emergencies',ref => ref.where('assignedHospital', '==', 'A6wOXwqpt4xiG68PZ6Qq')).valueChanges();
      this.emergencies = this.afs.collection(('emergencies'),ref => ref.where('assignedHospital', '==', 'A6wOXwqpt4xiG68PZ6Qq')).snapshotChanges().pipe(map(changes => {
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
  
  updateEmergency(emergency: Emergency){
    //this.emergencyDoc = this.afs.doc('emergencies/${emergency.id}');
    //this.emergencyDoc.update({inProgress:false})
  }
  updateDoc(_id: string, _value: boolean) {
    this.emergencies.subscribe((_doc: any) => {
       let id = _doc[0].payload.doc[0].id; //first result of query [0]
       this.afs.doc('emergencies/${id}').update({'inProgress': false});
      })
  }
}
