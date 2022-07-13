package com.example.bfg;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bfg.Fragments.ProfileFragment;
import com.example.bfg.Fragments.UserPostsFragment;
import com.example.bfg.Models.Cards;
import com.example.bfg.Models.User;
import com.google.android.material.imageview.ShapeableImageView;
import com.parse.ParseFile;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.io.File;

public class EditProfileActivity extends AppCompatActivity {
    private ShapeableImageView editProfileImage;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editUsername;
    private EditText editEmail;
    private EditText editBio;
    private ImageView close;
    private ImageButton ibChangeImage;
    private Button btnUpdateProfile;
    protected File photoFile;
    private String photoFileName = "photo.jpg";
    ParseUser user;
    User myUser;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editProfileImage = findViewById(R.id.editProfileImage);
        editFirstName  = findViewById(R.id.editFirstName);
        editLastName = findViewById(R.id.editLastName);
        editUsername = findViewById(R.id.editUsername);
        close = findViewById(R.id.close);
        editEmail = findViewById(R.id.editEmail);
        editBio = findViewById(R.id.editBio);
        ibChangeImage = findViewById(R.id.ibChangeImage);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        user = ParseUser.getCurrentUser();
        myUser = (User) user;

        ParseFile img = myUser.getProfileImage();
        if(img!= null)
        {
            Glide.with(this).load(img.getUrl()).circleCrop().into(editProfileImage);
        }
        else
        {
            editProfileImage.setImageResource(R.drawable.default_profile_icon);
        }
        editFirstName.setText(myUser.getFirstName());
        editLastName.setText(myUser.getLastName());
        editUsername.setText(myUser.getUsername());
        editEmail.setText(myUser.getEmail());
        String currBio = myUser.getUserDescription();
        if (currBio!= null)
        {
            editBio.setText(currBio);
        }
        else{
            editBio.setText("");
        }

        ibChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myUser.setLastName(editLastName.getText().toString());
                myUser.setUsername(editUsername.getText().toString());
                myUser.setUserDescription(editBio.getText().toString());
                myUser.setEmail(editEmail.getText().toString());
                myUser.saveInBackground();
                finish();
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
        Uri fileProvider = FileProvider.getUriForFile(this, "com.example.fileprovider.BFG", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(this.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myTag");

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
                Glide.with(this).load(takenImage).circleCrop().into(editProfileImage);
//                profileImage.setImageBitmap(takenImage);
                myUser.setProfileImage(new ParseFile(photoFile));
                user.saveInBackground();
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}