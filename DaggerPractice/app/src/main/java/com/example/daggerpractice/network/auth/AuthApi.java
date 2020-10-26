package com.example.daggerpractice.network.auth;

import com.example.daggerpractice.models.User;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface AuthApi
{
    // @GET
    // Call<ResponseBody> getFakeStuff();

    // Instead of Call object, we will return the Flowable object which is RxJava object which makes very easy to make the network request with retrofit.
    // That is we do not need to do any threading, AsyncTask etc all are taken care by the RxJava. Another benefit of using Flowable object is that we
    // can take it one step further and convert it in the LiveData object inside ViewModel to make MVVM easy.
    @GET("users/{id}")
    Flowable<User> getUser(@Path("id") int id);
}