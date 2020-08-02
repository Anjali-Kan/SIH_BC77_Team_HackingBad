export interface Patient {
    id?:string;
    name?:string;
    gender?:string;
    dob?:string;
    weight?:number;
    blood_group?:string;
    organ_donor?:boolean;
    medications?:string[];
    allergies?:string[];
    medical_conditions?:Map<String,String>;
    height?:number;
    other_notes?:String;
    emergency_contacts?:Map<String,String>;
    family_doctor?:Map<String,String>;
}