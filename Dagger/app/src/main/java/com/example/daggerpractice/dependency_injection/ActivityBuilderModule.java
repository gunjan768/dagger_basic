package com.example.daggerpractice.dependency_injection;

import com.example.daggerpractice.dependency_injection.auth.AuthModule;
import com.example.daggerpractice.dependency_injection.auth.AuthViewModelsModule;
import com.example.daggerpractice.dependency_injection.main.MainFragmentBuildersModule;
import com.example.daggerpractice.dependency_injection.main.MainModule;
import com.example.daggerpractice.dependency_injection.main.MainViewModelsModule;
import com.example.daggerpractice.ui.auth.AuthActivity;
import com.example.daggerpractice.ui.main.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

// Module is the place where dependencies live and you can add them to component. We have to let Dagger to know that AuthActivity is the potential
// client which we can inject dependencies into. ActivityBuilderModule class has to be abstract as there is an abstract method contributeAuthActivity().

// This module is for the activities and stores all their dependencies classes.
@Module
public abstract class ActivityBuilderModule
{
    // As AuthViewModelModule is only going to be scoped to only to AuthComponent and to make sure that we have to explicitly mention the module
    // name in the @ContributesAndroidInjector annotation. Now only the AuthActivity sub component will be able to use this ViewModel. It is
    // where we define the SubComponent. In general we make sub component for organisation and for scoping.
    @ContributesAndroidInjector(
        modules = {
            AuthViewModelsModule.class,
            AuthModule.class,
        }
    )
    abstract AuthActivity contributeAuthActivity();     // AuthActivity subComponent.

    @ContributesAndroidInjector(
        modules = {
            MainFragmentBuildersModule.class,
            MainViewModelsModule.class,
            // Now all the dependencies are accessible inside our MainActivity component (anywhere inside the MainComponent like inside the Profile
            // Fragment or ProfileViewModel).
            MainModule.class,
        }
    )
    abstract MainActivity contributeMainActivity();     // MainActivity subComponent.
}