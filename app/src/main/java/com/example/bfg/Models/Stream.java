package com.example.bfg.Models;

import org.json.JSONException;
import org.json.JSONObject;

public class Stream {

    public String viewCount;
    public String streamerName;
    public String streamForGame;
    public String Image;

    public Stream()
    {
    }
    public Stream(JSONObject jsonObject) throws JSONException {
        viewCount = jsonObject.getString("viewer_count");
        streamerName = jsonObject.getString("user_name");
        streamForGame = jsonObject.getString("game_name");
        Image = jsonObject.getString("thumbnail_url");
    }

    public String getViewCount()
    {
        return viewCount;
    }

    public String getStreamerName()
    {
        return streamerName;
    }

    public String getStreamForGame()
    {
        return streamForGame;
    }

    public String getImage()
    {
        return Image;
    }
}
