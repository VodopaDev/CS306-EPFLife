package ch.epfl.sweng.zuluzulu.Structure;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public final class AuthenticatedUser extends User {
    private static final List<String> fields = Arrays.asList("fav_assos", "followed_events", "followed_chats");
    private final DocumentReference ref;

    // Use sciper to check User (and not mail or gaspar)
    private final String sciper;
    private final String gaspar;
    private final String email;

    // WARNING: can user can have multiples names
    private final String first_names;
    private final String last_names;

    // All followed ids of Associations, Chats and Events
    private List<Integer> fav_assos;
    private List<Integer> followed_chats;
    private List<Integer> followed_events;

    protected AuthenticatedUser(final String sciper, String gaspar, String email, String first_names, String last_names) {
        this.sciper = sciper;
        this.gaspar = gaspar;
        this.email = email;
        this.first_names = first_names;
        this.last_names = last_names;

        ref = FirebaseFirestore.getInstance().collection("users_info").document(sciper);

        fav_assos = new ArrayList<>();
        followed_chats = new ArrayList<>();
        followed_events = new ArrayList<>();

            ref.get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            // If it is the first time connecting, we create the base user in the Firestore
                            if (!documentSnapshot.exists()) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("fav_assos", Arrays.asList());
                                data.put("followed_chats", Arrays.asList());
                                data.put("followed_events", Arrays.asList());
                                ref.set(data);
                                synchronized (this) {
                                    notify();
                                }
                            }
                            // Regular user
                            else if (Utils.isValidSnapshot(documentSnapshot, fields)) {
                                Utils.longListToIntList((List<Long>) documentSnapshot.get("fav_assos"), fav_assos);
                                Utils.longListToIntList((List<Long>) documentSnapshot.get("followed_chats"), followed_chats);
                                Utils.longListToIntList((List<Long>) documentSnapshot.get("followed_events"), followed_events);
                                synchronized (this) {
                                    notify();
                                }
                            } else
                                throw new IllegalArgumentException("Snapshot isn't valid");

                        }
                    });

        synchronized (this) {
            Log.d("USER", "waiting for Firestore data");
            try {
                TimeUnit.SECONDS.timedWait(this, 5);
            } catch (InterruptedException e) {
                Log.d("USER", "TimeOut while loading Firestore data");
                e.printStackTrace();
            }
            Log.d("USER", "Finished loading Firestore data");
        }


    }

    // TODO: Add a method to add/remove one Association to fav_assos, same for chats and events
    // TODO: Check inputs before changing fields

    public boolean isFavAssociation(Association asso) {
        return fav_assos.contains(asso.getId());
    }

    public boolean isFollowedEvent(Event event){
        return followed_events.contains(event.getId());
    }

    public boolean isFollowedChat(Channel channel) {
        return followed_chats.contains(channel.getId());
    }

    public boolean addFavAssociation(Association asso) {
        boolean needUpdate = fav_assos.add(asso.getId());
        if(needUpdate)
            ref.update("fav_assos", FieldValue.arrayUnion(asso.getId()));
        return needUpdate;
    }

    public boolean removeFavAssociation(Association asso){
        boolean needUpdate = fav_assos.remove((Integer)asso.getId());
        if(needUpdate)
            ref.update("fav_assos", FieldValue.arrayRemove(asso.getId()));
        return needUpdate;
    }

    public boolean addFollowedEvent(Event event) {
        boolean needUpdate = followed_events.add(event.getId());
        if(needUpdate)
            ref.update("followed_events", FieldValue.arrayUnion(event.getId()));
        return needUpdate;
    }

    public boolean removeFollowedEvent(Event event){
        boolean needUpdate = followed_events.remove((Integer)event.getId());
        if(needUpdate)
            ref.update("followed_events", FieldValue.arrayRemove(event.getId()));
        return needUpdate;
    }

    public boolean addFollowedChat(Channel channel) {
        boolean needUpdate = followed_chats.add(channel.getId());
        if(needUpdate)
            ref.update("followed_chats", FieldValue.arrayUnion(channel.getId()));
        return needUpdate;
    }

    public boolean removeFollowedChat(Channel channel) {
        boolean needUpdate = followed_chats.remove((Integer)channel.getId());
        if(needUpdate)
            ref.update("followed_chats", FieldValue.arrayRemove(channel.getId()));
        return needUpdate;
    }


    @Override
    public String getFirstNames() {
        return first_names;
    }

    @Override
    public String getLastNames() {
        return last_names;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getGaspar() {
        return gaspar;
    }

    @Override
    public String getSciper() {
        return sciper;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public String toString() {
        return first_names + " " + last_names
                + "\nsciper: " + sciper
                + "\ngaspar: " + gaspar
                + "\nemail: " + email;
    }
}
