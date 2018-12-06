package ch.epfl.sweng.zuluzulu.Firebase;

import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

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

    void getRepliesFromPost(String channelId, String postId, OnResult<List<Post>> onResult);

    void getUserWithIdOrCreateIt(String sciper, OnResult<Map<String, Object>> onResult);
    void getAllUsers(OnResult<List<Map<String, Object>>> onResult);
    void updateUserRole(String sciper, List<String> roles);

    void updatePost(Post post);
}
