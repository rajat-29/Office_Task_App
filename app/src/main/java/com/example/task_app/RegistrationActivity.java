package com.example.task_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class RegistrationActivity extends AppCompatActivity {

    private EditText reqId;
    private EditText pass;
    private EditText conpass;
    private Button btnReg;
    private TextView loginTxt;
    private FirebaseAuth mAuth;
    private ProgressDialog mDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        reqId = findViewById(R.id.enter_id);
        pass = findViewById(R.id.enter_password1);
        conpass = findViewById(R.id.enter_password2);
        btnReg = findViewById(R.id.signup_btn);
        loginTxt = findViewById(R.id.login_txt);
        mAuth = FirebaseAuth.getInstance();
        mDailog = new ProgressDialog(this);

        loginTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userEmail = reqId.getText().toString().trim();
                String pass1 = pass.getText().toString().trim();
                String pass2 = conpass.getText().toString().trim();

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

                if(TextUtils.isEmpty(pass2))
                {
                    conpass.setError("Required...");
                    return;
                }

                if(!pass1.equals(pass2)) {
                    conpass.setError("Password not Matches..");
                    return;
                }

                mDailog.setMessage("Processing...");
                mDailog.show();

                mAuth.createUserWithEmailAndPassword(userEmail,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"Successfully Registered",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            mDailog.dismiss();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Problem in Registration",Toast.LENGTH_LONG).show();
                            mDailog.dismiss();
                        }

                    }
                });
            }
        });

    }
}
