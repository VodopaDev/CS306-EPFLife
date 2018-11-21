package ch.epfl.sweng.zuluzulu.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

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

    //----- Association related methods -----\\

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
    public void getAssociationFromId(String id, OnResult<Association> onResult){
        assoCollection.document(id).get().addOnSuccessListener(documentSnapshot -> {
            FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
            if(fmap.hasFields(Association.requiredFields()))
                onResult.apply(new Association(fmap));
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch the association with id " + id));
    }

    /**
     * Get all associations from an ID list and apply an OnResult on them
     * @param onResult interface defining apply()
     */
    public void getAssociationsFromIds(List<String> ids, OnResult<List<Association>> onResult){
        List<Association> result = new ArrayList<>();
        Counter counter = new Counter(ids.size());

        for(String id: ids){
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

    public void addAssociation(Association.AssociationBuilder builder){
        DocumentReference newRef = assoCollection.document();
        builder.setId(newRef.getId());
        Association association = builder.build();
        if(association != null)
            newRef.set(association.getData());
        else
            newRef.delete();
    }

    //----- Event related methods -----\\

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
    public void getEventFromId(String id, OnResult<Event> onResult){
        eventCollection.document(id).get().addOnSuccessListener(documentSnapshot -> {
            FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
            if(fmap.hasFields(Event.requiredFields()))
                onResult.apply(new Event(fmap));
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch the association with id " + id));
    }

    /**
     * Get all events from an ID list and apply an OnResult on them
     * @param onResult interface defining apply()
     */
    public void getEventsFromIds(List<String> ids, OnResult<List<Event>> onResult){
        List<Event> result = new ArrayList<>();
        Counter counter = new Counter(ids.size());

        for(String id: ids){
            eventCollection.document(id).get().addOnSuccessListener(documentSnapshot -> {
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

    //----- Channel related methods -----\\

    /**
     * Get all events and apply an OnResult on them
     * @param onResult interface defining apply()
     */
    public void getAllChannels(OnResult<List<Channel>> onResult){
        channelCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Channel> resultList = new ArrayList<>();
            for(DocumentSnapshot snap: queryDocumentSnapshots.getDocuments()){
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                if(fmap.hasFields(Channel.FIELDS))
                    resultList.add(new Channel(fmap));
            }
            onResult.apply(resultList);
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch all channels"));
    }

    /**
     * Get one event from its id and apply an OnResult on them
     * @param onResult interface defining apply()
     */
    public void getChannelFromId(String id, OnResult<Channel> onResult){
        channelCollection.document(id).get().addOnSuccessListener(documentSnapshot -> {
            FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
            if(fmap.hasFields(Channel.FIELDS))
                onResult.apply(new Channel(fmap));
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch the channel with id " + id));
    }

    /**
     * Get all events from an ID list and apply an OnResult on them
     * @param onResult interface defining apply()
     */
    public void getChannelsFromIds(List<String> ids, OnResult<List<Channel>> onResult){
        List<Channel> result = new ArrayList<>();
        Counter counter = new Counter(ids.size());

        for(String id: ids){
            channelCollection.document(id).get().addOnSuccessListener(documentSnapshot -> {
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
                if(fmap.hasFields(Channel.FIELDS))
                    result.add(new Channel(fmap));
                if(counter.increment())
                    onResult.apply(result);
            }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch the association with id " + id))
                    .addOnFailureListener(e -> {
                        if(counter.increment())
                            onResult.apply(result);
                    });
        }
    }

    public void getMessagesFromChannelWithUser(String id, String userId, OnResult<List<ChatMessage>> onResult){
        channelCollection.document(id).collection("messages").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<ChatMessage> result = new ArrayList<>();
            for(DocumentSnapshot snap: queryDocumentSnapshots.getDocuments()){
                FirebaseMapDecorator data = new FirebaseMapDecorator(snap);
                if(data.hasFields(ChatMessage.FIELDS))
                    result.add(new ChatMessage(data, userId));
            }
           onResult.apply(result);
        });
    }

    public void onMessageAddedInChannel(String id, String userId, OnResult<ChatMessage> onResult){
        channelCollection.document(id).collection("messages").addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null)
                System.err.println("Listen failed: " + e);
            else
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            FirebaseMapDecorator data = new FirebaseMapDecorator(dc.getDocument());
                            if(data.hasFields(ChatMessage.FIELDS))
                                onResult.apply(new ChatMessage(data,userId));
                            break;
                        default:
                            break;
                    }

        });
    }

    //----- User related methods -----\\

    public void getUserWithIdOrCreateIt(String id, OnResult<FirebaseMapDecorator> onResult){
        userCollection.document(id).get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                Map<String, Object> map = new HashMap<>();
                map.put("followed_associations", new ArrayList<String>());
                map.put("followed_events", new ArrayList<String>());
                map.put("followed_channels", new ArrayList<String>());
                userCollection.document(id).set(map);
                onResult.apply(new FirebaseMapDecorator(map));
            } else {
                FirebaseMapDecorator data = new FirebaseMapDecorator(documentSnapshot);
                onResult.apply(data);
            }
        });

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
