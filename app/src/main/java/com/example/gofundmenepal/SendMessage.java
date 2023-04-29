package com.example.gofundmenepal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class SendMessage extends AppCompatActivity {
    private ImageButton Legaldocs , Addimage ;
    private EditText MessageName , MessageDetails , Messageamount ;
    private Button Sendmessage ;
    private ImageView PeopleImage ;
    private FirebaseAuth mAuth ;
    private ProgressDialog loadingBar ;
    String currentUserID ;
    private  String saveCurrentDate , downloadUrl ;
    DatabaseReference dataref ;
    private DatabaseReference UsersRef ;
    private Uri resultUri;
    private StorageReference ImageRef ;
    final static int Gallery_Pick = 1 ;
    final static int Folder_Pick = 1 ;

    private ImageView PeoplefolderImage ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        dataref = FirebaseDatabase.getInstance().getReference("Details").child(currentUserID);
        ImageRef = FirebaseStorage.getInstance().getReference().child("Needy People's Images").child(currentUserID);
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        loadingBar = new ProgressDialog(this);





        MessageName = (EditText) findViewById (R.id.messagename);
        MessageDetails = (EditText) findViewById(R.id.messagedetails);
        Messageamount = (EditText) findViewById(R.id.messageamount) ;
        Sendmessage = (Button) findViewById(R.id.SendMessage) ;
        PeopleImage = (ImageView)findViewById(R.id.addimage);
      //  PeoplefolderImage = (ImageView)findViewById(R.id.peoplefolderimage);

        Sendmessage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               SendMessageToAdminActivity();
           }
       });



        PeopleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent GalleryIntent = new Intent();
                GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                GalleryIntent.setType("image/*");
                startActivityForResult(GalleryIntent , Gallery_Pick );
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null)
        {
            Uri ImageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(3 ,2)
                    .start(this);



        }
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result =  CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {

                loadingBar.setTitle("Needy Image");
                loadingBar.setMessage("Please wait,we are adding needy image !  ");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);


               Uri resultUri = result.getUri();
               StorageReference filepath = ImageRef.child( currentUserID + ".jpg");

                /*
                filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {@Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri.toString();
                                Toast.makeText(SendMessage.this, "Image Successfully stored", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent selfIntent = new Intent(SendMessage.this, SendMessage.class);
                                startActivity(selfIntent);
                            }
                        });

                    }
                });

                */

                 filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(SendMessage.this, "Image Successfully stored", Toast.LENGTH_SHORT).show();
                            final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();
                            dataref.child("Needyimage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Intent selfIntent = new Intent(SendMessage.this, SendMessage.class);
                                        startActivity(selfIntent);

                                        Toast.makeText(SendMessage.this, "Image successfully sent to admin", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else
                                    {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(SendMessage.this, "Error Occured" + message , Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }

                                }
                            });
                        }

                    }
                });



            }
            else
            {
                Toast.makeText(this, "Error Occured , Image cant be cropped", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }


    private void SendMessageToAdminActivity() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMMM ");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        String NeedyName = MessageName.getText().toString();
        String NeedyDetails = MessageDetails.getText().toString();
        String RequiredAmount = Messageamount.getText().toString();

        if (TextUtils.isEmpty(NeedyName))
        {
            Toast.makeText(this, "Name Field cannot be Empty !", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(NeedyDetails))
        {
            Toast.makeText(this, "Please enter the details of people to be funded", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(RequiredAmount))
        {
            Toast.makeText(this, "Please enter the amount to be funded", Toast.LENGTH_SHORT).show();
        }
        else
        {

            //String currentUserID = dataref.push().getKey();


            /*Details details = new Details(messagename ,messageamount , messagedetails);
            dataref.child(currentUserID).setValue(details);
            Toast.makeText(this, "Message sent successfully", Toast.LENGTH_SHORT).show();
            SendUserToMainActivity();
            */

            loadingBar.setTitle("Please wait,we are creating your new account !  ");
            loadingBar.setMessage("Note: We will be reviewing your government issued id soon and ask for it again if needed ");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap dataMap = new HashMap();
            dataMap.put("Date", saveCurrentDate);
            dataMap.put("NeedyName", NeedyName);
            dataMap.put("NeedyDetails", NeedyDetails);
            dataMap.put("RequiredAmount", RequiredAmount);




            dataref.updateChildren(dataMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {
                        SendUserToMainActivity();
                        Toast.makeText(SendMessage.this, "Message Successfully sent", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message =  task.getException().getMessage();
                        Toast.makeText(SendMessage.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });


        }

    }

    private void SendUserToMainActivity() {
        Intent intent = new Intent(SendMessage.this , MainActivity.class);

        startActivity(intent);
    }
}
