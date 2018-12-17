package ch.epfl.sweng.zuluzulu.firebase;

import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.structure.Association;
import ch.epfl.sweng.zuluzulu.structure.Channel;
import ch.epfl.sweng.zuluzulu.structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.structure.Event;
import ch.epfl.sweng.zuluzulu.structure.Post;
import ch.epfl.sweng.zuluzulu.structure.user.AuthenticatedUser;

public interface Proxy {

    String getNewChannelId();

    String getNewEventId();

    String getNewAssociationId();

    String getNewPostId(String channelId);

    String getNewMessageId(String channelId);

    String getNewReplyId(String channelId, String originalPostId);

    void addAssociation(Association association);

    void addEvent(Event event);

    void addMessage(ChatMessage message);

    void addPost(Post post);

    void addReply(Post post);

    void updateUser(AuthenticatedUser user);

    void getAllChannels(OnResult<List<Channel>> onResult);

    void getAllEvents(OnResult<List<Event>> onResult);

    void getEventsFromToday(OnResult<List<Event>> onResult, int limit);

    void getAllAssociations(OnResult<List<Association>> onResult);

    void getChannelsFromIds(List<String> ids, OnResult<List<Channel>> onResult);

    void getEventsFromIds(List<String> ids, OnResult<List<Event>> onResult);

    void getAssociationsFromIds(List<String> ids, OnResult<List<Association>> onResult);

    void getChannelFromId(String id, OnResult<Channel> onResult);

    void getEventFromId(String id, OnResult<Event> onResult);

    void getAssociationFromId(String id, OnResult<Association> onResult);

    void updateOnNewMessagesFromChannel(String channelId, OnResult<List<ChatMessage>> onResult);

    void getMessagesFromChannel(String channelId, OnResult<List<ChatMessage>> onResult);

    void getPostsFromChannel(String channelId, OnResult<List<Post>> onResult);

    /*
    All these methods should also add/remove the user sciper from the "followers" field of an event.
     */
    void addChannelToUserFollowedChannels(Channel channel, AuthenticatedUser user);

    void addEventToUserFollowedEvents(Event event, AuthenticatedUser user);

    void addAssociationToUserFollowedAssociations(Association association, AuthenticatedUser user);

    void removeChannelFromUserFollowedChannels(Channel channel, AuthenticatedUser user);

    void removeEventFromUserFollowedEvents(Event event, AuthenticatedUser user);

    void removeAssociationFromUserFollowedAssociations(Association association, AuthenticatedUser user);

    void getRepliesFromPost(String channelId, String postId, OnResult<List<Post>> onResult);

    void getUserWithIdOrCreateIt(String sciper, OnResult<AuthenticatedUser> onResult);

    void getAllUsers(OnResult<List<Map<String, Object>>> onResult);

    void updateUserRole(String sciper, List<String> roles);

    void updatePost(Post post);
}
