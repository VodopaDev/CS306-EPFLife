package ch.epfl.sweng.zuluzulu.Proxy;

import java.util.List;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Firebase.OnResult;
import ch.epfl.sweng.zuluzulu.Firebase.Proxy;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.User.User;

public class ProxyMock implements Proxy {
    private static int id = 0;

    private String defaultId(){
        return Integer.toString(id++);
    }

    @Override
    public String getNewChannelId() {
        return defaultId();
    }

    @Override
    public String getNewEventId() {
        return null;
    }

    @Override
    public String getNewAssociationId() {
        return null;
    }

    @Override
    public String getNewPostId(String channelId) {
        return null;
    }

    @Override
    public String getNewMessageId(String channelId) {
        return null;
    }

    @Override
    public String getNewReplyId(String channelId, String originalPostId) {
        return null;
    }

    @Override
    public void addAssociation(Association association) {

    }

    @Override
    public void addEvent(Event event) {

    }

    @Override
    public void addMessage(ChatMessage message) {

    }

    @Override
    public void addPost(Post post) {

    }

    @Override
    public void addReply(Post post) {

    }

    @Override
    public void updateUser(User user) {

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
}
