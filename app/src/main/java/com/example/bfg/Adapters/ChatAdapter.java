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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bfg.ChatActivity;
import com.example.bfg.MainActivity;
import com.example.bfg.MessagingActivity;
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

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {
    private Context context;
    private List<Message> messages;
    private User otherUser;
    private ParseUser currUser;
    public static final int MESSAGE_OUTGOING = 1;
    public static final int MESSAGE_INCOMING = 2;


    public ChatAdapter(Context context, User otherUser,List<Message>messages) {
        this.context = context;
        this.messages = messages;
        this.otherUser = otherUser;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

//        if it's incoming inflate the message_incoming view
        if (viewType == MESSAGE_INCOMING) {
            View contactView = inflater.inflate(R.layout.message_incoming, parent, false);
            return new IncomingMessageViewHolder(contactView);
            //        if it's outgoing inflate the message_outgoing view
        } else if (viewType == MESSAGE_OUTGOING) {
            View contactView = inflater.inflate(R.layout.message_outgoing, parent, false);
            return new OutgoingMessageViewHolder(contactView);
        } else {
            throw new IllegalArgumentException("Unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bindMessage(message);
    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(isMe(position))
        {
            return MESSAGE_OUTGOING;
        }
        else{
            return MESSAGE_INCOMING;
        }
    }

//    helper function
    private boolean isMe(int position) {
        Boolean isMe =  null;
        currUser = ParseUser.getCurrentUser();
        Message message = messages.get(position);
        if(message.getSender().getObjectId().equals(currUser.getObjectId()))
        {
            isMe = true;
        }
        else if(message.getSender().getObjectId().equals(otherUser.getObjectId()))
        {
            isMe = false;
        }
        return  isMe;
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public abstract class MessageViewHolder extends RecyclerView.ViewHolder {

        public MessageViewHolder(View itemView) {
            super(itemView);
        }

        abstract void bindMessage(Message message);
        }

    public class IncomingMessageViewHolder extends MessageViewHolder{
        private TextView otherUserMsg;
        private TextView otherUserTimeStamp;
        private RelativeLayout rlIncoming;
        private CircleImageView ivChatProfileImage;

        public IncomingMessageViewHolder(View itemView)
        {
            super(itemView);
            otherUserMsg = itemView.findViewById(R.id.otherUserMsg);
            otherUserTimeStamp = itemView.findViewById(R.id.otherUserTimeStamp);
            rlIncoming = itemView.findViewById(R.id.rlIncoming);
            ivChatProfileImage = itemView.findViewById(R.id.ivChatProfileImage);
        }

        @Override
        public void bindMessage(Message message) {

            otherUserMsg.setText(message.getMessageText());
            otherUserTimeStamp.setText(message.getCreatedAt().toString());
            rlIncoming.setGravity(Gravity.LEFT);;
            ParseFile img = otherUser.getProfileImage();
            if(img != null)
            {
                Glide.with(context).load(img.getUrl()).into(ivChatProfileImage);
            }
            else{
                ivChatProfileImage.setImageResource(R.drawable.default_profile_icon);
            }
            MainActivity.setBorderColorStatus(otherUser,ivChatProfileImage);
        }

    }


    public class OutgoingMessageViewHolder extends MessageViewHolder{
        private TextView currUserMsg;
        private TextView currUserTimeStamp;
        private RelativeLayout rlOutgoing;

        public OutgoingMessageViewHolder(View itemView)
        {
            super(itemView);
            currUserMsg = itemView.findViewById(R.id.currUserMsg);
            currUserTimeStamp = itemView.findViewById(R.id.currUserTimeStamp);
            rlOutgoing = itemView.findViewById(R.id.rlOutgoing);
        }

        @Override
        public void bindMessage(Message message) {
            currUserMsg.setText(message.getMessageText());
            currUserTimeStamp.setText(message.getCreatedAt().toString());
            rlOutgoing.setGravity(Gravity.RIGHT);
        }


    }


}
