package com.example.bfg.Adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bfg.Fragments.ComposeFragment;
import com.example.bfg.MainActivity;
import com.example.bfg.Models.Cards;
import com.example.bfg.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

import java.util.List;

public class GameSearchAdapter extends RecyclerView.Adapter<GameSearchAdapter.ViewHolder> {
    private Context context;
    private List<Cards> allcards;
    RelativeLayout rlSearchForGames;

    public GameSearchAdapter(Context context, List<Cards>cards) {
        this.context = context;
        this.allcards = cards;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_games, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cards cards = allcards.get(position);
        Log.i("Adapter",cards.toString());
        holder.bind(cards);
    }

    @Override
    public int getItemCount() {
        return allcards.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView ivSearchGameImage;
        public TextView tvSearchGameName;
        RelativeLayout rlSearchForGames;
        BottomNavigationView bottomNavigationView;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(this);
            ivSearchGameImage = itemView.findViewById(R.id.ivSearchGameImage);
            tvSearchGameName = itemView.findViewById(R.id.tvSearchGameName);
            rlSearchForGames = itemView.findViewById(R.id.rlSearchForGames);
            bottomNavigationView = itemView.findViewById(R.id.bottom_navigation);
        }

        public void bind(Cards cards) {
            int position  = getAdapterPosition();
            Log.i("gT", String.valueOf(position));
            tvSearchGameName.setText(cards.getName());
            String img = cards.getImage();
            img = img.replace("{width}","496");
            img = img.replace("{height}","600");
            Glide.with(context).load(img).into(ivSearchGameImage);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.i("hey","9999");
            if(position != RecyclerView.NO_POSITION)
            {
                Cards cards = allcards.get(position);
                Intent intent = new Intent(context, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", "hey");
                new ComposeFragment().setArguments(bundle);
                intent.putExtra(Cards.class.getSimpleName(), Parcels.wrap(cards));
                context.startActivity(intent);

//                Cards cards = allcards.get(position);
//                Intent i = new Intent(context, MainActivity.class);
//                context.startActivity(i);
            }
        }
    }


}