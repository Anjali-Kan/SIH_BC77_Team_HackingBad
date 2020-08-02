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
    medical_conditions?:any;
    height?:number;
}