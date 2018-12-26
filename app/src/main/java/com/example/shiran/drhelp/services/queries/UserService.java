package com.example.shiran.drhelp.services.queries;

import android.app.Activity;

import com.example.shiran.drhelp.entities.RegistrationForm;
import com.example.shiran.drhelp.entities.User;

public interface UserService {
    void registerUser(RegistrationForm registrationForm, Activity activity);

    void setUserRegistrationObserver(UserRegistrationObserver userRegistrationObserver);

    User loginUser(String email, String password);

    User getUserById(String id);

    User getUserByEmail(String email);

    void logout(User user);
}
