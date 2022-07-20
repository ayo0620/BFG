package com.example.bfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bfg.Adapters.PostsAdapter;
import com.example.bfg.Models.Cards;
import com.example.bfg.Models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailCardActivity extends AppCompatActivity {

    private ImageView detailViewClose;
    private ImageView ivDetailImage;
    private TextView tvDetailGameName;
    private SwipeRefreshLayout detailSwipeContainer;
    private RecyclerView rvDetailCardView;
    private TextView tvPostCount;
    private TextView tvLikeDetailCount;
    private Cards card;
    private List<Post> allCardPosts;
    private PostsAdapter adapter;
    public int likeTotal ;
    EndlessRecyclerViewScrollListener scrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_card);

        detailViewClose = findViewById(R.id.detailViewClose);
        ivDetailImage = findViewById(R.id.ivDetailImage);
        tvDetailGameName = findViewById(R.id.tvDetailGameName);
        tvPostCount = findViewById(R.id.tvPostCount);
        tvLikeDetailCount = findViewById(R.id.tvLikeDetailCount);
        detailSwipeContainer = findViewById(R.id.detailSwipeContainer);
        rvDetailCardView = findViewById(R.id.rvDetailCardView);

        card = Parcels.unwrap(getIntent().getParcelableExtra(Cards.class.getSimpleName()));

        detailViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String img = card.getImage();
        img = img.replace("{width}","496");
        img = img.replace("{height}","600");
        Glide.with(this).load(img).into(ivDetailImage);
        tvDetailGameName.setText(card.getName());

        detailSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allCardPosts.clear();
                queryPostsForGame(0);
                adapter.notifyDataSetChanged();
            }
        });

        detailSwipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        allCardPosts = new ArrayList<>();
        likeTotal = 0;
        adapter = new PostsAdapter(this, allCardPosts);

        // set the adapter on the recycler view
        rvDetailCardView.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvDetailCardView.setLayoutManager(layoutManager);
        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryPostsForGame(allCardPosts.size());
            }
        };

        rvDetailCardView.addOnScrollListener(scrollListener);
        queryPostsForGame(0);
    }

    private void queryPostsForGame(int skip) {
        // specify what type of data we want to query - Post.class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereContains(Post.KEY_POST_FOR_GAME, card.getName());
        query.setLimit(10);
        query.setSkip(skip);
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e("DetailCardActivity", "Issue with getting posts", e);
                    return;
                }
                for(Post post: posts)
                {
                    likeTotal += post.getLikedBy().size();
                }
                // save received posts to list and notify adapter of new data
                allCardPosts.addAll(posts);
                tvLikeDetailCount.setText("Likes: "+likeTotal);
                tvPostCount.setText("Posts: "+allCardPosts.size());
                detailSwipeContainer.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }
}