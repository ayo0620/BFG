package com.example.bfg.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("UserFeedback")
public class FeedBack extends ParseObject {

    public static final String KEY_DESCRIPTION = "Description";
    public static final String KEY_USER  = "userID";

    public void setDescription(String description)
    {
        put(KEY_DESCRIPTION,description);
    }
    public String getDescription()
    {
        return getString(KEY_DESCRIPTION);
    }
    public void setUser(ParseUser user)
    {
        put(KEY_USER, user);
    }
    public ParseUser getUser()
    {
        return getParseUser(KEY_USER);
    }
}
