package com.example.daggerpractice.dependency_injection;

import com.example.daggerpractice.viewmodels.ViewModelProviderFactory;

import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;

// This is going to be responsible for generating the dependency during the dependency injection for the ViewModelProviderFactory class. It is
// responsible for providing the dependency for the ViewModelProviderFactory.
@Module
public abstract class ViewModelFactoryModule
{
    // This will return the instance of ViewModelProviderFactory class. You could have done this using @Providers annotation also.
    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory viewModelFactory);
}