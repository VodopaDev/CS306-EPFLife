package ch.epfl.sweng.zuluzulu.TestingUtility;

import android.app.Notification;
import android.util.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Firebase.OnResult;
import ch.epfl.sweng.zuluzulu.Firebase.Proxy;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.User.User;

public class MockedProxy implements Proxy {
    private Map<String, Association> associationMap;
    private Map<String, Event> eventMap;
    private Map<String, ChannelRepresentation> channelMap;
    private Map<String, FirebaseMapDecorator> userMap;

    @Override
    public String getNewChannelId() {
        return String.valueOf(channelMap.size());
    }

    @Override
    public String getNewEventId() {
        return String.valueOf(eventMap.size());
    }

    @Override
    public String getNewAssociationId() {
        return String.valueOf(associationMap.size());
    }

    @Override
    public String getNewPostId(String channelId) {
        if(!channelMap.containsKey(channelId))
            return "0";
        return String.valueOf(channelMap.get(channelId).postMap.size());
    }

    @Override
    public String getNewMessageId(String channelId) {
        if(!channelMap.containsKey(channelId))
            return "0";
        return String.valueOf(channelMap.get(channelId).messageMap.size());
    }

    @Override
    public String getNewReplyId(String channelId, String originalPostId) {
        if(!channelMap.containsKey(channelId) || !channelMap.get(channelId).postMap.containsKey(originalPostId))
            return "0";
        return String.valueOf(channelMap.get(channelId).postMap.get(originalPostId).second.size());
    }

    @Override
    public void addAssociation(Association association) {
        associationMap.put(association.getId(), association);
    }

    @Override
    public void addEvent(Event event) {
        eventMap.put(event.getId(), event);
    }

    @Override
    public void addMessage(ChatMessage message) {
        if(!channelMap.containsKey(message.getChannelId()))
            return;
        channelMap.get(message.getChannelId()).messageMap.put(message.getId(), message);
    }

    @Override
    public void addPost(Post post) {
        if(!channelMap.containsKey(post.getChannelId()))
            return;
        channelMap.get(post.getChannelId()).postMap.put(post.getId(), new Pair<>(post, Collections.EMPTY_MAP));
    }

    @Override
    public void addReply(Post post) {
        if(!channelMap.containsKey(post.getChannelId()) || !channelMap.get(post.getChannelId()).postMap.containsKey(post.getId()))
            return;
        channelMap.get(post.getChannelId()).postMap.get(post.getId()).second.put(post.getId(), post);
    }

    @Override
    public void updateUser(User user) {
        userMap.put(user.getSciper(), new FirebaseMapDecorator(user.getData()));
    }

    @Override
    public void getAllChannels(OnResult<List<Channel>> onResult) {

    }

    @Override
    public void getAllEvents(OnResult<List<Event>> onResult) {

    }

    @Override
    public void getAllAssociations(OnResult<List<Association>> onResult) {

    }

    @Override
    public void getChannelsFromIds(List<String> ids, OnResult<List<Channel>> onResult) {

    }

    @Override
    public void getEventsFromIds(List<String> ids, OnResult<List<Event>> onResult) {

    }

    @Override
    public void getAssociationsFromIds(List<String> ids, OnResult<List<Association>> onResult) {

    }

    @Override
    public void getChannelFromId(String id, OnResult<Channel> onResult) {

    }

    @Override
    public void getEventFromId(String id, OnResult<Event> onResult) {

    }

    @Override
    public void getAssociationFromId(String id, OnResult<Association> onResult) {

    }

    @Override
    public void updateOnNewMessagesFromChannel(String channelId, OnResult<List<ChatMessage>> onResult) {

    }

    @Override
    public void getMessagesFromChannel(String channelId, OnResult<List<ChatMessage>> onResult) {

    }

    @Override
    public void getPostsFromChannel(String channelId, OnResult<List<Post>> onResult) {

    }

    @Override
    public void getRepliesFromPost(String channelId, String postId, OnResult<List<Post>> onResult) {

    }

    @Override
    public void getUserWithIdOrCreateIt(String id, OnResult<FirebaseMapDecorator> onResult) {

    }

    private final class ChannelRepresentation{
        private Channel channel;
        private Map<String, ChatMessage> messageMap;
        private Map<String, Pair<Post, Map<String,Post>>> postMap;

        private ChannelRepresentation(Channel channel){
            this.channel = channel;
            messageMap = new HashMap<>();
            postMap = new HashMap<>();
        }
    }
}
