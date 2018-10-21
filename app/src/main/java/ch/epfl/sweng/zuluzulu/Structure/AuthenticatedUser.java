package ch.epfl.sweng.zuluzulu.Structure;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    protected AuthenticatedUser(final String sciper, String gaspar, String email, String first_names, String last_names, List<Integer> fav_assos, List<Integer> followed_events, List<Integer> followed_chats) {

        this.sciper = sciper;
        this.gaspar = gaspar;
        this.email = email;
        this.first_names = first_names;
        this.last_names = last_names;
        this.fav_assos = fav_assos;
        this.followed_chats = followed_chats;
        this.followed_events = followed_events;
        ref = FirebaseFirestore.getInstance().collection("users_info").document(sciper);

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
