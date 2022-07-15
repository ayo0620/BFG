package com.example.bfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.bfg.Fragments.ComposeFragment;
import com.example.bfg.Fragments.ExploreFragment;
import com.example.bfg.Fragments.HomeFeedFragment;
import com.example.bfg.Fragments.ProfileFragment;
import com.example.bfg.Fragments.SearchFragment;
import com.example.bfg.Models.Cards;
import com.example.bfg.Models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    public BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager = getSupportFragmentManager();
    public Cards cards;
    public static final String KEY_STATUS_COUNT = "statusCount";
    public static final int INCREMENT_BY = 10;

    HomeFeedFragment homeFeedFragment = new HomeFeedFragment(this);
    SearchFragment searchFragment = new SearchFragment();
    ComposeFragment composeFragment = new ComposeFragment(this);
    ExploreFragment exploreFragment = new ExploreFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Navigation_drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_home_screen:
                        fragment = homeFeedFragment;
                        break;
                    case R.id.action_Search:
                        fragment = searchFragment;
                        break;
                    case R.id.action_addPost:
                        fragment = composeFragment;
                        break;
                    case R.id.action_explore:
                        fragment = exploreFragment;
                        break;
                    case R.id.action_profile_screen:
                        fragment = profileFragment;
                        Log.i("profile", "selectTab");
                        break;
                    default:
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

//       setting fl layout for composeFragment if intent comes from searchGameActivity
        cards = Parcels.unwrap(getIntent().getParcelableExtra(Cards.class.getSimpleName()));
        if (cards != null) {
            bottomNavigationView.setSelectedItemId(R.id.action_addPost);
        } else {

            statusIncrementation(INCREMENT_BY);
            bottomNavigationView.setSelectedItemId(R.id.action_home_screen);
        }
    }

    public static void statusIncrementation(int incrementBy) {
        ParseUser user = ParseUser.getCurrentUser();
        try {
            int val = (int) user.getNumber(KEY_STATUS_COUNT);
            User myUser = (User) user;
            myUser.setStatusCount(val + incrementBy);
            myUser.saveInBackground();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void statusDecrementation(int decrementBy) {
        ParseUser user = ParseUser.getCurrentUser();
        try {
            int val = (int) user.getNumber(KEY_STATUS_COUNT);
            User myUser = (User) user;
            myUser.setStatusCount(val - decrementBy);
            myUser.saveInBackground();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void setBorderColorStatus(User user, CircleImageView ivSearchImage) {
        if (user.getStatus().equals("Noobie")) {
            ivSearchImage.setBorderColor(Color.YELLOW);
        } else if (user.getStatus().equals("Regular")) {
            ivSearchImage.setBorderColor(Color.GREEN);
        } else if (user.getStatus().equals("Pro")) {
            ivSearchImage.setBorderColor(Color.BLUE);
        } else if (user.getStatus().equals("Elite")) {
            ivSearchImage.setBorderColor(Color.parseColor("#8B4513"));
        } else if (user.getStatus().equals("Legend")) {
            ivSearchImage.setBorderColor(Color.RED);
        }
    }
}

