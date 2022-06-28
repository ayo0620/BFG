package com.example.bfg.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bfg.Adapters.GridPostsAdapter;
import com.example.bfg.Models.Post;
import com.example.bfg.R;

import java.util.ArrayList;
import java.util.List;


public class UserPostsFragment extends Fragment {
    List<Post> allUserPosts;
    GridPostsAdapter adapter;
    RecyclerView rvGridPosts;

    public UserPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_posts, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        allUserPosts = new ArrayList<>();
        adapter = new GridPostsAdapter(getContext(),allUserPosts);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvGridPosts = view.findViewById(R.id.rvGridPosts);

        Log.i("users",allUserPosts.toString());
        rvGridPosts.setLayoutManager(layoutManager);
        rvGridPosts.setAdapter(adapter);
        rvGridPosts.setHasFixedSize(true);
        super.onViewCreated(view, savedInstanceState);


    }
}