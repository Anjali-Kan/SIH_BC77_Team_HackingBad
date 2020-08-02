import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { AngularFireModule } from '@angular/fire';
import { AngularFirestoreModule } from 'angularfire2/firestore';
import { environment } from '../environments/environment';
import { EmergenciesComponent } from './components/emergencies/emergencies.component';
import { EmergencyService } from './services/emergency.service';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material/material.module';
import { PatientDetailComponent } from './components/patient-detail/patient-detail.component';
import { AppRoutingModule } from './app-routing.module';
import { HomepageComponent } from './components/homepage/homepage.component';
import { EmergencyFormComponent } from './components/emergency-form/emergency-form.component';

@NgModule({
  declarations: [
    AppComponent,
    EmergenciesComponent,
    PatientDetailComponent,
    HomepageComponent,
    EmergencyFormComponent
  ],
  imports: [
    BrowserModule,
    AngularFireModule.initializeApp(environment.firebase, 'jeevan'),
    AngularFirestoreModule,
    BrowserAnimationsModule,
    MaterialModule,
    AppRoutingModule,
  ],
  providers: [EmergencyService],
  bootstrap: [AppComponent]
})
export class AppModule { }
