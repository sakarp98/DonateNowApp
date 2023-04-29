package com.example.gofundmenepal;

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

    private TextView ClickPostTitle , ClickPostDesc , ClickPostDateTime , ClickPostLocation,
            ClickPostInitialAmount, ClickPostFinalAmount ;


    private ImageView ClickPostImage , IVtick;
    private  String PostKey ;
    public static int clickaddedAmt, initialProgressAmount, finalProgressAmount;
    private DatabaseReference ClickPostRef , UsersRef;
  private Button ClickPostDonate, clickprogressincrement ;
    private ImageButton ClickPostLike ,ClickPostComment ,  ClickPostShare ;
    private FirebaseAuth mAuth;
    String currentUserID;


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


        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();





        ClickPostTitle = (TextView)findViewById(R.id.click_post_title);
        ClickPostDesc = (TextView)findViewById(R.id.click_post_desc);
        ClickPostImage = (ImageView)findViewById(R.id.click_post_image);
        ClickPostDateTime = (TextView)findViewById(R.id.click_post_datetime);
        ClickPostLocation = (TextView)findViewById(R.id.click_post_location);
        clickprogressincrement = findViewById(R.id.incrementButton);

        ClickPostInitialAmount = findViewById(R.id.clickpostdonation_Raised_desc1);
        ClickPostFinalAmount = findViewById(R.id.clickpostdonation_Raised_desc2);
        ProgressBar progressBar = findViewById(R.id.post_progress_bar_desc);



        IVtick = (ImageView)findViewById(R.id.tick);


        Intent intent1 = getIntent();
        clickaddedAmt = intent1.getIntExtra("amtclick",0);
        initialProgressAmount = intent1.getIntExtra("initialProgress",1);
        finalProgressAmount = intent1.getIntExtra("finalProgress",2);

//        if (currentUserID.equals("RYLY33JaFsXLdQs24HuvxRCnHo13")) {
//            IVtick.setVisibility(View.VISIBLE);
//        }
//        else {
//            IVtick.setVisibility(View.GONE);
//        }


        clickprogressincrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        ClickPostDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ProgressBar progressBar = findViewById(R.id.post_progress_bar_desc);
//                progressBar.setMax(100);
//                int currentProgress = progressBar.getProgress();
//                int newProgress = currentProgress + 10;
//                progressBar.setProgress(newProgress);
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
                String initialAmount = dataSnapshot.child("initialAmount").getValue().toString();
                String finalAmount = dataSnapshot.child("finalAmount").getValue().toString();


                if (date.equals("Prakash") || date.equals("Company xyz") || date.equals("Taylor Swift")) {
            IVtick.setVisibility(View.VISIBLE);
        }
        else {
            IVtick.setVisibility(View.GONE);
        }

                ClickPostTitle.setText(title);
                ClickPostDateTime.setText(date);
                ClickPostLocation.setText(location);

                progressBar.setMax(finalProgressAmount);
                progressBar.setProgress(initialProgressAmount);

                int currentProgress = progressBar.getProgress();
                int newProgress = currentProgress + clickaddedAmt;
                progressBar.setProgress(newProgress);


                ClickPostDesc.setText(description);
                ClickPostInitialAmount.setText("$" + (Integer.parseInt(initialAmount)  + clickaddedAmt) + " raised of $");
                ClickPostFinalAmount.setText(finalAmount);
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
