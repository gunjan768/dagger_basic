package com.example.daggerpractice;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.example.daggerpractice.models.User;
import com.example.daggerpractice.ui.auth.AuthResource;

@Singleton
public class SessionManager
{
    // ****************************************************** About Mediator Live Data ***************************************************************


    // Consider the following scenario: we have 2 instances of LiveData, let's name them liveData1 and liveData2, and we want to merge their emissions
    // in one object: liveDataMerger. Then, liveData1 and liveData2 will become sources for the MediatorLiveData liveDataMerger and every time onChanged
    // callback is called for either of them, we set a new value in liveDataMerger.
    // LiveData  liveData1 = ...;
    // LiveData  liveData2 = ...;

    // MediatorLiveData  liveDataMerger = new MediatorLiveData<>();
    // liveDataMerger.addSource(liveData1, value -> liveDataMerger.setValue(value));
    // liveDataMerger.addSource(liveData2, value -> liveDataMerger.setValue(value));

    // Let's consider that we only want 10 values emitted by liveData1, to be merged in the liveDataMerger. Then, after 10 values, we can stop
    // listening to liveData1 and remove it as a source.

//    liveDataMerger.addSource(liveData1, new Observer ()
//    {
//        private int count = 1;
//
//        @Override
//        public void onChanged(@Nullable Integer s) {
//            count++;
//            liveDataMerger.setValue(s);
//            if (count > 10) {
//                liveDataMerger.removeSource(liveData1);
//            }
//        }
//    });


    // *************************************************************************************************************************************************


    private static final String TAG = "DaggerExample";

    // data
    private final MediatorLiveData<AuthResource<User>> cachedUser = new MediatorLiveData<>();

    @Inject
    public SessionManager()
    {

    }

    public void authenticateWithId(final LiveData<AuthResource<User>> source)
    {
        if(cachedUser != null)
        {
            cachedUser.setValue(AuthResource.loading((User)null));

            cachedUser.addSource(source, new Observer<AuthResource<User>>()
            {
                @Override
                public void onChanged(AuthResource<User> userAuthResource)
                {
                    cachedUser.setValue(userAuthResource);
                    cachedUser.removeSource(source);

                    if(userAuthResource.status.equals(AuthResource.AuthStatus.ERROR))
                    {
                        cachedUser.setValue(AuthResource.<User>logout());
                    }
                }
            });
        }
    }

    public void logOut()
    {
        // Log.d(TAG, "logOut: logging out...");

        cachedUser.setValue(AuthResource.<User>logout());
    }


    public LiveData<AuthResource<User>> getAuthUser(){
        return cachedUser;
    }
}