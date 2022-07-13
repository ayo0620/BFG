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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.bfg.Adapters.CardsAdapter;
import com.example.bfg.BuildConfig;
import com.example.bfg.Models.Cards;
import com.example.bfg.Models.Post;
import com.example.bfg.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Headers;

public class ExploreFragment extends Fragment {
    public static final String TAG = "ExploreFragment";
    List<Cards> allcards;
    List<Cards> myCards;
    public static List<Cards> allItems2;
    CardsAdapter adapter;
    RecyclerView rvCards;
    MenuItem menuItem;
    SearchView searchView;
    Toolbar toolbar;
    public HashMap<String, Integer> likesTotal = new HashMap<String, Integer>();
    public HashMap<String, Integer> postTotal = new HashMap<String, Integer>();
    public HashMap<String, Double> likePostRatio = new HashMap<String, Double>();
    HashMap<String, Double> sortedMap;


    public ExploreFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        toolbar = getActivity().findViewById(R.id.exploreToolbar);
        toolbar.setTitle("Explore");
        toolbar.setTitleTextColor(Color.WHITE);

        allcards = new ArrayList<>();
        myCards = new ArrayList<>();
        adapter = new CardsAdapter(getContext(), allcards);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvCards = view.findViewById(R.id.rvCards);

        rvCards.setLayoutManager(layoutManager);
        rvCards.setAdapter(adapter);
        rvCards.setHasFixedSize(true);

        //        Api call
        sortExplorePage();

        super.onViewCreated(view, savedInstanceState);
    }

    private void sortExplorePage() {
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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menuItem= menu.findItem(R.id.action_Search_icon);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i(TAG,"submitted!");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}



