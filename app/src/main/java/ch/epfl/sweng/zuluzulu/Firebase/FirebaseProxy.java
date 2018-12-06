package ch.epfl.sweng.zuluzulu.Firebase;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.sweng.zuluzulu.Firebase.Database.Database;
import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseCollection;
import ch.epfl.sweng.zuluzulu.Firebase.Database.FirebaseFactory;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.MainActivity;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.ChatMessage;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

public class FirebaseProxy implements Proxy {

    private static FirebaseProxy proxy;
    private Database firebaseInstance;

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
    private void create(){
        firebaseInstance = FirebaseFactory.getDependency();
        userCollection = firebaseInstance.collection("new_user");
        assoCollection = firebaseInstance.collection("new_asso");
        eventCollection = firebaseInstance.collection("new_even");
        channelCollection = firebaseInstance.collection("new_chan");
    }

    /**
     * Get all objects T from database
     * @param collection from the collection
     * @param onResult Called on result
     * @param creator Create the object
     * @param <T> The object
     */
    private <T> void getAll(DatabaseCollection collection, OnResult<List<T>> onResult, mapToObject<T> creator) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        collection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<T> resultList = new ArrayList<>();
            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                try {
                    T object = creator.apply(fmap);
                    if (object != null)
                        resultList.add(object);
                } catch (Exception ignored) {}
            }
            onResult.apply(resultList);
            IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch all"));
    }

    /**
     * Get object T by ID
     * @param collection Collection
     * @param id        Id
     * @param onResult  On result
     * @param creator   Create the object
     * @param <T>       The object
     */
    private <T> void getObjectById(DatabaseCollection collection , String id, OnResult<T> onResult, mapToObject<T> creator) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        collection.document(id).get().addOnSuccessListener(documentSnapshot -> {
            FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
            T object = null;
            try {
                object = creator.apply(fmap);
            } catch (Exception ignored) {
            }
            onResult.apply(object);
            IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch the with id " + id));
    }

    /**
     * Get Objects T by Ids
     * @param collection    colection
     * @param ids       ID
     * @param onResult  OnResult
     * @param creator   Create the object
     * @param <T>       The object
     */
    private <T> void getFromIds(DatabaseCollection collection, List<String> ids, OnResult<List<T>> onResult, mapToObject<T> creator) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        List<T> result = new ArrayList<>();
        Counter counter = new Counter(ids.size());

        for (String id : ids) {
            collection.document(id).get().addOnSuccessListener(documentSnapshot -> {
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
                T object = null;
                try {
                    object = creator.apply(fmap);
                } catch (Exception ignored) {
                }
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
        getAll(assoCollection, onResult, fmap -> {
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
        getAll(eventCollection, onResult, fmap -> {
            if (fmap.hasFields(Event.requiredFields()))
                return new Event(fmap);
            return null;
        });
    }

    /**
     * Get Events from today ordered by date.
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getEventsFromToday(OnResult<List<Event>> onResult, int limit) {
        IdlingResourceFactory.incrementCountingIdlingResource();


        eventCollection.whereGreaterThan("end_date", new Date()).orderBy("end_date").limit(limit).get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Event> resultList = new ArrayList<>();
            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                try {
                    Event event = new Event(fmap);
                    resultList.add(event);
                } catch (Exception ignored) {}
            }
            onResult.apply(resultList);
            IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch all"));
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
        getAll(channelCollection, onResult, fmap -> {
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

    @Override
    public void getMessagesFromChannel(String id, OnResult<List<ChatMessage>> onResult) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(id).collection("messages").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<ChatMessage> result = new ArrayList<>();
            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                FirebaseMapDecorator data = new FirebaseMapDecorator(snap);
                if (data.hasFields(ChatMessage.requiredFields()))
                    result.add(new ChatMessage(data));
            }
            onResult.apply(result);
            IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot get MessagesFromChannel " + id));
    }

    @Override
    public void getPostsFromChannel(String id, OnResult<List<Post>> onResult) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(id).collection("posts").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Post> result = new ArrayList<>();
            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                FirebaseMapDecorator data = new FirebaseMapDecorator(snap);
                if (data.hasFields(Post.requiredFields())) {

                    /**
                     * On devrait faire ça partout
                     * Capturer les erreurs lors des créations
                     * et envoyer null ??
                     *
                     * Car new Post() peut renvoyer une exception !
                     *
                     * Si on se met d'accord sur la facon d'implémenter cela, je suis chaud
                     * à modifier le reste
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
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot get PostsFromChannel " + id));
    }

    @Override
    public void getRepliesFromPost(String channelId, String postId, OnResult<List<Post>> onResult) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(channelId).collection("posts")
                .document(postId)
                .collection("replies").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Post> result = new ArrayList<>();
            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                FirebaseMapDecorator data = new FirebaseMapDecorator(snap);
                if (data.hasFields(Post.requiredFields()))
                    result.add(new Post(data));
            }
            onResult.apply(result);
            IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot get Replies from Post  " + postId));
    }

    @Override
    public void updateOnNewMessagesFromChannel(String channelId, OnResult<List<ChatMessage>> onResult) {
        channelCollection.document(channelId).collection("messages").addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null)
                System.err.println("Listen failed: " + e);
            else {
                if(!queryDocumentSnapshots.isEmpty()) {
                    IdlingResourceFactory.incrementCountingIdlingResource();
                    List<ChatMessage> result = new ArrayList<>();
                    for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                        FirebaseMapDecorator data = new FirebaseMapDecorator(snap);
                        if (data.hasFields(ChatMessage.requiredFields()))
                            result.add(new ChatMessage(data));
                    }
                    onResult.apply(result);
                    IdlingResourceFactory.decrementCountingIdlingResource();
                }
            }
        });
    }

    @Override
    public void addPost(Post post) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(post.getChannelId())
                .collection("posts")
                .document(post.getId())
                .set(post.getData());
        IdlingResourceFactory.decrementCountingIdlingResource();
    }

    @Override
    public void addReply(Post post) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(post.getChannelId())
                .collection("posts")
                .document(post.getOriginalPostId())
                .collection("replies")
                .document(post.getId())
                .set(post.getData());
        IdlingResourceFactory.decrementCountingIdlingResource();
    }

    @Override
    public void updateUser(User user) {

    }

    public void updatePost(Post post) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(post.getChannelId())
                .collection("posts")
                .document(post.getId())
                .update(post.getData());
        IdlingResourceFactory.decrementCountingIdlingResource();
    }

    public void addMessage(ChatMessage message) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(message.getChannelId())
                .collection("messages")
                .document(message.getId())
                .set(message.getData());
        IdlingResourceFactory.decrementCountingIdlingResource();
    }


    //----- User related methods -----\\

    public void updateUser(AuthenticatedUser user) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        userCollection.document(user.getSciper()).set(user.getData());
        IdlingResourceFactory.decrementCountingIdlingResource();
    }

    public void getUserWithIdOrCreateIt(String id, OnResult<Map<String, Object>> onResult){
        IdlingResourceFactory.incrementCountingIdlingResource();
        userCollection.document(id).get().addOnSuccessListener(documentSnapshot -> {
            Map<String, Object> map = new HashMap<>();
            map.put("sciper", documentSnapshot.getId());
            if (!documentSnapshot.exists()) {
                map.put("followed_associations", new ArrayList<String>());
                map.put("followed_events", new ArrayList<String>());
                map.put("followed_channels", new ArrayList<String>());
                map.put("roles", Arrays.asList("USER"));
            } else {
                map.putAll(Objects.requireNonNull(documentSnapshot.getData()));
            }
            onResult.apply(map);
            IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot set user " + id));

    }

    @Override
    public void getAllUsers(OnResult<List<Map<String, Object>>> onResult) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        userCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Map<String, Object>> resultList = new ArrayList<>();
            for(DocumentSnapshot snap: queryDocumentSnapshots.getDocuments()){
                Map<String, Object> map = new HashMap<>(snap.getData());
                map.put("sciper", snap.getId());
                resultList.add(map);
            }
            onResult.apply(resultList);
            IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch all users"));
    }

    @Override
    public void updateUserRole(String sciper, List<String> roles) {
        userCollection.document(sciper).update("roles", roles);
    }

    //----- Generating new id to store -----\\

    @Override
    public String getNewChannelId() {
        return channelCollection.document().getId();
    }

    @Override
    public String getNewEventId() {
        return eventCollection.document().getId();
    }

    @Override
    public String getNewAssociationId() {
        return assoCollection.document().getId();
    }

    @Override
    public String getNewPostId(String channelId) {
        return channelCollection.document(channelId).collection("posts").document().getId();
    }

    @Override
    public String getNewMessageId(String channelId) {
        return channelCollection.document(channelId).collection("messages").document().getId();
    }

    @Override
    public String getNewReplyId(String channelId, String originalPostId) {
        return channelCollection.document(channelId).collection("posts").document(originalPostId).collection("replies").getId();
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
