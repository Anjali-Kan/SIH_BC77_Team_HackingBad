import { Component, OnInit } from '@angular/core';
import { EmergencyService } from 'src/app/services/emergency.service';
import { AngularFirestore } from 'angularfire2/firestore';
import { Appointment } from 'src/app/models/appointment';

@Component({
  selector: 'app-upcoming-appointments',
  templateUrl: './upcoming-appointments.component.html',
  styleUrls: ['./upcoming-appointments.component.css']
})
export class UpcomingAppointmentsComponent implements OnInit {
  upcomingAppointments: Appointment[];

  constructor(private emergencyService: EmergencyService, public afs: AngularFirestore) { }

  ngOnInit(): void {
    this.emergencyService.getUpcomingAppointments().subscribe(appointments => {
      console.log('upcoming appointments : ', appointments);
      this.upcomingAppointments = appointments;
    });

  }

}
