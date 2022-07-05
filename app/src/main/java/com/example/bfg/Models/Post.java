package com.example.bfg.Models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ParseClassName("Posts")
public class Post extends ParseObject {
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_DESCRIPTION = "Description";
    public static final String KEY_USER = "user";
    public static final String KEY_LIKED_BY = "likedby";
    public static final String KEY_FAVORITED_BOOL = "favorited";
    public static final String KEY_POST_FOR_GAME = "PostForGame";

    public ParseFile getImage(){
        return (getParseFile(KEY_IMAGE));
    }
    public void setImage(ParseFile parseFile)
    {
        put(KEY_IMAGE, parseFile);
    }
    public String getDescription()
    {
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description){
        put(KEY_DESCRIPTION,description);
    }
    public ParseUser getUser()
    {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseObject parseObject)
    {
        put(KEY_USER,parseObject);
    }
    public List<String> getLikedBy(){
        List<String> likeBy = getList(KEY_LIKED_BY);
        if(likeBy == null)
        {
            likeBy = new ArrayList<>();
        }
        return likeBy;
    }
    public void setLikedBy(List<String> likedBy){
        put(KEY_LIKED_BY, likedBy);
    }
    public String likeCountDisplayText()
    {
        String likesText = String.valueOf(getLikedBy().size());
        if (likesText.equals("1"))
        {
            likesText = "like";
        }
        else if(likesText.equals("0"))
        {
            likesText = "like";
        }
        else
        {
            likesText = "likes";
        }
        return likesText;
    }
    public Boolean getFavoritedBool()
    {
        return getBoolean(KEY_FAVORITED_BOOL);
    }
    public void setFavoritedBool(Boolean favoritedBool)
    {
        put(KEY_FAVORITED_BOOL, favoritedBool);
    }
    public String getPostForGame(){
        return getString(KEY_POST_FOR_GAME);
    }
    public void setPostForGame(String gameName){
        put(KEY_POST_FOR_GAME,gameName);
    }

    public String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }

}
