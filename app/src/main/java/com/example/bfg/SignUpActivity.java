package com.example.bfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bfg.Models.User;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText etUserNameSignUp;
    EditText etPasswordSignUp;
    EditText etConfirmPassword;
    Button btnSignUp;
    TextView tvLogInLink;
    private static final String TAG = "SignUpActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

//        getSupportActionBar().hide();

        firstName = findViewById(R.id.etFirstName);
        lastName = findViewById(R.id.etLastName);
        email = findViewById(R.id.etEmail);
        etUserNameSignUp = findViewById(R.id.etUserNameSignUp);
        etPasswordSignUp = findViewById(R.id.etPasswordSignUp);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogInLink = findViewById(R.id.tvLogInLink);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setFirstName(String.valueOf(firstName.getText()));
                user.setLastName(String.valueOf(lastName.getText()));
                user.setEmail(String.valueOf(email.getText()));
                user.setUsername(String.valueOf(etUserNameSignUp.getText()));
                user.setPassword(String.valueOf(etPasswordSignUp.getText()));
                user.setStatusCount(0);
                user.setStatus("Noobie");
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e!= null)
                        {
                            Log.i(TAG, "Error!",e);
                            Toast.makeText(SignUpActivity.this,"Error!",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            goToMainActivity();
                        }
                    }
                });
            }
        });

        tvLogInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private void goToMainActivity() {
        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(i);
    }
}