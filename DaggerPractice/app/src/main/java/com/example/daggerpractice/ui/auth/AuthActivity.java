package com.example.daggerpractice.ui.auth;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.RequestManager;
import com.example.daggerpractice.R;
import com.example.daggerpractice.models.User;
import com.example.daggerpractice.ui.main.MainActivity;
import com.example.daggerpractice.viewmodels.ViewModelProviderFactory;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

// MultiBinding essentially works by defining a key and map certain dependencies to that key and you can use that dependency and inject them in
// different places.

// At it's core, Dagger is a code generator. It generates code that you can reuse in certain compartment of an application. The code it generates is
// written is as such that you can inject it as a dependency. Dependency injection simply refers to the act of using an object(s) as a dependency to
// another object or set of objects. Since we are using @ContributesAndroidInjector annotation ( see ActivityBuilderModule.java class ) so
// we need to extends our AuthActivity by DaggerAppCompatActivity instead of AppCompatActivity.

public class AuthActivity extends DaggerAppCompatActivity implements View.OnClickListener
{
    private static final String TAG = "AuthActivity";
    private AuthViewModel viewModel;
    private EditText userId;
    private ProgressBar progressBar;

    @Inject
    String example;

    @Inject
    boolean isAppNull;

    // It's module class is ViewModelFactoryModule.java. There we are returning the instance of the ViewModelProvider Factory.
    @Inject
    ViewModelProviderFactory providerFactory;

    @Inject
    Drawable logo;

    @Inject
    RequestManager requestManager;      // See AppModule class ( There this dependency is defined ).

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // Log.d(TAG, "onCreate: " + example);
        // Log.d(TAG, "onCreate: Is App NULL : " + isAppNull);

        userId = findViewById(R.id.user_id_input);
        progressBar = findViewById(R.id.progress_bar);

        findViewById(R.id.login_button).setOnClickListener(this);

        viewModel = new ViewModelProvider(this, providerFactory).get(AuthViewModel.class);
        setLogo();

        subscribeObservers();
    }

    private void subscribeObservers()
    {
        viewModel.observeAuthState().observe(this, new Observer<AuthResource<User>>()
        {
            @Override
            public void onChanged(AuthResource<User> userAuthResource)
            {
                if(userAuthResource != null)
                {
                    switch(userAuthResource.status)
                    {
                        case LOADING:
                        {
                            showProgressBar(true);

                            break;
                        }

                        case AUTHENTICATED:
                        {
                            showProgressBar(false);

                            assert userAuthResource.data != null;
                            // Log.d(TAG, "onChanged: LOGIN SUCCESS: " + userAuthResource.data.getEmail());

                            onLoginSuccess();

                            break;
                        }

                        case ERROR:
                        {
                            showProgressBar(false);

                            Toast.makeText(AuthActivity.this, userAuthResource.message + "B/w 1 and 10", Toast.LENGTH_SHORT).show();

                            break;
                        }

                        case NOT_AUTHENTICATED:
                        {
                            showProgressBar(false);

                            break;
                        }
                    }
                }
            }
        });
    }

    private void onLoginSuccess()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    private void showProgressBar(boolean isVisible)
    {
        if(isVisible)
        {
            progressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void setLogo()
    {
        requestManager
            .load(logo)
            .into((ImageView) findViewById(R.id.login_logo));
    }

    private void attemptLogin()
    {
        if(TextUtils.isEmpty(userId.getText().toString()))
        {
            return;
        }

        viewModel.authenticateWithId(Integer.parseInt(userId.getText().toString()));
    }

    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.login_button:

                attemptLogin();

                break;

            default: break;
        }
    }
}