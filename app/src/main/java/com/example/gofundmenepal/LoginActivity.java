package com.example.gofundmenepal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Button LoginButton ;
    private EditText UserEmail , UserPassword ;
    private TextView NeedNewAccountLink , AdminLoginLink , NotAdminLoginLink ;
    private FirebaseAuth mAuth ;
    private ProgressDialog loadingBar ;
    String currentUserID ;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();


        showSnackbar();

        NeedNewAccountLink = (TextView) findViewById(R.id.register_account_link);

        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText) findViewById(R.id.register_password) ;
        LoginButton = (Button) findViewById(R.id.login_button);
        loadingBar = new ProgressDialog(this);
        


        NeedNewAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SendUserToRegisterActivity();
            }
        });

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllowingUserToLogin();
            }
        });
    }

    private void showSnackbar() {
        Snackbar snackbar = Snackbar.make(findViewById(R.id.facebook_si), "Wanna watch video tutorial for using DonateNow??", Snackbar.LENGTH_LONG);
        //snackbar.getView().setBackgroundColor(Color.);
        snackbar.setAction("Click Here", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.youtube.com/watch?v=bbf54eAPBKI&ab_channel=PRAKASH";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
        snackbar.show();
    }




    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null ){
            sendUserToMainActivity();
        }

    }

    private void AllowingUserToLogin() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter your email ", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter your password ", Toast.LENGTH_SHORT).show();
        }
        else
        {

            loadingBar.setTitle("Login ");
        loadingBar.setMessage("Logging in . . Please wait ! ");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);




            mAuth.signInWithEmailAndPassword(email , password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {



                                if (task.isSuccessful()) {
                                    sendUserToMainActivity();

                                    Toast.makeText(LoginActivity.this, "You are successfully logged in", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                } else {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(LoginActivity.this, "Error Occured " + message, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();

                            }

                        }
                    });
        }
    }

    private void sendUserToAdminActivity() {
        Intent adminIntent = new Intent (LoginActivity.this , AdminMainActivity.class);
        adminIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(adminIntent);
        finish();
    }

    private void sendUserToMainActivity() {
         Intent mainIntent = new Intent (LoginActivity.this , MainActivity.class);
         mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(mainIntent);
         finish();
    }

    private void SendUserToRegisterActivity() {
        Intent registerIntent = new Intent(LoginActivity.this , RegisterActivity.class);
        startActivity(registerIntent);


    }
}
