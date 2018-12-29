package com.example.shiran.drhelp.services;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.shiran.drhelp.entities.RegistrationForm;
import com.example.shiran.drhelp.entities.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseUserService extends UserServiceObservable {

    /** Singleton **/
    private static FirebaseUserService instance;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;

    public FirebaseUserService(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserId = firebaseAuth.getCurrentUser().getUid();
    }

    /** Singleton **/
    public static UserService getInstance(){
        if(instance == null){
            instance = new FirebaseUserService();
        }
        return instance;
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
            publishAboutRegistration(observer -> {
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
            publishAboutRegistration(observer -> {
                observer.onUserRegistrationSucceed(user);
                return null;
            });
        }
    }

    @Override
    public void loginUser(String email, String password, Activity activity) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, this::onLogginInCompleted);
    }

    private void onLogginInCompleted(@NonNull Task<AuthResult> task) {
        if(!task.isSuccessful()){
            publishAboutLoggingIn(observer -> {observer.onLoginFailed();
            return null;
            });
        } else {
            publishAboutLoggingIn(observer -> {observer.onLoginSucceed();
            return null;
            });
        }
    }

    @Override
    public void resetPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this::onResetPasswordCompleted);
    }

    private void onResetPasswordCompleted(@NonNull Task<Void> task) {
        if(!task.isSuccessful()){
            publishAboutResetPassword(observer -> {observer.onResetPasswordFailed();
                return null;
            });
        } else {
            publishAboutResetPassword(observer -> {observer.onResetPasswordSucceed();
                return null;
            });
        }
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

    @Override
    public String getUserStatuse() {
        return
                databaseReference.child(currentUserId).child("available").getKey();
    }

    @Override
    public void setUserStatuse(boolean available) {

        databaseReference.child(currentUserId).child("available").setValue(available);
    }

    @Override
    public int getNumberOfUsers() {
        return
                databaseReference.child("users").getKey().length();
    }

    @Override
    public List<User> getAllAvailableUsers(String id) {
        List<User> userList = new ArrayList<User>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    if(!user.getId().equals(id)){
                        Log.d("current user", "id = " + id.toString());
                        if(user.getAvailable()){
                            userList.add(user);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return userList;
    }

    @Override
    public String getCurrentUserId() {
        return
                firebaseAuth.getCurrentUser().getUid();
    }
}
