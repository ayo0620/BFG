package com.example.bfg.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bfg.Models.FeedBack;
import com.example.bfg.R;
import com.parse.ParseUser;

public class UserFeedBackFragment extends Fragment {
    Toolbar toolbar;
    private EditText userFeedBackText;
    private Button feedbackSubmit;


    public UserFeedBackFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_feed_back, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = view.findViewById(R.id.feedback_Toolbar);
        userFeedBackText = view.findViewById(R.id.userFeedBackText);
        feedbackSubmit = view.findViewById(R.id.feedbackSubmit);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        feedbackSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userFeedBackText.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(),"feedback cannot be empty",Toast.LENGTH_SHORT).show();
                }
                else{
                    FeedBack userFeedback = new FeedBack();
                    userFeedback.setDescription(userFeedBackText.getText().toString());
                    ParseUser user = ParseUser.getCurrentUser();
                    userFeedback.setUser(user);
                    userFeedback.saveInBackground();
                }

                userFeedBackText.setText("");
            }
        });
    }
}