package propya.mr.jeevan.BluePrint;

import android.os.Parcel;
import android.os.Parcelable;

public class Scheme implements Parcelable {

    private String mSchemeName;
    private String mSchemeStatus;
    private String mSchemeLink;
    private String mSchemeDesc;
    private String mSchemeWhat;
    private String mSchemeCoverage;


    public Scheme(String schemeName, String schemeStatus, String schemeLink, String schemeDesc, String schemeWhat, String schemeCoverage){
        mSchemeName = schemeName;
        mSchemeStatus = schemeStatus;
        mSchemeLink = schemeLink;
        mSchemeDesc = schemeDesc;
        mSchemeWhat = schemeWhat;
        mSchemeCoverage = schemeCoverage;
    }

    protected Scheme(Parcel in) {
        mSchemeName = in.readString();
        mSchemeStatus = in.readString();
        mSchemeLink = in.readString();
        mSchemeDesc = in.readString();
        mSchemeWhat = in.readString();
        mSchemeCoverage = in.readString();
    }

    public static final Creator<Scheme> CREATOR = new Creator<Scheme>() {
        @Override
        public Scheme createFromParcel(Parcel in) {
            return new Scheme(in);
        }

        @Override
        public Scheme[] newArray(int size) {
            return new Scheme[size];
        }
    };

    public String getmSchemeName() {
        return mSchemeName;
    }

    public String getmSchemeStatus() {
        return mSchemeStatus;
    }

    public String getmSchemeLink() {
        return mSchemeLink;
    }

    public String getmSchemeDesc() {
        return mSchemeDesc;
    }

    public String getmSchemeWhat() {
        return mSchemeWhat;
    }

    public String getmSchemeCoverage() {
        return mSchemeCoverage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mSchemeName);
        dest.writeString(mSchemeStatus);
        dest.writeString(mSchemeLink);
        dest.writeString(mSchemeDesc);
        dest.writeString(mSchemeWhat);
        dest.writeString(mSchemeCoverage);
    }
}
