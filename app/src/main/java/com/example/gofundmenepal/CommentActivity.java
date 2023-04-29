package com.example.gofundmenepal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CommentActivity extends AppCompatActivity {
    private ImageButton PostCommentButton ;
    private EditText CommentInputText ;
    private RecyclerView CommentsList ;
    private String Post_Key , current_user_id ;
    private DatabaseReference UsersRef , PostsRef ;
    private FirebaseAuth mAuth ;

    private LinearLayout linearLayout;
    private TextView donorUsername, donotDonation ;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Post_Key = getIntent().getExtras().get("PostKey").toString();
        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(Post_Key).child("Comments");

        CommentsList = (RecyclerView) findViewById(R.id.comments_list);
        CommentsList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        CommentsList.setLayoutManager(linearLayoutManager);
        CommentInputText = (EditText)findViewById(R.id.comment_input);
        PostCommentButton = (ImageButton) findViewById(R.id.post_comment_button);
        linearLayout = (LinearLayout) findViewById(R.id.don_Image);

        donorUsername = findViewById(R.id.donor_username);
        donotDonation = findViewById(R.id.donor_amount);

        if(Post_Key.equals("fzpwEaxpouOspte8E6dO9RD7G9h1null"))
        {
            linearLayout.setVisibility(View.VISIBLE);
        }
        else if (Post_Key.equals("T31qnBHYnxXAmgNZn62Of5kd5np1null"))
        {
            donorUsername.setText("Anonymous Donation");
            donotDonation.setText("100$ donated");

            linearLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            linearLayout.setVisibility(View.INVISIBLE);
        }







        PostCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if(dataSnapshot.exists())
                        {
                            String userName = dataSnapshot.child("username").getValue().toString();
                            ValidateComment(userName);
                            CommentInputText.setText("");
                        }
                    }





                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });

            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Comments , CommentsViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Comments, CommentsViewHolder>
                (
                Comments.class ,
                        R.layout.all_comments_layout ,
                        CommentsViewHolder.class ,
                        PostsRef


                )
        {
            @Override
            protected void populateViewHolder(CommentsViewHolder viewHolder, Comments model, int position) {
               viewHolder.setUsername(model.getUsername());
               viewHolder.setComment(model.getComment());
               viewHolder.setDate(model.getDate());
               viewHolder.setTime(model.getTime());
            }
        };

        CommentsList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CommentsViewHolder extends RecyclerView.ViewHolder{
        View mView ;

        public CommentsViewHolder(@NonNull View itemView) {


            super(itemView);
            mView = itemView ;
        }

        public void setUsername(String username)
        {
            TextView MyUserName = (TextView) mView.findViewById(R.id.comment_username);
            MyUserName.setText("@"+ username+ "");
        }
        public void setComment(String comment)
        {
            TextView MyComment = (TextView) mView.findViewById(R.id.comment_text);
            MyComment.setText(comment);
        }
        public void setDate(String date)
        {
            TextView MyDate = (TextView) mView.findViewById(R.id.comment_date);
            MyDate.setText("  Date: " +date);
        }
        public void setTime(String time)
        {
            TextView MyTime = (TextView) mView.findViewById(R.id.comment_time);
            MyTime.setText("  Time: "+time);
        }
    }

    private void ValidateComment(String userName) {
        String commentText = CommentInputText.getText().toString();
        if(TextUtils.isEmpty(commentText))
        {
            Toast.makeText(CommentActivity.this, "Comment Field cannot be empty !!!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar calforDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
            final String saveCurrentDate = currentDate.format(calforDate.getTime());

            Calendar calforTime = Calendar.getInstance();
            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
            final String saveCurrentTime = currentTime.format(calforTime.getTime());

            final String RandomKey = current_user_id + saveCurrentDate + saveCurrentTime ;

            HashMap commentMap = new HashMap();
            commentMap.put ("uid" , current_user_id);
            commentMap.put ("comment" , commentText);
            commentMap.put ("date" , saveCurrentDate);
            commentMap.put ("time" , saveCurrentTime);
            commentMap.put ("username" , userName);

            PostsRef.child(RandomKey).updateChildren(commentMap)
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(CommentActivity.this, "Updating your comment", Toast.LENGTH_SHORT).show();
                            }

                            else
                            {
                                Toast.makeText(CommentActivity.this, "Error occured while commenting . Please try again", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

        }
    }

}
