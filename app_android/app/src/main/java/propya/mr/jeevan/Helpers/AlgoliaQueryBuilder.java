package propya.mr.jeevan.Helpers;

import java.util.ArrayList;
import java.util.Set;

public class AlgoliaQueryBuilder {

    QueryReady callBack;

    String baseQuery = "";


    public AlgoliaQueryBuilder(QueryReady callBack) {
        this.callBack = callBack;
    }

    private String getFilterString(){
        return baseQuery;
    }

    public void addOrQuery(String key, ArrayList<String> values){
        String miniQuery = "";

        if(baseQuery.length() != 0)
            baseQuery += " AND (";
        else
            baseQuery += "(";

        for(String val : values) {
            if(miniQuery.length() != 0)
                miniQuery += " OR ";
            miniQuery += String.format("%s:'%s'", key,val);

        }
        baseQuery += String.format("%s)", miniQuery);
    }

    public void addAndQuery(String key,String value){
        if(baseQuery.length() != 0)
            baseQuery += " AND ";
        baseQuery += String.format("%s:'%s'", key, value);
    }

    public void addRadioQuery(String key,String value){
        String mathExp = "";

        if(baseQuery.length() != 0)
            baseQuery += " AND ";

        switch(value) {
            case "low":
                mathExp += ":0 TO 1.999";
                break;
            case "mid":
                mathExp += ":2 TO 3.999";
                break;
            case "high":
                mathExp += ":4 TO 5";
                break;
        }

        baseQuery += String.format("%s%s", key, mathExp);
    }

    public void addDoctorExpRadioQuery(String key,String value){
        String mathExp = "";

        if(baseQuery.length() != 0)
            baseQuery += " AND ";

        switch(value) {
            case "low":
                mathExp += " < 5";
                break;
            case "mid":
                mathExp += ":5 TO 14";
                break;
            case "high":
                mathExp += " >= 15";
                break;
        }

        baseQuery += String.format("%s%s", key, mathExp);
    }

    public void search(String userInput){
        callBack.queryString(userInput, getFilterString());

        // Set baseQuery empty to avoid concatenating future search filters
        baseQuery = "";
    }

    public interface QueryReady{
        public void queryString(String userInput, String filters);
    }

}
