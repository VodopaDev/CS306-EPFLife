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
import ch.epfl.sweng.zuluzulu.Structure.FirebaseStructure;

import static ch.epfl.sweng.zuluzulu.CommunicationTag.INCREMENT_IDLING_RESOURCE;
import static ch.epfl.sweng.zuluzulu.CommunicationTag.DECREMENT_IDLING_RESOURCE;

public class FirebaseProxy {

    private static FirebaseProxy proxy;

    private final OnFragmentInteractionListener mListener;
    private final FirebaseFirestore firebaseInstance;
    private final CollectionReference assoCollection;
    private final CollectionReference eventCollection;
    private final CollectionReference channelCollection;




    private FirebaseProxy(OnFragmentInteractionListener mListener, Context appContext) {
        this.mListener = mListener;

        FirebaseApp.initializeApp(appContext);
        firebaseInstance = FirebaseFirestore.getInstance();
        assoCollection = firebaseInstance.collection("assos_info");
        eventCollection = firebaseInstance.collection("events_info");
        channelCollection = firebaseInstance.collection("channels");
    }

    public static void init(OnFragmentInteractionListener mListener, Context appContext) {
        if (proxy == null)
            proxy = new FirebaseProxy(mListener, appContext);
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
        assoCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Association> resultList = new ArrayList<>();
            for(DocumentSnapshot snap: queryDocumentSnapshots.getDocuments()){
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                if(fmap.hasFields(Association.requiredFields()))
                    resultList.add(new Association(fmap));
            }
            onResult.apply(resultList);
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch all associations"));
    }

    /**
     * Return a new OnFailureListener logging the error in the error section of the console
     * @param message Body of the error message
     * @return OnFailureListener with customized text
     */
    private OnFailureListener onFailureWithErrorMessage(String message) {
        return e -> {
            Log.e("PROXY", message);
        };
    }

}
