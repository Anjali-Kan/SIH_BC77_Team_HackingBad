package propya.mr.jeevan.BluePrint;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Hospital implements Parcelable {

    public static final String unknown = "UNKNOWN";
    public static final boolean defaultBool = false;

    public String hospitalName;
    public Location hospitalLocation;
    public String hospitalAddress = unknown; // default

    public int budget = 0; /// between 0 and 5
    public double rating = 0.0;

    public boolean burnWard = defaultBool;
    public boolean childWard = defaultBool;
    public boolean emergency = defaultBool;
    public boolean government = defaultBool;
    public boolean heartWard = defaultBool;

    public String phone = unknown;
    public String email = unknown;

    public ArrayList<String> facilities;
    public ArrayList<String> insurance;
    public ArrayList<String> schemes;
    public ArrayList<String> specialists;

    public Hospital(String name, Location location, int budget, double rating, boolean burnWard, boolean emergency, boolean government,
                    boolean heartWard, String phone, String email, ArrayList<String> facilities, ArrayList<String> insurance,
                    ArrayList<String> schemes, ArrayList<String> specialists ) {
        if(name!=null)
            this.hospitalName = name;

        if(location!=null)
            this.hospitalLocation = location;

        if(budget>0)
            this.budget = budget;
        if(rating>0)
            this.rating = rating;
        this.burnWard = burnWard;
        this.emergency = emergency;
        this.government = government;
        this.heartWard = heartWard;

        if(phone!=null && phone.length()>0)
            this.phone = phone;

        if(email!=null && email.length()>0)
            this.email = email;

        if(facilities!=null)
            this.facilities = facilities;

        if(insurance!=null)
            this.insurance = insurance;

        if(schemes!=null)
            this.schemes = schemes;

        if(specialists!=null)
            this.specialists = specialists;

    }

    public Hospital(JSONObject jsonObject){
        try {
            setHospitalAddress(jsonObject.getString("address"));
            setHospitalName(jsonObject.getString("name"));
            JSONObject locationJSONObject = jsonObject.getJSONObject("location");
            Location location = new Location("");
            location.setLatitude(locationJSONObject.getDouble("_latitude"));
            location.setLongitude(locationJSONObject.getDouble("_longitude"));
            setHospitalLocation(location);
            setPhone(jsonObject.getString("contact"));
            setEmail(jsonObject.getString("email"));
            setEmergency(jsonObject.getBoolean("emergency"));
            setBudget(jsonObject.getInt("budget"));
            setBurnWard(jsonObject.getBoolean("burn"));
            setChildWard(jsonObject.getBoolean("child"));

            JSONArray facilitiesJSONArray = jsonObject.getJSONArray("facility");
            ArrayList<String> facilities = new ArrayList<>();
            for(int i=0; i<facilitiesJSONArray.length(); i++) {
                facilities.add(facilitiesJSONArray.getString(i));
            }
            setFacilities(facilities);

            setGovernment(jsonObject.getBoolean("government"));
            setHeartWard(jsonObject.getBoolean("heart"));

            JSONArray insuranceJSONArray = jsonObject.getJSONArray("insurance");
            ArrayList<String> insurances = new ArrayList<>();
            for(int i=0; i<insuranceJSONArray.length(); i++) {
                insurances.add(insuranceJSONArray.getString(i));
            }
            setInsurance(insurances);




            setRating(jsonObject.getDouble("rating"));

            JSONArray schemeJSONArray = jsonObject.getJSONArray("scheme");
            ArrayList<String> schemes = new ArrayList<>();
            for(int i=0; i<schemeJSONArray.length(); i++) {
                schemes.add(schemeJSONArray.getString(i));
            }
            setSchemes(schemes);

            JSONArray specialistsJSONArray = jsonObject.getJSONArray("specialists");
            ArrayList<String> specialists = new ArrayList<>();
            for(int i=0; i<specialistsJSONArray.length(); i++) {
                specialists.add(specialistsJSONArray.getString(i));
            }
            setSpecialists(specialists);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    protected Hospital(Parcel in) {
        hospitalName = in.readString();
        hospitalLocation = in.readParcelable(Location.class.getClassLoader());
        hospitalAddress = in.readString();
        budget = in.readInt();
        rating = in.readDouble();
        burnWard = in.readByte() != 0;
        childWard = in.readByte() != 0;
        emergency = in.readByte() != 0;
        government = in.readByte() != 0;
        heartWard = in.readByte() != 0;
        phone = in.readString();
        email = in.readString();
        facilities = in.createStringArrayList();
        insurance = in.createStringArrayList();
        schemes = in.createStringArrayList();
        specialists = in.createStringArrayList();
    }

    public static final Creator<Hospital> CREATOR = new Creator<Hospital>() {
        @Override
        public Hospital createFromParcel(Parcel in) {
            return new Hospital(in);
        }

        @Override
        public Hospital[] newArray(int size) {
            return new Hospital[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(hospitalName);
        dest.writeParcelable(hospitalLocation, flags);
        dest.writeString(hospitalAddress);
        dest.writeInt(budget);
        dest.writeDouble(rating);
        dest.writeByte((byte) (burnWard ? 1 : 0));
        dest.writeByte((byte) (childWard ? 1 : 0));
        dest.writeByte((byte) (emergency ? 1 : 0));
        dest.writeByte((byte) (government ? 1 : 0));
        dest.writeByte((byte) (heartWard ? 1 : 0));
        dest.writeString(phone);
        dest.writeString(email);
        dest.writeStringList(facilities);
        dest.writeStringList(insurance);
        dest.writeStringList(schemes);
        dest.writeStringList(specialists);
    }

//    {"hits":[{"address":"Marol, Andheri East, Mumbai 400059","budget":4,"burn":true,"child ":true,"contact":"022 6767 6767","email":"response@sevenhillshospital.com","emergency":true,"facility":["x ray","ct scan","operation theatre","pharmacy"],"government":false,"heart":true,"insurance":["lic","max bupa"],"location":{"_latitude":19.117736,"_longitude":72.878197},"name":"SevenHills Hospital","rating":4.5,"scheme":["Universal Health Insurance Scheme"],"specialists":["eye","dentist","neurologist","urologist"],"objectID":"oU06pO0y3t4EsJimZfhB","_highlightResult":{"address":{"value":"Marol, Andheri East, Mumbai 400059","matchLevel":"none","matchedWords":[]},"budget":{"value":"4","matchLevel":"none","matchedWords":[]},"name":{"value":"SevenHills Hospital","matchLevel":"none","matchedWords":[]}}},{"address":"Deonar Bus depot, Deonar, Chembur, Mumbai 400088","budget":3,"burn":true,"child":false,"contact":"022 4334 4600","email":"spectradesk@apollo.com","emergency":true,"facility":["icu","iccu","mri scan","ct scan"],"government":false,"heart":true,"insurance":["lic","apollo","hdfc"],"location":{"_latitude":19.044698,"_longitude":72.910216},"name":"Apollo Spectra Hospital","rating":4.5,"scheme":["universal health insurance scheme","janashree bima yojana"],"specialists":["heart","child","gynecologist","urologist"],"objectID":"g8g4qMx4JDzp6sqEqBRK","_highlightResult":{"address":{"value":"Deonar Bus depot, Deonar, Chembur, Mumbai 400088","matchLevel":"none","matchedWords":[]},"budget":{"value":"3","matchLevel":"none","matchedWords":[]},"name":{"value":"Apollo Spectra Hospital","matchLevel":"none","matchedWords":[]}}},{"address":"Patwardhan Marg, Four Bunglows, Andheri west, Mumbai 400053","budget":5,"burn":true,"child":true,"contact":"022 3069 6969","email":"reception@kokilabenhospital.com","emergency":true,"facility":["x ray","ct scan","operation theatre","dialysis","blood bank"],"government":false,"heart":true,"insurance":["lic","apollo","icici"],"location":{"_latitude":19.131031,"_longitude":72.824924},"name":"Kokilaben Ambani Hospital","rating":4.5,"scheme":["Universal health Insurance Scheme"],"specialists":["cancer","eye","therapist","heart"],"objectID":"0Q8jI6Ls8369L4fZs3GC","_highlightResult":{"address":{"value":"Patwardhan Marg, Four Bunglows, Andheri west, Mumbai 400053","matchLevel":"none","matchedWords":[]},"budget":{"value":"5","matchLevel":"none","matchedWords":[]},"name":{"value":"Kokilaben Ambani Hospital","matchLevel":"none","matchedWords":[]}}},{"address":"Nahur west, Mulund, Mumbai 400078","budget":4,"burn":true,"child":true,"contact":"096060 47050","email":"reception@fortis.com","emergency":true,"facility":["mri scan","laboratory","icu","blood bank"],"government":false,"heart":true,"insurance":["lic","apollo","hdfc"],"location":{"_latitude":19.162216,"_longitude":72.942045},"name":"Fortis Hospitals","rating":4.3,"scheme":["universal health insurance scheme"],"specialists":["child","caner","heart","neurology"],"objectID":"5ewyfBCw2VQrFPXUDcxE","_highlightResult":{"address":{"value":"Nahur west, Mulund, Mumbai 400078","matchLevel":"none","matchedWords":[]},"budget":{"value":"4","matchLevel":"none","matchedWords":[]},"name":{"value":"Fortis Hospitals","matchLevel":"none","matchedWords":[]}}},{"address":"Lallubhai Park road, Navpada, Andheri west, Mumbai 400058 ","budget":3,"burn":false,"child":true,"contact":"022 2623 7088","email":"masranihospital@mimas.com","emergency":false,"facility":["neo natal ward","incubator","operation theater","pharmacy"],"government":false,"heart":false,"insurance":["hdfc","lic","apollo"],"location":{"_latitude":19.112621,"_longitude":72.844603},"name":"Masrani Hospital","rating":4,"scheme":["janashree bima yojana","aam aadmi bima yojana"],"specialists":["chilld","gynecologist"],"objectID":"lJYES4DdJxFxz46ocShZ","_highlightResult":{"address":{"value":"Lallubhai Park road, Navpada, Andheri west, Mumbai 400058 ","matchLevel":"none","matchedWords":[]},"budget":{"value":"3","matchLevel":"none","matchedWords":[]},"name":{"value":"Masrani Hospital","matchLeve


    public static ArrayList<Hospital> parseJSON(JSONArray array) throws JSONException {
        ArrayList<Hospital> hospitals = new ArrayList<>();
        for(int i=0; i<array.length();i++ ){
            JSONObject hospital = array.getJSONObject(i);
            if(hospital.has("indexType")){
                if(!hospital.getString("indexType").equals("hospital"))
                    continue;
            }
            hospitals.add(new Hospital(hospital));
        }
        return hospitals;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public void setHospitalLocation(Location hospitalLocation) {
        this.hospitalLocation = hospitalLocation;
    }

    public void setHospitalAddress(String hospitalAddress) {
        this.hospitalAddress = hospitalAddress;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public void setBurnWard(boolean burnWard) {
        this.burnWard = burnWard;
    }

    public void setChildWard(boolean childWard) {
        this.childWard = childWard;
    }

    public void setEmergency(boolean emergency) {
        this.emergency = emergency;
    }

    public void setGovernment(boolean government) {
        this.government = government;
    }

    public void setHeartWard(boolean heartWard) {
        this.heartWard = heartWard;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFacilities(ArrayList<String> facilities) {
        this.facilities = facilities;
    }

    public void setInsurance(ArrayList<String> insurance) {
        this.insurance = insurance;
    }

    public void setSchemes(ArrayList<String> schemes) {
        this.schemes = schemes;
    }

    public void setSpecialists(ArrayList<String> specialists) {
        this.specialists = specialists;
    }
}
