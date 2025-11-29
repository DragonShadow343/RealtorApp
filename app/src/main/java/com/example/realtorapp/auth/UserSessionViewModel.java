package com.example.realtorapp.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.realtorapp.model.User;

public class UserSessionViewModel {

    private MutableLiveData<User> currentUser = new MutableLiveData<>();

    public LiveData<User> getUser() {
        return currentUser;
    }

    public void setUser(User user) {
        currentUser.postValue(user);
    }

    public void logout() {
        currentUser.postValue(null);
    }

    public boolean isLoggedIn() {
        return currentUser.getValue() != null;
    }
}
