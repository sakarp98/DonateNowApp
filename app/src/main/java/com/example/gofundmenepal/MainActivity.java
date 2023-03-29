package com.example.gofundmenepal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView postList, postData;
    private CircleImageView NavProfileImage;
    private TextView NavProfileUserName;
    private Toolbar mToolbar;
    private ImageView navtick ;
    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef, PostsRef, rootRef, LikesRef, DonateRef;
    private ImageButton SendMessageButton, CreatePost;
    String currentUserID;
    Boolean LikeChecker = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
  navtick = findViewById(R.id.ticknav);
       // navtick.setVisibility(View.GONE);



        if (currentUserID.equals("JRtEIb5kuLfMHAJhKU71FBdWigH3") || currentUserID.equals("RYLY33JaFsXLdQs24HuvxRCnHo13") || currentUserID.equals("9LYGbUm8RPRWSbP8g9RzdEbyMaA3" )) {
            setContentView(R.layout.activity_main);
            // navtick.setVisibility(View.GONE);
            CreatePost = (ImageButton) findViewById(R.id.createpost);
            CreatePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CreatePost.setVisibility(View.VISIBLE);
                    SendAdminToPostActivity();
                }
            });

        }


        else {
            setContentView(R.layout.activity_user);
           // navtick.setVisibility(View.GONE);
            SendMessageButton = (ImageButton) findViewById(R.id.sendmessage);
            SendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SendMessageButton.setVisibility(View.VISIBLE);
                    SendUserToMessageActivity();
                }
            });

        }
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        rootRef = FirebaseDatabase.getInstance().getReference();
        PostsRef = rootRef.child("Posts");
        PostsRef.keepSynced(true);

        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        DonateRef = FirebaseDatabase.getInstance().getReference().child("DonationProgress");

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Home");


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View navView = navigationView.inflateHeaderView(R.layout.navigation_header);
        NavProfileImage = (CircleImageView) navView.findViewById(R.id.navprofile_image);
        NavProfileUserName = (TextView) navView.findViewById(R.id.nav_user_full_name);







        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {


                    if (dataSnapshot.hasChild("fullname")) {
                        String fullname = dataSnapshot.child("fullname").getValue().toString();


                        NavProfileUserName.setText(fullname);




                    }
                    if (dataSnapshot.hasChild("profileimage")) {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Picasso.with(MainActivity.this).load(image).placeholder(R.drawable.profile).into(NavProfileImage);

                    } else {

                        Toast.makeText(MainActivity.this, "Loading", Toast.LENGTH_SHORT).show();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });




        postData = (RecyclerView) findViewById(R.id.myrecyclerview);
        postData.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postData.setLayoutManager(linearLayoutManager);


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                UserMenuSelector(menuItem);
                return false;

            }
        });


    }

    private void SendAdminToPostActivity() {
        Intent postIntent = new Intent(MainActivity.this, PostActivity.class);
        startActivity(postIntent);

    }

    private void DisplayAllUsersPost() {
        FirebaseRecyclerAdapter<Posts, PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Posts, PostViewHolder>
                        (
                                Posts.class,
                                R.layout.posts,
                                PostViewHolder.class,
                                PostsRef

                        ) {
                    @Override
                    protected void populateViewHolder(PostViewHolder viewHolder, Posts model, int position) {

                        final String PostKey = getRef(position).getKey();
                        viewHolder.setTitle(model.getTitle());
                        viewHolder.setDesc(model.getDesc());
                        viewHolder.setDate(model.getDate());

                        viewHolder.setLocation(model.getLocation());

                        viewHolder.setImage(getApplicationContext(), model.getImage());

                        viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent ClickPostIntent = new Intent(MainActivity.this, ClickPost.class);
                                ClickPostIntent.putExtra("PostKey", PostKey);
                                startActivity(ClickPostIntent);
                            }
                        });




                        viewHolder.CommentPostButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent CommentsIntent = new Intent(MainActivity.this, CommentActivity.class);
                                CommentsIntent.putExtra("PostKey", PostKey);
                                startActivity(CommentsIntent);
                            }
                        });
                        viewHolder.setLikeButtonStatus(PostKey);

                        viewHolder.LikePostButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                ProgressBar progressBar2 = findViewById(R.id.post_progress_bar);
                                int currentProgress = progressBar2.getProgress();
                                int newProgress = currentProgress + 10;
                                progressBar2.setProgress(newProgress);
                                LikeChecker = true;
                                LikesRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        if (LikeChecker.equals(true)) {
                                            if (dataSnapshot.child(PostKey).hasChild(currentUserID)) {
                                                LikesRef.child(PostKey).child(currentUserID).removeValue();
                                                LikeChecker = false;
                                            } else {
                                                LikesRef.child(PostKey).child(currentUserID).setValue(true);
                                                LikeChecker = false;

                                            }

                                        }


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        });

                    }
                };
        postData.setAdapter(firebaseRecyclerAdapter);

    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {


        View mView;
        ImageButton LikePostButton, CommentPostButton;
        TextView DisplayNoOfLikes;
        int countLikes;
        String currentUserId;
        DatabaseReference LikesRef;
        ProgressBar progressBar ;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;

            ProgressBar progressBar = mView.findViewById(R.id.post_progress_bar);
                     progressBar.setProgress(10);

            LikePostButton = (ImageButton) mView.findViewById(R.id.like_button);
            CommentPostButton = (ImageButton) mView.findViewById(R.id.comment_button);
            DisplayNoOfLikes = (TextView) mView.findViewById(R.id.display_no_of_likes);

            LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();



        }

        public void setLikeButtonStatus(final String PostKey) {
            LikesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(PostKey).hasChild(currentUserId)) {
                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.like);
                        DisplayNoOfLikes.setText(Integer.toString(countLikes) + (" Likes"));
                    } else {
                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                        LikePostButton.setImageResource(R.drawable.dislike);
                        DisplayNoOfLikes.setText(Integer.toString(countLikes) + (" Likes"));

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        public void setDate(String date) {
            TextView postDate = (TextView) mView.findViewById(R.id.post_date);
            postDate.setText(date);

        }


        public void setLocation(String location) {
            TextView postLocation = (TextView) mView.findViewById(R.id.post_location);
            postLocation.setText(location);


        }

        public void setDesc(String desc) {
            TextView postDescription = (TextView) mView.findViewById(R.id.post_desc);
            postDescription.setText(desc);


        }

        public void setTitle(String title) {
            TextView postTitle = (TextView) mView.findViewById(R.id.post_title);
            postTitle.setText(title);
        }

        public void setImage(Context ctx, String image) {
            ImageView featimage = (ImageView) mView.findViewById(R.id.featured_image);
            Picasso.with(ctx).load(image).into(featimage);
        }


    }

    private void SendUserToMessageActivity() {
        Intent sendmessageintent = new Intent(MainActivity.this, SendMessage.class);
        startActivity(sendmessageintent);
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            SendUserToMainActivity();
        } else {
            CheckUserExistence();
        }
        DisplayAllUsersPost();


    }


    private void CheckUserExistence() {
        final String current_user_id = mAuth.getCurrentUser().getUid();
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(current_user_id)) {
                    SendUserToSetupActivity();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void SendUserToSetupActivity() {
        Intent SetupIntent = new Intent(MainActivity.this, SetupActivity.class);
        SetupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(SetupIntent);
        finish();
    }


    private void SendUserToMainActivity() {
        Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void UserMenuSelector(MenuItem menuItem) {



        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_home:
                SendUserToMainActivity();
                break;

            case R.id.nav_inbox:
                SendUserToChatActivity();
                break;


            case R.id.nav_notifications:
                Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_donation:
                Toast.makeText(this, "My Donations", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_account:
                SendUserToAccountActivity();
                break;

            case R.id.nav_help:
                SendUserToHeplActivity();

                break;

            case R.id.nav_aboutus:
                SendUserToAboutActivity();
                break;


            case R.id.nav_logout:
                mAuth.signOut();
                SendUserToMainActivity();
                break;

        }
    }

    private void SendUserToChatActivity() {
        if (currentUserID.equals("qxA7zRc87XfsieDw2h7DkhCT3or1")) {
            Intent chatIntent = new Intent(MainActivity.this, MessageToAdmin.class);

            startActivity(chatIntent);
        } else {
            Intent chatIntent = new Intent(MainActivity.this, SendMessage.class);

            startActivity(chatIntent);
        }
    }


    private void SendUserToAboutActivity() {
        Intent aboutintent = new Intent(MainActivity.this, AboutUs.class);

        startActivity(aboutintent);

    }

    private void SendUserToHeplActivity() {
        Intent helpintent = new Intent(MainActivity.this, Help.class);

        startActivity(helpintent);


    }

    private void SendUserToAccountActivity() {
        Intent intent = new Intent(MainActivity.this, AccountSettings.class);

        startActivity(intent);


    }





}
