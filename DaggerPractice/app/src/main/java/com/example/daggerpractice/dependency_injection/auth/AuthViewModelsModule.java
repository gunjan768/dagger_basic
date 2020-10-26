package com.example.daggerpractice.dependency_injection.auth;

import com.example.daggerpractice.dependency_injection.ViewModelKey;
import com.example.daggerpractice.ui.auth.AuthViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AuthViewModelsModule
{
    @Binds
    @IntoMap    // Binding to a particular key and this is the multiBinding.
    @ViewModelKey(AuthViewModel.class)
    public abstract ViewModel bindAuthViewModel(AuthViewModel viewModel);
}
