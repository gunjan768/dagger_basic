package com.example.daggerpractice.dependency_injection.auth;

import com.example.daggerpractice.models.User;
import com.example.daggerpractice.network.auth.AuthApi;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

// AuthModule is the module where we are going to add all the dependencies for the AuthActivity sub component.
@Module
public class AuthModule
{
    @AuthScope
    @Provides
    @Named("auth_user")
    static User someUser(){
        return new User();
    }

    // We can access this Retrofit instance inside this AuthModule because this AuthModule is a module which is defined inside the AuthActivity Component
    // ( See ActivityBuilderModule.java class ) which is in return is the subComponent of the AppComponent. And inside the AppComponent, Retrofit instance
    // has been defined.
    @AuthScope
    @Provides
    static AuthApi provideAuthApi(Retrofit retrofit){
        return retrofit.create(AuthApi.class);
    }
}