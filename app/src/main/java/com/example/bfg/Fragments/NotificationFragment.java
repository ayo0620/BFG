package com.example.bfg.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.bfg.Adapters.NotificationsAdapter;
import com.example.bfg.MainActivity;
import com.example.bfg.Models.Notifications;
import com.example.bfg.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {
    RecyclerView rvNotifications;
    protected List<Notifications> allNotifications;
    protected NotificationsAdapter adapter;
    public static final String TAG ="NotificationFragment";
    private ImageView notificationClose;
    ProgressBar notificationProgressBar;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       notificationProgressBar = view.findViewById(R.id.notificationProgressBar);
       notificationProgressBar.setVisibility(View.VISIBLE);

        rvNotifications = view.findViewById(R.id.rvNotifications);
        notificationClose = view.findViewById(R.id.notificationClose);

        notificationClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        allNotifications = new ArrayList<>();
        rvNotifications.setLayoutManager(layoutManager);
        adapter = new NotificationsAdapter(getContext(),allNotifications);
        rvNotifications.setAdapter(adapter);
        queryNotifications();
    }

    private void queryNotifications() {
        ParseQuery<Notifications> query = ParseQuery.getQuery(Notifications.class);
        ParseUser user = ParseUser.getCurrentUser();
        query.whereContains(Notifications.KEY_notifyThis,user.getObjectId());
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Notifications>() {
            @Override
            public void done(List<Notifications> notifications, ParseException e) {
                if(e!=null)
                {
                    Log.i(TAG, "issue with getting notification",e);
                }
                allNotifications.addAll(notifications);
                adapter.notifyDataSetChanged();
                notificationProgressBar.setVisibility(View.GONE);
            }
        });
    }
}