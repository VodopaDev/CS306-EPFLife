
package ch.epfl.sweng.zuluzulu.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

public class FirebaseProxy {

    private static FirebaseProxy proxy;
    private static OnFragmentInteractionListener mListener;

    private CollectionReference userCollection;
    private CollectionReference eventCollection;
    private CollectionReference associationCollection;
    private CollectionReference channelCollection;


    private FirebaseProxy(OnFragmentInteractionListener mListener, Context appContext) {
        this.mListener = mListener;

        FirebaseApp.initializeApp(appContext);
        FirebaseFirestore firebaseInstance = FirebaseFirestore.getInstance();
        userCollection = firebaseInstance.collection("users_info");
        eventCollection = firebaseInstance.collection("events_info");
        channelCollection = firebaseInstance.collection("channels");
        associationCollection = firebaseInstance.collection("assos_info");
    }

    public static void init(OnFragmentInteractionListener mListener, Context appContext) {
        if (proxy == null)
            proxy = new FirebaseProxy(mListener, appContext);
        else
            throw new IllegalStateException("The FirebaseProxy is already initialized");
    }

    public static FirebaseProxy getInstance() {
        if (proxy == null)
            throw new IllegalStateException("The FirebaseProxy hasn't been initialized");
        else
            return proxy;
    }

    /**
     *
     * @param tag Tag of the error message
     * @param message Body of the error message
     * @return
     */
    private OnFailureListener failureWithError(String tag, String message) {
        return e -> {
            Log.e(tag, message);
            mListener.onFragmentInteraction(CommunicationTag.DECREMENT_IDLING_RESOURCE, null);
        };
    }

}
