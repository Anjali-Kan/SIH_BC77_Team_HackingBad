import { Component, OnInit } from '@angular/core';
import { Patient } from 'src/app/models/patient'
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from 'angularfire2/firestore';
import { Observable } from 'rxjs';
import { map } from "rxjs/operators";
import { ActivatedRoute } from '@angular/router';
import { EmergencyService } from 'src/app/services/emergency.service';
import { Emergency } from 'src/app/models/Emergency';
import { Appointment } from 'src/app/models/appointment';



@Component({
  selector: 'app-patient-detail',
  templateUrl: './patient-detail.component.html',
  styleUrls: ['./patient-detail.component.css']
})
export class PatientDetailComponent implements OnInit {
  patient: Patient;
  emergency: Emergency;
  prescriptions;
  constructor(
    private route: ActivatedRoute,
    private emergencyService: EmergencyService) {
   
  }

  getPatient():void {
    this.emergencyService.getUser().subscribe((patient) => {this.patient = patient
      this.prescriptions = this.emergencyService.getPrescriptions(this.patient.id).subscribe(pres => {
        console.log('pres : ', pres);
        this.prescriptions = pres;
      });;
      console.log(this.prescriptions);})
  }

  
  
  ngOnInit(): void {
    this.getPatient()
    this.emergency = this.emergencyService.currEmergency;
    
    

    //console.log(this.prescriptions);
    console.log(this.emergency.location['Ic']);

  }

  getURL(){
    var url = "https://maps.google.com/?q=23.22,88.32&z=8"
    return this.emergency.location[0]
  }

}
