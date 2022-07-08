package com.example.bfg.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comments")
public class Comments extends ParseObject {
    public static final String KEY_USER = "user";
    public static final String KEY_POST = "post";
    public static final String KEY_DESCRIPTION = "description";

    public ParseUser getUser()
    {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user)
    {
        put(KEY_USER, user);
    }
    public Post getPost()
    {
        return (Post) getParseObject(KEY_POST);
    }
    public void setPost(Post post)
    {
        put(KEY_POST,post);
    }

    public String getDescription()
    {
        return getString(KEY_DESCRIPTION);
    }
    public void setDescription(String description)
    {
        put(KEY_DESCRIPTION, description);
    }
}
