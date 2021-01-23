package com.example.daggerpractice.dependency_injection.main;

import com.example.daggerpractice.network.main.MainApi;
import com.example.daggerpractice.ui.main.posts.PostsRecyclerAdapter;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

// Contains the dependencies that only exist in the MainActivity component and cannot be accessed outside it like in AuthActivity component.
@Module
public class MainModule
{
//    @MainScope
    @Provides
    static PostsRecyclerAdapter provideAdapter()
    {
        return new PostsRecyclerAdapter();
    }

//    @MainScope
    @Provides
    static MainApi provideMainApi(Retrofit retrofit)
    {
        return retrofit.create(MainApi.class);
    }
}