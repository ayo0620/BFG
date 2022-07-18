package com.example.bfg;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.bfg.Adapters.MessageThreadAdapter;
import com.example.bfg.Models.Message;
import com.example.bfg.Models.MessageThread;
import com.example.bfg.Models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessagingActivity extends AppCompatActivity {
    RecyclerView rvMessages;
    List<MessageThread> allMessageThreads;
    MessageThreadAdapter adapter;
    private ImageView ivMessageClose;
    List<ParseQuery<MessageThread>> queries;
    public static final int MAX_MESSAGES_TO_QUERY = 15;
    public static final String TAG = MessagingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        rvMessages = findViewById(R.id.rvMessages);
        ivMessageClose = findViewById(R.id.ivMessageClose);

        allMessageThreads = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvMessages.setLayoutManager(linearLayoutManager);
        adapter = new MessageThreadAdapter(this,allMessageThreads);
        rvMessages.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        ivMessageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        queryMessageThreads();
    }

    private void queryMessageThreads() {
        ParseQuery<MessageThread> queryForSender = ParseQuery.getQuery(MessageThread.class);
        queryForSender.whereEqualTo(MessageThread.KEY_MESSAGE_FROM_USER,  User.getCurrentUser());

        ParseQuery<MessageThread> queryForReceiver = ParseQuery.getQuery(MessageThread.class);
        queryForReceiver.whereEqualTo(MessageThread.KEY_MESSAGE_TO_USER, User.getCurrentUser());

        queries = new ArrayList<ParseQuery<MessageThread>>();
        queries.add(queryForSender);
        queries.add(queryForReceiver);

        ParseQuery<MessageThread> mainQuery = ParseQuery.or(queries);
        mainQuery.setLimit(MAX_MESSAGES_TO_QUERY);
        mainQuery.addDescendingOrder(Message.KEY_CREATED_AT);
        mainQuery.findInBackground(new FindCallback<MessageThread>() {
            @Override
            public void done(List<MessageThread> messageThreads, ParseException e) {
                if (e==null) {
                    allMessageThreads.addAll(messageThreads);
                    Log.i(TAG, messageThreads.toString());
//                    Set<String> container = new HashSet<>();
//                    for (MessageThread m: messageThreads) {
//                        String senderandreceiver = "";
//                        try{
//                            String sender = ((User) (m.getMessageFromUser().fetchIfNeeded())).getObjectId();
//                            String receiver = ((User) (m.getMessageToUser().fetchIfNeeded())).getObjectId();
//                            if (sender.equals(ParseUser.getCurrentUser().getObjectId())) {
//                                senderandreceiver += sender + " " + receiver;
//
//                            } else {
//                                senderandreceiver += receiver + " " + sender;
//                            }
//                            if (container.contains(senderandreceiver)) {
//                                continue;
//                            }  else {
//                                allMessageThreads.add(m);
//                                container.add(senderandreceiver);
//                            }
//
//
//                        } catch(ParseException err) {
//
//                        }
//                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.e("done", "done: ",  e);
                }
            }
        });

    }
}