package com.example.shiran.drhelp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.example.shiran.drhelp.services.queries.UserRegistrationObserver;
import com.example.shiran.drhelp.services.queries.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements UserRegistrationObserver {

    private TextInputEditText editText_userFirstName;
    private TextInputEditText editText_userLastName;
    private TextInputEditText editText_userEmail;
    private TextInputEditText editText_userPassword;
    private MaterialButton button_Register;
    private Intent intent_toLogin;

    //--database--//
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private UserService userService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initRegistrationReferences();
        //initDatabaseComponents();
        userService = new FirebaseUserService();
        button_Register.setOnClickListener(this::onRegistrationButtonClicked);
        //registerUser();

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

    private void initDatabaseComponents() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
    }

   /* private void registerUser(){
        button_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = editText_userFirstName.getText().toString().trim();
                String lastName = editText_userLastName.getText().toString().trim();
                String email = editText_userEmail.getText().toString().trim();
                String password = editText_userPassword.getText().toString().trim();

                if(!isValidForm(firstName, lastName, email, password)){
                    Log.d("register-user:", "invalid form.");
                    return;
                }

                Log.d("register-user:", "valid form.");
                createUser(firstName, lastName, email, password);
            }
        });
    }*/

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

    /*private void createUser(final String firstName, final String lastName, final String email, final String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Authentication failed."
                            + task.getException(), Toast.LENGTH_SHORT).show();
                    Log.d("create-user:", "Authentication failed.");
                } else {
                    String userId  = firebaseAuth.getUid();
                    User user = new User(userId, firstName, lastName, email, password);
                    databaseReference.child(userId).setValue(user);
                    Toast.makeText(RegisterActivity.this, "Registration succeeded."
                            , Toast.LENGTH_SHORT).show();

                    intent_toLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent_toLogin);
                    finish();
                    Log.d("create-user:", "create user succeeded.");
                }
            }
        });
    }*/

    @Override
    public void onUserRegistrationSucceed(User user) {
        Toast.makeText(RegisterActivity.this, "Registration succeeded."
                , Toast.LENGTH_SHORT).show();
        intent_toLogin = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent_toLogin);
        finish();
        Log.d("create-user:", "create user succeeded.");
    }

    @Override
    public void onUserRegistrationFailed(Exception exception) {
        Toast.makeText(RegisterActivity.this, "Authentication failed."
                + exception, Toast.LENGTH_SHORT).show();
        Log.d("create-user:", "Authentication failed.");
    }
}
