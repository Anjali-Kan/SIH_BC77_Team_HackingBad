import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import {  PatientDetailComponent } from './components/patient-detail/patient-detail.component'
import { HomepageComponent } from './components/homepage/homepage.component';
import { EmergencyFormComponent } from './components/emergency-form/emergency-form.component';


const routes: Routes = [
  {path: 'detail',component: PatientDetailComponent},
  {path: '', component: HomepageComponent},
  { path:  'emergency/:id', component:  EmergencyFormComponent},

];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    RouterModule.forRoot(routes),
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
