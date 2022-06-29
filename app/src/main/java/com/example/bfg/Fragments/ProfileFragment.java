package com.example.bfg.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;
import com.example.bfg.Adapters.GridPostsAdapter;
import com.example.bfg.MainActivity;
import com.example.bfg.Models.Post;
import com.example.bfg.R;
import com.example.bfg.Adapters.ViewPagerAdapter;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ProfileFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ShapeableImageView profileImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i("profile","hey profilefragment");
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        profileImage = view.findViewById(R.id.profileImage);

        TabLayout.Tab myTab = tabLayout.newTab().setText("Posts");

        tabLayout.addTab(myTab);
        tabLayout.addTab(tabLayout.newTab().setText("Favorited"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//
        ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getContext(),getActivity().getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(vpAdapter);


        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.i("profile","onTabSelected");
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.i("profile","onTabUnselected");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.i("profile","onTabReselected");
                ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getContext(),getActivity().getSupportFragmentManager(),2);
                viewPager.setAdapter(vpAdapter);
                viewPager.setCurrentItem(tab.getPosition());
            }
        });

        tabLayout.selectTab(myTab);
    }

}