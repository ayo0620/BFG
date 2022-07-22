package com.example.bfg.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bfg.Fragments.ComposeFragment;
import com.example.bfg.MainActivity;
import com.example.bfg.Models.Cards;
import com.example.bfg.Models.Comments;
import com.example.bfg.Models.User;
import com.example.bfg.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseFile;

import org.parceler.Parcels;
import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private Context context;
    private List<Comments> comments;

    protected void onRestart(){}
    public CommentsAdapter(Context context, List<Comments>comments) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comments, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comments comment = comments.get(position);
        Log.i("Adapter",comment.toString());
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAuthor;
        private TextView tvBody;
        private CircleImageView ivCommentedUserPic;
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvBody = itemView.findViewById(R.id.tvBody);
            ivCommentedUserPic = itemView.findViewById(R.id.ivCommentedUserPic);
        }

        public void bind(Comments comment) {
            tvBody.setText(comment.getDescription());
            tvAuthor.setText(comment.getUser().getUsername());
            User user = (User) comment.getUser();
            ParseFile img = user.getProfileImage();
            if(img != null)
            {
                Glide.with(context).load(img.getUrl()).into(ivCommentedUserPic);
            }
            else{
                ivCommentedUserPic.setImageResource(R.drawable.default_profile_icon);
            }
            MainActivity.setBorderColorStatus(user,ivCommentedUserPic);
            Log.i("Size", String.valueOf(comments.size()));
        }


    }


}
