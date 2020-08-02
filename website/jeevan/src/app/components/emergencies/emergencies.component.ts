import { Component, OnInit } from '@angular/core';
import { EmergencyService } from '../../services/emergency.service';
import { Emergency } from 'src/app/models/Emergency';
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from 'angularfire2/firestore';
import { Patient } from '../../models/patient-details';


@Component({
  selector: 'app-emergencies',
  templateUrl: './emergencies.component.html',
  styleUrls: ['./emergencies.component.css']
})
export class EmergenciesComponent implements OnInit {
  emergencies: Emergency[];
  editState: boolean = false;
  emergencyToEdit: Emergency;
  userDoc: AngularFirestoreDocument<Patient>;
  //typeMap: Map<String,String>;
  constructor(private emergencyService: EmergencyService, public afs: AngularFirestore) { }

  ngOnInit(): void {
    this.emergencyService.getEmergencies().subscribe(emergencies => {
      console.log(emergencies);
      this.emergencies = emergencies;
    });
      // this.typeMap.set('fits_or_seizure','Fits or Seizure');
      // this.typeMap.set('gunshot_or_stabbing','Gunshot or Stabbing');
      // this.typeMap.set('heart_problem','Heart Problem');
      // this.typeMap.set('road_accident','Road Accident');
      // this.typeMap.set('breathing_problem','Breathing Problem');
      // this.typeMap.set('burns','Burns');
  }

  updateEmergency(emergency: Emergency){
    this.editState = true;
    this.emergencyToEdit = emergency;
    this.emergencyService.updateDoc(emergency.id, false);

  }
  
  sendId(id: String, emergency: Emergency){
    this.emergencyService.patientId = id;
    this.emergencyService.currEmergency = emergency;
  }

  getCleanedType(type:String){
    var re = /_/gi; 
    var newstr = type.replace(re, " "); 
      return newstr;  
  }
}
