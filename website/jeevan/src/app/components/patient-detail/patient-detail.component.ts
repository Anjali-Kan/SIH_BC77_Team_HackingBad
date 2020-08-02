import { Component, OnInit } from '@angular/core';
import { Patient } from 'src/app/models/patient-details'

@Component({
  selector: 'app-patient-detail',
  templateUrl: './patient-detail.component.html',
  styleUrls: ['./patient-detail.component.css']
})
export class PatientDetailComponent implements OnInit {
  patient: Patient={
    id:'1',
    name:'tanvi'
  };

  constructor() {
   }

  ngOnInit(): void {
  }

}
