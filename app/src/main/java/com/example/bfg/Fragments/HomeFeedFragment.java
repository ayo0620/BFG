package com.example.bfg.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.bfg.Adapters.ShowActiveStreamAdapter;
import com.example.bfg.BuildConfig;
import com.example.bfg.EndlessRecyclerViewScrollListener;
import com.example.bfg.LoginActivity;
import com.example.bfg.MainActivity;
import com.example.bfg.Models.Post;
import com.example.bfg.Adapters.PostsAdapter;
import com.example.bfg.Models.Stream;
import com.example.bfg.Models.User;
import com.example.bfg.R;
import com.google.android.material.navigation.NavigationView;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Headers;

public class HomeFeedFragment extends Fragment {

    private static String TAG ="HomeFeedFragment";
    RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    private SwipeRefreshLayout swipeContainer;
    private TextView tvDisplayUserName;
    EndlessRecyclerViewScrollListener scrollListener;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ImageView ivPostImage;
    private CircleImageView side_nav_profile_img;
    private TextView side_nav_user;
    NavigationView navigationView;
    public MainActivity activity;
    ShowActiveStreamAdapter showActiveStreamAdapter;
    List<Stream> streamList;
    RecyclerView rvStream;

    public HomeFeedFragment(MainActivity mainActivity){
        activity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment;
        return inflater.inflate(R.layout.fragment_home_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        drawerLayout = view.findViewById(R.id.drawer_layout);
        navigationView = view.findViewById(R.id.nav_view);
        ivPostImage = view.findViewById(R.id.ivPostImage);
        toolbar = view.findViewById(R.id.toolbar);
        tvDisplayUserName = view.findViewById(R.id.tvDisplayUserName);
        View headerView = navigationView.inflateHeaderView(R.layout.side_nav_header);
        side_nav_profile_img = headerView.findViewById(R.id.side_nav_profile_img);
        side_nav_user = headerView.findViewById(R.id.side_nav_user);
        rvStream = view.findViewById(R.id.rvStream);


        streamList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        rvStream.setLayoutManager(linearLayoutManager);
        showActiveStreamAdapter = new ShowActiveStreamAdapter(getContext(),streamList);
        rvStream.setAdapter(showActiveStreamAdapter);
        getLiveStreamContents();


        //        Toolbar
        activity.setSupportActionBar(toolbar);
        ParseUser user = User.getCurrentUser();
        tvDisplayUserName.setText("Welcome "+user.getUsername());
        //        Navigation Drawer menu
        User currUser = (User) user;
        side_nav_user.setText(currUser.getUsername());
        ParseFile img = currUser.getProfileImage();
        if(img!= null)
        {
            Glide.with(getContext()).load(img.getUrl()).into(side_nav_profile_img);
        }
        else
        {
            side_nav_profile_img.setImageResource(R.drawable.default_profile_icon);
        }
        MainActivity.setBorderColorStatus(currUser,side_nav_profile_img);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toogle);
        toogle.syncState();
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;
                Class fragmentClass;
                switch (menuItem.getItemId())
                {
                    case R.id.nav_notification:
                        fragmentClass = NotificationFragment.class;
                        break;
                    case R.id.nav_library:
                        fragmentClass = LibraryFragment.class;
                        break;
                    case R.id.nav_settings:
                        fragmentClass = SettingsFragment.class;
                        break;
                    case R.id.nav_UserFeedBack:
                        fragmentClass = UserFeedBackFragment.class;
                        break;
                    case R.id.nav_logout:
                        ParseUser.logOut();
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                    default:
                        fragmentClass = null;

                }
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Insert the fragment by replacing any existing fragment
                if (fragment != null) {

                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                }
                // Highlight the selected item has been done by NavigationView
                menuItem.setChecked(true);
                // Set action bar title
                activity.setTitle(menuItem.getTitle());
                // Close the navigation drawer
                drawerLayout.closeDrawers();
                return true;
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPosts(0);
                adapter.notifyDataSetChanged();
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        rvPosts = view.findViewById(R.id.rvPosts);

        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);

        // set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvPosts.setLayoutManager(layoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryPosts(allPosts.size());
            }
        };

        rvPosts.addOnScrollListener(scrollListener);
        queryPosts(0);

    }


    public void queryPosts(int skip) {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        // limit query to latest 20 items
        query.setLimit(10);
        query.setSkip(skip);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }

                // save received posts to list and notify adapter of new data
                allPosts.addAll(posts);
                swipeContainer.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getLiveStreamContents() {
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        AsyncHttpClient client = new AsyncHttpClient();

        String url = "https://api.twitch.tv/helix/streams";
        params.put("first", 100);
        headers.put("Client-Id", BuildConfig.CLIEND_ID);
        headers.put("Authorization",BuildConfig.TOKEN);
        client.get(url, headers,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray gameItems = jsonObject.getJSONArray("data");
                    for (int i=1; i<=gameItems.length();i++)
                    {
                        streamList.add(new Stream(gameItems.getJSONObject(i)));

                        if(streamList.size()==10)
                        {
                            break;
                        }
                    }
                    showActiveStreamAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.e("gg", "Hit json exception",e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i("gg",response);
            }
        });

    }

}