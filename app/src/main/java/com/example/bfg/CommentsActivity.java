package com.example.bfg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bfg.Adapters.CommentsAdapter;
import com.example.bfg.Models.Comments;
import com.example.bfg.Models.Notifications;
import com.example.bfg.Models.Post;
import com.example.bfg.Models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {
    Post post;
    EditText etCommentInput;
    Button commentBtn;
    CommentsAdapter adapter;
    RecyclerView rvComments;
    List<Comments> allComments;
    public static final String KEY_STATUS_COUNT = "statusCount";
    public static final int INCREMENT_BY = 5;

    @Override
    protected void onRestart() {
        super.onRestart();
        reFreshComment();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        etCommentInput = findViewById(R.id.etCommentInput);
        commentBtn = findViewById(R.id.commentBtn);
        rvComments = findViewById(R.id.rvComments);

        post = getIntent().getParcelableExtra(Post.class.getSimpleName());

        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Comments comments = new Comments();
                comments.setUser(ParseUser.getCurrentUser());
                comments.setDescription(etCommentInput.getText().toString());
                comments.setPost(post);
                statusIncrementComment();
                commentsNotification(ParseUser.getCurrentUser(),post.getUser());
                comments.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e!= null)
                        {
                            return;
                        }
                        else{
                            finish();
                        }
                    }
                });

            }
        });
        allComments = new ArrayList<>();
        reFreshComment();
        adapter = new CommentsAdapter(this, allComments);
        rvComments.setLayoutManager(new LinearLayoutManager(this));
        rvComments.setAdapter(adapter);
        adapter.notifyDataSetChanged();



    }

    private void commentsNotification(ParseUser fromUser, ParseUser toUser) {
        if (!fromUser.getObjectId().equals(toUser.getObjectId()))
        {
            Notifications notification = new Notifications();
            notification.setNotifiedFrom(fromUser);
            notification.setNotification("commented on your post");
            notification.setNotifyThis(toUser);
            notification.saveInBackground();
        }
    }


    private void statusIncrementComment() {
        ParseUser user = ParseUser.getCurrentUser();
        int val = (int) user.getNumber(KEY_STATUS_COUNT);
        User myUser = (User) user;
        myUser.setStatusCount(val+INCREMENT_BY);
        myUser.saveInBackground();
    }

    public void reFreshComment(){
        ParseQuery<Comments> query = ParseQuery.getQuery(Comments.class);
        query.whereEqualTo(Comments.KEY_POST, post);
        query.orderByDescending("createdAt");
        query.include(Comments.KEY_USER);
        query.findInBackground(new FindCallback<Comments>() {
            @Override
            public void done(List<Comments> objects, ParseException e) {
                if(e!= null){
                    return;
                }else{
                    allComments.addAll(objects);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}