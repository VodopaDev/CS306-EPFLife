package ch.epfl.sweng.zuluzulu.Structure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthenticatedUser extends User {
    public static final List<String> fields = Arrays.asList("fav_assos", "followed_events", "followed_chats");
    private final String firestore_path;
    // Use sciper to check User (and not mail or gaspar)
    private final String sciper;
    private final String gaspar;
    private final String email;
    private final String section;
    private final String semester;
    // WARNING: can user can have multiples names
    private final String first_names;
    private final String last_names;
    private Object listener;
    // All followed ids of Associations, Chats and Events
    private List<Integer> fav_assos;
    private List<Integer> followed_chats;
    private List<Integer> followed_events;
    // TODO add argument to constructor and store liked_event in firebase
    private Set<Integer> liked_events = new HashSet<>();

    protected AuthenticatedUser(String sciper, String gaspar, String email, String section, String semester, String first_names, String last_names, List<Integer> fav_assos, List<Integer> followed_events, List<Integer> followed_chats) {
        firestore_path = "users_info/" + sciper;
        this.sciper = sciper;
        this.gaspar = gaspar;
        this.email = email;
        this.section = section;
        this.semester = semester;
        this.first_names = first_names;
        this.last_names = last_names;
        this.fav_assos = fav_assos;
        this.followed_chats = followed_chats;
        this.followed_events = followed_events;

        // Add role
        this.addRole(UserRole.USER);

        // TO REMOVE , change with database access
        if (gaspar.equals("dahn") || sciper.equals("268785") || sciper.equals("270103")) {
            this.addRole(UserRole.ADMIN);
        }
    }

    public boolean isFavAssociation(Association asso) {
        return fav_assos.contains(asso.getId());
    }

    public boolean isFollowedEvent(Event event) {
        return followed_events.contains(event.getId());
    }

    public boolean isFollowedChat(Channel channel) {
        return followed_chats.contains(channel.getId());
    }

    public void addFavAssociation(Association asso) {
        fav_assos.add(asso.getId());
        Utils.addIdToList(firestore_path, "fav_assos", asso.getId());
    }

    public void removeFavAssociation(Association asso) {
        fav_assos.remove((Integer) asso.getId());
        Utils.removeIdFromList(firestore_path, "fav_assos", asso.getId());
    }

    public void setFollowedChats(List<Integer> followed_chats) {
        this.followed_chats = followed_chats;
    }

    public void setFavAssos(List<Integer> fav_assos) {
        this.fav_assos = fav_assos;
    }

    public void setFollowedEvents(List<Integer> followed_events) {
        this.followed_events = followed_events;
    }

    public void setLikedEvent(Set<Integer> liked_events){
        this.liked_events = liked_events;
    }

    public boolean isEventLiked(Integer event){
        return liked_events.contains(event);
    }

    public void likeEvent(Integer event){
        this.liked_events.add(event);
    }

    public void dislikeEvent(Integer event){
        this.liked_events.remove(event);
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
    public String getSection() {
        return section;
    }

    @Override
    public String getSemester() { return semester; }

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
                + "\nemail: " + email
                + "\nsection: " + section
                + "\nsemester: " + semester;
    }
}
