package com.example.shiran.drhelp.services.queries;

import com.example.shiran.drhelp.entities.User;

public interface UserRegistrationObserver {
    void onUserRegistrationSucceed(User user);

    void onUserRegistrationFailed(Exception exception);

}
