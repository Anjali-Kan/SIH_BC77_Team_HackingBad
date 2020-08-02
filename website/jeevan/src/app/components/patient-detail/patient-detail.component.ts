import { Component, OnInit } from '@angular/core';
import { Patient } from 'src/app/models/patient-details'
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from 'angularfire2/firestore';
import { Observable } from 'rxjs';
import { map } from "rxjs/operators";
import { ActivatedRoute } from '@angular/router';
import { EmergencyService } from 'src/app/services/emergency.service';
import { Emergency } from 'src/app/models/Emergency';



@Component({
  selector: 'app-patient-detail',
  templateUrl: './patient-detail.component.html',
  styleUrls: ['./patient-detail.component.css']
})
export class PatientDetailComponent implements OnInit {
  patient: Patient;
  emergency: Emergency;
  constructor(
    private route: ActivatedRoute,
    private emergencyService: EmergencyService) {
   
  }

  getPatient():void {
    const id = this.route.snapshot.paramMap.get('id')
    this.emergencyService.getUser(id).subscribe(patient => this.patient = patient)
  }
  
  ngOnInit(): void {
    this.getPatient()
  }

}
