import { Component, OnInit } from '@angular/core';
import { EmergencyService } from '../../services/emergency.service';
import { AngularFirestore, AngularFirestoreCollection, AngularFirestoreDocument } from 'angularfire2/firestore';
import { Appointment } from 'src/app/models/appointment';
import { Doctor } from 'src/app/models/doctor';



@Component({
  selector: 'app-appointments',
  templateUrl: './appointments.component.html',
  styleUrls: ['./appointments.component.css']
})
export class AppointmentsComponent implements OnInit {
  appointments: Appointment[];
  doctors: Doctor[];
  selected  ='';
  constructor(private emergencyService: EmergencyService, public afs: AngularFirestore) { }

  ngOnInit(): void {
    this.emergencyService.getAppointments().subscribe(appointments => {
      console.log(appointments);
      this.appointments = appointments;
    });

    this.emergencyService.getDOctors().subscribe(doctors => {
      console.log(doctors);
      this.doctors = doctors;
    });
  }
  updateAppointment(appointment: Appointment){
    console.log('assigned')
    this.emergencyService.updateAppointments(appointment.id, this.selected);

  }


  changeDoctor(docId:string){
    console.log(docId)
    this.selected = docId
  }
}

