package ch.epfl.sweng.zuluzulu.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.User;
import ch.epfl.sweng.zuluzulu.Structure.Utils;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String TAG = "LOGIN_TAG";

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "user:password",
            "vincent:password",
            "dahn:password",
            "nicolas:password",
            "luca:password",
            "gaultier:password",
            "yann:password",
            "bar:world"
    };

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    private EditText mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mPasswordView = view.findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mUsernameView = view.findViewById(R.id.username);

        Button mSignInButton = view.findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = view.findViewById(R.id.login_form);
        mProgressView = view.findViewById(R.id.login_progress);

        return view;
    }

    /**
     * Is executed once the session is active
     * Log in the main activity
     */
    private void activate_session(User user) {
        // Pass the user to the activity
        mListener.onFragmentInteraction(TAG, user);
    }


    /**
     * Reset the errors
     */
    private void reset_errors() {
        mUsernameView.setError(null);
        mPasswordView.setError(null);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        reset_errors();

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password
        if (!isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // Focus the first form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Check if the username is valid
     *
     * @param username username
     * @return boolean
     */
    private boolean isUsernameValid(String username) {
        return username.length() > 3;
    }

    /**
     * Check if the password is valid
     *
     * @param password password
     * @return boolean
     */
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with our future logic
        return password.length() > 4;
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(TAG, uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /*private static String read(String prompt) throws IOException {
        System.out.print(prompt + ": ");
        return new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
    }*/

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;
        //private Map<String, String> tokens;
        private User user = null;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.


            /*
            //CODE FOR TEQUILA LOGIN
            //create the config
            OAuth2Config config = new OAuth2Config(new String[]{"Tequila.profile"}, "id", "secret", "epflife://login"); //We will have to fill with correct values
            String codeRequestUrl = AuthClient.createCodeRequestUrl(config);

            //start the browser with the Tequila URL (temporary solution)
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(codeRequestUrl));
            startActivity(browserIntent);

            //part to understand
            String redirectUri;
            try{
            redirectUri = read("Go to the above URL, authenticate, then enter the redirect URI");
            }catch (IOException e){
                return false;
            }
            String code = AuthClient.extractCode(redirectUri);
            //end part to understand


            try{
            tokens = AuthServer.fetchTokens(config, code);
            } catch (IOException e){
                return false;
            }


            try{
             user = AuthServer.fetchUser(tokens.get("Tequila.profile"));
            } catch( IOException e){
                return false;
            }*/


            /////////////////////////////////////
            //CODE FOR LOCAL LOGIN
            /*
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }
            */


            // Check credentials
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mUsername)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            return false;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            // CODE FOR LOCAL LOGIN
            // Nicolas: I have set the sciper to 000000 for testing :)
            final User.UserBuilder builder = new User.UserBuilder();
            builder.setEmail("nicolas.jomeau@epfl.ch");
            builder.setSciper("000001");
            builder.setGaspar(mUsername);
            builder.setFirst_names("nicolas");
            builder.setLast_names("jomeau");
            builder.setFollowedChats(new ArrayList<Integer>());
            builder.setFavAssos(new ArrayList<Integer>());
            builder.setFollowedEvents(new ArrayList<Integer>());
            user = builder.buildAuthenticatedUser();


            if (success && user != null) {
                //open the main activity then terminate the login activity (Ikaras998)
                final DocumentReference ref = FirebaseFirestore.getInstance()
                        .collection("users_info")
                        .document(user.getSciper());

                ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(!documentSnapshot.exists()){
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("fav_assos", new ArrayList<Integer>());
                                    map.put("followed_events", new ArrayList<Integer>());
                                    map.put("followed_chats", new ArrayList<Integer>());
                                    ref.set(map);
                                    activate_session(user);
                                }
                                else if (Utils.isValidSnapshot(documentSnapshot, AuthenticatedUser.fields)){
                                    List<Integer> received_assos = Utils.longListToIntList((List<Long>)documentSnapshot.get("fav_assos"));
                                    List<Integer> received_events = Utils.longListToIntList((List<Long>)documentSnapshot.get("followed_events"));
                                    List<Integer> received_chats = Utils.longListToIntList((List<Long>)documentSnapshot.get("followed_chats"));

                                    ((AuthenticatedUser)user).setFavAssos(received_assos);
                                    ((AuthenticatedUser)user).setFollowedEvents(received_events);
                                    ((AuthenticatedUser)user).setFollowedChats(received_chats);
                                    activate_session(user);
                                }
                             }
                        });


            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
            showProgress(false);
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


}
