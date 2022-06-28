package com.example.bfg.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bfg.Fragments.HomeFeedFragment;
import com.example.bfg.Models.Cards;
import com.example.bfg.Models.Post;
import com.example.bfg.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.util.List;

public class GridPostsAdapter extends RecyclerView.Adapter<GridPostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> allUsersPosts;
    private static final String TAG = "GridsPostAdapter";

    public GridPostsAdapter(Context context, List<Post>allUsersPosts) {
        this.context = context;
        this.allUsersPosts = allUsersPosts;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_grid_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        // include data referred by user key
        query.include(Post.KEY_USER);
        query.include(Post.KEY_IMAGE);
        // limit query to latest 20 items
        query.setLimit(10);
        query.setSkip(0);
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
                    allUsersPosts.add(post);
                }
                // save received posts to list and notify adapter of new data
//                allUsersPosts.addAll(posts);
            }
        });
        Post post = allUsersPosts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return allUsersPosts.size();
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private ImageView ivCardViewImgPosts;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ivCardViewImgPosts = itemView.findViewById(R.id.ivCardViewImgPosts);
        }

        public void bind(Post post) {
            ParseFile img = post.getImage();
            Glide.with(context).load(img).into(ivCardViewImgPosts);
        }
    }

}
