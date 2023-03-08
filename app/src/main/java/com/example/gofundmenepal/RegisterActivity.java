package com.example.gofundmenepal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText UserEmail ,UserPassword , UserConfirmPassword ;
    private Button createButton ;
    private FirebaseAuth mAuth ;
    private ProgressDialog loadingBar ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText) findViewById(R.id.register_password);
        UserConfirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        createButton = (Button) findViewById(R.id.Register_button);

        loadingBar = new ProgressDialog(this);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) 
            {
                createAccountActivity();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null ){
            SendUserToMainActivity();
        }
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent (RegisterActivity.this , MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void createAccountActivity()
    {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        String confirmpassword = UserConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter your Email ", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter a password ", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmpassword)){
            Toast.makeText(this, " Please write a valid password ", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmpassword) ){
            Toast.makeText(this, "Your password didnot match with your confirm password", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("You are one step away from creating account ");
            loadingBar.setMessage("Please wait,we are creating your new account ! ");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            mAuth.createUserWithEmailAndPassword( email , password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                sendUserToSetupActivity();
                                Toast.makeText(RegisterActivity.this, " Account Authenticated Successfully! ", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(RegisterActivity.this, "Error Occured ! " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }

                            
                        }
                    });
        }

    }

    private void sendUserToSetupActivity() {
       Intent setupIntent = new  Intent (RegisterActivity.this , SetupActivity.class );
       setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
       startActivity(setupIntent);
       finish();

    }
}
