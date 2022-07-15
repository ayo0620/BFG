package com.example.bfg.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bfg.Models.Cards;
import com.example.bfg.Models.Notifications;
import com.example.bfg.Models.User;
import com.example.bfg.R;
import com.google.android.material.imageview.ShapeableImageView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    private Context context;
    private List<Notifications> allNotifications;

    public NotificationsAdapter(Context context, List<Notifications>notifications) {
        this.context = context;
        this.allNotifications = notifications;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notifications, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notifications notification = allNotifications.get(position);
        Log.i("Adapter",notification.toString());
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return allNotifications.size();
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private ShapeableImageView ivUserAvatar;
        private TextView tvUserNameNotify;
        private TextView tvNotificationText;
        private TextView tvNotificationTime;
        private Button btnDeleteRequest;
        private Button btnAcceptRequest;
        public static final String KEY_PROFILE_IMAGE = "ProfileImage";

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ivUserAvatar = itemView.findViewById(R.id.ivUserAvatar);
            tvUserNameNotify = itemView.findViewById(R.id.tvUserNameNotify);
            tvNotificationText = itemView.findViewById(R.id.tvNotificationText);
            tvNotificationTime = itemView.findViewById(R.id.tvNotificationTime);
            btnDeleteRequest = itemView.findViewById(R.id.btnDeleteRequest);
            btnAcceptRequest = itemView.findViewById(R.id.btnAcceptRequest);

        }

        public void bind(Notifications notification) {
            try {
                ParseFile img = notification.getNotifiedFrom().fetchIfNeeded().getParseFile(KEY_PROFILE_IMAGE);
                if (img != null)
                {
                    Glide.with(context).load(img.getUrl()).placeholder(R.drawable.default_profile_icon).circleCrop().into(ivUserAvatar);
                }
                else
                {
                    ivUserAvatar.setImageResource(R.drawable.default_profile_icon);
                }
            } catch (ParseException e) {
                Log.v("notificationAdapter", e.toString());
                e.printStackTrace();
            }

//            remove decline and accept button if a friend request
            if (!notification.getNotification().equals("added you as a friend"))
            {
                tvUserNameNotify.setText(notification.getNotifiedFrom().getUsername());
                tvNotificationText.setText(notification.getNotification());
                Date timeCreated  = notification.getCreatedAt();
                tvNotificationTime.setText(notification.calculateTimeAgo(timeCreated));
                btnAcceptRequest.setVisibility(View.GONE);
                btnDeleteRequest.setVisibility(View.GONE);

                //            click notification to open post
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    intent to get to post detail
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Delete");
                        builder.setMessage("Are you sure you want to delete this notification?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete notification
                                ParseQuery<Notifications> query = ParseQuery.getQuery(Notifications.class);
                                query.include("createdAt");
                                query.whereEqualTo("createdAt",notification.getCreatedAt());
                                query.addDescendingOrder("createdAt");
                                query.findInBackground(new FindCallback<Notifications>() {
                                    @Override
                                    public void done(List<Notifications> objects, ParseException e) {
                                        if (e!= null) {
                                            Log.i("NotifyAdapter", e.toString());
                                        }
                                        objects.get(0).deleteInBackground();
                                        allNotifications.remove(getAdapterPosition());
                                        notifyItemRemoved(getAdapterPosition());
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //cancel
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        return false;
                    }
                });
            }

            else
            {
                tvUserNameNotify.setText(notification.getNotifiedFrom().getUsername());
                tvNotificationText.setText(notification.getNotification());
                Date timeCreated  = notification.getCreatedAt();
                tvNotificationTime.setText(notification.calculateTimeAgo(timeCreated));

//                if user denies friend request
                btnDeleteRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ParseQuery<Notifications> query = ParseQuery.getQuery(Notifications.class);
                        query.include("createdAt");
                        query.whereEqualTo("createdAt",notification.getCreatedAt());
                        query.addDescendingOrder("createdAt");
                        query.findInBackground(new FindCallback<Notifications>() {
                            @Override
                            public void done(List<Notifications> objects, ParseException e) {
                                if (e!= null) {
                                    Log.i("NotifyAdapter", e.toString());
                                }
                                objects.get(0).deleteInBackground();
                                allNotifications.remove(getAdapterPosition());
                                notifyItemRemoved(getAdapterPosition());
                            }
                        });
                    }
                });

//                if user accepts friend request
                btnAcceptRequest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ParseUser user = ParseUser.getCurrentUser();
                        User currUser = (User) user;
                        List<String> friendList = currUser.getUserFriends();
//                        if user not in the current user's friend list
                        if(!friendList.contains(notification.getNotifiedFrom().getObjectId()))
                        {
                            friendList.add(notification.getNotifiedFrom().getObjectId());
                            currUser.setUserFriend(friendList);
                        }
                        currUser.saveInBackground();
                        btnAcceptRequest.setText("Added");
                        btnAcceptRequest.setEnabled(false);
                    }
                });

                //            click notification to open post
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    intent to get to post detail
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Delete");
                        builder.setMessage("Are you sure you want to delete this notification?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //delete notification
                                ParseQuery<Notifications> query = ParseQuery.getQuery(Notifications.class);
                                query.include("createdAt");
                                query.whereEqualTo("createdAt",notification.getCreatedAt());
                                query.addDescendingOrder("createdAt");
                                query.findInBackground(new FindCallback<Notifications>() {
                                    @Override
                                    public void done(List<Notifications> objects, ParseException e) {
                                        if (e!= null) {
                                            Log.i("NotifyAdapter", e.toString());
                                        }
                                        objects.get(0).deleteInBackground();
                                        allNotifications.remove(getAdapterPosition());
                                        notifyItemRemoved(getAdapterPosition());
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //cancel
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        return false;
                    }
                });
            }

        }

    }

}
