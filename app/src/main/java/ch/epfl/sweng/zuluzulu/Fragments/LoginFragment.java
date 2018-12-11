package ch.epfl.sweng.zuluzulu.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.User.UserRole;
import ch.epfl.sweng.zuluzulu.tequila.AuthClient;
import ch.epfl.sweng.zuluzulu.tequila.AuthServer;
import ch.epfl.sweng.zuluzulu.tequila.OAuth2Config;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends SuperFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public final static String TAG = "Login TAG";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private View mProgressView;
    private View mLoginFormView;

    private WebView webview;

    private String redirectURICode;
    private OAuth2Config config = new OAuth2Config(new String[]{"Tequila.profile"}, "b7b4aa5bfef2562c2a3c3ea6@epfl.ch", "15611c6de307cd5035a814a2c209c115", "epflife://login");
    private String code;
    private User user;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    /**
     * Create the login instance
     *
     * @param savedInstanceState state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, getResources().getString(R.string.action_sign_in));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        mLoginFormView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);
        webview = view.findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView wView, String url) {
                if (url.contains("code=")) {
                    redirectURICode = url;
                    showProgress(true);
                    webview.setVisibility(View.INVISIBLE);
                    finishLogin();
                    return true;
                }
                return false;
            }
        });

        String codeRequestUrl = AuthClient.createCodeRequestUrl(config);
        webview.loadUrl(codeRequestUrl);

        return view;
    }

    /**
     * Is executed once the session is active
     * Log in the main activity
     */
    private void transfer_main() {
        // Pass the user to the activity
            Map<Integer, Object> toTransfer = new HashMap<Integer, Object>();
            toTransfer.put(0, user);
            toTransfer.put(1, code);
            toTransfer.put(2, config);
            mListener.onFragmentInteraction(CommunicationTag.SET_USER, toTransfer);
            mListener.onFragmentInteraction(CommunicationTag.OPEN_MAIN_FRAGMENT, null);
            showProgress(false);
    }

    private void finishLogin() {
        code = AuthClient.extractCode(redirectURICode);

        Map<String, String> tokens;
        try {
            tokens = AuthServer.fetchTokens(config, code);
        } catch (IOException e) {
            return;
        }

        try {
            user = AuthServer.fetchUser(tokens.get("Tequila.profile"));
        } catch (IOException e) {
            return;
        }


        updateUserAndFinishLogin();
    }

    private void updateUserAndFinishLogin() {
        DatabaseFactory.getDependency().getUserWithIdOrCreateIt(user.getSciper(), result -> {
            List<String> receivedAssociations = (List<String>) result.get("followed_associations");
            List<String> receivedEvents = (List<String>) result.get("followed_events");
            List<String> receivedChannels = (List<String>) result.get("followed_channels");
            for (String role : (List<String>) result.get("roles"))
                user.addRole(UserRole.valueOf(role));
            ((AuthenticatedUser) user).setFollowedAssociation(receivedAssociations);
            ((AuthenticatedUser) user).setFollowedEvents(receivedEvents);
            ((AuthenticatedUser) user).setFollowedChannels(receivedChannels);
            DatabaseFactory.getDependency().updateUser((AuthenticatedUser) user);
            transfer_main();
            showProgress(false);
        });
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {

    }
}
