package com.example.bfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.RequestHeaders;
import com.codepath.asynchttpclient.RequestParams;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.bfg.Adapters.CardsAdapter;
import com.example.bfg.Adapters.GameSearchAdapter;
import com.example.bfg.Models.Cards;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class SearchGamesActivity extends AppCompatActivity {
    public static final String TAG = "SearchGameActivity";
    androidx.appcompat.widget.SearchView searchView;
    RecyclerView rvSearchGames;
    List<Cards> gameResults;
    GameSearchAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_games);

        gameResults = new ArrayList<>();
        adapter = new GameSearchAdapter(this, gameResults);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvSearchGames = findViewById(R.id.rvSearchGames);

        rvSearchGames.setLayoutManager(layoutManager);
        rvSearchGames.setAdapter(adapter);
        rvSearchGames.setHasFixedSize(true);
        searchView = findViewById(R.id.searchGameInput);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchGame(query);
                Log.i("diff",gameResults.toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }


    private void fetchGame(String query) {
        RequestParams params = new RequestParams();
        RequestHeaders headers = new RequestHeaders();
        AsyncHttpClient client = new AsyncHttpClient();

        gameResults.clear();

        String url = "https://api.twitch.tv/helix/search/categories";
        params.put("query", query);
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
                    gameResults.addAll(Cards.fromJsonArray(gameItems));
                    Log.i(TAG, "Items: " + gameResults.size());
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

}