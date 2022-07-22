package com.example.bfg.Fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.bfg.Adapters.CardsAdapter;
import com.example.bfg.BuildConfig;
import com.example.bfg.Models.Cards;
import com.example.bfg.Models.Post;
import com.example.bfg.Models.User;
import com.example.bfg.R;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import okhttp3.Headers;

public class ExploreFragment extends Fragment {
    public static final String TAG = "ExploreFragment";
    List<Cards> allcards;
    List<Cards> myCards;
    CardsAdapter adapter;
    RecyclerView rvCards;
    Toolbar toolbar;
    Spinner spinner;
    TextView sortedByText;
    public HashMap<String, Integer> likesTotal = new HashMap<String, Integer>();
    public HashMap<String, Integer> postTotal = new HashMap<String, Integer>();
    public HashMap<String, Double> likePostRatio = new HashMap<String, Double>();
    public static HashMap<String, Double> sortedMap;
    ParseUser user;
    User currUser;
    ProgressBar exploreProgressBar;
    List<ParseQuery<Post>> queries;

    public ExploreFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        user = ParseUser.getCurrentUser();
        currUser = (User) user;

        toolbar = view.findViewById(R.id.exploreToolbar);
        spinner = view.findViewById(R.id.exploreSpinner);
        sortedByText = view.findViewById(R.id.sortedByText);
        exploreProgressBar = view.findViewById(R.id.exploreProgressBar);

        exploreProgressBar.setVisibility(View.VISIBLE);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.names) );
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(myAdapter);

        toolbar.setTitle("Explore");
        toolbar.setTitleTextColor(Color.WHITE);

//        recyclerview
        allcards = new ArrayList<>();
        myCards = new ArrayList<>();
        queries = new ArrayList<ParseQuery<Post>>();
        adapter = new CardsAdapter(getContext(), allcards);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvCards = view.findViewById(R.id.rvCards);

        rvCards.setLayoutManager(layoutManager);
        rvCards.setAdapter(adapter);
        rvCards.setHasFixedSize(true);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItem().equals("popular"))
                {
                    sortedByText.setText("Popular in the app");
                    Toast.makeText(getContext(),"popular",Toast.LENGTH_SHORT).show();
                    sortByPopularity();
                }
                if (view != null) {
                    //do things here
                    if(spinner.getSelectedItem().equals("For you"))
                    {
                        sortedByText.setText("You might like");
                        allcards.clear();
                        sortByPersonalization();
                        Toast.makeText(getContext(),"for yoy",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        super.onViewCreated(view, savedInstanceState);
    }

    private void sortByPersonalization() {
        exploreProgressBar.setVisibility(View.VISIBLE);
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        AsyncHttpClient client = new AsyncHttpClient();

        String url = "https://api.twitch.tv/helix/games/top";
        params.put("first", 100);
        headers.put("Client-Id", BuildConfig.CLIEND_ID);
        headers.put("Authorization",BuildConfig.TOKEN);
        client.get(url, headers,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    HashMap<String, Cards> gameList = new HashMap<String, Cards>();
                    JSONArray gameItems = jsonObject.getJSONArray("data");
                    JSONObject obj = gameItems.getJSONObject(0);
                    Log.i(TAG, obj.toString());
                    String arr = obj.getString("name");
                    for (int i= 0; i<gameItems.length();i++)
                    {
                        String gameName = gameItems.getJSONObject(i).getString("name");
                        gameList.put(gameName,new Cards(gameItems.getJSONObject(i)));
                    }


                    List<String> friendList = currUser.getUserFriends();
                    for (int i = 0; i<friendList.size(); i++)
                    {
//                        query for posts the user friend has made
                        ParseQuery<Post> queryPosts = ParseQuery.getQuery(Post.class);
                        queryPosts.whereContains("user",friendList.get(i));

//                        query for posts the user friend has liked
                        ParseQuery<Post> queryLikedByFriend = ParseQuery.getQuery(Post.class);
                        queryLikedByFriend.whereContains("likedby",friendList.get(i));

                        Log.i("eachTime2",queryPosts.toString());
                        Log.i("eachTime2",queryLikedByFriend.toString());
                        queries.add(queryPosts);
                        queries.add(queryLikedByFriend);
                    }
                    //  query for posts the user has liked
                    ParseQuery<Post> queryLikedBy = ParseQuery.getQuery(Post.class);
                    queryLikedBy.whereContains("likedby",user.getObjectId());
                    queries.add(queryLikedBy);

                    ParseQuery<Post> mainQuery = ParseQuery.or(queries);

                    Log.i("eachTime",mainQuery.toString());
                    mainQuery.findInBackground(new FindCallback<Post>() {
                        @Override
                        public void done(List<Post> objects, ParseException e) {
                            Cards card;
                            for(Post post:objects)
                            {
                                card  = new Cards();
                                card = gameList.get(post.getPostForGame());
                                if(gameList.containsKey(post.getPostForGame()) && !allcards.contains(card))
                                {
                                    allcards.add(gameList.get(post.getPostForGame()));
                                }
                            }

                            adapter.notifyDataSetChanged();
//                            Log.i("eachTime2",allcards.toString());
                            exploreProgressBar.setVisibility(View.GONE);
                        }
                    });

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

    private void sortByPopularity() {
        allcards.clear();
        ParseQuery<Post> myPosts = ParseQuery.getQuery(Post.class);
        myPosts.include(Post.KEY_POST_FOR_GAME);
        myPosts.include(Post.KEY_LIKED_BY);
        myPosts.addDescendingOrder("createdAt");
        myPosts.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                for (Post post : posts) {
                    if (!postTotal.containsKey(post.getPostForGame())) {
                        postTotal.put(post.getPostForGame(), 1);
                        likesTotal.put(post.getPostForGame(), post.getLikedBy().size());
                        Double calc = post.getLikedBy().size() / 1.0;
                        likePostRatio.put(post.getPostForGame(), calc);
                    } else {
                        int postTotalForGame = postTotal.get(post.getPostForGame()) + 1;
                        int likeTotalForGame = likesTotal.get(post.getPostForGame()) + post.getLikedBy().size();
                        postTotal.put(post.getPostForGame(), postTotalForGame);
                        likesTotal.put(post.getPostForGame(), likeTotalForGame);
                        Double calc = Double.valueOf(likeTotalForGame) / Double.valueOf(postTotalForGame);
                        likePostRatio.put(post.getPostForGame(), calc);
                    }
                }
                Log.i("posts", postTotal.toString());
                Log.i("posts", likesTotal.toString());
                Log.i("posts", likePostRatio.toString());

                sortedMap = sortedByValue(likePostRatio, false);
                Log.i("sorted", sortedMap.toString());
//                addToExploreList(sortedMap);
                setSortedGames(sortedMap);
            }
        });
    }

    private void setSortedGames(HashMap<String, Double> likePostRatio) {
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        AsyncHttpClient client = new AsyncHttpClient();

        String url = "https://api.twitch.tv/helix/games/top";
        params.put("first", 100);
        headers.put("Client-Id", BuildConfig.CLIEND_ID);
        headers.put("Authorization",BuildConfig.TOKEN);
        client.get(url, headers,params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;
                try {
                    HashMap<String, Cards> gameList = new HashMap<String, Cards>();
                    JSONArray gameItems = jsonObject.getJSONArray("data");
                    JSONObject obj = gameItems.getJSONObject(0);
                    Log.i(TAG, obj.toString());
                    String arr = obj.getString("name");
                    for (int i= 0; i<gameItems.length();i++)
                    {
                        String gameName = gameItems.getJSONObject(i).getString("name");
                        gameList.put(gameName,new Cards(gameItems.getJSONObject(i)));
                    }
                    for (String keys: likePostRatio.keySet())
                    {
                        if(gameList.containsKey(keys))
                        {
                            allcards.add(gameList.get(keys));
                        }
                    }

                    for (String keys: gameList.keySet())
                    {
                        if(!likePostRatio.containsKey(keys))
                        {
                            allcards.add(gameList.get(keys));
                        }
                    }

                    arr = arr.replace("{width}","20");
                    arr  = arr.replace("{height}","20");
                    Log.i(TAG, arr);
                    Log.i("myCards",Cards.fromJsonArray(gameItems).toString());
//                    allcards.addAll(Cards.fromJsonArray(gameItems));
                    Log.i(TAG, "Items: " + allcards.size());
                    adapter.notifyDataSetChanged();
                    exploreProgressBar.setVisibility(View.GONE);
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


    private HashMap<String, Double> sortedByValue(HashMap<String, Double> likePostRatio, boolean order) {
        //convert HashMap into List
        List<HashMap.Entry<String, Double>> list = new LinkedList<HashMap.Entry<String, Double>>(likePostRatio.entrySet());
        //sorting the list elements
        Collections.sort(list, new Comparator<HashMap.Entry<String, Double>>()
        {
            public int compare(HashMap.Entry<String, Double> o1, HashMap.Entry<String, Double> o2)
            {
                if (order)
                {
                //compare two object and return an integer
                    return o1.getValue().compareTo(o2.getValue());}
                else
                {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });
        //prints the sorted HashMap
        HashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (HashMap.Entry<String, Double> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_addToLibrary:
                adapter.addToLibrary(item.getGroupId(),getActivity());
                Snackbar.make(getActivity().findViewById(R.id.rlExplore),"Added to library",Snackbar.LENGTH_SHORT).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}



