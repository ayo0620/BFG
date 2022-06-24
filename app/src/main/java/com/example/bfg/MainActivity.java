package com.example.bfg;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.bfg.Fragments.ComposeFragment;
import com.example.bfg.Fragments.ExploreFragment;
import com.example.bfg.Fragments.HomeFeedFragment;
import com.example.bfg.Fragments.NotificationFragment;
import com.example.bfg.Fragments.ProfileFragment;
import com.example.bfg.Fragments.SearchFragment;
import com.example.bfg.Fragments.SettingsFragment;
import com.example.bfg.Models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    public BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager = getSupportFragmentManager();

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

        drawerLayout = findViewById(R.id.drawer_layout);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.action_home_screen:
                        Toast.makeText(MainActivity.this,"home!",Toast.LENGTH_SHORT).show();
                        fragment = homeFeedFragment;
                        break;
                    case R.id.action_Search:
                        Toast.makeText(MainActivity.this,"search!",Toast.LENGTH_SHORT).show();
                        fragment = searchFragment;
                        break;
                    case R.id.action_addPost:
                        Toast.makeText(MainActivity.this,"addPost!",Toast.LENGTH_SHORT).show();
                        fragment = composeFragment;
                        break;
                    case R.id.action_explore:
                        Toast.makeText(MainActivity.this,"explore!",Toast.LENGTH_SHORT).show();
                        fragment = exploreFragment;
                        break;
                    case R.id.action_profile_screen:
                        Toast.makeText(MainActivity.this,"profile!",Toast.LENGTH_SHORT).show();
                        fragment = profileFragment;
                        break;
                    default:
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.action_home_screen);
    }

//    @Override
//    public void onBackPressed() {
//        if(drawerLayout.isDrawerOpen(GravityCompat.START))
//        {
//            drawerLayout.closeDrawer(GravityCompat.START);
//        }
//        else{
//            super.onBackPressed();
//        }
//    }


}