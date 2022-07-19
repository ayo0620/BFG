package com.example.bfg.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bfg.Models.Cards;
import com.example.bfg.Models.Library;
import com.example.bfg.R;

import java.util.List;
import java.util.ListResourceBundle;

public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.ViewHolder> {
    private Context context;
    private List<Library> allitems;

    public LibraryAdapter(Context context, List<Library>items) {
        this.context = context;
        this.allitems = items;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_added_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Library library = allitems.get(position);
        holder.bind(library);
    }

    @Override
    public int getItemCount() {
        return allitems.size();
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private ImageView ivGameImage;
        private TextView tvGameName;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ivGameImage = itemView.findViewById(R.id.ivGameImage);
            tvGameName = itemView.findViewById(R.id.tvGameName);
        }

        public void bind(Library lib) {
            tvGameName.setText(lib.getGameName());
            String img = lib.getGameImage();
            img = img.replace("{width}","496");
            img = img.replace("{height}","600");
            Glide.with(context).load(img).into(ivGameImage);
        }

    }

}

