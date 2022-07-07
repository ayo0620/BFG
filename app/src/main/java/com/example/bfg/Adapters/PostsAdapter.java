package com.example.bfg.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.bfg.Models.Post;
import com.example.bfg.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;
    public String likeCount;
    public String likeText;

    public PostsAdapter(Context context, List<Post>posts) {
        this.context = context;
        this.posts = posts;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        Log.i("Adapter",post.toString());
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvUsername;
        public ImageView ivPostImage;
        public TextView tvDescription;
        public ShapeableImageView ivProfileImage;
        public TextView tvStatus;
        public TextView tvTimeStamp;
        public ImageButton ibLike;
        public TextView tvLikeCount;
        public static  final String KEY_PROFILE_IMAGE = "ProfileImage";


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvUsername =  itemView.findViewById(R.id.tvUsername);
            ivPostImage = itemView.findViewById(R.id.ivPostImage);
            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTimeStamp = itemView.findViewById(R.id.tvTimeStamp);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ibLike = itemView.findViewById(R.id.ibLike);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
        }

        public void bind(Post post) {
            List<String> likeBy = post.getLikedBy();
            tvUsername.setText(post.getUser().getUsername());
            tvStatus.setText("Elite");
            Date createdAt  = post.getCreatedAt();
            String timeAgo = post.calculateTimeAgo(createdAt);
            tvTimeStamp.setText(timeAgo);
            Glide.with(context).load(post.getImage().getUrl()).into(ivPostImage);
            tvDescription.setText(post.getDescription());
            ParseFile image = post.getUser().getParseFile(KEY_PROFILE_IMAGE);
            if(image == null)
            {
                Glide.with(context).load(R.drawable.default_profile_icon).centerCrop().into(ivProfileImage);
            }
            else {
                Glide.with(context).load(image.getUrl()).centerCrop().into(ivProfileImage);
            }
            ParseUser user = ParseUser.getCurrentUser();
            if(post.getLikedBy().contains(user.getObjectId()))
            {
                Drawable newImage = context.getDrawable(R.drawable.ic_favorited_active);
                ibLike.setImageDrawable(newImage);
                likeText = String.valueOf(post.likeCountDisplayText());
                likeCount = String.valueOf(likeBy.size());
                tvLikeCount.setText(likeCount +" "+likeText);
            }
           else if(!post.getLikedBy().contains(user.getObjectId()))
            {
                Drawable newImage = context.getDrawable(R.drawable.ic_favorite_icon_24);
                ibLike.setImageDrawable(newImage);
                likeText = String.valueOf(post.likeCountDisplayText());
                likeCount = String.valueOf(likeBy.size());
                tvLikeCount.setText(likeCount +" "+likeText);
            }
            ibLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!likeBy.contains(ParseUser.getCurrentUser().getObjectId()))
                    {
                        likeBy.add(ParseUser.getCurrentUser().getObjectId());
                        post.setLikedBy(likeBy);
                        Drawable newImage = context.getDrawable(R.drawable.ic_favorited_active);
                        ibLike.setImageDrawable(newImage);
                        post.setFavoritedBool(true);
                        likeText = String.valueOf(post.likeCountDisplayText());
                        likeCount = String.valueOf(likeBy.size());
                        tvLikeCount.setText(likeCount +" "+likeText);
                    }
                    else
                    {
                        likeBy.remove(ParseUser.getCurrentUser().getObjectId());
                        post.setLikedBy(likeBy);
                        Drawable newImage = context.getDrawable(R.drawable.ic_favorite_icon_24);
                        ibLike.setImageDrawable(newImage);
                        post.setFavoritedBool(false);
                        likeText = String.valueOf(post.likeCountDisplayText());
                        likeCount = String.valueOf(likeBy.size());
                        tvLikeCount.setText(likeCount+ " "+likeText);
                    }
                    post.saveInBackground();
                }
            });
        }
    }
    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

}