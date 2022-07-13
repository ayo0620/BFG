package com.example.bfg.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.example.bfg.Adapters.GridPostsAdapter;
import com.example.bfg.Adapters.ViewPagerAdapter;
import com.example.bfg.MainActivity;
import com.example.bfg.Models.Post;
import com.example.bfg.R;
import com.google.android.material.tabs.TabLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class UserPostsFragment extends Fragment {
    GridView gridView;
    RecyclerView rvGridPosts;
    private Context mContext;
    public String profileId;
    public ArrayList<ParseFile> allposts;
    public ParseUser user;
    public static final String TAG = UserPostsFragment.class.getSimpleName();

    public UserPostsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        profileId = ProfileFragment.getProfileId();
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_posts, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        user = ParseUser.getCurrentUser();
        gridView = view.findViewById(R.id.gridView);

        if(!profileId.equals(user.getObjectId())) {
            Log.i("UserPosts",profileId);
            setUserViewMode();
        }
        else{
            Log.i("UserPosts",profileId);
           setUpGridView();
        }
        super.onViewCreated(view, savedInstanceState);

    }

    private void setUserViewMode() {
        allposts = new ArrayList<>();
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereContains("user",profileId);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e!= null)
                {
                    Log.i("UserPost ","issue accessing posts",e);
                }
                for (Post post : posts) {
                    ParseUser user = ParseUser.getCurrentUser();
                    if(post.getUser().getObjectId().equals(user.getObjectId())) {
                        allposts.add(post.getImage());
                    }
                }
                int gridwidth = getResources().getDisplayMetrics().widthPixels;
                int imageWidth = gridwidth/ 3;
                gridView.setColumnWidth(imageWidth);

                ArrayList<String> imgUrls = new ArrayList<String>();
                for (int i = 0; i<allposts.size();i++)
                {
                    imgUrls.add(allposts.get(i).getUrl());
                }
                GridPostsAdapter adapter = new GridPostsAdapter(getContext(),imgUrls);
                gridView.setAdapter((adapter));
            }
        });
        profileId = user.getObjectId();
    }

    private void setUpGridView()
    {
       allposts = new ArrayList<>();
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.addDescendingOrder("createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e!= null)
                {
                    Log.i("UserPost ","issue accessing posts",e);
                }
                for (Post post : posts) {
                    ParseUser user = ParseUser.getCurrentUser();
                    if(post.getUser().getObjectId().equals(user.getObjectId())) {
                        allposts.add(post.getImage());
                    }
                }
                int gridwidth = getResources().getDisplayMetrics().widthPixels;
                int imageWidth = gridwidth/ 3;
                gridView.setColumnWidth(imageWidth);

                ArrayList<String> imgUrls = new ArrayList<String>();
                for (int i = 0; i<allposts.size();i++)
                {
                    imgUrls.add(allposts.get(i).getUrl());
                }
                GridPostsAdapter adapter = new GridPostsAdapter(getContext(),imgUrls);
                gridView.setAdapter((adapter));
            }

        });
        ;
    }
}