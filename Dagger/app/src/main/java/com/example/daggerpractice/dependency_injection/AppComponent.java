package com.example.daggerpractice.dependency_injection;

import android.app.Application;

import com.example.daggerpractice.BaseApplication;
import com.example.daggerpractice.SessionManager;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

// @Component will tell the code generator that this will act as a Component class. AndroidInjector will inject the BaseApplication to this component.
// BaseApplication is going to be a client of the AppComponent service. So think this AppComponent as a service and BaseApplication as a client. If
// you use this AndroidInjector class you have to add a module which you have to declare it once. AndroidInjectionModule is the module which we
// always need to use the Dagger. It's an inbuilt module.

// Annotating with annotation Singleton : to let dagger know that a particular thing or object or dependency is persist and kept in memory and in the
// example of application component, the dependencies that exist in the Application component we want them to exist as long as the application is
// alive as that much of time our application component (AppComponent) will exist as it starts when our app starts and destroys when app is closed.

@Singleton      // This will tell dagger that AppComponent owns @Singleton scope.
@Component(
    modules = {
        AndroidInjectionModule.class,
        ActivityBuilderModule.class,
        AppModule.class,
        ViewModelFactoryModule.class,   // Scoped all over the application.
    }
)
public interface AppComponent extends AndroidInjector<BaseApplication>
{
    SessionManager sessionManager();    // As defined here so scoped all over the application.

    // Overwrite the regular Builder class which returns a type of AppComponent.
    @Component.Builder
    interface Builder
    {
        // @BindsInstance is something you can use if you want to bind a particular object to the component at the time of it's construction.
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }
}

// Scopes are a way to give a name to the lifetime of a component and all the dependencies that exist inside the component are scoped to that component.
// It's like you a sort of ownership to a component and it's dependencies. It's like packaging the dependencies. If the parent (component) dies then
// all the dependencies also die. Why we use Scoping ? ANS : It provides efficiency like you create subComponents and you scope those components so that
// dependencies only live till they required and die when their parent component dies. Every component is created for the specific job.