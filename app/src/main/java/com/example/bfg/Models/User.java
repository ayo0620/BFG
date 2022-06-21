package com.example.bfg.Models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String KEY_FIRST_NAME = "First_Name";
    public static final String KEY_LAST_NAME = "Last_Name";

    public String getFirstName(){
        return getString(KEY_FIRST_NAME);
    }
    public void setFirstName(String firstName)
    {
        put(KEY_FIRST_NAME,firstName);
    }

    public void setLastName(String lastName)
    {
        put(KEY_LAST_NAME,lastName);
    }
}
