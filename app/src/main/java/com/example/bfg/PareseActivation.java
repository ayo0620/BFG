package com.example.bfg;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bfg.Models.Comments;
import com.example.bfg.Models.FeedBack;
import com.example.bfg.Models.Library;
import com.example.bfg.Models.Message;
import com.example.bfg.Models.MessageThread;
import com.example.bfg.Models.Notifications;
import com.example.bfg.Models.Post;
import com.example.bfg.Models.User;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

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
        ParseObject.registerSubclass(Library.class);

        // Use for monitoring Parse network traffic
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // any network interceptors must be added with the Configuration Builder given this syntax
        builder.networkInterceptors().add(httpLoggingInterceptor);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("5DIGQo5cnYxZX4MjMgFG9I0LE5smGFoGbcV1IO33")
                .clientKey("hNPPgaLef5lmoIUUWPABzitt6ngXWUeGkprmFQ4z")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
