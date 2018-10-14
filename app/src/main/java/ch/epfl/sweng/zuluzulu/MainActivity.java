package ch.epfl.sweng.zuluzulu;

import android.content.Intent;
import android.net.Uri;
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

import com.google.firebase.FirebaseApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Fragments.AboutZuluzuluFragment;
import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.Fragments.LoginFragment;
import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SettingsFragment;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.tequila.AuthClient;
import ch.epfl.sweng.zuluzulu.tequila.OAuth2Config;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private User user;
    private Map<String, String> tokens;
    private OAuth2Config config;


    //(temporary) store the URI from the browser
    private String redirectURIwithCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(getApplicationContext());

        // Needed to use Firebase storage and Firestore
        FirebaseApp.initializeApp(getApplicationContext());

        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);

        // Initial to guestUser
        this.user = new User.UserBuilder().buildGuestUser();

        navigationView = initNavigationView();
        initDrawerContent();

        // The first seen fragment is the main fragment

        Intent i = getIntent();
        if(Intent.ACTION_VIEW.equals(i.getAction())){
            //get the redirectURI with the code from the intent
            redirectURIwithCode = i.getDataString();
            selectItem(navigationView.getMenu().findItem(R.id.nav_login));
        } else {
            selectItem(navigationView.getMenu().findItem(R.id.nav_main));
        }
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

    /**
     * Create the navigation view and the toolbar
     *
     * @return The navigation view
     */
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

    /**
     * Attach the drawer_view to the navigation view and set a listener on the menu
     */
    private void initDrawerContent() {
        updateMenuItems();
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        if (!menuItem.isChecked()) {
                            selectItem(menuItem);
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                }
        );
    }

    /**
     * Attach the drawer_view to the navigation view
     */
    private void updateMenuItems() {
        if (isAuthenticated()) {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_view_user);
        } else {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_view_guest);
        }
    }

    public boolean isAuthenticated() {
        return user.isConnected();
    }

    /**
     * Create a new fragment and replace it in the activity
     *
     * @param menuItem The item that corresponds to a fragment on the menu
     */
    private void selectItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        boolean isLogin = false;
        switch (menuItem.getItemId()) {
            case R.id.nav_main:
                fragmentClass = MainFragment.class;
                break;
            case R.id.nav_login:
                fragmentClass = LoginFragment.class;
                //to set arguments for the login
                //////////////////////////
                isLogin = true;
                //////////////////////////
                break;
            case R.id.nav_about:
                fragmentClass = AboutZuluzuluFragment.class;
                break;
            case R.id.nav_associations:
                fragmentClass = AssociationFragment.class;
                break;
            case R.id.nav_settings:
                fragmentClass = SettingsFragment.class;
                break;
            case R.id.nav_logout:
                this.user = new User.UserBuilder().buildGuestUser();

                //create a logout URL and open it in the browser
                String logoutURL = AuthClient.createCodeRequestUrlLogout(config, tokens.get("Tequila.profile"));
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(logoutURL));
                startActivity(browserIntent);


                tokens.clear();
                redirectURIwithCode = null;
                updateMenuItems();
                fragmentClass = MainFragment.class;
                menuItem.setTitle(navigationView.getMenu().findItem(R.id.nav_main).getTitle());
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
            //if is a login fragment then set argument with the URI
            ///////////////////////////////////////////////////////////
            if(isLogin){
                isLogin = false;
                Bundle toSend = new Bundle(1);
                toSend.putString("",redirectURIwithCode);
                fragment.setArguments(toSend);
            }
            //////////////////////////////////////////////////////////

            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContent, fragment).commit();
                menuItem.setChecked(true);
                setTitle(menuItem.getTitle());
            }
        }

    }

    @Override
    public void onFragmentInteraction(String tag, Object data) {
        switch(tag) {
            case LoginFragment.TAG:
                Map<Integer, Object> received = (HashMap<Integer,Object>) data;
                this.user = (User) received.get(0);
                this.tokens = (Map<String, String>) received.get(1);
                this.config = (OAuth2Config) received.get(2);
                updateMenuItems();
                selectItem(navigationView.getMenu().findItem(R.id.nav_main));
                break;
            default:
                // Should never happen
                throw new AssertionError("Invalid message");
        }
    }

    public User getUser() {
        return user;
    }
}
