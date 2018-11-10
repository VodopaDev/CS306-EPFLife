package ch.epfl.sweng.zuluzulu.Firebase;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.Structure.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.Structure.User;

public class FirebaseProxy {

    private static FirebaseProxy proxy;

    private CollectionReference userCollection;
    private CollectionReference eventCollection;
    private CollectionReference associationCollection;
    private CollectionReference channelCollection;


    private OnFragmentInteractionListener mListener;

    private FirebaseProxy(OnFragmentInteractionListener mListener){
        this.mListener = mListener;

        FirebaseFirestore firebaseInstance = FirebaseFirestore.getInstance();
        userCollection = firebaseInstance.collection("users_info");
        eventCollection = firebaseInstance.collection("events_info");
        channelCollection = firebaseInstance.collection("channels");
        associationCollection = firebaseInstance.collection("assos_info");
    }

    public static void init(OnFragmentInteractionListener mListener){
        if(proxy == null)
            proxy = new FirebaseProxy(mListener);
        else
            throw new IllegalStateException("The FirebaseProxy is already initialized");
    }

    public static FirebaseProxy getInstance(){
        if(proxy == null)
            throw new IllegalStateException("The FirebaseProxy hasn't been initialized");
        else
            return proxy;
    }

    public void loadUser(User user){
        if(user == null || !user.isConnected())
            throw new IllegalArgumentException("User must be connected to imports its preferences");

        mListener.onFragmentInteraction(CommunicationTag.INCREMENT_IDLING_RESOURCE, null);
        userCollection.document(user.getSciper()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (!documentSnapshot.exists()) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("fav_assos", new ArrayList<Integer>());
                    map.put("followed_events", new ArrayList<Integer>());
                    map.put("followed_chats", new ArrayList<Integer>());
                    userCollection.document(user.getSciper()).set(map);
                    Log.d("PROXY","Loaded and created new user");
                } else {
                    FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
                    List<Integer> received_assos = fmap.getIntegerList("fav_assos");
                    List<Integer> received_events = fmap.getIntegerList("followed_events");
                    List<Integer> received_chats = fmap.getIntegerList("followed_chats");

                    ((AuthenticatedUser) user).setFavAssos(received_assos);
                    ((AuthenticatedUser) user).setFollowedEvents(received_events);
                    ((AuthenticatedUser) user).setFollowedChats(received_chats);
                    Log.d("PROXY","Loaded existent user n°"+user.getSciper());
                }
                mListener.onFragmentInteraction(CommunicationTag.DECREMENT_IDLING_RESOURCE, null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("PROXY", "Error loading user n°" + user.getSciper());
                mListener.onFragmentInteraction(CommunicationTag.DECREMENT_IDLING_RESOURCE, null);
            }
        });
    }

}
