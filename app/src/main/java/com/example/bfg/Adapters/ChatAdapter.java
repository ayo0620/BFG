package com.example.bfg.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bfg.ChatActivity;
import com.example.bfg.Models.Message;
import com.example.bfg.Models.MessageThread;
import com.example.bfg.Models.User;
import com.example.bfg.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private List<Message> messages;
    private User otherUser;
    private Boolean isCurrentUser;
    private ParseUser currUser;

    public ChatAdapter(Context context, User otherUser,List<Message>messages) {
        this.context = context;
        this.messages = messages;
        this.otherUser = otherUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        currUser = ParseUser.getCurrentUser();
        if(message.getSender().getObjectId().equals(currUser.getObjectId()))
        {
//          populate right
            Log.i("Side", "hey");
            holder.bindRight(message);
        }
        else if(message.getSender().getObjectId().equals(otherUser.getObjectId()))
        {
//          populate left
            Log.i("Side", "hey2");
            holder.bindLeft(message);
        }
        Log.i("AdapterMessage",messages.toString());

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView otherUserMsg;
        private TextView otherUserTimeStamp;
        private TextView currUserMsg;
        private TextView currUserTimeStamp;
        private LinearLayout currUserLayout;
        private LinearLayout otherUserLayout;

        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            otherUserMsg = itemView.findViewById(R.id.otherUserMsg);
            otherUserTimeStamp = itemView.findViewById(R.id.otherUserTimeStamp);
            currUserMsg = itemView.findViewById(R.id.currUserMsg);
            currUserTimeStamp = itemView.findViewById(R.id.currUserTimeStamp);
            currUserLayout = itemView.findViewById(R.id.currUserLayout);
            otherUserLayout = itemView.findViewById(R.id.otherUserLayout);

        }

        public void bindLeft(Message message) {
            otherUserMsg.setText(message.getMessageText());
            otherUserTimeStamp.setText(message.getCreatedAt().toString());
            otherUserLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            currUserLayout.setVisibility(View.GONE);
        }

        public void bindRight(Message message) {
            currUserMsg.setText(message.getMessageText());
            currUserTimeStamp.setText(message.getCreatedAt().toString());
            currUserLayout.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            otherUserLayout.setVisibility(View.GONE);
        }
    }


}
