package com.example.bfg.Adapters;

import android.content.Context;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.bfg.Fragments.UserFavoritedFragment;
import com.example.bfg.Fragments.UserPostsFragment;
import com.example.bfg.R;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Context mycontext;
    int totalTabs;

    public ViewPagerAdapter(@NonNull Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        mycontext = context;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new UserPostsFragment();
                break;
            case 1:
                fragment = new UserFavoritedFragment();
                break;
            default:
                return null;
        }
        return fragment;
    }


    @Override
    public int getCount() {
        return  totalTabs;
    }

}
