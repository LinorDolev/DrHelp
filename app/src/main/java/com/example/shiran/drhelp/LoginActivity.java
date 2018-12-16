package com.example.shiran.drhelp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

    private EditText editText_userEmail;
    private EditText editText_userPassword;
    private TextView textView_forgotMyPassword;
    private TextView textView_toRegister;
    private Button button_login;

    private Intent intent_toRegister;
    private Intent intent_toMember;
    private Intent intent_toResetPassword;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initLoginReferences();
        needToRegister();
        login();
        forgotMyPassword();
    }

    private void initLoginReferences() {
        editText_userEmail = findViewById(R.id.email_editText_login);
        editText_userPassword = findViewById(R.id.password_editText_login);
        textView_forgotMyPassword = findViewById(R.id.link_forgot_password);
        textView_toRegister = findViewById(R.id.link_to_register);
        button_login = findViewById(R.id.btn_login);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void needToRegister() {
        textView_toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent_toRegister = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent_toRegister);
            }
        });
    }

    private void login() {
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editText_userEmail.getText().toString().trim();
                String password = editText_userPassword.getText().toString().trim();

                if (!isValidForm(email, password))
                    return;

                userAuthentication(email, password);
            }
        });
    }

    private void userAuthentication(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Authentication failed.\n"
                                    + "check your email and password or sign up"
                                    , Toast.LENGTH_SHORT).show();
                            Log.d("login:", "Authentication failed.");
                        } else{
                            intent_toMember = new Intent(getApplicationContext(), MemberActivity.class);
                            startActivity(intent_toMember);
                            Log.d("login:", "Authentication succeeded.");
                            finish();
                        }
                    }
                });
    }

    private boolean isValidForm(String email, String password) {
        if(TextUtils.isEmpty(email)){
            editText_userEmail.setError("Email Required.");
            return false;
        }
        if(TextUtils.isEmpty(password)){
            editText_userPassword.setError("Password Required.");
            return false;
        }
        return true;
    }

    private void forgotMyPassword() {
        textView_forgotMyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent_toResetPassword = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivity(intent_toResetPassword);
            }
        });

    }
}
