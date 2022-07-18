package com.example.bfg.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bfg.ChatActivity;
import com.example.bfg.MainActivity;
import com.example.bfg.Models.Comments;
import com.example.bfg.Models.Message;
import com.example.bfg.Models.MessageThread;
import com.example.bfg.Models.User;
import com.example.bfg.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageThreadAdapter extends RecyclerView.Adapter<MessageThreadAdapter.ViewHolder> {
    private Context context;
    private List<MessageThread> messageThreads;

    public MessageThreadAdapter(Context context, List<MessageThread>threads) {
        this.context = context;
        this.messageThreads = threads;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.messages_viewholder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MessageThread messageThread = messageThreads.get(position);
        Log.i("Adapter",messageThread.toString());
        holder.bind(messageThread);

    }

    @Override
    public int getItemCount() {
        return messageThreads.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CircleImageView messageProfilePic;
        private TextView tvMessageUsername;
        private TextView tvLastSentMsg;
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            messageProfilePic = itemView.findViewById(R.id.messageProfilePic);
            tvMessageUsername = itemView.findViewById(R.id.tvMessageUsername);
            tvLastSentMsg = itemView.findViewById(R.id.tvLastSentMsg);
            itemView.setOnClickListener(this);
        }

        public void bind(MessageThread messageThread) {
            User otherUser = new User();
            if(!messageThread.getMessageToUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId()))
            {
                try {
                    otherUser = (User) messageThread.getMessageToUser().fetchIfNeeded();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            else if(!messageThread.getMessageFromUser().getObjectId().equals(ParseUser.getCurrentUser().getObjectId())){
                try{
                    otherUser = (User) messageThread.getMessageFromUser().fetchIfNeeded();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            ParseFile img = otherUser.getProfileImage();
            if(img!= null)
            {
                Glide.with(context).load(img.getUrl()).into(messageProfilePic);
            }
            else
            {
                messageProfilePic.setImageResource(R.drawable.default_profile_icon);
            }
            MainActivity.setBorderColorStatus(otherUser,messageProfilePic);
            tvMessageUsername.setText(otherUser.getUsername());
            tvLastSentMsg.setText(messageThread.getRecentMessages());

        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            User otherUserPos;
            String userTo = ParseUser.getCurrentUser().getObjectId();
            String messageSenderId = messageThreads.get(getAdapterPosition()).getMessageFromUser().getObjectId();
            if(userTo.equals(messageSenderId))
            {
                otherUserPos = (User) messageThreads.get(getAdapterPosition()).getMessageToUser();
            }
            else{
                otherUserPos = (User) messageThreads.get(getAdapterPosition()).getMessageFromUser();
            }
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("otherUser", otherUserPos);
            context.startActivity(intent);
        }
    }


}
