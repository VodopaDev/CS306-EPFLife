package ch.epfl.sweng.zuluzulu.Structure;

import com.google.common.collect.Sets;

import java.util.HashSet;
import java.util.Set;

public final class AuthenticatedUser extends User {

    // Use sciper to check User (and not mail or gaspar)
    private final String sciper;
    private final String gaspar;
    private final String email;

    // WARNING: can user can have multiples names
    private final String first_names;
    private final String last_names;

    // All followed ids of Associations, Chats and Events
    private Set<Integer> assos_id;
    private Set<String> chats_names;
    private Set<Integer> events_id;

    // TODO: Get data from cloud service using the id
    protected AuthenticatedUser(String sciper, String gaspar, String email, String first_names, String last_names) {
        this.sciper = sciper;
        this.gaspar = gaspar;
        this.email = email;
        this.first_names = first_names;
        this.last_names = last_names;

        assos_id = Sets.newHashSet(1,3,4,6);
        chats_names = new HashSet<>();
        events_id = Sets.newHashSet(1);

    }

    // TODO: Add a method to add/remove one Association to assos_id, same for chats and events
    // TODO: Check inputs before changing fields

    public boolean isFavAssociation(Association asso){
        return assos_id.contains(asso.getId());
    }

    public boolean isFavEvent(Event event){ return events_id.contains(event.getId()); }

    public boolean addFavAssociation(Association asso){
        return assos_id.add(asso.getId());
    }

    public boolean removeFavAssociation(Association asso){
        return assos_id.remove(asso.getId());
    }

    public boolean isFollowedEvent(Event event){
        return events_id.contains(event.getId());
    }

    public boolean addFollowedEvent(Event event){ return events_id.add(event.getId()); }

    public boolean removeFollowedEvent(Event event){
        return events_id.remove(event.getId());
    }

    public boolean isFollowedChat(Channel channel){
        return chats_names.contains(channel.getName());
    }

    public boolean addFollowedChat(Channel channel){
        return chats_names.add(channel.getName());
    }

    public boolean removeFollowedChat(Channel channel){
        return chats_names.remove(channel.getName());
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
