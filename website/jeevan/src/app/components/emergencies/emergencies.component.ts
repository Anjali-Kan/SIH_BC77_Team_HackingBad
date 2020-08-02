import { Component, OnInit } from '@angular/core';
import { EmergencyService } from '../../services/emergency.service';
import { Emergency } from 'src/app/models/Emergency';


@Component({
  selector: 'app-emergencies',
  templateUrl: './emergencies.component.html',
  styleUrls: ['./emergencies.component.css']
})
export class EmergenciesComponent implements OnInit {
  emergencies: Emergency[];
  editState: boolean = false;
  emergencyToEdit: Emergency;
  constructor(private emergencyService: EmergencyService) { }

  ngOnInit(): void {
    this.emergencyService.getEmergencies().subscribe(emergencies => {
      console.log(emergencies);
      this.emergencies = emergencies;
    })
  }

  updateEmergency(emergency: Emergency){
    this.editState = true;
    this.emergencyToEdit = emergency;
    this.emergencyService.updateDoc(emergency.id, false);

  }
  
}
