package ch.epfl.sweng.zuluzulu.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
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

public class FirebaseProxy {

    private static FirebaseProxy proxy;
    private final FirebaseFirestore firebaseInstance;

    private final CollectionReference userCollection;
    private final CollectionReference assoCollection;
    private final CollectionReference eventCollection;
    private final CollectionReference channelCollection;




    private FirebaseProxy(Context appContext) {

        FirebaseApp.initializeApp(appContext);
        firebaseInstance = FirebaseFirestore.getInstance();
        userCollection = firebaseInstance.collection("new_user");
        assoCollection = firebaseInstance.collection("new_asso");
        eventCollection = firebaseInstance.collection("new_even");
        channelCollection = firebaseInstance.collection("new_chan");
    }

    public static void init(Context appContext) {
        if (proxy == null)
            proxy = new FirebaseProxy(appContext);
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
     * Get one association from its id and apply an OnResult on them
     * @param onResult interface defining apply()
     */
    public void getAssociationFromId(Long id, OnResult<Association> onResult){
        assoCollection.document(id.toString()).get().addOnSuccessListener(documentSnapshot -> {
            FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
            if(fmap.hasFields(Association.requiredFields()))
                onResult.apply(new Association(fmap));
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch the association with id " + id));
    }

    /**
     * Get all associations from an ID list and apply an OnResult on them
     * @param onResult interface defining apply()
     */
    public void getAssociationsFromIds(List<Long> ids, OnResult<List<Association>> onResult){
        List<Association> result = new ArrayList<>();
        Counter counter = new Counter(ids.size());

        for(Long id: ids){
            assoCollection.document(id.toString()).get().addOnSuccessListener(documentSnapshot -> {
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
                if(fmap.hasFields(Association.requiredFields()))
                    result.add(new Association(fmap));
                if(counter.increment())
                    onResult.apply(result);
            }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch the association with id " + id))
            .addOnFailureListener(e -> {
                if(counter.increment())
                    onResult.apply(result);
            });
        }
    }

    /**
     * Get all events and apply an OnResult on them
     * @param onResult interface defining apply()
     */
    public void getAllEvents(OnResult<List<Event>> onResult){
        eventCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Event> resultList = new ArrayList<>();
            for(DocumentSnapshot snap: queryDocumentSnapshots.getDocuments()){
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                if(fmap.hasFields(Event.requiredFields()))
                    resultList.add(new Event(fmap));
            }
            onResult.apply(resultList);
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch all events"));
    }

    /**
     * Get one event from its id and apply an OnResult on them
     * @param onResult interface defining apply()
     */
    public void getEventFromId(Long id, OnResult<Event> onResult){
        eventCollection.document(id.toString()).get().addOnSuccessListener(documentSnapshot -> {
            FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
            if(fmap.hasFields(Event.requiredFields()))
                onResult.apply(new Event(fmap));
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch the association with id " + id));
    }

    /**
     * Get all events from an ID list and apply an OnResult on them
     * @param onResult interface defining apply()
     */
    public void getEventsFromIds(List<Long> ids, OnResult<List<Event>> onResult){
        List<Event> result = new ArrayList<>();
        Counter counter = new Counter(ids.size());

        for(Long id: ids){
            eventCollection.document(id.toString()).get().addOnSuccessListener(documentSnapshot -> {
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
                if(fmap.hasFields(Event.requiredFields()))
                    result.add(new Event(fmap));
                if(counter.increment())
                    onResult.apply(result);
            }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch the association with id " + id))
                    .addOnFailureListener(e -> {
                        if(counter.increment())
                            onResult.apply(result);
                    });
        }
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

    private class Counter {
        private int counter = 0;
        private int end;

        public Counter(int end) {
            this.end = end;
        }

        public boolean increment() {
            counter++;
            return counter >= end;
        }
    }

}
