package com.example.shiran.drhelp.services;

import android.arch.core.util.Function;

import com.example.shiran.drhelp.entities.User;
import com.example.shiran.drhelp.services.queries.UserRegistrationObserver;
import com.example.shiran.drhelp.services.queries.UserService;

public abstract class UserServiceObservable implements UserService {
    private UserRegistrationObserver observer;


    @Override
    public void setUserRegistrationObserver(UserRegistrationObserver observer){
        this.observer = observer;
    }

    protected void publish(Function<UserRegistrationObserver, Void> function){
        function.apply(observer);
    }
}
