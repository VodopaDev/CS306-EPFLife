package ch.epfl.sweng.zuluzulu;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import ch.epfl.sweng.zuluzulu.Fragments.AboutZuluzuluFragment;
import ch.epfl.sweng.zuluzulu.Fragments.LoginFragment;
import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean isAuthenticated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);

        // Just a boolean for the moment, need to update this by the user profile later
        isAuthenticated = false;

        navigationView = initNavigationView();
        initDrawerContent();

        // The first seen fragment is the main fragment
        selectItem(navigationView.getMenu().getItem(0));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private NavigationView initNavigationView() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        return navigationView;
    }

    private void initDrawerContent() {
        updateMenuItems();

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectItem(menuItem);
                        return true;
                    }
                }
        );
    }

    private void updateMenuItems() {
        if (isAuthenticated) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_view_user);
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_view_guest);
        }
    }

    // Just methods to test the change of the menu
    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
        updateMenuItems();
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    private void selectItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch (menuItem.getItemId()) {
            case R.id.nav_main:
                fragmentClass = MainFragment.class;
                break;
            case R.id.nav_login:
                fragmentClass = LoginFragment.class;
                break;
            case R.id.nav_about:
                fragmentClass = AboutZuluzuluFragment.class;
                break;
            default:
                fragmentClass = MainFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContent, fragment).commit();
                menuItem.setChecked(true);
                setTitle(menuItem.getTitle());
            }
        }

        drawerLayout.closeDrawers();
    }

    @Override
    public void onFragmentInteraction(String tag, Object data) {
        switch(tag) {
            case MainFragment.TAG:
                // You get something from the main fragment
                break;
            case LoginFragment.TAG:
                // You get something from the login fragment
                break;
            default:
                // Should never happen
        }
    }
}
