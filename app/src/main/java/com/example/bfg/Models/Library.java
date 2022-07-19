package com.example.bfg.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;

@ParseClassName("Library")
public class Library extends ParseObject {
    public static final String KEY_GAME_NAME = "gameName";
    public static final String KEY_GAME_IMAGE = "gameImage";
    public static final String KEY_FOR_USER = "forUser";

    public void setGameImage(String gameImage) {
        put(KEY_GAME_IMAGE, gameImage);
    }

    public String getGameImage() {
        return getString(KEY_GAME_IMAGE);
    }

    public void setGameName(String gameName) {
        put(KEY_GAME_NAME, gameName);
    }

    public String getGameName() {
        return getString(KEY_GAME_NAME);
    }

    public void setForUser(ParseUser parseUser) {
        put(KEY_FOR_USER, parseUser);
    }

    public ParseUser getForUser()
    {
        return getParseUser(KEY_FOR_USER);
    }
}
