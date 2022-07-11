package com.example.bfg.Models;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Notifications")
public class Notifications extends ParseObject {
    public static final String KEY_NOTIFIED_FROM = "notifiedFrom";
    public static final String KEY_NOTIFICATION = "notification";
    public static final String KEY_notifyThis = "notifyThis";

    public void setNotifiedFrom (ParseUser user)
    {
        put(KEY_NOTIFIED_FROM, user);
    }
    public ParseUser getNotifiedFrom()
    {
        return getParseUser(KEY_NOTIFIED_FROM);
    }

    public void setNotification(String notification)
    {
        put(KEY_NOTIFICATION, notification);
    }
    public String getNotification()
    {
        return getString(KEY_NOTIFICATION);
    }

    public void setNotifyThis(ParseUser toUser)
    {
        put(KEY_notifyThis, toUser);
    }
    public ParseUser getNotifyThis()
    {
        return getParseUser(KEY_notifyThis);
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
