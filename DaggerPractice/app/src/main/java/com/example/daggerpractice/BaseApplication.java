package com.example.daggerpractice;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

// This is the application level class. Normally we extends Application but since we are using Dagger Android dependency ( not the Dagger core dependency )
// so we use DaggerApplication class. We can say that BaseApplication is a client and AppComponent is a server ( or a service ).
public class BaseApplication extends DaggerApplication
{
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector()
    {
        // application() is the method of the AppComponent interface : Builder application(Application application);
        return DaggerAppComponent.builder().application(this).build();
    }
}