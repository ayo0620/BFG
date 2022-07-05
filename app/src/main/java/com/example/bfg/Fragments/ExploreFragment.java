package com.example.bfg.Fragments;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.ActionMenuItem;
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
import com.example.bfg.MainActivity;
import com.example.bfg.Models.Cards;
import com.example.bfg.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class ExploreFragment extends Fragment {
    public static final String TAG = "ExploreFragment";
    List<Cards> allcards;
    CardsAdapter adapter;
    RecyclerView rvCards;
    MenuItem menuItem;
    SearchView searchView;
    Toolbar toolbar;
    MainActivity activity;

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
        adapter = new CardsAdapter(getContext(), allcards);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvCards = view.findViewById(R.id.rvCards);

        rvCards.setLayoutManager(layoutManager);
        rvCards.setAdapter(adapter);
        rvCards.setHasFixedSize(true);

        super.onViewCreated(view, savedInstanceState);



//        Api call
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
                    JSONArray gameItems = jsonObject.getJSONArray("data");
                    JSONObject obj = gameItems.getJSONObject(0);
                    String arr = obj.getString("box_art_url");
                    arr = arr.replace("{width}","20");
                    arr  = arr.replace("{height}","20");;
                    Log.i(TAG, arr);
//                    Log.i(TAG, gameItems.toString());
                    allcards.addAll(Cards.fromJsonArray(gameItems));
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



