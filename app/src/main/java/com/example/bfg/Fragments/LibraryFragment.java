package com.example.bfg.Fragments;

import android.content.Intent;
import android.icu.text.TimeZoneFormat;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bfg.Adapters.CardsAdapter;
import com.example.bfg.Adapters.LibraryAdapter;
import com.example.bfg.Adapters.UsersAdapter;
import com.example.bfg.MainActivity;
import com.example.bfg.Models.Cards;
import com.example.bfg.Models.Library;
import com.example.bfg.Models.Notifications;
import com.example.bfg.Models.Post;
import com.example.bfg.R;
import com.example.bfg.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {
    private ActivityMainBinding binding;
    private RecyclerView rvLibrary;
    private ImageView libraryClose;
    LibraryAdapter adapter;
    List<Library> allItems;
    ProgressBar libraryProgressBar;
    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        libraryProgressBar = view.findViewById(R.id.libraryProgressBar);
        rvLibrary = view.findViewById(R.id.rvLibrary);
        libraryClose = view.findViewById(R.id.libraryClose);

        libraryProgressBar.setVisibility(View.VISIBLE);

        libraryClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        allItems = new ArrayList<>();
        adapter = new LibraryAdapter(getContext(),allItems);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvLibrary.setLayoutManager(layoutManager);
        rvLibrary.setAdapter(adapter);
        queryLibraryItems();


//        swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Library deletedCourse = allItems.get(viewHolder.getAdapterPosition());
                int position = viewHolder.getAdapterPosition();
//                adapter.removeItem(position);
                allItems.remove(position);
                adapter.notifyItemRemoved(position);
                ParseQuery<Library> query = ParseQuery.getQuery(Library.class);
                query.include("createdAt");
                query.whereEqualTo("gameName",deletedCourse.getGameName());
                query.addDescendingOrder("createdAt");
                query.findInBackground(new FindCallback<Library>() {
                    @Override
                    public void done(List<Library> objects, ParseException e) {
                        if (e != null) {
                            Log.i("NotifyAdapter", e.toString());
                        }
                        objects.get(0).deleteInBackground();
                    }
                });
                Snackbar.make(rvLibrary, deletedCourse.getGameName(), Snackbar.LENGTH_SHORT).setAction("undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Library library = new Library();
                        library.setGameImage(deletedCourse.getGameImage());
                        library.setForUser(ParseUser.getCurrentUser());
                        library.setGameName(deletedCourse.getGameName());
                        library.saveInBackground();
                        Log.i("snackbar", String.valueOf(position));
                        allItems.add(position, deletedCourse);
                        adapter.notifyItemInserted(position);
//                        adapter.addItem(position, deletedCourse);
                    }
                }).show();
            }
        }).attachToRecyclerView(rvLibrary);
    }


    private void queryLibraryItems() {
        ParseQuery<Library> libraryParseQuery = ParseQuery.getQuery(Library.class);
        libraryParseQuery.whereContains("forUser", ParseUser.getCurrentUser().getObjectId());
        libraryParseQuery.addDescendingOrder("createdAt");
        libraryParseQuery.findInBackground(new FindCallback<Library>() {
            @Override
            public void done(List<Library> objects, ParseException e) {
                if(e!= null)
                {
                    Log.i("LibraryFragment", "issue with getting library objects",e);
                }
                allItems.addAll(objects);
                adapter.notifyDataSetChanged();
                Log.i("queryLib", allItems.toString());;
                libraryProgressBar.setVisibility(View.GONE);
            }
        });
    }
}