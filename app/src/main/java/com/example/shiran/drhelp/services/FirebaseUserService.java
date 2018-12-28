package com.example.shiran.drhelp.services;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.shiran.drhelp.entities.RegistrationForm;
import com.example.shiran.drhelp.entities.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUserService extends UserServiceObservable {

    /** Singleton **/
    private static FirebaseUserService instance;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    public FirebaseUserService(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
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
}
