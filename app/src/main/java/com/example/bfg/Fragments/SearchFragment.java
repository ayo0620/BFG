package com.example.bfg.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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
import java.util.Locale;

public class SearchFragment extends Fragment {
    RecyclerView rvSearch;
    androidx.appcompat.widget.SearchView searchView;
    private TextView tvSearchPlaceholder;
    protected UsersAdapter adapter;
    protected List<User> allUsers;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvSearch = view.findViewById(R.id.rvSearch);
        searchView = view.findViewById(R.id.searchUser);
        tvSearchPlaceholder = view.findViewById(R.id.tvSearchPlaceholder);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());


        allUsers = new ArrayList<>();
        adapter = new UsersAdapter(getContext(), allUsers);

        // set the adapter on the recycler view
        rvSearch.setAdapter(adapter);
        rvSearch.clearFocus();
        // set the layout manager on the recycler view
        rvSearch.setLayoutManager(layoutManager);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tvSearchPlaceholder.setVisibility(View.GONE);
                queryForUsers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    private void queryForUsers(String query) {
        ParseQuery<User> queryUsers = ParseQuery.getQuery(User.class);
        queryUsers.whereContains("username", query);
        queryUsers.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> users, ParseException e) {
                allUsers.clear();
                adapter.notifyDataSetChanged();

                allUsers.addAll(users);
                adapter.notifyDataSetChanged();

                }
        });
    }

}