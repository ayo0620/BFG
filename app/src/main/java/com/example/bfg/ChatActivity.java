package com.example.bfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bfg.Adapters.ChatAdapter;
import com.example.bfg.Models.Message;
import com.example.bfg.Models.MessageThread;
import com.example.bfg.Models.User;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import org.parceler.Parcels;
import org.w3c.dom.Comment;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView rvChats;
    private EditText etInputMsgField;
    private ImageView sendTextBtn;
    private CircleImageView toUserProfilePic;
    private TextView tvToUserName;
    private ImageView chat_back_btn;
    MessageThread messageThread;
    ChatAdapter adapter;
    private List<Message> allMessages;
    User fromUser;
    List<ParseQuery<Message>> allMessageQuery;
    Boolean mFirstLoad;
    List<ParseQuery<MessageThread>> allMessageThreadQuery;
    public static final int MAX_CHAT_MESSAGES = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        fromUser = getIntent().getParcelableExtra("otherUser");
        if(ParseUser.getCurrentUser()!= null)
        {
            startWithCurrentUser();
        }


        refreshMessages();

        String websocketUrl = "wss://finalprojectdraft.b4a.io/";
        ParseLiveQueryClient parseLiveQueryClient = null;
        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(websocketUrl));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ParseQuery<Message> parseQuery = ParseQuery.getQuery(Message.class);
//        when the sender is not the current user
        parseQuery.whereNotEqualTo(Message.KEY_SENDER,ParseUser.getCurrentUser().getObjectId());
        SubscriptionHandling<Message> subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> {
            allMessages.add(0,object);
            // RecyclerView updates need to be run on the UI thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("Live Query", "run: working");
                    adapter.notifyDataSetChanged();
                    rvChats.scrollToPosition(0);
                }
            });
        });

        chat_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void startWithCurrentUser() {
        setupMessagePosting();
    }

    private void setupMessagePosting() {

        etInputMsgField = findViewById(R.id.etInputMsgField);
        sendTextBtn = findViewById(R.id.sendTextBtn);
        rvChats = findViewById(R.id.rvChats);
        toUserProfilePic = findViewById(R.id.toUserProfilePic);
        tvToUserName = findViewById(R.id.tvToUserName);
        chat_back_btn = findViewById(R.id.chat_back_btn);

        tvToUserName.setText(fromUser.getUsername());
        if(fromUser.getProfileImage()!= null)
        {
            ParseFile img = fromUser.getProfileImage();
            Glide.with(this).load(img.getUrl()).into(toUserProfilePic);
        }
        else {
            toUserProfilePic.setImageResource(R.drawable.default_profile_icon);
        }
        MainActivity.setBorderColorStatus(fromUser,toUserProfilePic);

        allMessages = new ArrayList<>();
        mFirstLoad = true;
        adapter = new ChatAdapter(this,fromUser, allMessages);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
        linearLayoutManager.setReverseLayout(true);
        rvChats.setLayoutManager(linearLayoutManager);
        rvChats.setAdapter(adapter);

        sendTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageToSend = etInputMsgField.getText().toString();
                Message message = new Message();
                message.setSender(ParseUser.getCurrentUser());
                message.setReceiver(fromUser);
                message.setMessageText(messageToSend);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        refreshMessages();
                    }
                });
                checkIfFirstTimeChatting(messageToSend);
                etInputMsgField.setText("");
            }
        });
    }

    private void refreshMessages() {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.whereEqualTo(Message.KEY_SENDER, ParseUser.getCurrentUser());
        query.whereEqualTo(Message.KEY_RECEIVER, fromUser);

        ParseQuery<Message> query2 = ParseQuery.getQuery(Message.class);
        query2.whereEqualTo(Message.KEY_RECEIVER,ParseUser.getCurrentUser());
        query2.whereEqualTo(Message.KEY_SENDER, fromUser);


        allMessageQuery = new ArrayList<ParseQuery<Message>>();
        allMessageQuery.add(query);
        allMessageQuery.add(query2);

        ParseQuery<Message> query3 = ParseQuery.or(allMessageQuery);

        query3.setLimit(MAX_CHAT_MESSAGES);
        query3.include(Message.KEY_SENDER);
        query3.include(Message.KEY_RECEIVER);
        query3.orderByDescending(Message.KEY_CREATED_AT);

        query3.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> objects, ParseException e) {
                if (e == null) ;
                {
                    allMessages.clear();
                    allMessages.addAll(objects);
                    Log.i("Messages", allMessages.toString());
                    adapter.notifyDataSetChanged(); // update adapter

                    if(mFirstLoad)
                    {
                        rvChats.scrollToPosition(0);
                        mFirstLoad = false;
                    }
                    else{
                        Log.e("message","Error Loading messages"+e);
                    }
                }
            }
        });
    }

    private void checkIfFirstTimeChatting(String messageToSend) {
        ParseQuery<MessageThread> query = ParseQuery.getQuery(MessageThread.class);
        query.whereEqualTo(MessageThread.KEY_MESSAGE_FROM_USER, ParseUser.getCurrentUser());
        query.whereEqualTo(MessageThread.KEY_MESSAGE_TO_USER, fromUser);

        ParseQuery<MessageThread> query2 = ParseQuery.getQuery(MessageThread.class);
        query2.whereEqualTo(MessageThread.KEY_MESSAGE_TO_USER,ParseUser.getCurrentUser());
        query2.whereEqualTo(MessageThread.KEY_MESSAGE_FROM_USER, fromUser);


        allMessageThreadQuery = new ArrayList<ParseQuery<MessageThread>>();
        allMessageThreadQuery.add(query);
        allMessageThreadQuery.add(query2);

        ParseQuery<MessageThread> query3 = ParseQuery.or(allMessageThreadQuery);

        query3.include(Message.KEY_SENDER);
        query3.include(Message.KEY_RECEIVER);
        query3.addAscendingOrder(Message.KEY_CREATED_AT);

        query3.findInBackground(new FindCallback<MessageThread>() {
            @Override
            public void done(List<MessageThread> objects, ParseException e) {
                if (e == null) ;
                {
                    if(objects.size()==0)
                    {
                        MessageThread messageThread = new MessageThread();
                        messageThread.setMessageFromUser(ParseUser.getCurrentUser());
                        messageThread.setMessageToUser(fromUser);
                        messageThread.setRecentMessages(messageToSend);
                        messageThread.saveInBackground();
                    }
                    else
                    {
                        MessageThread myMessageThread = objects.get(0);
                        myMessageThread.setRecentMessages(messageToSend);
                        myMessageThread.saveInBackground();
                    }
                }
            }
        });
    }


}