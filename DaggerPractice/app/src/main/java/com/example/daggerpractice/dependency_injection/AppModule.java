package com.example.daggerpractice.dependency_injection;

import android.app.Application;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.daggerpractice.R;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.daggerpractice.util.Constants.BASE_URL;

// This is where we are going to put all the application level dependencies of the project like Retrofit, Glide etc which will never change throughout
// the life of the application.
@Module
public class AppModule
{
    // ******************************************************* For Example purpose starts **************************************************************



    // @Provides annotation is used to declare a dependency like here creating a String dependency. Use static as suggested in the documentation.
    @Singleton
    @Provides
    static String someString()
    {
        return "This is me my love as you can remember it";
    }

    // We have an instance ( application ) of an Application available here because when you go to the AppComponent.java, there you are binding the
    // application object using @BindsInstance annotation when the AppComponent is created. Because we have this Application object so we can access
    // this inside the module ( inside this module and we added this module to the AppComponent @Component annotation ).
    @Singleton
    @Provides
    static boolean getApp(Application application)
    {
        return application == null;
    }



    // ******************************************************* For Example purpose ends **************************************************************


    @Singleton
    @Provides
    static Retrofit provideRetrofitInstance()
    {
        return new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    @Singleton
    @Provides
    static RequestOptions provideRequestOptions()
    {
        return RequestOptions.placeholderOf(R.drawable.white_background).error(R.drawable.white_background);
    }

    // As a first parameter we are passing the Application instance and we can access it here as we have bound it in the AppComponent class. As a
    // second parameter to the provideGlideInstance() we are passing an instance of RequestOptions. As RequestOptions is not bound then what dagger
    // will do that it will find it each and every module inside this app. As we have defined it inside this module just above of it.
    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions)
    {
        return Glide.with(application).setDefaultRequestOptions(requestOptions);
    }

    @Singleton
    @Provides
    static Drawable provideAppDrawable(Application application)
    {
        return ContextCompat.getDrawable(application, R.drawable.logo);
    }
}