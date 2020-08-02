package propya.mr.jeevan.BluePrint;

public class Scheme {

    private String mSchemeName;
    private String mSchemeStatus;

    public Scheme(String schemeName, String schemeStatus){
        mSchemeName = schemeName;
        mSchemeStatus = schemeStatus;
    }

    public String getSchemeName(){ return mSchemeName; }
    public String getmSchemeStatus(){return mSchemeStatus;}
}
