package com.example.gofundmenepal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ClickPost extends AppCompatActivity {

    private TextView ClickPostTitle , ClickPostDesc , ClickPostDateTime , ClickPostLocation, donation_raised_value ;


    private ImageView ClickPostImage , IVtick;
    private  String PostKey ;
    private DatabaseReference ClickPostRef , UsersRef;
  private Button ClickPostDonate, clickprogressincrement ;
    private ImageButton ClickPostLike ,ClickPostComment ,  ClickPostShare ;
    private FirebaseAuth mAuth;
    String currentUserID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);
        PostKey = getIntent().getExtras().get("PostKey").toString();
        ClickPostRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(PostKey);
       // UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        ClickPostDonate = (Button)findViewById(R.id.click_post_donate);
        ClickPostLike = (ImageButton) findViewById(R.id.clickpostlike_button);
        ClickPostComment = (ImageButton)findViewById(R.id.clickpost_comment_button);
        ClickPostShare = (ImageButton)findViewById(R.id.clickpost_share);

//        ClickPostLike.setVisibility(View.INVISIBLE);
//        ClickPostComment.setVisibility(View.INVISIBLE);
//        ClickPostShare.setVisibility(View.INVISIBLE);

        donation_raised_value = findViewById(R.id.donation_Raised_desc);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        ProgressBar progressBar = findViewById(R.id.post_progress_bar_desc);
        progressBar.setProgress(0);



        ClickPostTitle = (TextView)findViewById(R.id.click_post_title);
        ClickPostDesc = (TextView)findViewById(R.id.click_post_desc);
        ClickPostImage = (ImageView)findViewById(R.id.click_post_image);
        ClickPostDateTime = (TextView)findViewById(R.id.click_post_datetime);
        ClickPostLocation = (TextView)findViewById(R.id.click_post_location);
        clickprogressincrement = findViewById(R.id.incrementButton);

        IVtick = (ImageView)findViewById(R.id.tick);

//        if (currentUserID.equals("RYLY33JaFsXLdQs24HuvxRCnHo13")) {
//            IVtick.setVisibility(View.VISIBLE);
//        }
//        else {
//            IVtick.setVisibility(View.GONE);
//        }


        clickprogressincrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressBar progressBar = findViewById(R.id.post_progress_bar_desc);
                int currentProgress = progressBar.getProgress();
                int newProgress = currentProgress + 10;
                progressBar.setProgress(newProgress);
            }
        });

        ClickPostDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToDonateActivity();
            }
        });


        ClickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String description = dataSnapshot.child("desc").getValue().toString();
                String title = dataSnapshot.child("title").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String date = dataSnapshot.child("fullname").getValue().toString();
                String location = dataSnapshot.child("location").getValue().toString();


                if (date.equals("Prakash") || date.equals("Company xyz")) {
            IVtick.setVisibility(View.VISIBLE);
        }
        else {
            IVtick.setVisibility(View.GONE);
        }

                ClickPostTitle.setText(title);
                ClickPostDateTime.setText(date);
                ClickPostLocation.setText(location);



                ClickPostDesc.setText(description);
                Picasso.with(ClickPost.this).load(image).into(ClickPostImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void SendUserToDonateActivity() {
        Intent clickIntent = new Intent(ClickPost.this , Payment.class);
        clickIntent.putExtra("PostKey" , PostKey);
        startActivity(clickIntent);
    }


}
