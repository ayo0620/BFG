package com.example.bfg.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bfg.Models.Stream;
import com.example.bfg.R;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowActiveStreamAdapter extends RecyclerView.Adapter<ShowActiveStreamAdapter.ViewHolder> {
    private Context context;
    private List<Stream> allStreams;
    private Boolean isAddToLib;

    public ShowActiveStreamAdapter(Context context, List<Stream>streams) {
        this.context = context;
        this.allStreams = streams;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Stream stream= allStreams.get(position);
        holder.bind(stream);
    }

    @Override
    public int getItemCount() {
        return allStreams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvViewStreamCount;
        TextView tvStreamerName ;
        TextView tvGameStreamName;
        CircleImageView ivGameImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvViewStreamCount = itemView.findViewById(R.id.tvViewStreamCount);
            tvStreamerName = itemView.findViewById(R.id.tvStreamerName);
            tvGameStreamName = itemView.findViewById(R.id.tvGameStreamName);
            ivGameImage = itemView.findViewById(R.id.ivGameImage);
        }

        public void bind(Stream stream) {
           tvViewStreamCount.setText(stream.getViewCount());
           tvGameStreamName.setText(stream.getStreamForGame());
           tvStreamerName.setText(stream.getStreamerName());
           String img = stream.getImage();
           img = img.replace("{width}","496");
           img = img.replace("{height}","600");
           Glide.with(context).load(img).into(ivGameImage);
        }


    }

}

