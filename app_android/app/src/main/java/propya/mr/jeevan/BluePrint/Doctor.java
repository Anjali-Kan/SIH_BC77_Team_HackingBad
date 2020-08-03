package propya.mr.jeevan.BluePrint;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Doctor implements Parcelable {
    public static final String unknown = "UNKNOWN";
    public static final boolean defaultBool = false;

    public String phone = unknown;
    public String selfUid = null;
    public String degree = unknown;
    public int experience = 0;
    public String gender = unknown;
    public ArrayList<String> hospitalsLinked;
    public boolean isAvailable = defaultBool;

    public String name = unknown;
    public double rating = 0.0;
    public ArrayList<String> specialities;
    public boolean telemedicineAvail = defaultBool;

    public Doctor(JSONObject jsonObject){
        try {
            setPhone(jsonObject.getString("contact"));
            setDegree(jsonObject.getString("degree"));
            setExperience(jsonObject.getInt("experience"));
            setGender(jsonObject.getString("gender"));
            selfUid = jsonObject.getString("objectID");
            ArrayList<String> hospitalsLinked = new ArrayList<String>();
            JSONArray hospitalsLinkedJsonArray = jsonObject.getJSONArray("hospitalsLinked");
            for(int i=0; i<hospitalsLinkedJsonArray.length(); i++) {
                hospitalsLinked.add(hospitalsLinkedJsonArray.getString(i));
            }
            setHospitalsLinked(hospitalsLinked);

            setAvailable(jsonObject.getBoolean("isAvail"));
            setName(jsonObject.getString("name"));
            setRating(jsonObject.getDouble("rating"));

            ArrayList<String> specialities = new ArrayList<String>();
            JSONArray specialitiesLinkedJsonArray = jsonObject.getJSONArray("specialist");
            for(int i=0; i<hospitalsLinkedJsonArray.length(); i++) {
                specialities.add(specialitiesLinkedJsonArray.getString(i));
            }
            setSpecialities(specialities);

            setTelemedicineAvail(jsonObject.getBoolean("telemedicineAvail"));

            // implement this func for creating doctor object
//            setHospitalAddress(jsonObject.getString("address"));
//
//            JSONArray facilitiesJSONArray = jsonObject.getJSONArray("facility");
//            ArrayList<String> facilities = new ArrayList<>();
//            for(int i=0; i<facilitiesJSONArray.length(); i++) {
//                facilities.add(facilitiesJSONArray.getString(i));
//            }
//            setFacilities(facilities);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    protected Doctor(Parcel in) {
        phone = in.readString();
        selfUid = in.readString();
        degree = in.readString();
        experience = in.readInt();
        gender = in.readString();
        hospitalsLinked = in.createStringArrayList();
        isAvailable = in.readByte() != 0;
        name = in.readString();
        rating = in.readDouble();
        specialities = in.createStringArrayList();
        telemedicineAvail = in.readByte() != 0;
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public static ArrayList<Doctor> parseJSON(JSONArray array) throws JSONException {
        ArrayList<Doctor> doctors = new ArrayList<>();
        for(int i=0; i<array.length();i++ ){
            JSONObject doctor = array.getJSONObject(i);
            if(doctor.has("indexType")){
                if(!doctor.getString("indexType").equals("doctor"))
                    continue;
            }
            doctors.add(new Doctor(doctor));
        }
        return doctors;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setHospitalsLinked(ArrayList<String> hospitalsLinked) {
        this.hospitalsLinked = hospitalsLinked;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setSpecialities(ArrayList<String> specialities) {
        this.specialities = specialities;
    }

    public void setTelemedicineAvail(boolean telemedicineAvail) {
        this.telemedicineAvail = telemedicineAvail;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phone);
        dest.writeString(selfUid);
        dest.writeString(degree);
        dest.writeInt(experience);
        dest.writeString(gender);
        dest.writeStringList(hospitalsLinked);
        dest.writeByte((byte) (isAvailable ? 1 : 0));
        dest.writeString(name);
        dest.writeDouble(rating);
        dest.writeStringList(specialities);
        dest.writeByte((byte) (telemedicineAvail ? 1 : 0));
    }
}
