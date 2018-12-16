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
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText editText_userEmail;
    private Button button_resetPassword;
    private FirebaseAuth firebaseAuth;
    private Intent intent_toLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        initResetPasswordReferences();
        resetMyPassword();
    }

    private void initResetPasswordReferences() {
        editText_userEmail = findViewById(R.id.email_editText_resetPassword);
        button_resetPassword = findViewById(R.id.btn_resetPassword);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void resetMyPassword() {
        button_resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editText_userEmail.getText().toString().trim();
                if(!isValidField(email)){
                    return;
                }
                sendPassword(email);
            }
        });
    }

    private void sendPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(ResetPasswordActivity.this, "Reset Password failed.\n"
                            + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Reset-Password:", "Reset Password failed.");
                } else{
                    Toast.makeText(ResetPasswordActivity.this,
                            "We have sent you instructions to reset your password!",
                            Toast.LENGTH_SHORT).show();
                    intent_toLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent_toLogin);
                    Log.d("Reset-Password:", "Reset Password succeeded.");
                    finish();
                }
            }
        });
    }

    private boolean isValidField(String email) {
        if(TextUtils.isEmpty(email)){
            editText_userEmail.setError("Email Required.");
            return false;
        }
        return true;
    }
}
