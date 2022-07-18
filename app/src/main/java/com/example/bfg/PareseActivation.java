package com.example.bfg;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bfg.Models.Comments;
import com.example.bfg.Models.FeedBack;
import com.example.bfg.Models.Message;
import com.example.bfg.Models.MessageThread;
import com.example.bfg.Models.Notifications;
import com.example.bfg.Models.Post;
import com.example.bfg.Models.User;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class PareseActivation extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        Register your parse model
        ParseObject.registerSubclass(Comments.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(FeedBack.class);
        ParseObject.registerSubclass(Notifications.class);
        ParseObject.registerSubclass(MessageThread.class);
        ParseObject.registerSubclass(Message.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("5DIGQo5cnYxZX4MjMgFG9I0LE5smGFoGbcV1IO33")
                .clientKey("hNPPgaLef5lmoIUUWPABzitt6ngXWUeGkprmFQ4z")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
