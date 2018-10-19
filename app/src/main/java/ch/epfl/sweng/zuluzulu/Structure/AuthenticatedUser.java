package ch.epfl.sweng.zuluzulu.Structure;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class AuthenticatedUser extends User {
    private List<String> fields = Arrays.asList("fav_assos", "followed_events", "followed_chats");

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

    protected AuthenticatedUser(String sciper, String gaspar, String email, String first_names, String last_names) {
        this.sciper = sciper;
        this.gaspar = gaspar;
        this.email = email;
        this.first_names = first_names;
        this.last_names = last_names;

        CollectionReference ref =  FirebaseFirestore
                .getInstance()
                .collection("users_info");

        fav_assos = new ArrayList<>();
        followed_chats = new ArrayList<>();
        followed_events = new ArrayList<>();

        ref.document(sciper).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(Utils.isValidSnapshot(documentSnapshot, fields)){
                            Utils.longListToIntList((List<Long>)documentSnapshot.get("fav_assos"), fav_assos);
                            Utils.longListToIntList((List<Long>)documentSnapshot.get("followed_chats"), followed_chats);
                            Utils.longListToIntList((List<Long>)documentSnapshot.get("followed_events"), followed_events);
                        }
                        else
                            throw new IllegalArgumentException("Snapshot isn't valid");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // TODO: push a new document on the FireStore
                    }
                });



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
        return fav_assos.add(asso.getId());
    }

    public boolean removeFavAssociation(Association asso){
        return fav_assos.remove((Integer)asso.getId());
    }

    public boolean addFollowedEvent(Event event) {
        return followed_events.add(event.getId());
    }

    public boolean removeFollowedEvent(Event event){
        return followed_events.remove((Integer)event.getId());
    }

    public boolean addFollowedChat(Channel channel) {
        return followed_chats.add(channel.getId());
    }

    public boolean removeFollowedChat(Channel channel) {
        return followed_chats.remove((Integer)channel.getId());
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
