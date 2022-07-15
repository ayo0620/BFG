package com.example.bfg.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.example.bfg.Adapters.GridPostsAdapter;
import com.example.bfg.Models.Post;
import com.example.bfg.Models.User;
import com.example.bfg.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class UserFavoritedFragment extends Fragment {

    GridView gridViewFavorited;
    public ArrayList<ParseFile> allFavorited;
    private ParseUser user;
    User currUser;
    public UserFavoritedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_favorited, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        user = ParseUser.getCurrentUser();
        currUser = (User) user;

        gridViewFavorited = view.findViewById(R.id.gridViewFavorited);

        setUpGridViewFavorited();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setUpGridViewFavorited() {
        allFavorited = new ArrayList<>();
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.addDescendingOrder("createdAt");
        query.whereContains("favoritedBy",currUser.getObjectId());
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e!= null)
                {
                    Log.i("UserPost ","issue accessing posts",e);
                }
                for (Post post : posts) {
                    allFavorited.add(post.getImage());
                }
                int gridwidth = getResources().getDisplayMetrics().widthPixels;
                int imageWidth = gridwidth/ 3;
                gridViewFavorited.setColumnWidth(imageWidth);

                ArrayList<String> imgUrls = new ArrayList<String>();
                for (int i = 0; i<allFavorited.size();i++)
                {
                    imgUrls.add(allFavorited.get(i).getUrl());
                }
                GridPostsAdapter adapter = new GridPostsAdapter(getContext(),imgUrls);
                gridViewFavorited.setAdapter((adapter));
            }

        });
    }
}