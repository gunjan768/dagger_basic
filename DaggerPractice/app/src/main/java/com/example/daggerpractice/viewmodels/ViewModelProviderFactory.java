package com.example.daggerpractice.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;

public class ViewModelProviderFactory implements ViewModelProvider.Factory
{
    private static final String TAG = "ViewModelProviderFactor";

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;

    // We are performing the dependency injection and we are injecting the map where the keys are the classes of some ViewModel and the values are the
    // providers of that ViewModel ( ViewModel wrapped in the Provider ). Then from where this dependency comes from ? Ans : The process of binding
    // the ViewModel to the key, this is essentially is creating that dependency, creating that Map behind that scenes, you can't see it.

    // Create a ViewModelProviderFactory, go into the Activity and inject the ViewModelProviderFactory and create an instance of it. Create a ViewModelKey
    // class and map the ViewModel to the key.

    @Inject
    public ViewModelProviderFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators)
    {
        this.creators = creators;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass)
    {
        Provider<? extends ViewModel> creator = creators.get(modelClass);

        // If the ViewModel has not been created.
        if(creator == null)
        {

            // loop through the allowable keys (aka allowed classes with the @ViewModelKey)
            for(Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : creators.entrySet())
            {
                // If it's allowed, set the Provider<ViewModel>.
                if(modelClass.isAssignableFrom(entry.getKey()))
                {
                    creator = entry.getValue();

                    break;
                }
            }
        }

        // If this is not one of the allowed keys, throw exception.
        if(creator == null)
        {
            throw new IllegalArgumentException("unknown model class " + modelClass);
        }

        // Return the Provider.
        try
        {
            return (T) creator.get();
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}