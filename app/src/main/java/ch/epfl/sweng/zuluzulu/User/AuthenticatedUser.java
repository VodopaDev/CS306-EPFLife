package ch.epfl.sweng.zuluzulu.User;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthenticatedUser extends User {
    // Use sciper to check User (and not mail or gaspar)
    private final String sciper;
    private final String gaspar;
    private final String email;
    private final String section;
    private final String semester;
    // WARNING: can user can have multiples names
    private final String firstNames;
    private final String lastNames;

    // All followed ids of Associations, Chats and Events
    private List<String> followedAssociations;
    private List<String> followedChannels;
    private List<String> followedEvents;

    protected AuthenticatedUser(String sciper, String gaspar, String email, String section,
                                String semester, String firstNames, String lastNames,
                                List<String> followedAssociations, List<String> followedEvents,
                                List<String> followedChannels) {
        this.sciper = sciper;
        this.gaspar = gaspar;
        this.email = email;
        this.section = section;
        this.semester = semester;
        this.firstNames = firstNames;
        this.lastNames = lastNames;
        this.followedAssociations = new ArrayList<>(followedAssociations);
        this.followedChannels = new ArrayList<>(followedChannels);
        this.followedEvents = new ArrayList<>(followedEvents);
        addRole(UserRole.USER);
    }

    //----- Association related methods -----\\

    public boolean isFollowedAssociation(String associationId) {
        android.util.Log.d("Function called", "isFollowedAssociation");
        if (associationId == null || associationId.isEmpty())
            return false;
        return followedAssociations.contains(associationId);
    }

    public boolean addFollowedAssociation(String associationId) {
        android.util.Log.d("Function called", "addFollowedAssociation");
        if (associationId == null || associationId.isEmpty())
            return false;
        return followedAssociations.add(associationId);
    }

    public boolean removeFavAssociation(String associationId) {
        android.util.Log.d("Function called", "removeFavAssociation");
        if (associationId == null || associationId.isEmpty())
            return false;
        return followedAssociations.remove(associationId);
    }

    public void setFollowedAssociation(List<String> associationsIds) {
        android.util.Log.d("Function called", "setFollowedAssociation");
        assert (associationsIds != null);
        followedAssociations = new ArrayList<>(associationsIds);
    }

    public List<String> getFollowedAssociations() {
        android.util.Log.d("Function called", "getFollowedAssociations");
        return followedAssociations;
    }

    //----- Event related methods -----\\

    public boolean isFollowedEvent(String eventId) {
        android.util.Log.d("Function called", "isFollowedEvent");
        if (eventId == null || eventId.isEmpty())
            return false;
        return followedEvents.contains(eventId);
    }

    public boolean addFollowedEvent(String eventId) {
        android.util.Log.d("Function called", "addFollowedEvent");
        if (eventId == null || eventId.isEmpty())
            return false;
        return followedEvents.add(eventId);
    }

    public boolean removeFollowedEvent(String eventId) {
        android.util.Log.d("Function called", "removeFollowedEvent");
        if (eventId == null || eventId.isEmpty())
            return false;
        return followedEvents.remove(eventId);
    }

    public List<String> getFollowedEvents() {
        android.util.Log.d("Function called", "getFollowedEvents");
        return followedEvents;
    }

    public void setFollowedEvents(List<String> eventsIds) {
        android.util.Log.d("Function called", "setFollowedEvents");
        assert (eventsIds != null);
        followedEvents = new ArrayList<>(eventsIds);
    }

    //----- Channel related methods -----\\

    public boolean isFollowedChannel(String channelId) {
        android.util.Log.d("Function called", "isFollowedChannel");
        if (channelId == null || channelId.isEmpty())
            return false;
        return followedChannels.contains(channelId);
    }

    public boolean addFollowedChannel(String channelId) {
        android.util.Log.d("Function called", "addFollowedChannel");
        if (channelId == null || channelId.isEmpty())
            return false;
        return followedChannels.add(channelId);
    }

    public boolean removeFollowedChannel(String channelId) {
        android.util.Log.d("Function called", "removeFollowedChannel");
        if (channelId == null || channelId.isEmpty())
            return false;
        return followedChannels.remove(channelId);
    }

    public List<String> getFollowedChannels() {
        android.util.Log.d("Function called", "getFollowedChannels");
        return followedChannels;
    }

    public void setFollowedChannels(List<String> channelsIds) {
        android.util.Log.d("Function called", "setFollowedChannels");
        assert (channelsIds != null);
        followedChannels = new ArrayList<>(channelsIds);
    }

    @Override
    public String getFirstNames() {
        android.util.Log.d("Function called", "getFirstNames");
        return firstNames;
    }

    @Override
    public String getLastNames() {
        android.util.Log.d("Function called", "getLastNames");
        return lastNames;
    }

    @Override
    public String getEmail() {
        android.util.Log.d("Function called", "getEmail");
        return email;
    }

    @Override
    public String getSection() {
        android.util.Log.d("Function called", "getSection");
        return section;
    }

    @Override
    public String getSemester() {
        android.util.Log.d("Function called", "getSemester");
        return semester;
    }

    @Override
    public String getGaspar() {
        android.util.Log.d("Function called", "getGaspar");
        return gaspar;
    }

    @Override
    public String getSciper() {
        android.util.Log.d("Function called", "getSciper");
        return sciper;
    }

    @Override
    public boolean isConnected() {
        android.util.Log.d("Function called", "isConnected");
        return true;
    }

    public Map<String, Object> getData() {
        android.util.Log.d("Function called", "getData");
        List<String> roles = new ArrayList<>();
        for (UserRole role : this.roles)
            roles.add(role.name());

        Map<String, Object> map = new HashMap<>();
        map.put("followed_associations", followedAssociations);
        map.put("followed_events", followedEvents);
        map.put("followed_channels", followedChannels);
        map.put("roles", roles);
        map.put("first_name", firstNames);
        map.put("last_name", lastNames);
        map.put("section", section);
        map.put("semester", semester);
        map.put("gaspar", gaspar);
        map.put("email", email);
        map.put("sciper", sciper);
        return map;
    }

    @NonNull
    @Override
    public String toString() {
        android.util.Log.d("Function called", "toString");
        return firstNames + " " + lastNames
                + "\nsciper: " + sciper
                + "\ngaspar: " + gaspar
                + "\nemail: " + email
                + "\nsection: " + section
                + "\nsemester: " + semester;
    }
}
