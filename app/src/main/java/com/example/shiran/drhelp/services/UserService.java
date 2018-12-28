package com.example.shiran.drhelp.services;

import android.app.Activity;

import com.example.shiran.drhelp.entities.RegistrationForm;
import com.example.shiran.drhelp.entities.User;
import com.example.shiran.drhelp.services.observers.UserLoginObserver;
import com.example.shiran.drhelp.services.observers.UserRegistrationObserver;
import com.example.shiran.drhelp.services.observers.UserResetPasswordObserver;

public interface UserService {
    void registerUser(RegistrationForm registrationForm, Activity activity);

    void setUserRegistrationObserver(UserRegistrationObserver userRegistrationObserver);

    void loginUser(String email, String password, Activity activity);

    void setUserLoginObserver(UserLoginObserver userLoginObserver);

    void resetPassword(String email);

    void logout(User user);

    void setUserResetPasswordObserver(UserResetPasswordObserver userResetPasswordObserver);

    User getUserById(String id);

    User getUserByEmail(String email);


}
