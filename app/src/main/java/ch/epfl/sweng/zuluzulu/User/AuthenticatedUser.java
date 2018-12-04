package ch.epfl.sweng.zuluzulu.User;

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
        this.followedAssociations = followedAssociations;
        this.followedChannels = followedChannels;
        this.followedEvents = followedEvents;
        addRole(UserRole.USER);
    }

    //----- Association related methods -----\\

    public boolean isFollowedAssociation(String associationId) {
        if (associationId == null || associationId.isEmpty())
            return false;
        return followedAssociations.contains(associationId);
    }

    public boolean addFollowedAssociation(String associationId) {
        if (associationId == null || associationId.isEmpty())
            return false;
        return followedAssociations.add(associationId);
    }

    public boolean removeFavAssociation(String associationId) {
        if (associationId == null || associationId.isEmpty())
            return false;
        return followedAssociations.remove(associationId);
    }

    public void setFollowedAssociation(List<String> associationsIds) {
        assert (associationsIds != null);
        followedAssociations = associationsIds;
    }

    public List<String> getFollowedAssociations() {
        return followedAssociations;
    }

    //----- Event related methods -----\\

    public boolean isFollowedEvent(String eventId) {
        if (eventId == null || eventId.isEmpty())
            return false;
        return followedEvents.contains(eventId);
    }

    public boolean addFollowedEvent(String eventId) {
        if (eventId == null || eventId.isEmpty())
            return false;
        return followedEvents.add(eventId);
    }

    public boolean removeFollowedEvent(String eventId) {
        if (eventId == null || eventId.isEmpty())
            return false;
        return followedEvents.remove(eventId);
    }

    public List<String> getFollowedEvents() {
        return followedEvents;
    }

    public void setFollowedEvents(List<String> eventsIds) {
        assert (eventsIds != null);
        followedEvents = eventsIds;
    }

    //----- Channel related methods -----\\

    public boolean isFollowedChannel(String channelId) {
        if (channelId == null || channelId.isEmpty())
            return false;
        return followedChannels.contains(channelId);
    }

    public boolean addFollowedChannel(String channelId) {
        if (channelId == null || channelId.isEmpty())
            return false;
        return followedChannels.add(channelId);
    }

    public boolean removeFollowedChannel(String channelId) {
        if (channelId == null || channelId.isEmpty())
            return false;
        return followedChannels.remove(channelId);
    }

    public List<String> getFollowedChannels() {
        return followedChannels;
    }

    public void setFollowedChannels(List<String> channelsIds) {
        assert (channelsIds != null);
        followedChannels = channelsIds;
    }

    @Override
    public String getFirstNames() {
        return firstNames;
    }

    @Override
    public String getLastNames() {
        return lastNames;
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
    public String getSemester() {
        return semester;
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

    public Map<String, Object> getData() {
        ArrayList<String> roles = new ArrayList<>();
        for (UserRole role : this.roles)
            roles.add(role.name());

        Map<String, Object> map = new HashMap<>();
        map.put("followed_associations", followedAssociations);
        map.put("followed_events", followedEvents);
        map.put("followed_channels", followedChannels);
        map.put("roles", roles);
        return map;
    }

    @Override
    public String toString() {
        return firstNames + " " + lastNames
                + "\nsciper: " + sciper
                + "\ngaspar: " + gaspar
                + "\nemail: " + email
                + "\nsection: " + section
                + "\nsemester: " + semester;
    }
}
