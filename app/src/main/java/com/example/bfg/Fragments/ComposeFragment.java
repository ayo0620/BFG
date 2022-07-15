package com.example.bfg.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.example.bfg.MainActivity;
import com.example.bfg.Models.Cards;
import com.example.bfg.Models.Post;
import com.example.bfg.Models.User;
import com.example.bfg.R;
import com.example.bfg.SearchGamesActivity;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.io.File;

public class ComposeFragment extends Fragment {

    private ImageView ivComposeProfileImage;
    private TextView etComposeDescription;
    private ImageView ivUserImage;
    private Cards cards;
    private Button btnPost;
    private Toolbar toolbarCompose;
    private TextView tvPickGameCategory;
    private TextView tvSetGameCategory;
    MainActivity activity;
    protected File photoFile;
    protected String photoFileName = "photo.jpg";
    private static final String TAG = "ComposeFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public static final String KEY_STATUS_COUNT = "statusCount";
    public static final int INCREMENT_BY = 20;

    public ComposeFragment() {
    }
    public ComposeFragment(MainActivity mainActivity){
        activity = mainActivity;
    }
    private static final String KEY_PROFILE_IMAGE = "ProfileImage";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivComposeProfileImage = view.findViewById(R.id.ivComposeProfileImage);
        etComposeDescription = view.findViewById(R.id.etComposeDescription);
        ivUserImage = view.findViewById(R.id.ivUserImage);
        btnPost = view.findViewById(R.id.btnPost);
        toolbarCompose = (Toolbar)getActivity().findViewById(R.id.toolbarCompose);
        tvPickGameCategory = view.findViewById(R.id.tvPickGameCategory);
        tvSetGameCategory = view.findViewById(R.id.tvSetGameCategory);
        tvSetGameCategory.setVisibility(View.GONE);

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbarCompose);

//        set title choosing by user from searchGameActivity;
        if (activity.cards!= null)
        {
            tvSetGameCategory.setVisibility(View.VISIBLE);
            tvSetGameCategory.setText(activity.cards.getName());
            tvSetGameCategory.setTextColor(Color.WHITE);
        }

        ParseUser user = ParseUser.getCurrentUser();
        ParseFile image = user.getParseFile(KEY_PROFILE_IMAGE);
        if (image == null)
        {
            ivComposeProfileImage.setImageResource(R.drawable.default_profile_icon);
        }
        else {
            Glide.with(getActivity()).load(image.getUrl()).circleCrop().into(ivComposeProfileImage);
        }
        setHasOptionsMenu(true);

//        tvSetGameCategory.setVisibility(View.GONE);
        tvPickGameCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), SearchGamesActivity.class);
                startActivity(i);
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etComposeDescription.getText().toString();
                if(description.isEmpty())
                {
                    Toast.makeText(getContext(),"Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(photoFile == null || ivUserImage.getDrawable() == null)
                {
                    Toast.makeText(getContext(),"There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                String gameForPost = tvSetGameCategory.getText().toString();
                savePost(description, currentUser, photoFile,gameForPost);
                activity.bottomNavigationView.setSelectedItemId(R.id.action_home_screen);
//                increment status
                MainActivity.statusIncrementation(INCREMENT_BY);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.camera_icon_view, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.camera_button)
        {
            Toast.makeText(getContext(),"Hello",Toast.LENGTH_SHORT).show();
            launchCamera();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
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
                Glide.with(getContext()).load(takenImage).into(ivUserImage);

            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void savePost(String description, ParseUser currentUser, File photoFile, String gameName) {
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.setPostForGame(gameName);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null)
                {
                    Log.e(TAG, "Error while saving",e);
                    Toast.makeText(getContext(), "Error while saving",Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG,"Post was saved successfully!");
                etComposeDescription.setText("");
                tvSetGameCategory.setText("");
                ivUserImage.setImageResource(0);
            }
        });
    }
}