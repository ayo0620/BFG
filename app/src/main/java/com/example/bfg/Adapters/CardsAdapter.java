package com.example.bfg.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.example.bfg.Models.Cards;
import com.example.bfg.R;

import java.util.List;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder> {
    private Context context;
    private  List<Cards> allCards;

    public CardsAdapter(Context context, List<Cards>cards) {
        this.context = context;
        this.allCards = cards;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cards cards = allCards.get(position);
        Log.i("Adapter",cards.toString());
        holder.bind(cards);
    }

    @Override
    public int getItemCount() {
        return allCards.size();
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private ImageView ivCardViewImg;
        private TextView tvGameTitle;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            ivCardViewImg = itemView.findViewById(R.id.ivCardViewImg);
            tvGameTitle = itemView.findViewById(R.id.tvGameTitle);
        }

        public void bind(Cards cards) {
            tvGameTitle.setText(cards.getName());
            String img = cards.getImage();
            img = img.replace("{width}","496");
            img = img.replace("{height}","600");
            Glide.with(context).load(img).into(ivCardViewImg);
        }
    }

}
