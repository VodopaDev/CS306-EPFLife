package ch.epfl.sweng.zuluzulu.Fragments;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.util.HashMap;
import java.util.Map;
import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.Firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;
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
public class LoginFragment extends SuperFragment {
    public final static String TAG = "Login TAG";
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private View mProgressView;

    private String redirectURICode;
    private OAuth2Config config = new OAuth2Config(new String[]{"Tequila.profile"}, "b7b4aa5bfef2562c2a3c3ea6@epfl.ch", "15611c6de307cd5035a814a2c209c115", "epflife://login");
    private User user;
    private WebView webview;
    private String codeRequestUrl;

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

        if (getArguments() != null) {
            codeRequestUrl = (String) getArguments().getSerializable("uri");
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mListener.onFragmentInteraction(CommunicationTag.SET_TITLE, getResources().getString(R.string.action_sign_in));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        mProgressView = view.findViewById(R.id.linlaHeaderProgress_login_progress);

        webview = view.findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView wView, String url) {
                if (url.contains("code=")) {
                    redirectURICode = url;
                    showProgress(true);
                    finishLogin();
                    return true;
                }
                return false;
            }
        }); //if the webView loads the redirectURI, the app starts finishing logging in

        showProgress(false);

        if(codeRequestUrl == null) {
            codeRequestUrl = AuthClient.createCodeRequestUrl(config);
        }
        webview.loadUrl(codeRequestUrl);

        return view;
    }


    /**
     * Is executed once the session is active, transfer the user to the app
     * and open the main activity
     */
    private void transfer_main() {
        // Pass the user to the activity
        Map<Integer, Object> toTransfer = new HashMap<Integer, Object>();
        toTransfer.put(0, user);
        mListener.onFragmentInteraction(CommunicationTag.SET_USER, toTransfer);
        mListener.onFragmentInteraction(CommunicationTag.OPEN_MAIN_FRAGMENT, null);
        showProgress(false);
    }


    /**
     * Once the credentials have been accepted, get the tokens, then
     * get the user from tequila, filling the User in local
     */
    private void finishLogin() {
        String code = AuthClient.extractCode(redirectURICode);

        Map<String, String> tokens;
        try {
            tokens = AuthServer.fetchTokens(config, code);
            user = AuthServer.fetchUser(tokens.get("Tequila.profile"));
        } catch (Exception e) {
            user = new User.UserBuilder().buildGuestUser();
        }

        showProgress(false);

        updateUserAndFinishLogin();
    }

    /**
     * check if we already got the user, if not create a new one on the database before
     * sending the user to the main activity
     */
    private void updateUserAndFinishLogin() {
        DatabaseFactory.getDependency().getUserWithIdOrCreateIt(user.getSciper(), result -> {
            if (result == null && user.isConnected()) {
                DatabaseFactory.getDependency().updateUser((AuthenticatedUser) user);
            } else {
                this.user = result;
            }
            transfer_main();
        });
    }


    /**
     * Shows the progress UI
     */
    private void showProgress(final boolean show) {
        if (show) {
            mProgressView.setVisibility(View.VISIBLE);
        } else {
            mProgressView.setVisibility(View.GONE);
        }
    }
}
