package ch.epfl.sweng.zuluzulu.Structure;

import java.util.Arrays;
import java.util.List;

public class AuthenticatedUser extends User {
    public static final List<String> fields = Arrays.asList("fav_assos", "followed_events", "followed_chats");
    private Object listener;
    private final String firestore_path;

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

    protected AuthenticatedUser(String sciper, String gaspar, String email, String first_names, String last_names, List<Integer> fav_assos, List<Integer> followed_events, List<Integer> followed_chats) {
        firestore_path = "users_info/" + sciper;
        this.sciper = sciper;
        this.gaspar = gaspar;
        this.email = email;
        this.first_names = first_names;
        this.last_names = last_names;
        this.fav_assos = fav_assos;
        this.followed_chats = followed_chats;
        this.followed_events = followed_events;

        // Add role
        this.addRole(UserRole.USER);

        // TO REMOVE
        if(gaspar.equals("dahn")){
            this.addRole(UserRole.ADMIN);
        }
    }

    public boolean isFavAssociation(Association asso) {
        return fav_assos.contains(asso.getId());
    }

    public boolean isFollowedEvent(Event event){
        return followed_events.contains(event.getId());
    }

    public boolean isFollowedChat(Channel channel) {
        return followed_chats.contains(channel.getId());
    }

    public void addFavAssociation(Association asso) {
        fav_assos.add(asso.getId());
        Utils.addIdToList(firestore_path, "fav_assos", asso.getId());
    }

    public void removeFavAssociation(Association asso){
        fav_assos.remove((Integer)asso.getId());
        Utils.removeIdFromList(firestore_path, "fav_assos", asso.getId());
    }

    /**
    public void addFollowedEvent(Event event) {
        followed_events.add(event.getId());
        Utils.addIdToList(firestore_path, "followed_events", event.getId());
    }

    public void removeFollowedEvent(Event event) {
        followed_events.remove((Integer) event.getId());
        Utils.removeIdFromList(firestore_path, "followed_events", event.getId());
    }

    public void addFollowedChat(Channel channel) {
        followed_chats.add(channel.getId());
        Utils.addIdToList(firestore_path, "followed_chats", channel.getId());
    }

    public void removeFollowedChat(Channel channel) {
        followed_chats.remove((Integer)channel.getId());
        Utils.removeIdFromList(firestore_path, "followed_chats", channel.getId());
    }
    */

    public void setFollowedChats(List<Integer> followed_chats){
        this.followed_chats = followed_chats;
    }

    public void setFavAssos(List<Integer> fav_assos){
        this.fav_assos = fav_assos;
    }

    public void setFollowedEvents(List<Integer> followed_events){
        this.followed_events = followed_events;
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
