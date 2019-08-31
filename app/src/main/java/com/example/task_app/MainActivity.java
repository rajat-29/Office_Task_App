package com.example.task_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {

    private TextView signup;
    private EditText reqId;
    private EditText pass;
    private Button btnLogin;
    private FirebaseAuth mAuth;
    private ProgressDialog mDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signup = findViewById(R.id.signup_txt);
        reqId = findViewById(R.id.email_login);
        pass = findViewById(R.id.password_login);
        btnLogin = findViewById(R.id.login_btn);
        mAuth = FirebaseAuth.getInstance();
        mDailog = new ProgressDialog(this);

        if(mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),RegistrationActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEmail = reqId.getText().toString().trim();
                String pass1 = pass.getText().toString().trim();

                if(TextUtils.isEmpty(userEmail))
                {
                    reqId.setError("Required...");
                    return;
                }
                else if(!userEmail.matches("^(.+)@(.+)\\.(.+)")) {
                    reqId.setError("Please enter a valid email id");
                    return;
                }

                if(TextUtils.isEmpty(pass1))
                {
                    pass.setError("Required...");
                    return;
                }

                mDailog.setMessage("Processing...");
                mDailog.show();

                mAuth.signInWithEmailAndPassword(userEmail,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Successfully LogIn",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            mDailog.dismiss();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Error In LogIn",Toast.LENGTH_LONG).show();
                            mDailog.dismiss();
                        }

                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mAuth.getCurrentUser() == null) {
            ActivityCompat.finishAffinity(MainActivity.this);
        } else {
            super.onBackPressed();
        }
    }
}