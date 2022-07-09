package com.example.bfg.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bfg.Models.User;
import com.example.bfg.R;
import com.example.bfg.Adapters.ViewPagerAdapter;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabLayout;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;


public class ProfileFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ShapeableImageView profileImage;
    private TextView tvUserProfileName;
    private TextView tvUserBio;
    protected File photoFile;
    private TextView tvCurrUserStatus;
    private String photoFileName = "photo.jpg";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public User user = (User) User.getCurrentUser();

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
        tvUserProfileName = view.findViewById(R.id.tvUserProfileName);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        profileImage = view.findViewById(R.id.profileImage);
        tvUserBio = view.findViewById(R.id.tvUserBio);
        tvCurrUserStatus = view.findViewById(R.id.tvCurrUserStatus);


//        User's Status
        ParseUser parseuser = ParseUser.getCurrentUser();
        User myUser = (User) parseuser;
        if(myUser.getStatusCount()<=5 || myUser.getStatusCount()<=50)
        {
            tvCurrUserStatus.setText("Noobie");
            myUser.setStatus("Noobie");
            myUser.saveInBackground();
        }
        else if(myUser.getStatusCount()<=51 || myUser.getStatusCount()<=150)
        {
            tvCurrUserStatus.setText("Regular");
            myUser.setStatus("Regular");
            myUser.saveInBackground();
        }
        else if(myUser.getStatusCount()<=151 || myUser.getStatusCount()<=300)
        {
            tvCurrUserStatus.setText("Pro");
            myUser.setStatus("pro");
            myUser.saveInBackground();
        }
        else if(myUser.getStatusCount()<=301 || myUser.getStatusCount()<=500)
        {
            tvCurrUserStatus.setText("Elite");
            myUser.setStatus("Elite");
            myUser.saveInBackground();
        }
        else if(myUser.getStatusCount()>500)
        {
            tvCurrUserStatus.setText("Legend");
            myUser.setStatus("Legend");
            myUser.saveInBackground();
        }


//        set profile picture
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        user.fetchInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                user = (User) object;
                displayInfo();
            }
        });

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
        tvUserProfileName.setText(user.getUsername());
        ParseFile profilePhoto = user.getProfileImage();
        if(profilePhoto != null)
        {
            Glide.with(getContext()).load(profilePhoto.getUrl()).centerCrop().into(profileImage);
        }
        else
        {
            Toast.makeText(getContext(), "profile photo does not exist for"+ user.getUsername(),Toast.LENGTH_SHORT).show();
        }
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