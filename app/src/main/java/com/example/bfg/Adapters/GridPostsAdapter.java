package com.example.bfg.Adapters;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
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

public class GridPostsAdapter implements ListAdapter {
    private Context context;
    private List<String> allUsersPosts;
    private static final String TAG = "GridsPostAdapter";

    public GridPostsAdapter(Context context, List<String>allUsersPosts) {
        this.context = context;
        this.allUsersPosts = allUsersPosts;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return allUsersPosts.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listitemView = convertView;
        if(listitemView == null){
            listitemView = LayoutInflater.from(context).inflate(R.layout.item_grid_post, parent, false);
        }
        String post = allUsersPosts.get(position);
        ImageView ivCardViewImgPosts = listitemView.findViewById(R.id.ivCardViewImgPosts);
        Glide.with(context).load(post).into(ivCardViewImgPosts);
        return listitemView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}
