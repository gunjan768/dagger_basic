package com.example.daggerpractice.ui.auth;

import android.util.Log;

import com.example.daggerpractice.SessionManager;
import com.example.daggerpractice.models.User;
import com.example.daggerpractice.network.auth.AuthApi;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AuthViewModel extends ViewModel
{
    private static final String TAG = "AuthViewModel";

    // inject
    private final SessionManager sessionManager;        // @Singleton scoped dependency
    private final AuthApi authApi;                      // @AuthScope scoped dependency

    @Inject
    public AuthViewModel(AuthApi authApi, SessionManager sessionManager)
    {
        this.sessionManager = sessionManager;
        this.authApi = authApi;

        // forTestPurpose();
    }


    // *********************************************  Authenticate without using SessionManager Starts  **********************************************


    // MediatorLiveData : LiveData subclass which may observe other LiveData objects and react on OnChanged events from them. This class correctly
    // propagates its active/inactive states down to source LiveData objects. Consider the following scenario: we have 2 instances of LiveData,
    // let's name them liveData1 and liveData2, and we want to merge their emissions in one object: liveDataMerger. Then, liveData1 and liveData2
    //  will become sources for the MediatorLiveData liveDataMerger and every time onChanged callback is called for either of them, we set a new
    // value in liveDataMerger.
    private MediatorLiveData<AuthResource<User>> authUser = new MediatorLiveData<>();

    private void forTestPurpose()
    {
        authApi.getUser(1)
            .toObservable()   // Convert observable object to Flowable.
            .subscribeOn(Schedulers.io())   // Subscribe on background thread.
            .subscribe(new Observer<User>()
            {
                @Override
                public void onSubscribe(@NonNull Disposable d) {

                }

                @Override
                public void onNext(@NonNull User user) {

                }

                @Override
                public void onError(@NonNull Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
    }

    public void authenticateWithID(int userId)
    {
        authUser.setValue(AuthResource.loading((User)null));

        // We are creating a liveData source, the source is gonna be api call from the Flowable object. This is the converting of Flowable object to
        // LiveData object using LiveDataReactiveStreams.
        final LiveData<AuthResource<User>> source = LiveDataReactiveStreams.fromPublisher(authApi.getUser(userId)
            .onErrorReturn(new Function<Throwable, User>()      // Instead of calling onError ( Error happens ).
            {
                @Override
                public User apply(Throwable throwable) throws Throwable
                {
                    // Whatever returned from here will be passed to the map() method defined just below it.
                    User errorUser = new User();
                    errorUser.setId(-1);

                    return errorUser;
                }
            })
            .map(new Function<User, AuthResource<User>>()
            {
                @Override
                public AuthResource<User> apply(User user) throws Throwable
                {
                    if(user.getId() == -1)
                    {
                        return AuthResource.error("Could not authenticate", null);
                    }

                    return AuthResource.authenticated(user);
                }
            })
            .subscribeOn(Schedulers.io())
        );

        authUser.addSource(source, new androidx.lifecycle.Observer<AuthResource<User>>()
        {
            @Override
            public void onChanged(AuthResource<User> user)
            {
                authUser.setValue(user);
                authUser.removeSource(source);
            }
        });
    }

    public LiveData<AuthResource<User>> observeUser()
    {
        return authUser;
    }



    // **********************************************  Authenticate without using SessionManager Ends  **********************************************




    // *********************************************  Authenticate with using SessionManager Starts  ************************************************


    public LiveData<AuthResource<User>> observeAuthState()
    {
        return sessionManager.getAuthUser();
    }

    public void authenticateWithId(int userId)
    {
        sessionManager.authenticateWithId(queryUserId(userId));
    }

    private LiveData<AuthResource<User>> queryUserId(int userId)
    {
        // We are creating a liveData source, the source is gonna be api call from the Flowable object. This is the converting of Flowable object to
        // LiveData object using LiveDataReactiveStreams.
        return LiveDataReactiveStreams.fromPublisher(authApi.getUser(userId)
            .onErrorReturn(new Function<Throwable, User>()      // instead of calling onError, do this
            {
                @Override
                public User apply(Throwable throwable) throws Exception
                {
                    User errorUser = new User();
                    errorUser.setId(-1);

                    return errorUser;
                }
            })
            .map(new Function<User, AuthResource<User>>()        // wrap User object in AuthResource
            {
                @Override
                public AuthResource<User> apply(User user) throws Exception
                {
                    if(user.getId() == -1)
                    {
                        return AuthResource.error("Could not authenticate", null);
                    }

                    return AuthResource.authenticated(user);
                }
            })
            .subscribeOn(Schedulers.io()));      // Subscribe on the background thread.
    }


    // **********************************************  Authenticate with using SessionManager Ends  ************************************************
}