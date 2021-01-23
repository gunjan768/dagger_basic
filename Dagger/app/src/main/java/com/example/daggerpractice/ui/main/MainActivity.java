package com.example.daggerpractice.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.daggerpractice.BaseActivity;
import com.example.daggerpractice.R;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

// Remember that it extends BaseActivity.
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;

    // Navigation Component library works by creating a Navigation Graph (See res --> navigation). Navigation Graph is a layout file (i.e .xml)
    // which contains all the Fragment names which are used (previously we do using FrameLayout and replace it with Fragment in the Java file
    // dynamically). But here we are defining them in the layout file itself statically. Even which fragment has to be shown when the activity
    // is created is also defined in the layout file only.
    private NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        init();
    }

    private void init()
    {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        NavController navController = NavHostFragment.findNavController(fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.logout :
            {
                sessionManager.logOut();

                return true;
            }

            // android.R.id.home will reference the back arrow.
            case android.R.id.home :
            {
                if(drawerLayout.isDrawerOpen(GravityCompat.START))
                {
                    drawerLayout.closeDrawer(GravityCompat.START);

                    return true;
                }

                return false;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // NavigationView.OnNavigationItemSelectedListener.
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        switch (menuItem.getItemId())
        {
            case R.id.nav_profile :
            {
                // You can define the backStack in the XML file but here we will do it programmatically. setPopUpTo() will clear the backStack if
                // "inclusive" is set to true.
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.main, true)
                        .build();

                // If we use the old way of displaying the fragment using getSupportFragmentManager() ..... we have to manage everything like
                // backStack etc. But in this new way we don't need to do anything as everything is taken care. We first find the nav controller
                // (i.e. FragmentContainer) using ID and then we will search for the id associated with the fragment which we want to display
                // (R.id.nav_host_fragment). We will search the id of the fragment in the Navigation Graph (res --> navigation).
                Navigation.findNavController(this, R.id.nav_host_fragment).navigate(
                        R.id.profileScreen,
                        null,
                        navOptions
                );

                break;
            }

            case R.id.nav_posts :
            {
                // Will check the condition : will only navigate to the postFragment if we are not currently present in the same postFragment.
                if(isValidDestination(R.id.postsScreen))
                {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.postsScreen);
                }

                break;
            }
        }

        menuItem.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private boolean isValidDestination(int destination)
    {
        return (
            destination != Objects.requireNonNull(
                Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination()
            ).getId()
        );
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        // Whenever you navigate up, it reference the navigation controller and drawer layout and do whatever it provides. It provides a couple
        // of things : when you click on Hamburger it will open it (but not close it), enables the back arrow.
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), drawerLayout);
    }
}