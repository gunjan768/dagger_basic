package com.example.daggerpractice.ui.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

// It is wrapper class that wraps around some type of T ( means any type ), in our case it will be the User class.
public class AuthResource<T>
{
    @NonNull
    public final AuthStatus status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;

    public enum AuthStatus { AUTHENTICATED, ERROR, LOADING, NOT_AUTHENTICATED }

    public AuthResource(@NonNull AuthStatus status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    // AuthResource<T> is the return type of the methods defined below.

    public static <T> AuthResource<T> authenticated (@Nullable T data) {
        return new AuthResource<>(AuthStatus.AUTHENTICATED, data, null);
    }

    public static <T> AuthResource<T> error(@NonNull String msg, @Nullable T data) {
        return new AuthResource<>(AuthStatus.ERROR, data, msg);
    }

    public static <T> AuthResource<T> loading(@Nullable T data) {
        return new AuthResource<>(AuthStatus.LOADING, data, null);
    }

    public static <T> AuthResource<T> logout () {
        return new AuthResource<>(AuthStatus.NOT_AUTHENTICATED, null, null);
    }
}