package ch.epfl.sweng.zuluzulu.Firebase;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.Event;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.INCREMENT_IDLING_RESOURCE;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.DECREMENT_IDLING_RESOURCE;

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
     * Get all associations and apply an OnResult on them
     * @param onResult interface defining apply()
     */
    public void getAllAssociations(OnResult<List<Association>> onResult){
        mListener.onFragmentInteraction(INCREMENT_IDLING_RESOURCE, null);
        associationCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Association> resultList = new ArrayList<>();
            for(DocumentSnapshot snap: queryDocumentSnapshots){
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                if(fmap.hasFields(Association.FIELDS))
                    resultList.add(new Association(fmap));
            }
            onResult.apply(resultList);
            mListener.onFragmentInteraction(DECREMENT_IDLING_RESOURCE, null);
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch all associations"));
    }

    /**
     * Get all events and apply an OnResult on them
     * @param onResult interface defining apply()
     */
    public void getAllEvents(OnResult<List<Event>> onResult){
        mListener.onFragmentInteraction(INCREMENT_IDLING_RESOURCE, null);
        eventCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Event> resultList = new ArrayList<>();
            for(DocumentSnapshot snap: queryDocumentSnapshots){
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                if(fmap.hasFields(Event.FIELDS))
                    resultList.add(new Event(fmap));
            }
            onResult.apply(resultList);
            mListener.onFragmentInteraction(DECREMENT_IDLING_RESOURCE, null);
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch all events"));
    }

    /**
     * Get all channels and apply an OnResult on them
     * @param onResult interface defining apply()
     */
    public void getAllChannels(OnResult<List<Channel>> onResult){
        mListener.onFragmentInteraction(INCREMENT_IDLING_RESOURCE, null);
        channelCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Channel> resultList = new ArrayList<>();
            for(DocumentSnapshot snap: queryDocumentSnapshots){
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                if(fmap.hasFields(Channel.FIELDS))
                    resultList.add(new Channel(fmap));
            }
            onResult.apply(resultList);
            mListener.onFragmentInteraction(DECREMENT_IDLING_RESOURCE, null);
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch all channels"));
    }

    /**
     * Return a new OnFailureListener logging the error in the error section of the console
     * @param message Body of the error message
     * @return OnFailureListener with customized text
     */
    private OnFailureListener onFailureWithErrorMessage(String message) {
        return e -> {
            Log.e("PROXY", message);
            mListener.onFragmentInteraction(DECREMENT_IDLING_RESOURCE, null);
        };
    }

}
