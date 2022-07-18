package com.example.bfg.Models;

import android.os.Message;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("MessageThread")
public class MessageThread extends ParseObject {
    public static final String KEY_MESSAGE_FROM_USER = "messageFromUser";
    public static final String KEY_MESSAGE_TO_USER = "messageToUser";
    public static final String KEY_RECENT_MESSAGES = "recentMessage";

    public void setMessageFromUser(ParseUser parseUser)
    {
        put(KEY_MESSAGE_FROM_USER, parseUser);
    }
    public ParseUser getMessageFromUser()
    {
        return  getParseUser(KEY_MESSAGE_FROM_USER);
    }

    public void setMessageToUser(ParseUser parseUser)
    {
        put(KEY_MESSAGE_TO_USER, parseUser);
    }
    public ParseUser getMessageToUser()
    {
        return  getParseUser(KEY_MESSAGE_TO_USER);
    }

    public void setRecentMessages(String messages)
    {
        put(KEY_RECENT_MESSAGES, messages);
    }
    public String getRecentMessages()
    {
        return  getString(KEY_RECENT_MESSAGES);
    }
}
