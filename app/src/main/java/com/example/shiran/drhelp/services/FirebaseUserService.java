package com.example.shiran.drhelp.services;


import android.app.Activity;
import android.util.Log;

import com.example.shiran.drhelp.entities.RegistrationForm;
import com.example.shiran.drhelp.entities.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUserService extends UserServiceObservable {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    public FirebaseUserService(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void registerUser(final RegistrationForm registrationForm, final Activity activity) {
        firebaseAuth.createUserWithEmailAndPassword(
                registrationForm.getEmail(), registrationForm.getPassword())
                .addOnCompleteListener(activity,
                        task ->onRegistrationComplete(task, registrationForm));
    }

    private void onRegistrationComplete(final Task<AuthResult> task, RegistrationForm registrationForm){
        if(!task.isSuccessful()){
            publish(observer -> {
                observer.onUserRegistrationFailed(task.getException());
                return null;
            });
        } else{
            Log.d("RegisterForm", registrationForm.toString());
            saveUserToFirebaseDatabase(registrationForm);
        }
    }

    private void saveUserToFirebaseDatabase(RegistrationForm registrationForm) {
        String userId = firebaseAuth.getUid();
        User user = new User(
                userId,
                registrationForm.getFirstName(),
                registrationForm.getLastName(),
                registrationForm.getEmail(),
                registrationForm.getPassword());
        if (userId != null){
            Log.d("User1", user.toString());
            databaseReference.child(user.getId()).setValue(user);
            Log.d("User", databaseReference.child(userId).toString());
            publish(observer -> {
                observer.onUserRegistrationSucceed(user);
                return null;
            });
        }
    }

    @Override
    public User loginUser(String email, String password) {
        return null;
    }

    @Override
    public User getUserById(String id) {
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }

    @Override
    public void logout(User user) {

    }
}
