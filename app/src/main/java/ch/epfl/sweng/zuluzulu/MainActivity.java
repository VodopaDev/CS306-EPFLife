package ch.epfl.sweng.zuluzulu;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.FirebaseApp;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import ch.epfl.sweng.zuluzulu.Fragments.AboutZuluzuluFragment;
import ch.epfl.sweng.zuluzulu.Fragments.AddEventFragment;
import ch.epfl.sweng.zuluzulu.Fragments.AssociationDetailFragment;
import ch.epfl.sweng.zuluzulu.Fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.Fragments.AssociationsGeneratorFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ChannelFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ChatFragment;
import ch.epfl.sweng.zuluzulu.Fragments.EventFragment;
import ch.epfl.sweng.zuluzulu.Fragments.LoginFragment;
import ch.epfl.sweng.zuluzulu.Fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.Fragments.PostFragment;
import ch.epfl.sweng.zuluzulu.Fragments.ProfileFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SettingsFragment;
import ch.epfl.sweng.zuluzulu.Fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.Fragments.WebViewFragment;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.GPS;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.Structure.UserRole;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    // Const used to send a Increment or Decrement message
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private SuperFragment current_fragment;
    private Stack<SuperFragment> previous_fragments;
    private User user;

    // This resource is used for tests
    // That's the recommended way to implement it
    // @see https://developer.android.com/training/testing/espresso/idling-resource#integrate-recommended-approach
    private CountingIdlingResource resource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the resource
        resource = new CountingIdlingResource("Main Activity");

        // Needed to use Firebase storage and Firestore
        FirebaseApp.initializeApp(getApplicationContext());

        // Initialize the fragment stack used for the back button
        previous_fragments = new Stack<>();

        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);

        // Initial to guestUser
        this.user = new User.UserBuilder().buildGuestUser();

        navigationView = initNavigationView();
        initDrawerContent();

        Intent i = getIntent();

        String redirectURIwithCode = i.getStringExtra("redirectUri");
        if (redirectURIwithCode != null) {
            openFragmentWithStringData(LoginFragment.newInstance(), LoginFragment.TAG, redirectURIwithCode);
        } else {
            // Look if there is a user object set
            User user = (User) i.getSerializableExtra("user");
            if (user != null) {
                this.user = user;
                updateMenuItems();
            }

            selectItem(navigationView.getMenu().findItem(R.id.nav_main));
        }
    }


    /**
     * Open fragment and add tag
     *
     * @param fragment Any fragment
     * @param tag      Fragment tag
     * @param data     String data
     */
    private void openFragmentWithStringData(SuperFragment fragment, String tag, String data) {
        Bundle toSend = new Bundle(1);
        toSend.putString(tag, data);
        fragment.setArguments(toSend);
        openFragment(fragment);
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
        navigationView.getMenu().clear();
        if (getUser().hasRole(UserRole.ADMIN)) {
            navigationView.inflateMenu(R.menu.drawer_view_admin);
        } else if (isAuthenticated()) {
            navigationView.inflateMenu(R.menu.drawer_view_user);
        } else {
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

        SuperFragment fragment;

        switch (menuItem.getItemId()) {
            case R.id.nav_main:
                fragment = MainFragment.newInstance(user);
                break;
            case R.id.nav_login:
                //to set arguments for the login
                fragment = LoginFragment.newInstance();
                break;
            case R.id.nav_about:
                fragment = AboutZuluzuluFragment.newInstance();
                break;
            case R.id.nav_associations:
                fragment = AssociationFragment.newInstance(user);
                break;
            case R.id.nav_events:
                fragment = EventFragment.newInstance(user);
                break;
            case R.id.nav_settings:
                fragment = SettingsFragment.newInstance();
                break;
            case R.id.nav_profile:
                fragment = ProfileFragment.newInstance(user);
                break;
            case R.id.nav_logout:
                this.user = new User.UserBuilder().buildGuestUser();

                android.webkit.CookieManager.getInstance().removeAllCookie();
                GPS.stop();

                updateMenuItems();
                fragment = MainFragment.newInstance(user);
                break;
            case R.id.nav_chat:
                fragment = ChannelFragment.newInstance(user);
                break;
            case R.id.nav_associations_generator:
                fragment = AssociationsGeneratorFragment.newInstance(user);
                break;
            default:
                fragment = MainFragment.newInstance(user);
        }

        if (openFragment(fragment)) {
            // Opening the fragment worked
            menuItem.setChecked(true);
        }
    }

    public boolean openFragment(SuperFragment fragment) {
        return openFragment(fragment, false);
    }

    public boolean openFragment(SuperFragment fragment, boolean backPressed) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContent, fragment).commit();
                if (!backPressed)
                    previous_fragments.push(current_fragment);
                current_fragment = fragment;

                return true;
            }
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(CommunicationTag tag, Object data) {
        switch (tag) {
            case SET_USER:
                Map<Integer, Object> received = (HashMap<Integer, Object>) data;
                this.user = (User) received.get(0);
                updateMenuItems();
                break;
            case OPENING_WEBVIEW:
                openFragmentWithStringData(WebViewFragment.newInstance(), WebViewFragment.URL, (String) data);
                break;
            case CREATE_EVENT:
                openFragment(AddEventFragment.newInstance());
                break;
            case INCREMENT_IDLING_RESOURCE:
                incrementCountingIdlingResource();
                break;
            case DECREMENT_IDLING_RESOURCE:
                decrementCountingIdlingResource();
                break;
            case SET_TITLE:
                setTitle((String) data);
                break;
            case OPEN_CHAT_FRAGMENT:
                Channel channel = (Channel) data;
                openFragment(ChatFragment.newInstance(user, channel));
                break;
            case OPEN_POST_FRAGMENT:
                channel = (Channel) data;
                openFragment(PostFragment.newInstance(user, channel));
                break;
            case OPEN_ASSOCIATION_FRAGMENT:
                openFragment(AssociationFragment.newInstance(user));
                selectItem(navigationView.getMenu().findItem(R.id.nav_associations));
                break;
            case OPEN_ASSOCIATION_DETAIL_FRAGMENT:
                Association association = (Association) data;
                openFragment(AssociationDetailFragment.newInstance(user, association));
                break;
            case OPEN_ABOUT_US_FRAGMENT:
                openFragment(AboutZuluzuluFragment.newInstance());
                selectItem(navigationView.getMenu().findItem(R.id.nav_about));
                break;
            case OPEN_MAIN_FRAGMENT:
                openFragment(MainFragment.newInstance(user));
                selectItem(navigationView.getMenu().findItem(R.id.nav_main));
                break;
            case OPEN_EVENT_FRAGMENT:
                openFragment(EventFragment.newInstance(user));
                selectItem(navigationView.getMenu().findItem(R.id.nav_events));
                break;
            case OPEN_EVENT_DETAIL_FRAGMENT:
                Event event = (Event) data;
                // openFragment(EventDetailFragment.newInstance(user, event));
                break;
            case OPEN_CHANNEL_FRAGMENT:
                openFragment(ChannelFragment.newInstance(user));
                selectItem(navigationView.getMenu().findItem(R.id.nav_chat));
                break;
            case OPEN_LOGIN_FRAGMENT:
                openFragment(LoginFragment.newInstance());
                selectItem(navigationView.getMenu().findItem(R.id.nav_login));
                break;
            case OPEN_PROFILE_FRAGMENT:
                openFragment(ProfileFragment.newInstance(user));
                selectItem(navigationView.getMenu().findItem(R.id.nav_profile));
                break;
            case OPEN_SETTINGS_FRAGMENT:
                openFragment(SettingsFragment.newInstance());
                selectItem(navigationView.getMenu().findItem(R.id.nav_settings));
                break;
            default:
                // Should never happen
                throw new AssertionError(tag);
        }
    }

    @Override
    public void onBackPressed() {
        if (!previous_fragments.empty())
            openFragment(previous_fragments.pop(), true);
    }

    @Override
    public void onStop() {
        super.onStop();
        GPS.stop();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (user.isConnected()) {
            boolean hadPermissions = GPS.start(this);
            if (!hadPermissions) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS.MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    /**
     * Return the current fragment
     *
     * @return current fragment
     */
    public SuperFragment getCurrentFragment() {
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

    /**
     * Increment the countingIdlingResource
     * Do this before a async task
     */
    public void incrementCountingIdlingResource() {
        resource.increment();
    }

    /**
     * Decrement the countingIdlingResource
     * Do this after a async task
     */
    public void decrementCountingIdlingResource() {
        resource.decrement();
    }

    /**
     * Return the resource for the tests
     *
     * @return resource
     */
    public CountingIdlingResource getCountingIdlingResource() {
        return resource;
    }
}
