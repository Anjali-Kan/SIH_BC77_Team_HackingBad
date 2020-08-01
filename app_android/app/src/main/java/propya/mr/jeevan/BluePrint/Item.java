package propya.mr.jeevan.BluePrint;

import org.json.JSONArray;
import java.io.Serializable;

public class Item {
    private String id;
    private String name;
    private JSONArray choices;
    private int choicesLength;

    public JSONArray getChoices() {
        return choices;
    }

    public void setChoices(JSONArray choices) {
        this.choices = choices;
        setChoicesLength(this.choices.length());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getChoicesLength() {
        return choicesLength;
    }

    public void setChoicesLength(int choicesLength) {
        this.choicesLength = choicesLength;
    }
}
