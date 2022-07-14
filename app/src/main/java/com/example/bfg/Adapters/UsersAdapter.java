package com.example.bfg.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.bfg.Fragments.ProfileFragment;
import com.example.bfg.Models.Post;
import com.example.bfg.Models.User;
import com.example.bfg.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.parse.ParseFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {
    private Context context;
    private List<User> users;

    public UsersAdapter(Context context, List<User>users) {
        this.context = context;
        this.users = users;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        Log.i("Adapter",user.toString());
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView tvSearchUsername;
        public ShapeableImageView ivSearchImage;
        public TextView tvSearchStatus;
        public static  final String KEY_PROFILE_IMAGE = "ProfileImage";


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvSearchUsername =  itemView.findViewById(R.id.tvSearchUsername);
            ivSearchImage = itemView.findViewById(R.id.ivSearchImage);
            tvSearchStatus = itemView.findViewById(R.id.tvSearchStatus);
        }

        public void bind(User user) {
            tvSearchUsername.setText(user.getUsername());
            tvSearchStatus.setText(user.getStatus());
            ParseFile image = user.getParseFile(KEY_PROFILE_IMAGE);
            if (image == null)
            {
                Glide.with(context).load(R.drawable.default_profile_icon).into(ivSearchImage);
            }
            else {
                int radius = 200;
                Glide.with(context).load(image.getUrl()).centerCrop().into(ivSearchImage);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = context.getSharedPreferences("PREFS",Context.MODE_PRIVATE).edit();
                    editor.putString("profileId",user.getObjectId());
                    editor.apply();
                    ((FragmentActivity)context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, new ProfileFragment()).commit();
                }
            });
        }

    }
    // Clean all elements of the recycler
    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<User> list) {
        users.addAll(list);
        notifyDataSetChanged();
    }

}
