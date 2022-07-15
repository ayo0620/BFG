package com.example.bfg.Models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

@ParseClassName("_User")
public class User extends ParseUser {
    public static final String KEY_FIRST_NAME = "First_Name";
    public static final String KEY_LAST_NAME = "Last_Name";
    public static final String KEY_PROFILE_IMAGE = "ProfileImage";
    public static final String KEY_USER_DESCRIPTION = "UserDescription";
    public static final String KEY_STATUS = "status";
    public static final String KEY_USER_FRIENDS = "UserFriends";
    public static final String KEY_STATUS_COUNT = "statusCount";

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
    public String getLastName(){return getString(KEY_LAST_NAME);}
    public void setProfileImage(ParseFile parsefile){
        put(KEY_PROFILE_IMAGE, parsefile);
    }
    public ParseFile getProfileImage(){
        return getParseFile(KEY_PROFILE_IMAGE);
    }
    public void setUserDescription(String description)
    {
        put(KEY_USER_DESCRIPTION,description);
    }
    public String getUserDescription(){
        return getString(KEY_USER_DESCRIPTION);
    }
    public void setStatus(String status){
        put(KEY_STATUS, status);
    }
    public String getStatus()
    {
        return getString(KEY_STATUS);
    }
    public void setUserFriend(List<String> user)
    {
        put(KEY_USER_FRIENDS, user);
    }
    public List<String> getUserFriends()
    {
        List<String> friendList = getList(KEY_USER_FRIENDS);
        if(friendList == null)
        {
            friendList = new ArrayList<>();
        }
        return friendList;
    }
    public void setStatusCount(int num)
    {
        put(KEY_STATUS_COUNT, num);
    }
    public int getStatusCount()
    {
        return getInt(KEY_STATUS_COUNT);
    }
}
