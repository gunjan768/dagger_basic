package com.example.daggerpractice.ui.main.profile;

import android.util.Log;
import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.daggerpractice.SessionManager;
import com.example.daggerpractice.models.User;
import com.example.daggerpractice.ui.auth.AuthResource;

public class ProfileViewModel extends ViewModel
{
    private static final String TAG = "DaggerExample";

    private final SessionManager sessionManager;

    @Inject
    public ProfileViewModel(SessionManager sessionManager)
    {
        this.sessionManager = sessionManager;
    }

    public LiveData<AuthResource<User>> getAuthenticatedUser()
    {
        return sessionManager.getAuthUser();
    }
}