package com.example.bfg.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bfg.Adapters.PostsAdapter;
import com.example.bfg.Adapters.UsersAdapter;
import com.example.bfg.Models.Post;
import com.example.bfg.Models.User;
import com.example.bfg.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    RecyclerView rvSearch;
    protected UsersAdapter adapter;
    protected List<User> allUsers;
    Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.searchToolbar);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        rvSearch = view.findViewById(R.id.rvSearch);

        allUsers = new ArrayList<>();
        adapter = new UsersAdapter(getContext(), allUsers);

        // set the adapter on the recycler view
        rvSearch.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvSearch.setLayoutManager(layoutManager);

        querySearch(0);

    }

    private void querySearch(int i) {
        ParseQuery<User> query = ParseQuery.getQuery(User.class);
        // include data referred by user key
        query.include("username");
        // limit query to latest 20 items
        query.setLimit(10);
        query.setSkip(i);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> users, ParseException e) {
                // for debugging purposes let's print every post description to logcat
                for (User user : users) {
                    Log.i("SearchActivity", "User: " + user.getFirstName() + ", username: " + user.getUsername());
                }

                // save received posts to list and notify adapter of new data
                allUsers.addAll(users);
                adapter.notifyDataSetChanged();
            }
        });
    }
}