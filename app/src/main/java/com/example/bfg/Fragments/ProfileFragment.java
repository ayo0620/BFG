package com.example.bfg.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bfg.EditProfileActivity;
import com.example.bfg.Models.User;
import com.example.bfg.R;
import com.example.bfg.Adapters.ViewPagerAdapter;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.util.List;


public class ProfileFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ShapeableImageView profileImage;
    private TextView tvUserProfileName;
    private TextView tvUserBio;
    private Button btnEditProfile;
    public static String profileId;
    public String dummyId;
    protected File photoFile;
    private TextView tvCurrUserStatus;
    private String photoFileName = "photo.jpg";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public User user ;
    public User fromUser = new User();
    public static final String TAG = "ProfileFragment";
    public SharedPreferences prefs;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        user = (User) ParseUser.getCurrentUser();
        prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileId = prefs.getString("profileId",user.getObjectId());
        dummyId = profileId;
        Log.i(TAG, profileId);
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUserProfileName = view.findViewById(R.id.tvUserProfileName);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        profileImage = view.findViewById(R.id.profileImage);
        tvUserBio = view.findViewById(R.id.tvUserBio);
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        tvCurrUserStatus = view.findViewById(R.id.tvCurrUserStatus);



        if(!profileId.equals(user.getObjectId())) {
            checkIfAdded();
            userViewMode();
        }
        else{
            btnEditProfile.setText("Edit Profile");
            Log.i(TAG, "here");
            setCurrentUserStatus();
            displayInfo();
        }


    }

    private void checkIfAdded() {
        List<String> friendList = user.getUserFriends();
        if (friendList.contains(profileId))
        {
            btnEditProfile.setText("Added");
            Drawable img = btnEditProfile.getContext().getResources().getDrawable(R.drawable.ic_check_friend);
            btnEditProfile.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,img,null);
            btnEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    friendList.remove(profileId);
                    user.setUserFriend(friendList);
                    btnEditProfile.setText("Add friend");
                }
            });
        }
        else
        {
            btnEditProfile.setText("Add Friend");
            btnEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    friendList.add(profileId);
                    user.setUserFriend(friendList);
                    btnEditProfile.setText("Added");
                    btnEditProfile.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);
                }
            });
        }
        user.saveInBackground();
    }


    public static String getProfileId()
    {
        return profileId;
    }
    private void setCurrentUserStatus() {
        if(user.getStatusCount()<=5 || user.getStatusCount()<=50)
        {
            tvCurrUserStatus.setText("Noobie");
            user.setStatus("Noobie");
            user.saveInBackground();
        }
        else if(user.getStatusCount()<=51 || user.getStatusCount()<=150)
        {
            tvCurrUserStatus.setText("Regular");
            user.setStatus("Regular");
            user.saveInBackground();
        }
        else if(user.getStatusCount()<=151 || user.getStatusCount()<=300)
        {
            tvCurrUserStatus.setText("Pro");
            user.setStatus("pro");
            user.saveInBackground();
        }
        else if(user.getStatusCount()<=301 || user.getStatusCount()<=500)
        {
            tvCurrUserStatus.setText("Elite");
            user.setStatus("Elite");
            user.saveInBackground();
        }
        else if(user.getStatusCount()>500)
        {
            tvCurrUserStatus.setText("Legend");
            user.setStatus("Legend");
            user.saveInBackground();
        }


    }

    private void userViewMode() {
        ParseQuery<User> userQuery = ParseQuery.getQuery(User.class);
        userQuery.whereEqualTo("objectId",profileId);
        userQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(List<User> objects, ParseException e) {
                if(objects.size()!= 0)
                {
                    fromUser = objects.get(0);
                    Log.i("checking", fromUser.toString());
                }
                Log.i("database", fromUser.toString());
                tvCurrUserStatus.setText(fromUser.getStatus());
                tvUserBio.setText(fromUser.getUserDescription());
                tvUserProfileName.setText(fromUser.getUsername());
                if (fromUser.getProfileImage() == null)
                {
                    profileImage.setImageResource(R.drawable.default_profile_icon);
                }
                else{
                    Glide.with(getContext()).load(fromUser.getProfileImage().getUrl()).centerCrop().into(profileImage);
                }
                if (fromUser.getUserDescription()== null)
                {
                    tvUserBio.setVisibility(View.GONE);
                }
                else
                {
                    tvUserBio.setText(fromUser.getUserDescription());
                }

            }

        });


//      Listener
        List<String> friendList = user.getUserFriends();
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(friendList.contains(dummyId))
                {
                    friendList.remove(dummyId);
                    user.setUserFriend(friendList);
                    btnEditProfile.setText("Add friend");
                    btnEditProfile.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,null,null);

                }
                else{
                    friendList.add(dummyId);
                    user.setUserFriend(friendList);
                    btnEditProfile.setText("Added");
                    Drawable img = btnEditProfile.getContext().getResources().getDrawable(R.drawable.ic_check_friend);
                    btnEditProfile.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,img,null);
                }
                user.saveInBackground();
            }
        });


        prefs.edit().clear().commit();
        profileId = user.getObjectId();
        Log.i("profile",profileId);

//        TabLayout
        TabLayout.Tab myTab = tabLayout.newTab().setText("Posts");
        tabLayout.addTab(myTab);
        tabLayout.addTab(tabLayout.newTab().setText("Favorited"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getContext(),getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(vpAdapter);

        Log.i("tag", String.valueOf(myTab.getPosition()));
        viewPager.setCurrentItem(0);
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
            }
        });
    }

    private void displayInfo() {
        //        set profile picture
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

//        Edit profile
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        tvUserProfileName.setText(user.getUsername());
        ParseFile profilePhoto = user.getProfileImage();
        if(profilePhoto != null)
        {
            Glide.with(getContext()).load(profilePhoto.getUrl()).centerCrop().into(profileImage);
        }
        else
        {
            profileImage.setImageResource(R.drawable.default_profile_icon);
        }

//        ParseUser user = ParseUser.getCurrentUser();
//        User currUser = (User) user;
        if(user.getUserDescription()== null)
        {
            tvUserBio.setVisibility(View.GONE);
        }
        else{
            tvUserBio.setText(user.getUserDescription());
        }

//        TabLayout
        TabLayout.Tab myTab = tabLayout.newTab().setText("Posts");
        tabLayout.addTab(myTab);
        tabLayout.addTab(tabLayout.newTab().setText("Favorited"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPagerAdapter vpAdapter = new ViewPagerAdapter(getContext(),getChildFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(vpAdapter);

        Log.i("tag", String.valueOf(myTab.getPosition()));
        viewPager.setCurrentItem(0);
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
            }
        });
    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.example.fileprovider.BFG", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myTag");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("myTag", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                Glide.with(getContext()).load(takenImage).circleCrop().into(profileImage);
//                profileImage.setImageBitmap(takenImage);
                user.setProfileImage(new ParseFile(photoFile));
                user.saveInBackground();
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}