package com.example.shiran.drhelp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.shiran.drhelp.R;
import com.example.shiran.drhelp.entities.RegistrationForm;
import com.example.shiran.drhelp.entities.User;
import com.example.shiran.drhelp.services.FirebaseUserService;
import com.example.shiran.drhelp.services.observers.UserRegistrationObserver;
import com.example.shiran.drhelp.services.UserService;

public class RegistrationActivity extends AppCompatActivity implements UserRegistrationObserver {

    private TextInputEditText editText_userFirstName;
    private TextInputEditText editText_userLastName;
    private TextInputEditText editText_userEmail;
    private TextInputEditText editText_userPassword;
    private MaterialButton button_Register;
    private Intent intent_toLogin;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        initRegistrationReferences();
        userService = new FirebaseUserService();
        userService.setUserRegistrationObserver(this);
        button_Register.setOnClickListener(this::onRegistrationButtonClicked);
    }

    private void onRegistrationButtonClicked(View view) {
        String firstName = editText_userFirstName.getText().toString().trim();
        String lastName = editText_userLastName.getText().toString().trim();
        String email = editText_userEmail.getText().toString().trim();
        String password = editText_userPassword.getText().toString().trim();

        RegistrationForm registrationForm = new RegistrationForm(
                firstName, lastName, email, password);

        if(!isValidForm(registrationForm)){
            Log.d("register-user:", "invalid form.");
            return;
        }
        Log.d("register-user:", "valid form.");
        userService.registerUser(registrationForm, this);
    }

    private void initRegistrationReferences(){
        editText_userFirstName = findViewById(R.id.firstName_editText_register);
        editText_userLastName = findViewById(R.id.lastName_editText_register);
        editText_userEmail = findViewById(R.id.email_editText_register);
        editText_userPassword = findViewById(R.id.password_editText_register);
        button_Register = findViewById(R.id.btn_register);
    }

    private boolean isValidForm(RegistrationForm registrationForm) {

        if(TextUtils.isEmpty(registrationForm.getFirstName())){
            editText_userFirstName.setError("First Name Required.");
            return false;
        }
        if(TextUtils.isEmpty(registrationForm.getLastName())){
            editText_userLastName.setError("Last Name Required.");
            return false;
        }
        if(TextUtils.isEmpty(registrationForm.getEmail())){
            editText_userEmail.setError("Email Required.");
            return false;
        }
        if(TextUtils.isEmpty(registrationForm.getPassword())){
            editText_userPassword.setError("Password Required.");
            return false;
        }

        return true;
    }

    public void onUserRegistrationSucceed(User user) {
        Toast.makeText(RegistrationActivity.this, "Registration succeeded."
                , Toast.LENGTH_SHORT).show();
        intent_toLogin = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent_toLogin);
        finish();
        Log.d("create-user:", "create user succeeded.");
    }

    @Override
    public void onUserRegistrationFailed(Exception exception) {
        Toast.makeText(RegistrationActivity.this, "Authentication failed."
                + exception, Toast.LENGTH_SHORT).show();
        Log.d("create-user:", "Authentication failed.");
    }
}
