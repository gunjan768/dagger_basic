package com.example.daggerpractice.dependency_injection;

import androidx.lifecycle.ViewModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.MapKey;

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@MapKey     // MapKey will help to mark the similar dependencies and group them together.
public @interface ViewModelKey
{
    Class<? extends ViewModel> value();
}