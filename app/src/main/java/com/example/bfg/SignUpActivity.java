package com.example.bfg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bfg.Models.User;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {
    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText etUserNameSignUp;
    private EditText etPasswordSignUp;
    private EditText etConfirmPassword;
    private Button btnSignUp;
    private TextView tvLogInLink;
    private TextInputLayout layout_ConfirmPasswordSignUp;
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
        layout_ConfirmPasswordSignUp = findViewById(R.id.layout_ConfirmPasswordSignUp);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvLogInLink = findViewById(R.id.tvLogInLink);


        etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!etPasswordSignUp.getText().toString().equals(etConfirmPassword.getText().toString()))
                {
                    layout_ConfirmPasswordSignUp.setError("password do not match");
                }
                else{
                    layout_ConfirmPasswordSignUp.setErrorEnabled(false);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etPasswordSignUp.getText().toString().equals(etConfirmPassword.getText().toString())) {
                    layout_ConfirmPasswordSignUp.setError("password do not match");
                }
                else {
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
                            if (e != null) {
                                Log.i(TAG, "Error!", e);
                                Toast.makeText(SignUpActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                            } else {
                                goToMainActivity();
                            }
                        }
                    });
                }
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