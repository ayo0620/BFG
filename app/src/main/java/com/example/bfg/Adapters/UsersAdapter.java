package com.example.bfg.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.bfg.Fragments.ProfileFragment;
import com.example.bfg.MainActivity;
import com.example.bfg.Models.Notifications;
import com.example.bfg.Models.Post;
import com.example.bfg.Models.User;
import com.example.bfg.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
        ParseUser queryUser = ParseUser.getCurrentUser();
        User currUser = (User) queryUser;
        List<String> currUserFriendList = currUser.getUserFriends();
        public TextView tvSearchUsername;
        public CircleImageView ivSearchImage;
        public TextView tvSearchStatus;
        private Button btnAddFriend;
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
            btnAddFriend = itemView.findViewById(R.id.btnAddFriend);
        }

        @SuppressLint("ResourceAsColor")
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
                Glide.with(context).load(image.getUrl()).into(ivSearchImage);
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


//            if it's the user om search result do not set Add friend button
            if(currUser.getObjectId().equals(user.getObjectId()))
            {
                btnAddFriend.setVisibility(View.GONE);
            }
            else{
                btnAddFriend.setVisibility(View.VISIBLE);
            }

            MainActivity.setBorderColorStatus(user,ivSearchImage);
//            setBorderColorStatus(user,ivSearchImage);
//         if the user is in current user friend list set text to Added
            if(!currUserFriendList.contains(user.getObjectId()))
            {
                btnAddFriend.setText("Add Friend");
                btnAddFriend.setCompoundDrawablesRelativeWithIntrinsicBounds((android.R.drawable.ic_input_add),0,0,0);
            }
            else
            {
                btnAddFriend.setText("Added");
                btnAddFriend.setCompoundDrawablesRelativeWithIntrinsicBounds((R.drawable.ic_check_friend),0,0,0);
            }

            btnAddFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!currUser.getUserFriends().contains(user.getObjectId()))
                    {
                        currUserFriendList.add(user.getObjectId());
                        currUser.setUserFriend(currUserFriendList);
                        btnAddFriend.setText("Added");
                        btnAddFriend.setCompoundDrawablesRelativeWithIntrinsicBounds((R.drawable.ic_check_friend),0,0,0);
                        addFriendNotification(currUser, user);
                    }
                    else{
                        currUserFriendList.remove(user.getObjectId());
                        currUser.setUserFriend(currUserFriendList);
                        btnAddFriend.setText("Add Friend");
                        btnAddFriend.setCompoundDrawablesRelativeWithIntrinsicBounds((android.R.drawable.ic_input_add),0,0,0);

                    }
                    currUser.saveInBackground();
                }
            });
        }

    }



    private void addFriendNotification(User currUser, User toUser) {
        if (!currUser.getObjectId().equals(toUser.getObjectId()))
        {
            Notifications notifications = new Notifications();
            notifications.setNotifiedFrom(currUser);
            notifications.setNotification("added you as a friend");
            notifications.setNotifyThis(toUser);
            notifications.saveInBackground();
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
