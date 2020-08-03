import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EmergencyService } from 'src/app/services/emergency.service';
import { Patient } from 'src/app/models/patient';
import { Emergency } from 'src/app/models/emergency'


@Component({
  selector: 'app-emergency-form',
  templateUrl: './emergency-form.component.html',
  styleUrls: ['./emergency-form.component.css']
})
export class EmergencyFormComponent implements OnInit {
  id: string;
  //emergencyService: EmergencyService;
  patient: Patient;
  emergency: Emergency;
  emergencyType: string;
  types: string[] = ['Road Accident', 'Burns', 'Heart Problem', 'Other'];
  ambulanceType: string;
  ambulances: string[] = ['Government', 'private'];
  hospitalType: string;
  hospitals: string[] = ['Government', 'private'];
  lat:number;
  long:number;
  not_submitted = true;

  constructor(private route: ActivatedRoute,private emergencyService:EmergencyService) { 
    this.route.queryParams.subscribe(params => {
      console.log(params);
      this.id = params['userId']
    });
    // this.route.params.subscribe(params => {
    //   this.id = params['id'];
    //   console.log(this.id);
    // });
    emergencyService.setOnlyId(this.id);
    emergencyService.getUser().subscribe(patient => this.patient = patient);
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      console.log(params);
    });

    this.emergencyService.getPosition().then(pos=>
      {
         console.log(`Positon: ${pos.lng} ${pos.lat}`);
         this.lat = pos.lat
         this.long = pos.lng
      });
    
    
  }

  submitEmergencyForm(){
    var at:boolean;
    var ht:boolean;
    this.not_submitted = false;
    if(this.ambulanceType=='Government'){
      at = true;
    }
    else{
      at=false;
    }

    if(this.hospitalType=='Government'){
      ht = true;
    }
    else{
      ht=false;
    }
    this.emergencyService.addEmergency(this.emergency, this.id, this.emergencyType,at,ht, this.lat, this.long);
  }

}
