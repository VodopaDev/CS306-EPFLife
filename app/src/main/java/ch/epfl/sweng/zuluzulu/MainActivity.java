package ch.epfl.sweng.zuluzulu;

import android.content.Intent;
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

import ch.epfl.sweng.zuluzulu.Fragments.AboutZuluzuluFragment;
import ch.epfl.sweng.zuluzulu.Fragments.AssociationDetailFragment;
import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ChannelFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ChatFragment;
import ch.epfl.sweng.zuluzulu.Fragments.LoginFragment;
import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ProfileFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SettingsFragment;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.User;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ArrayList<Fragment> previous_fragments;
    private Fragment current_fragment;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Needed to use Firebase storage and Firestore
        FirebaseApp.initializeApp(getApplicationContext());

        previous_fragments = new ArrayList<>();
        previous_fragments.add(null);

        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);

        // Initial to guestUser
        this.user = new User.UserBuilder().buildGuestUser();

        navigationView = initNavigationView();
        initDrawerContent();


        Intent i = getIntent();
        if (Intent.ACTION_VIEW.equals(i.getAction())) {
            selectItem(navigationView.getMenu().findItem(R.id.nav_login));
        } else {
            // Look if there is a user object set
            User user = (User) i.getSerializableExtra("user");
            if (user != null) {
                this.user = user;
            }
            
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

    /**
     * Return true if the user is connected
     *
     * @return boolean
     */
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
        switch (menuItem.getItemId()) {
            case R.id.nav_main:
                fragment = MainFragment.newInstance(user);
                break;
            case R.id.nav_login:
                fragment = LoginFragment.newInstance();
                break;
            case R.id.nav_about:
                fragment = AboutZuluzuluFragment.newInstance();
                break;
            case R.id.nav_associations:
                fragment = AssociationFragment.newInstance(user);
                break;
            case R.id.nav_settings:
                fragment = SettingsFragment.newInstance();
                break;
            case R.id.nav_profile:
                fragment = ProfileFragment.newInstance(user);
                break;
            case R.id.nav_logout:
                this.user = new User.UserBuilder().buildGuestUser();
                updateMenuItems();
                menuItem.setTitle(navigationView.getMenu().findItem(R.id.nav_main).getTitle());
                fragment = MainFragment.newInstance(user);
                break;
            case R.id.nav_chat:
                fragment = ChannelFragment.newInstance(user);
                break;
            default:
                fragment = MainFragment.newInstance(user);
        }

        if (openFragment(fragment)) {
            // Opening the fragment worked
            menuItem.setChecked(true);
            setTitle(menuItem.getTitle());
        }
    }

    public boolean openFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContent, fragment).commit();
                previous_fragments.add(0, current_fragment);
                current_fragment = fragment;
                return true;
            }
        }
        return false;
    }

    /**
     * Load the previous fragment (if there is one) into the fragment container
     */
    public void openPreviousFragment() {
        if (previous_fragments.get(0) != null) {
            Fragment fragment = previous_fragments.remove(0);
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContent, fragment).commit();
                current_fragment = fragment;
            }
        }
    }

    @Override
    public void onFragmentInteraction(String tag, Object data) {
        switch (tag) {
            case LoginFragment.TAG:
                this.user = (User) data;
                updateMenuItems();
                selectItem(navigationView.getMenu().findItem(R.id.nav_main));
                break;
            case ChannelFragment.TAG:
                int channelID = (Integer) data;
                openFragment(ChatFragment.newInstance(user, channelID));
                break;
            case AssociationDetailFragment.TAG:
                Association association = (Association) data;
                openFragment(AssociationDetailFragment.newInstance(user, association));
                break;
            default:
                // Should never happen
                throw new AssertionError(tag);
        }
    }

    /**
     * When back is pressed, load the previous fragment used
     */
    @Override
    public void onBackPressed() {
        openPreviousFragment();
    }

    /**
     * Return the current fragment
     *
     * @return current fragment
     */
    public Fragment getCurrentFragment() {
        return current_fragment;
    }

    /**
     * Return the current user
     *
     * @return current user
     */
    public User getUser() {
        return user;
    }
}
