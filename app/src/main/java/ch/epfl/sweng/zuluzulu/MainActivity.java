package ch.epfl.sweng.zuluzulu;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.firebase.FirebaseProxy;
import ch.epfl.sweng.zuluzulu.fragments.AboutZuluzuluFragment;
import ch.epfl.sweng.zuluzulu.fragments.AdminFragments.AddEventFragment;
import ch.epfl.sweng.zuluzulu.fragments.AdminFragments.AdminPanelFragment;
import ch.epfl.sweng.zuluzulu.fragments.AdminFragments.AssociationsGeneratorFragment;
import ch.epfl.sweng.zuluzulu.fragments.AdminFragments.ChangeUserRoleFragment;
import ch.epfl.sweng.zuluzulu.fragments.AdminFragments.MementoFragment;
import ch.epfl.sweng.zuluzulu.fragments.AssociationDetailFragment;
import ch.epfl.sweng.zuluzulu.fragments.AssociationFragment;
import ch.epfl.sweng.zuluzulu.fragments.CalendarFragment;
import ch.epfl.sweng.zuluzulu.fragments.ChannelFragment;
import ch.epfl.sweng.zuluzulu.fragments.ChatFragment;
import ch.epfl.sweng.zuluzulu.fragments.EventDetailFragment;
import ch.epfl.sweng.zuluzulu.fragments.EventFragment;
import ch.epfl.sweng.zuluzulu.fragments.LoginFragment;
import ch.epfl.sweng.zuluzulu.fragments.MainFragment;
import ch.epfl.sweng.zuluzulu.fragments.PostFragment;
import ch.epfl.sweng.zuluzulu.fragments.ProfileFragment;
import ch.epfl.sweng.zuluzulu.fragments.ReplyFragment;
import ch.epfl.sweng.zuluzulu.fragments.SettingsFragment;
import ch.epfl.sweng.zuluzulu.fragments.SuperFragment;
import ch.epfl.sweng.zuluzulu.fragments.WritePostFragment;
import ch.epfl.sweng.zuluzulu.localDatabase.UserDatabase;
import ch.epfl.sweng.zuluzulu.structure.Association;
import ch.epfl.sweng.zuluzulu.structure.Channel;
import ch.epfl.sweng.zuluzulu.structure.Event;
import ch.epfl.sweng.zuluzulu.structure.GPS;
import ch.epfl.sweng.zuluzulu.structure.Post;
import ch.epfl.sweng.zuluzulu.user.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.user.User;
import ch.epfl.sweng.zuluzulu.user.UserRole;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    // Const used to send a Increment or Decrement message
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SuperFragment current_fragment;
    private Stack<SuperFragment> previous_fragments;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Needed to have access to the Firestore
        FirebaseProxy.init(getApplicationContext());

        // Initialize the fragment stack used for the back button
        previous_fragments = new Stack<>();

        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);

        createUser();

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

            selectItem(navigationView.getMenu().findItem(R.id.nav_main), true);
        }
    }

    /**
     * Open the menu
     * @param item menu item
     * @return boolean
     */
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
                            selectItem(menuItem, true);
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

    private void createUser() {
        UserDatabase userDatabase = new UserDatabase(getApplicationContext());
        AuthenticatedUser localUser = userDatabase.getUser();
        if (localUser != null) {
            user = localUser;
            DatabaseFactory.getDependency().getUserWithIdOrCreateIt(user.getSciper(), result -> {
                if (result != null) {
                    user = result;
                    selectItem(navigationView.getMenu().findItem(R.id.nav_main), true);
                }
            });
        } else {
            user = new User.UserBuilder().buildGuestUser();
        }
    }

    /**
     * Create a new fragment and replace it in the activity
     *
     * @param menuItem The item that corresponds to a fragment on the menu
     */

    private void selectItem(MenuItem menuItem, boolean mustOpenFragment) {
        previous_fragments.clear();
        current_fragment = MainFragment.newInstance(user);

        SuperFragment fragment;

        switch (menuItem.getItemId()) {
            case R.id.nav_calendar:
                fragment = CalendarFragment.newInstance((AuthenticatedUser) user);
                break;
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
                fragment = ProfileFragment.newInstance((AuthenticatedUser) user, true);
                break;
            case R.id.nav_logout:
                UserDatabase userDatabase = new UserDatabase(getApplicationContext());
                userDatabase.delete((AuthenticatedUser) user);

                android.webkit.CookieManager.getInstance().removeAllCookie();
                GPS.stop();

                user = new User.UserBuilder().buildGuestUser();

                updateMenuItems();
                fragment = MainFragment.newInstance(user);
                break;
            case R.id.nav_chat:
                fragment = ChannelFragment.newInstance(user);
                break;
            case R.id.nav_admin_panel:
                fragment = AdminPanelFragment.newInstance();
                break;
            default:
                fragment = MainFragment.newInstance(user);
        }

        if (mustOpenFragment) {
            openFragment(fragment);
        }

        menuItem.setChecked(true);
    }

    public void openFragment(SuperFragment fragment) {
        openFragment(fragment, false);
    }

    public void openFragment(SuperFragment fragment, boolean backPressed) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            if (fragmentManager != null) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragmentContent, fragment).commit();
                if (!backPressed) {
                    previous_fragments.push(current_fragment);
                }
                current_fragment = fragment;
            }
        }
    }

    @Override
    public void onFragmentInteraction(CommunicationTag tag, Object data) {
        switch (tag) {
            case SET_USER:
                Map<Integer, Object> received = (HashMap<Integer, Object>) data;
                this.user = (User) received.get(0);
                if (user != null && user.isConnected()) {
                    UserDatabase userDatabase = new UserDatabase(getApplicationContext());
                    userDatabase.put((AuthenticatedUser) user);
                    updateMenuItems();
                } else{
                    this.user = new User.UserBuilder().buildGuestUser();
                }

                break;
            case OPEN_CREATE_EVENT:
                openFragment(AddEventFragment.newInstance());
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
            case OPEN_REPLY_FRAGMENT:
                Pair receivedData = (Pair<Channel, Post>) data;
                openFragment(ReplyFragment.newInstance(user, (Channel) receivedData.first, (Post) receivedData.second));
                break;
            case OPEN_WRITE_POST_FRAGMENT:
                channel = (Channel) data;
                openFragment(WritePostFragment.newInstance(user, channel));
                break;
            case OPEN_ASSOCIATION_FRAGMENT:
                selectItem(navigationView.getMenu().findItem(R.id.nav_associations), false);
                openFragment(AssociationFragment.newInstance(user));
                break;
            case OPEN_ASSOCIATION_DETAIL_FRAGMENT:
                Association association = (Association) data;
                openFragment(AssociationDetailFragment.newInstance(user, association));
                break;
            case OPEN_ABOUT_US_FRAGMENT:
                selectItem(navigationView.getMenu().findItem(R.id.nav_about), false);
                openFragment(AboutZuluzuluFragment.newInstance());
                break;
            case OPEN_MAIN_FRAGMENT:
                selectItem(navigationView.getMenu().findItem(R.id.nav_main), false);
                openFragment(MainFragment.newInstance(user));
                break;
            case OPEN_EVENT_FRAGMENT:
                selectItem(navigationView.getMenu().findItem(R.id.nav_events), false);
                openFragment(EventFragment.newInstance(user));
                break;
            case OPEN_EVENT_DETAIL_FRAGMENT:
                Event event = (Event) data;
                openFragment(EventDetailFragment.newInstance(user, event));
                break;
            case OPEN_CHANNEL_FRAGMENT:
                User visitedUser = data == null ? user : (User) data;
                selectItem(navigationView.getMenu().findItem(R.id.nav_chat), false);
                openFragment(ChannelFragment.newInstance(visitedUser));
                break;
            case OPEN_LOGIN_FRAGMENT:
                selectItem(navigationView.getMenu().findItem(R.id.nav_login), false);
                openFragment(LoginFragment.newInstance());
                break;
            case OPEN_PROFILE_FRAGMENT:
                AuthenticatedUser profileData = data == null ? (AuthenticatedUser) user : (AuthenticatedUser) data;
                boolean profileOwner = profileData.getSciper().equals(user.getSciper());
                selectItem(navigationView.getMenu().findItem(R.id.nav_profile), false);
                openFragment(ProfileFragment.newInstance(profileData, profileOwner));
                break;
            case OPEN_SETTINGS_FRAGMENT:
                selectItem(navigationView.getMenu().findItem(R.id.nav_settings), false);
                openFragment(SettingsFragment.newInstance());
                break;

            // Admin
            case OPEN_MEMENTO:
                openFragment(MementoFragment.newInstance(user));
                break;
            case OPEN_MANAGE_USER:
                openFragment(ChangeUserRoleFragment.newInstance());
                break;
            case OPEN_MANAGE_CHANNEL:
                //TODO: to add when finished
                //openFragment(...);
                break;
            case OPEN_MANAGE_ASSOCIATION:
                openFragment(AssociationsGeneratorFragment.newInstance(user));
                break;

            default:
                // Should never happen
                throw new AssertionError(tag);
        }
    }

    @Override
    public void onBackPressed() {
        if (!previous_fragments.empty()) {
            MenuItem checkedItem = navigationView.getCheckedItem();
            if (checkedItem != null && checkedItem.getItemId() != R.id.nav_main && previous_fragments.peek() instanceof MainFragment) {
                navigationView.getCheckedItem().setChecked(false);
                navigationView.setCheckedItem(R.id.nav_main);
            }
            openFragment(previous_fragments.pop(), true);
        }
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
}
