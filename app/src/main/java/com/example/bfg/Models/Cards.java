package com.example.bfg.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Cards {
    public String  gameName;
    public String gameimage;
    public String gameCatergory;

    public Cards(){}

    public Cards(JSONObject jsonObject) throws JSONException {
        gameName = jsonObject.getString("name");
        gameimage = jsonObject.getString("box_art_url");
    }
    public static List<Cards> fromJsonArray(JSONArray itemsJsonArray) throws JSONException {
        List<Cards> allItems = new ArrayList<>();
        for (int i = 0; i < itemsJsonArray.length(); i++) {
            if(new Cards(itemsJsonArray.getJSONObject(i)).getImage() != null){
                allItems.add(new Cards(itemsJsonArray.getJSONObject(i)));
            }
        }
        return allItems;
    }

    public String getName() {
        return gameName;
    }

    public String getImage() {
        return gameimage;
    }
}
