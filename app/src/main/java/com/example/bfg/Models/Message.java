package com.example.bfg.Models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Message")
public class Message extends ParseObject {
    public static final String KEY_MESSAGE_TEXT = "messageText";
    public static final String KEY_THREAD_ID = "Thread_Id";
    public static final String KEY_SENDER = "sender";
    public static final String KEY_RECEIVER = "receiver";

    public void setMessageText(String messageText)
    {
        put(KEY_MESSAGE_TEXT, messageText);
    }

    public String getMessageText()
    {
        return getString(KEY_MESSAGE_TEXT);
    }

    public void setThreadId(MessageThread messageThread)
    {
        put(KEY_THREAD_ID,messageThread);
    }

    public ParseObject getThreadId()
    {
        return getParseObject(KEY_THREAD_ID);
    }

    public void setSender(ParseUser parseUser)
    {
        put(KEY_SENDER, parseUser);
    }

    public ParseUser getSender()
    {
        return getParseUser(KEY_SENDER);
    }

    public void setReceiver(ParseUser parseUser)
    {
        put(KEY_RECEIVER,parseUser);
    }
    public ParseUser getReceiver()
    {
        return getParseUser(KEY_RECEIVER);
    }
}
