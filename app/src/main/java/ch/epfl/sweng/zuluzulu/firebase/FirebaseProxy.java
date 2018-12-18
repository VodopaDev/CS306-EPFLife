package ch.epfl.sweng.zuluzulu.firebase;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.firebase.Database.Database;
import ch.epfl.sweng.zuluzulu.firebase.Database.DatabaseCollection;
import ch.epfl.sweng.zuluzulu.firebase.Database.DatabaseQuery;
import ch.epfl.sweng.zuluzulu.firebase.Database.FirebaseFactory;
import ch.epfl.sweng.zuluzulu.idlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.structure.Association;
import ch.epfl.sweng.zuluzulu.structure.Channel;
import ch.epfl.sweng.zuluzulu.structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.structure.Event;
import ch.epfl.sweng.zuluzulu.structure.Post;
import ch.epfl.sweng.zuluzulu.structure.user.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.structure.user.User;
import ch.epfl.sweng.zuluzulu.structure.user.UserRole;

public class FirebaseProxy implements Proxy {

    private static FirebaseProxy proxy;

    private DatabaseCollection userCollection;
    private DatabaseCollection assoCollection;
    private DatabaseCollection eventCollection;
    private DatabaseCollection channelCollection;


    private FirebaseProxy(Context appContext) {
        FirebaseApp.initializeApp(appContext);
    }

    public static void init(Context appContext) {
        if (proxy == null)
            proxy = new FirebaseProxy(appContext);
    }

    public static FirebaseProxy getInstance() {
        if (proxy == null)
            throw new IllegalStateException("The FirebaseProxy hasn't been initialized");
        else {
            proxy.create();
            return proxy;
        }
    }

    /**
     * Create the instance
     * I can't put it in the constructor
     * because it's called in the mainactivity
     * and I can't inject from tests because tests are called after launching the main
     */
    private void create() {
        Database firebaseInstance = FirebaseFactory.getDependency();
        userCollection = firebaseInstance.collection("new_user");
        assoCollection = firebaseInstance.collection("new_asso");
        eventCollection = firebaseInstance.collection("new_even");
        channelCollection = firebaseInstance.collection("new_chan");
    }

    /**
     * Get all objects T from database
     *
     * @param query    from the collection
     * @param onResult Called on result
     * @param creator  Create the object
     * @param <T>      The object
     */
    private <T> void getAll(DatabaseQuery query, OnResult<List<T>> onResult, mapToObject<T> creator) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<T> resultList = new ArrayList<>();
            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                try {
                    T object = creator.apply(fmap);
                    if (object != null)
                        resultList.add(object);
                } catch (Exception ignored) {
                }
            }
            onResult.apply(resultList);
            IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch all"));
    }

    /**
     * Get object T by ID
     *
     * @param collection Collection
     * @param id         Id
     * @param onResult   On result
     * @param creator    Create the object
     * @param <T>        The object
     */
    private <T> void getObjectById(DatabaseCollection collection, String id, OnResult<T> onResult, mapToObject<T> creator) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        collection.document(id).getAndAddOnSuccessListener(fmap -> {
            T object = null;
            try {
                object = creator.apply(fmap);
            } catch (Exception ignored) {
            }
            if (object != null)
                onResult.apply(object);
            IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch the with id " + id));
    }

    /**
     * Get Objects T by Ids
     *
     * @param collection colection
     * @param ids        ID
     * @param onResult   OnResult
     * @param creator    Create the object
     * @param <T>        The object
     */
    private <T> void getFromIds(DatabaseCollection collection, List<String> ids, OnResult<List<T>> onResult, mapToObject<T> creator) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        List<T> result = new ArrayList<>();
        Counter counter = new Counter(ids.size());

        for (String id : ids) {
            collection.document(id).getAndAddOnSuccessListener(fmap -> {
                T object = null;
                try {
                    object = creator.apply(fmap);
                } catch (Exception ignored) {
                }
                if (object != null)
                    result.add(object);
                if (counter.increment()) {
                    onResult.apply(result);
                    IdlingResourceFactory.decrementCountingIdlingResource();
                }
            }).addOnFailureListener(e -> {
                Log.e("PROXY", "cannot fetch from id " + id);
                if (counter.increment()) {
                    onResult.apply(result);
                    IdlingResourceFactory.decrementCountingIdlingResource();
                }
            });
        }
    }


    //----- Association related methods -----\\

    /**
     * Get all associations and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getAllAssociations(OnResult<List<Association>> onResult) {
        getAll(assoCollection.orderBy("name"), onResult, fmap -> {
            if (fmap.hasFields(Association.requiredFields()))
                return new Association(fmap);
            return null;
        });
    }

    /**
     * Get one association from its id and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getAssociationFromId(String id, OnResult<Association> onResult) {
        getObjectById(assoCollection, id, onResult, fmap -> {
            if (fmap.hasFields(Association.requiredFields()))
                return new Association(fmap);
            return null;
        });
    }

    /**
     * Get all associations from an ID list and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getAssociationsFromIds(List<String> ids, OnResult<List<Association>> onResult) {
        getFromIds(assoCollection, ids, onResult, fmap -> {
                    if (fmap.hasFields(Association.requiredFields()))
                        return new Association(fmap);
                    return null;
                }
        );
    }

    @Override
    public void addAssociation(Association association) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        createChannel(association);
        assoCollection.document(association.getId()).set(association.getData());
        IdlingResourceFactory.decrementCountingIdlingResource();
    }

    //----- Event related methods -----\\

    /**
     * add an event to the database
     * @param event the event we want to add
     */
    @Override
    public void addEvent(Event event) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        createChannel(event);
        eventCollection.document(event.getId()).set(event.getData());
        IdlingResourceFactory.decrementCountingIdlingResource();
    }

    /**
     * Get all events and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getAllEvents(OnResult<List<Event>> onResult) {
        getEventsFromToday(onResult, 200);
    }

    /**
     * Get Events from today ordered by date.
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getEventsFromToday(OnResult<List<Event>> onResult, int limit) {
        getAll(eventCollection.whereGreaterThan("end_date", new Date()).orderBy("end_date").limit(limit), onResult, fmap -> {
            if (fmap.hasFields(Event.requiredFields()))
                return new Event(fmap);
            return null;
        });
    }

    /**
     * Get one event from its id and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getEventFromId(String id, OnResult<Event> onResult) {
        getObjectById(eventCollection, id, onResult, fmap -> {
            if (fmap.hasFields(Event.requiredFields()))
                return new Event(fmap);
            return null;
        });
    }

    /**
     * Get all events from an ID list and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getEventsFromIds(List<String> ids, OnResult<List<Event>> onResult) {
        getFromIds(eventCollection, ids, onResult, fmap -> {
                    if (fmap.hasFields(Event.requiredFields()))
                        return new Event(fmap);
                    return null;
                }
        );
    }

    //----- Channel related methods -----\\

    /**
     * create a channel for an association
     * @param association the association we want to create a channel for
     */
    private void createChannel(Association association) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        Map<String, Object> map = new HashMap<>();
        map.put("id", association.getChannelId());
        map.put("name", association.getName());
        map.put("short_description", association.getShortDescription());
        map.put("restrictions", new HashMap<>());
        map.put("icon_uri", association.getIconUri().toString());
        channelCollection.document(association.getChannelId()).set(map);
        IdlingResourceFactory.decrementCountingIdlingResource();
    }

    /**
     * create a channel for an event
     * @param event the event we want to create a channel for
     */
    private void createChannel(Event event) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        Map<String, Object> map = new HashMap<>();
        map.put("id", event.getChannelId());
        map.put("name", event.getName());
        map.put("short_description", event.getShortDescription());
        map.put("restrictions", new HashMap<>());
        map.put("icon_uri", event.getIconUri().toString());
        channelCollection.document(event.getChannelId()).set(map);
        IdlingResourceFactory.decrementCountingIdlingResource();
    }

    /**
     * Get all events and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getAllChannels(OnResult<List<Channel>> onResult) {
        getAll(channelCollection.orderBy("id"), onResult, fmap -> {
            if (fmap.hasFields(Channel.requiredFields()))
                return new Channel(fmap);
            return null;
        });
    }

    /**
     * Get one event from its id and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getChannelFromId(String id, OnResult<Channel> onResult) {
        getObjectById(channelCollection, id, onResult, fmap -> {
            if (fmap.hasFields(Channel.requiredFields()))
                return new Channel(fmap);
            return null;
        });
    }

    /**
     * Get all events from an ID list and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    public void getChannelsFromIds(List<String> ids, OnResult<List<Channel>> onResult) {
        getFromIds(channelCollection, ids, onResult, fmap -> {
                    if (fmap.hasFields(Channel.requiredFields()))
                        return new Channel(fmap);
                    return null;
                }
        );
    }

    /**
     * get the messages of a channel
     * @param id the id of the channel
     * @param onResult the onResult for the list of messages
     */
    @Override
    public void getMessagesFromChannel(String id, OnResult<List<ChatMessage>> onResult) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(id).collection("messages").getAndAddOnSuccessListener(fmapList -> {
            List<ChatMessage> result = new ArrayList<>();
            for (FirebaseMapDecorator fmap : fmapList) {
                if (fmap.hasFields(ChatMessage.requiredFields()))
                    result.add(new ChatMessage(fmap));
            }
            onResult.apply(result);
            IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot getAndAddOnSuccessListener MessagesFromChannel " + id));
    }

    /**
     * get the posts of a channel
     * @param id the id of the channel
     * @param onResult the onResult for the list of post
     */
    @Override
    public void getPostsFromChannel(String id, OnResult<List<Post>> onResult) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(id).collection("posts").getAndAddOnSuccessListener(fmapList -> {
            List<Post> result = new ArrayList<>();
            for (FirebaseMapDecorator data : fmapList) {
                if (data.hasFields(Post.requiredFields())) {

                    /*
                     * On devrait faire ça partout
                     * Capturer les erreurs lors des créations
                     * et envoyer null ??
                     *
                     * Car new Post() peut renvoyer une exception !
                     *
                     * Si on se met d'accord sur la facon d'implémenter cela, je suis chaud
                     * à modifier le reste
                     * //TODO
                     */
                    try {
                        result.add(new Post(data));
                    } catch (Exception e) {
                        // not added
                        // do somethine ?
                    }
                }
            }
            onResult.apply(result);
            IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot getAndAddOnSuccessListener PostsFromChannel " + id));
    }

    /**
     * add the channel to the list of followed channels of a user
     * @param channel the channel we want to add
     * @param user the user
     */
    @Override
    public void addChannelToUserFollowedChannels(Channel channel, AuthenticatedUser user) {
        userCollection.document(user.getSciper()).update("followed_channels", FieldValue.arrayUnion(channel.getId()));
    }

    /**
     * add the event to the list of followed events of a user
     * @param event the event we want to add
     * @param user the user
     */
    @Override
    public void addEventToUserFollowedEvents(Event event, AuthenticatedUser user) {
        userCollection.document(user.getSciper()).update("followed_events", FieldValue.arrayUnion(event.getId()));
        userCollection.document(user.getSciper()).update("followed_channels", FieldValue.arrayUnion(event.getChannelId()));
        eventCollection.document(event.getId()).update("followers", FieldValue.arrayUnion(user.getSciper()));
    }

    /**
     * add the association to the list of followed associations of a user
     * @param association the association we want to add
     * @param user the user
     */
    @Override
    public void addAssociationToUserFollowedAssociations(Association association, AuthenticatedUser user) {
        userCollection.document(user.getSciper()).update("followed_associations", FieldValue.arrayUnion(association.getId()));
        userCollection.document(user.getSciper()).update("followed_channels", FieldValue.arrayUnion(association.getChannelId()));
    }

    /**
     * remove the channel of the list of followed channels of a user
     * @param channel the channel we want to remove
     * @param user the user
     */
    @Override
    public void removeChannelFromUserFollowedChannels(Channel channel, AuthenticatedUser user) {
        userCollection.document(user.getSciper()).update("followed_channels", FieldValue.arrayRemove(channel.getId()));
    }

    /**
     * remove the event of the list of followed events of a user
     * @param event the event we want to remove
     * @param user the user
     */
    @Override
    public void removeEventFromUserFollowedEvents(Event event, AuthenticatedUser user) {
        userCollection.document(user.getSciper()).update("followed_events", FieldValue.arrayRemove(event.getId()));
        userCollection.document(user.getSciper()).update("followed_channels", FieldValue.arrayRemove(event.getChannelId()));
        eventCollection.document(event.getId()).update("followers", FieldValue.arrayRemove(user.getSciper()));
    }

    /**
     * remove the association of the list of followed associations of a user
     * @param association the association we want to remove
     * @param user the user
     */
    @Override
    public void removeAssociationFromUserFollowedAssociations(Association association, AuthenticatedUser user) {
        userCollection.document(user.getSciper()).update("followed_associations", FieldValue.arrayRemove(association.getId()));
        userCollection.document(user.getSciper()).update("followed_channels", FieldValue.arrayRemove(association.getChannelId()));
    }

    /**
     * get the replies from a post
     * @param channelId the id of the channel containing the post
     * @param postId the id of the post
     * @param onResult the onResult of the list of posts
     */
    @Override
    public void getRepliesFromPost(String channelId, String postId, OnResult<List<Post>> onResult) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(channelId).collection("posts")
                .document(postId)
                .collection("replies").getAndAddOnSuccessListener(fmapList -> {
            List<Post> result = new ArrayList<>();
            for (FirebaseMapDecorator data : fmapList) {
                if (data.hasFields(Post.requiredFields()))
                    result.add(new Post(data));
            }
            onResult.apply(result);
            IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot getAndAddOnSuccessListener Replies from Post  " + postId));
    }

    /**
     * update the channel when receiving a new message
     * @param channelId the channel to update
     * @param onResult the OnResult of the list of messages
     */
    @Override
    public void updateOnNewMessagesFromChannel(String channelId, OnResult<List<ChatMessage>> onResult) {
        channelCollection.document(channelId).collection("messages").addSnapshotListener(fmapList -> {
                    List<ChatMessage> result = new ArrayList<>();
                    for (FirebaseMapDecorator data : fmapList) {
                        if (data.hasFields(ChatMessage.requiredFields()))
                            result.add(new ChatMessage(data));
                    }
                    onResult.apply(result);
                }
        );
    }

    /**
     * add a post to a channel
     * @param post the post we want to add
     */
    @Override
    public void addPost(Post post) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(post.getChannelId())
                .collection("posts")
                .document(post.getId())
                .set(post.getData());
        IdlingResourceFactory.decrementCountingIdlingResource();
    }

    /**
     * add a reply to a post
     * @param post the reply
     */
    @Override
    public void addReply(Post post) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(post.getChannelId())
                .collection("posts")
                .document(post.getOriginalPostId())
                .collection("replies")
                .document(post.getId())
                .set(post.getData());
        channelCollection.document(post.getChannelId())
                .collection("posts")
                .document(post.getOriginalPostId())
                .update("replies", FieldValue.arrayUnion(post.getId()));
        IdlingResourceFactory.decrementCountingIdlingResource();
    }

    /**
     * update a post
     * @param post the post we want to modify
     */
    @Override
    public void updatePost(Post post) {
        if (post.getOriginalPostId() == null)
            updateOriginalPost(post);
        else
            updateReplyPost(post);
    }

    /**
     * update a source post
     * @param post the post to modify
     */
    private void updateOriginalPost(Post post) {
        channelCollection.document(post.getChannelId())
                .collection("posts")
                .document(post.getId())
                .update(post.getData());
    }

    /**
     * update a reply post
     * @param post the reply to modify
     */
    private void updateReplyPost(Post post) {
        channelCollection.document(post.getChannelId())
                .collection("posts")
                .document(post.getOriginalPostId())
                .collection("replies")
                .document(post.getId())
                .update(post.getData());
    }

    /**
     * add a message to a channel
     * @param message the message to add
     */
    public void addMessage(ChatMessage message) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(message.getChannelId())
                .collection("messages")
                .document(message.getId())
                .set(message.getData());
        IdlingResourceFactory.decrementCountingIdlingResource();
    }


    //----- User related methods -----\\

    /**
     * update the user on the database
     * @param user the user to update
     */
    @Override
    public void updateUser(AuthenticatedUser user) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        userCollection.document(user.getSciper()).set(user.getData());
        IdlingResourceFactory.decrementCountingIdlingResource();
    }

    /**
     * get a user from an id and if it doesn't exist, create it on the database
     * @param id the id of the user
     * @param onResult the OnResult for authenticated user
     */
    public void getUserWithIdOrCreateIt(String id, OnResult<AuthenticatedUser> onResult) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        userCollection.document(id).getAndAddOnSuccessListener(fmap -> {
            try {
                AuthenticatedUser user = new User.UserBuilder()
                        .setSciper(fmap.getString("sciper"))
                        .setFirst_names(fmap.getString("first_name"))
                        .setLast_names(fmap.getString("last_name"))
                        .setSection(fmap.getString("section"))
                        .setSemester(fmap.getString("semester"))
                        .setGaspar(fmap.getString("gaspar"))
                        .setEmail(fmap.getString("email"))
                        .setFollowedAssociations(fmap.getStringList("followed_associations"))
                        .setFollowedEvents(fmap.getStringList("followed_events"))
                        .setFollowedChannels(fmap.getStringList("followed_channels"))
                        .buildAuthenticatedUser();

                for (String role : fmap.getStringList("roles"))
                    user.addRole(UserRole.valueOf(role));

                onResult.apply(user);
            } catch (Exception e) {
                e.printStackTrace();
                onResult.apply(null);
            }
            IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot set user " + id));
    }

    /**
     * get all the users of the database
     * @param onResult the OnResult of all the data
     */
    @Override
    public void getAllUsers(OnResult<List<Map<String, Object>>> onResult) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        userCollection.getAndAddOnSuccessListener(fmapList -> {
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (FirebaseMapDecorator snap : fmapList) {
                Map<String, Object> map = new HashMap<>(snap.getMap());
                resultList.add(map);
            }
            onResult.apply(resultList);
            IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch all users"));
    }

    /**
     * update the role of a user
     * @param sciper the sciper of the user
     * @param roles the new roles
     */
    @Override
    public void updateUserRole(String sciper, List<String> roles) {
        userCollection.document(sciper).update("roles", roles);
    }

    //----- Generating new id to store -----\\

    /**
     * get a new Id for a channel
     * @return the new id
     */
    @Override
    public String getNewChannelId() {
        return channelCollection.document().getId();
    }

    /**
     * get a new Id for an event
     * @return the new id
     */
    @Override
    public String getNewEventId() {
        return eventCollection.document().getId();
    }

    /**
     * get a new id for an association
     * @return the new id
     */
    @Override
    public String getNewAssociationId() {
        return assoCollection.document().getId();
    }

    /**
     * get a new id for a post
     * @param channelId the id of the channel containing the post
     * @return the new id
     */
    @Override
    public String getNewPostId(String channelId) {
        return channelCollection.document(channelId).collection("posts").document().getId();
    }

    /**
     * get a new id for a message
     * @param channelId the id of the channel containing the message
     * @return the new id
     */
    @Override
    public String getNewMessageId(String channelId) {
        return channelCollection.document(channelId).collection("messages").document().getId();
    }

    /**
     * get a new id for a reply
     * @param channelId the id of the channel containing the reply
     * @param originalPostId the post the reply is answering to
     * @return the new id
     */
    @Override
    public String getNewReplyId(String channelId, String originalPostId) {
        return channelCollection.document(channelId).collection("posts").document(originalPostId).collection("replies").document().getId();
    }

    /**
     * Return a new OnFailureListener logging the error in the error section of the console
     *
     * @param message Body of the error message
     * @return OnFailureListener with customized text
     */
    private OnFailureListener onFailureWithErrorMessage(String message) {
        return e -> {
            Log.e("PROXY", message);
            IdlingResourceFactory.decrementCountingIdlingResource();
        };
    }

    private class Counter {
        private int counter = 0;
        private int end;

        public Counter(int end) {
            this.end = end;
        }

        public boolean increment() {
            counter++;
            return counter >= end;
        }
    }

}
