package propya.mr.jeevan.BluePrint;

import org.json.JSONObject;

import java.io.Serializable;
public class ChatQuestion {
    private String text;
    private String type;
    private Item[] items;
    private int itemLength;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
        setItemLength(this.items.length);
    }

    public int getItemLength() {
        return itemLength;
    }

    public void setItemLength(int itemLength) {
        this.itemLength = itemLength;
    }


//    public interface setItems
//    {
//
//        public  parseItemList(JSONObject[] items)
//        {
//
//        }
//    }

}
