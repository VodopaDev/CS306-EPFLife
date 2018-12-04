package ch.epfl.sweng.zuluzulu.Firebase;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.Database.Database;
import ch.epfl.sweng.zuluzulu.Firebase.Database.DatabaseCollection;
import ch.epfl.sweng.zuluzulu.Firebase.Database.FirebaseFactory;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
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

    //----- Association related methods -----\\

    /**
     * Get all associations and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getAllAssociations(OnResult<List<Association>> onResult) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        assoCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Association> resultList = new ArrayList<>();
            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                if (fmap.hasFields(Association.requiredFields()))
                    resultList.add(new Association(fmap));
            }
            onResult.apply(resultList);
            // IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch all associations"));
    }

    /**
     * Get one association from its id and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getAssociationFromId(String id, OnResult<Association> onResult) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        assoCollection.document(id).get().addOnSuccessListener(documentSnapshot -> {
            FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
            if (fmap.hasFields(Association.requiredFields())) {
                onResult.apply(new Association(fmap));
            }
            // IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch the association with id " + id));
    }

    /**
     * Get all associations from an ID list and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getAssociationsFromIds(List<String> ids, OnResult<List<Association>> onResult) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        List<Association> result = new ArrayList<>();
        Counter counter = new Counter(ids.size());

        for (String id : ids) {
            assoCollection.document(id).get().addOnSuccessListener(documentSnapshot -> {
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
                if (fmap.hasFields(Association.requiredFields()))
                    result.add(new Association(fmap));
                if (counter.increment()) {
                    onResult.apply(result);
                    // IdlingResourceFactory.decrementCountingIdlingResource();
                }
            }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch the association with id " + id))
                    .addOnFailureListener(e -> {
                        if (counter.increment()) {
                            onResult.apply(result);
                            // IdlingResourceFactory.decrementCountingIdlingResource();
                        }
                    });
        }
    }

    @Override
    public void addAssociation(Association association) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        createChannel(association);
        assoCollection.document(association.getId()).set(association.getData());
        // IdlingResourceFactory.decrementCountingIdlingResource();
    }

    //----- Event related methods -----\\

    @Override
    public void addEvent(Event event) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        createChannel(event);
        eventCollection.document(event.getId()).set(event.getData());
        // IdlingResourceFactory.decrementCountingIdlingResource();
    }

    /**
     * Get all events and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getAllEvents(OnResult<List<Event>> onResult) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        eventCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Event> resultList = new ArrayList<>();
            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                if (fmap.hasFields(Event.requiredFields()))
                    resultList.add(new Event(fmap));
            }
            onResult.apply(resultList);
            // IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch all events"));
    }

    /**
     * Get one event from its id and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getEventFromId(String id, OnResult<Event> onResult) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        eventCollection.document(id).get().addOnSuccessListener(documentSnapshot -> {
            FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
            if (fmap.hasFields(Event.requiredFields())) {
                onResult.apply(new Event(fmap));
            }
            // IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch the association with id " + id));
    }

    /**
     * Get all events from an ID list and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getEventsFromIds(List<String> ids, OnResult<List<Event>> onResult) {
        List<Event> result = new ArrayList<>();
        Counter counter = new Counter(ids.size());

        for (String id : ids) {
// IdlingResourceFactory.incrementCountingIdlingResource();
            eventCollection.document(id).get().addOnSuccessListener(documentSnapshot -> {
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
                if (fmap.hasFields(Event.requiredFields()))
                    result.add(new Event(fmap));
                if (counter.increment())
                    onResult.apply(result);
                // IdlingResourceFactory.decrementCountingIdlingResource();
            }).addOnFailureListener(e -> {
                if (counter.increment())
                    onResult.apply(result);
                // IdlingResourceFactory.decrementCountingIdlingResource();
            });
        }
    }

    //----- Channel related methods -----\\

    private void createChannel(Association association) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        Map<String, Object> map = new HashMap<>();
        map.put("id", association.getChannelId());
        map.put("name", association.getName());
        map.put("short_description", association.getShortDescription());
        map.put("restrictions", new HashMap<>());
        map.put("icon_uri", association.getIconUri().toString());
        channelCollection.document(association.getChannelId()).set(map);
        // IdlingResourceFactory.decrementCountingIdlingResource();
    }

    private void createChannel(Event event) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        Map<String, Object> map = new HashMap<>();
        map.put("id", event.getChannelId());
        map.put("name", event.getName());
        map.put("short_description", event.getShortDescription());
        map.put("restrictions", new HashMap<>());
        map.put("icon_uri", event.getIconUri().toString());
        channelCollection.document(event.getChannelId()).set(map);
        // IdlingResourceFactory.decrementCountingIdlingResource();
    }

    /**
     * Get all events and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getAllChannels(OnResult<List<Channel>> onResult) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Channel> resultList = new ArrayList<>();
            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                if (fmap.hasFields(Channel.requiredFields()))
                    resultList.add(new Channel(fmap));
            }
            onResult.apply(resultList);
            // IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch all channels"));
    }

    /**
     * Get one event from its id and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    @Override
    public void getChannelFromId(String id, OnResult<Channel> onResult) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(id).get().addOnSuccessListener(documentSnapshot -> {
            FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
            if (fmap.hasFields(Channel.requiredFields())) {
                onResult.apply(new Channel(fmap));
            }
            // IdlingResourceFactory.decrementCountingIdlingResource();
        }).addOnFailureListener(onFailureWithErrorMessage("Cannot fetch the channel with id " + id));
    }

    /**
     * Get all events from an ID list and apply an OnResult on them
     *
     * @param onResult interface defining apply()
     */
    public void getChannelsFromIds(List<String> ids, OnResult<List<Channel>> onResult) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        List<Channel> result = new ArrayList<>();
        Counter counter = new Counter(ids.size());

        for (String id : ids) {
            channelCollection.document(id).get().addOnSuccessListener(documentSnapshot -> {
                FirebaseMapDecorator fmap = new FirebaseMapDecorator(documentSnapshot);
                if (fmap.hasFields(Channel.requiredFields()))
                    result.add(new Channel(fmap));
                if (counter.increment()) {
                    onResult.apply(result);
                    // IdlingResourceFactory.decrementCountingIdlingResource();
                }
            }).addOnFailureListener(e -> {
                if (counter.increment()) {
                    onResult.apply(result);
                    // IdlingResourceFactory.decrementCountingIdlingResource();
                }
            });
        }
    }

    @Override
    public void getMessagesFromChannel(String id, OnResult<List<ChatMessage>> onResult) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(id).collection("messages").get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<ChatMessage> result = new ArrayList<>();
            for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                FirebaseMapDecorator data = new FirebaseMapDecorator(snap);
                if (data.hasFields(ChatMessage.requiredFields()))
                    result.add(new ChatMessage(data));
            }
            onResult.apply(result);
            // IdlingResourceFactory.decrementCountingIdlingResource();
        });
    }

    @Override
    public void getPostsFromChannel(String id, OnResult<List<Post>> onResult) {
// IdlingResourceFactory.incrementCountingIdlingResource();
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
            // IdlingResourceFactory.decrementCountingIdlingResource();
        });
    }

    @Override
    public void getRepliesFromPost(String channelId, String postId, OnResult<List<Post>> onResult) {
// IdlingResourceFactory.incrementCountingIdlingResource();
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
            // IdlingResourceFactory.decrementCountingIdlingResource();
        });
    }

    @Override
    public void updateOnNewMessagesFromChannel(String channelId, OnResult<List<ChatMessage>> onResult) {
        channelCollection.document(channelId).collection("messages").addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null)
                System.err.println("Listen failed: " + e);
            else {
                if(!queryDocumentSnapshots.isEmpty()) {
// IdlingResourceFactory.incrementCountingIdlingResource();
                    List<ChatMessage> result = new ArrayList<>();
                    for (DocumentSnapshot snap : queryDocumentSnapshots.getDocuments()) {
                        FirebaseMapDecorator data = new FirebaseMapDecorator(snap);
                        if (data.hasFields(ChatMessage.requiredFields()))
                            result.add(new ChatMessage(data));
                    }
                    onResult.apply(result);
                    // IdlingResourceFactory.decrementCountingIdlingResource();
                }
            }
        });
    }

    @Override
    public void addPost(Post post) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(post.getChannelId())
                .collection("posts")
                .document(post.getId())
                .set(post.getData());
        // IdlingResourceFactory.decrementCountingIdlingResource();
    }

    @Override
    public void addReply(Post post) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(post.getChannelId())
                .collection("posts")
                .document(post.getOriginalPostId())
                .collection("replies")
                .document(post.getId())
                .set(post.getData());
        // IdlingResourceFactory.decrementCountingIdlingResource();
    }

    @Override
    public void updateUser(User user) {

    }

    public void updatePost(Post post) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(post.getChannelId())
                .collection("posts")
                .document(post.getId())
                .update(post.getData());
        // IdlingResourceFactory.decrementCountingIdlingResource();
    }

    public void addMessage(ChatMessage message) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        channelCollection.document(message.getChannelId())
                .collection("messages")
                .document(message.getId())
                .set(message.getData());
        // IdlingResourceFactory.decrementCountingIdlingResource();
    }


    //----- User related methods -----\\

    public void updateUser(AuthenticatedUser user) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        userCollection.document(user.getSciper()).set(user.getData());
        // IdlingResourceFactory.decrementCountingIdlingResource();
    }

    public void getUserWithIdOrCreateIt(String id, OnResult<FirebaseMapDecorator> onResult) {
// IdlingResourceFactory.incrementCountingIdlingResource();
        userCollection.document(id).get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                Map<String, Object> map = new HashMap<>();
                map.put("followed_associations", new ArrayList<String>());
                map.put("followed_events", new ArrayList<String>());
                map.put("followed_channels", new ArrayList<String>());
                map.put("roles", Collections.singletonList("USER"));
                userCollection.document(id).set(map);
                onResult.apply(new FirebaseMapDecorator(map));
            } else {
                FirebaseMapDecorator data = new FirebaseMapDecorator(documentSnapshot);
                onResult.apply(data);
            }
            // IdlingResourceFactory.decrementCountingIdlingResource();
        });

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
            // IdlingResourceFactory.decrementCountingIdlingResource();
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
