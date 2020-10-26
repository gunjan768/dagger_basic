package com.example.daggerpractice.dependency_injection.main;


import com.example.daggerpractice.ui.main.posts.PostsFragment;
import com.example.daggerpractice.ui.main.profile.ProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

// Contains all the fragments ( here two fragments :  ProfileFragment and PostsFragment ) inside the main component.
@Module
public abstract class MainFragmentBuildersModule
{
    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector
    abstract PostsFragment contributePostsFragment();
}