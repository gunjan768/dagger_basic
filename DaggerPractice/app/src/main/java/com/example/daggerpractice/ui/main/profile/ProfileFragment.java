package com.example.daggerpractice.ui.main.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.daggerpractice.R;
import com.example.daggerpractice.models.User;
import com.example.daggerpractice.ui.auth.AuthResource;
import com.example.daggerpractice.viewmodels.ViewModelProviderFactory;

import dagger.android.support.DaggerFragment;

public class ProfileFragment extends DaggerFragment
{
    private static final String TAG = "ProfileFragment";

    private ProfileViewModel viewModel;
    private TextView email, username, website;

    @Inject
    ViewModelProviderFactory providerFactory;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        email = view.findViewById(R.id.email);
        username = view.findViewById(R.id.username);
        website = view.findViewById(R.id.website);

        viewModel = new ViewModelProvider(this, providerFactory).get(ProfileViewModel.class);

        // You can also question that for checking the current authenticated state of the user why are we not following this below process ?
        // Get the associated activity to this fragment and get the sessionManger instance from there to check the current state of the user.
        // We are not following this way of process because it will break the MVVM architecture . It's not a good idea because every time
        // view is created, it's gonna retrieve the data again and again and for some reason if there is no value then getValue() will
        // throw a NullPointer and app gonna crash.
        // AuthResource<User> userAuthResource = ((MainActivity)getActivity()).sessionManager.getAuthUser().getValue();

        // This is the proper way of getting the current state of the user.
        subscribeObservers();
    }

    private void subscribeObservers()
    {
        // For activity we are not required to remove the observer as we did here in the fragment. Why ? Because fragment has their own
        // lifecycle, they can be destroyed and recreated. So for these reasons we need to make sure that we remove the previous observer
        // before assigning the new one. getViewLifecycleOwner() is fairly new and created recently because of this issue.
        viewModel.getAuthenticatedUser().removeObservers(getViewLifecycleOwner());

        viewModel.getAuthenticatedUser().observe(getViewLifecycleOwner(), new Observer<AuthResource<User>>()
        {
            @Override
            public void onChanged(AuthResource<User> userAuthResource)
            {
                if(userAuthResource != null)
                {
                    switch(userAuthResource.status)
                    {
                        case AUTHENTICATED :
                        {
                            assert userAuthResource.data != null;

                            // Log.d(TAG, "onChanged: ProfileFragment: AUTHENTICATED... " + "Authenticated as: " + userAuthResource.data.getEmail());

                            setUserDetails(userAuthResource.data);

                            break;
                        }

                        case ERROR :
                        {
                            // Log.d(TAG, "onChanged: ProfileFragment: ERROR...");

                            setErrorDetails(userAuthResource.message);

                            break;
                        }
                    }
                }
            }
        });
    }

    private void setErrorDetails(String message)
    {
        email.setText(message);
        username.setText("error");
        website.setText("error");
    }

    private void setUserDetails(User user)
    {
        email.setText(user.getEmail());
        username.setText(user.getUsername());
        website.setText(user.getWebsite());
    }
}