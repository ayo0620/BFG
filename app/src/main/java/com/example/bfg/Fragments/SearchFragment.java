package com.example.bfg.Fragments;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bfg.Adapters.PostsAdapter;
import com.example.bfg.Adapters.UsersAdapter;
import com.example.bfg.LoginActivity;
import com.example.bfg.Models.Post;
import com.example.bfg.Models.User;
import com.example.bfg.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {
    RecyclerView rvSearch;
    androidx.appcompat.widget.SearchView searchView;
    private TextView tvSearchPlaceholder;
    private Chip chipNoobie, chipRegular, chipPro, chipElite, chipLegend;
    protected UsersAdapter adapter;
    protected List<User> allUsers;
    private ImageButton ibFilter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvSearch = view.findViewById(R.id.rvSearch);
        searchView = view.findViewById(R.id.searchUser);
        tvSearchPlaceholder = view.findViewById(R.id.tvSearchPlaceholder);
        ibFilter = view.findViewById(R.id.ibFilter);


        ibFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());


        allUsers = new ArrayList<>();
        adapter = new UsersAdapter(getContext(), allUsers);

        // set the adapter on the recycler view
        rvSearch.setAdapter(adapter);
        rvSearch.clearFocus();
        // set the layout manager on the recycler view
        rvSearch.setLayoutManager(layoutManager);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tvSearchPlaceholder.setVisibility(View.GONE);
                queryForUsers(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }


    private void queryForUsers(String query) {
        ParseQuery<User> queryUsers = ParseQuery.getQuery(User.class);
        queryUsers.whereContains("username", query);
        queryUsers.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> users, ParseException e) {
                allUsers.clear();
                adapter.notifyDataSetChanged();

                allUsers.addAll(users);
                adapter.notifyDataSetChanged();

                }
        });
    }

    private void queryForFilterResults(String checkStatus) {
        tvSearchPlaceholder.setVisibility(View.GONE);
        String queryText = searchView.getQuery().toString();
        ParseQuery<User> queryUsers = ParseQuery.getQuery(User.class);
        queryUsers.whereContains("username", queryText);
        queryUsers.whereContains("status", checkStatus);
        queryUsers.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> users, ParseException e) {
                allUsers.clear();
                adapter.notifyDataSetChanged();
                Log.i("parse", users.toString());
                allUsers.addAll(users);
                adapter.notifyDataSetChanged();

            }
        });
    }


    private void showDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        chipNoobie = dialog.findViewById(R.id.chipNoobie);
        chipRegular = dialog.findViewById(R.id.chipRegular);
        chipPro = dialog.findViewById(R.id.chipPro);
        chipElite = dialog.findViewById(R.id.chipElite);
        chipLegend = dialog.findViewById(R.id.chipLegend);

        CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    queryForFilterResults(buttonView.getText().toString());
                    Toast.makeText(getContext(),buttonView.getText().toString(),Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else{

                }
            }
        };


        chipNoobie.setOnCheckedChangeListener(checkedChangeListener);
        chipRegular.setOnCheckedChangeListener(checkedChangeListener);
        chipPro.setOnCheckedChangeListener(checkedChangeListener);
        chipElite.setOnCheckedChangeListener(checkedChangeListener);
        chipLegend.setOnCheckedChangeListener(checkedChangeListener);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

}