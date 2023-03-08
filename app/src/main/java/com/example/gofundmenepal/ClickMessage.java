package com.example.gofundmenepal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ClickMessage extends AppCompatActivity {
    private TextView ClickMessageTitle , ClickMessageDesc , ClickMessageAmount , ClickMessageDate ;
    private ImageView ClickMessageImage ;
    private DatabaseReference ClickmessageRef ;
    String currentUserID ;
    private FirebaseAuth mAuth ;
    private  String MszKey ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_message);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        MszKey = getIntent().getExtras().get("MszKey").toString();

        ClickmessageRef = FirebaseDatabase.getInstance().getReference().child("Details").child(MszKey);
        ClickMessageTitle = (TextView) findViewById(R.id.click_message_title);
        ClickMessageDesc = (TextView) findViewById(R.id.click_message_desc);
        ClickMessageAmount = (TextView) findViewById(R.id.click_message_amount);
        ClickMessageDate = (TextView) findViewById(R.id.click_message_date);
        ClickMessageImage = (ImageView) findViewById(R.id.click_message_image);

        ClickmessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String messagename = dataSnapshot.child("NeedyName").getValue().toString();
                String messagedetails = dataSnapshot.child("NeedyDetails").getValue().toString();
                String messageamt = dataSnapshot.child("RequiredAmount").getValue().toString();
                String messageimage = dataSnapshot.child("Needyimage").getValue().toString();
                String messagedate = dataSnapshot.child("Date").getValue().toString();

                ClickMessageTitle.setText(messagename);
                ClickMessageDesc.setText(messagedetails);
                ClickMessageAmount.setText(messageamt);
                ClickMessageDate.setText(messagedate);
                Picasso.with(ClickMessage.this).load(messageimage).into(ClickMessageImage);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });








    }
}
