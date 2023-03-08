package com.example.gofundmenepal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MessageToAdmin extends AppCompatActivity {
    private RecyclerView messageList ;
    private DatabaseReference messagesRef ;
    private FirebaseAuth mAuth ;
    String currentUserID ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_to_admin);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        messagesRef = FirebaseDatabase.getInstance().getReference().child("Details");

        messageList = (RecyclerView) findViewById(R.id.messagerecyclerview);
        messageList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        messageList.setLayoutManager(linearLayoutManager);
        DisplayMessages();

    }

    private void DisplayMessages() {
        FirebaseRecyclerAdapter < Messages ,MessagesViewHolder > firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Messages, MessagesViewHolder>
                (
                 Messages.class ,
                 R.layout.mesage_layout_of_user ,
                 MessagesViewHolder.class ,
                        messagesRef


                )
        {
            @Override
            protected void populateViewHolder(MessagesViewHolder viewHolder, Messages model, int position)
            {
                final String MszKey = getRef(position).getKey();
                viewHolder.setNeedyDetails(model.getNeedyDetails());
                viewHolder.setNeedyName(model.getNeedyName());
                viewHolder.setRequiredAmount(model.getRequiredAmount());
                viewHolder.setNeedyimage(getApplicationContext(),model.getNeedyimage());

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ClickMessageIntent = new Intent(MessageToAdmin.this , ClickMessage.class);
                        ClickMessageIntent.putExtra("MszKey" , MszKey);
                        startActivity(ClickMessageIntent);
                    }
                });


            }
        };
        messageList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class MessagesViewHolder extends RecyclerView.ViewHolder {

        View mView ;
        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setNeedyName(String needyName)
        {
            TextView messageNeedyName = (TextView) mView.findViewById(R.id.needyName);
            messageNeedyName.setText(needyName);
        }
        public void setNeedyDetails(String needyDetails)
        {
            TextView messageNeedydetails = (TextView) mView.findViewById(R.id.needydetails);
            messageNeedydetails.setText(needyDetails);
        }
        public void setRequiredAmount(String requiredAmount)
        {
            TextView messageNeedyamount = (TextView) mView.findViewById(R.id.requiredAmount);
            messageNeedyamount.setText(requiredAmount);
        }
        public void setNeedyimage(Context ctx , String needyimage)
        {
            ImageView needyimg = (ImageView)mView.findViewById(R.id.needyImage)  ;
            Picasso.with(ctx).load(needyimage).into(needyimg);
        }


    }

}
