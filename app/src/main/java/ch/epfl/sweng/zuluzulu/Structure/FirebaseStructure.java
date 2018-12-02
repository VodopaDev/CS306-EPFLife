package ch.epfl.sweng.zuluzulu.Structure;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;

/**
 * Simple class to describe an Object stored in the Firebase Firestore
 * It must have an ID (to be identifiable), a list of required fields (to verify the data fetched online
 * is corresponding to the class you want to build) and a Firestore path to fetch the online data.
 */
public abstract class FirebaseStructure implements Serializable {
    private String id;

    /**
     * Construct a FirebaseStructure from a FirebaseMap
     * @param id the ID
     */
    public FirebaseStructure(String id){
        this.id = id;
    }

    /**
     * Construct a FirebaseStructure from a FirebaseMap
     * @param data the data containing the ID
     */
    public FirebaseStructure(FirebaseMapDecorator data){
        if(!data.hasFields(requiredFields()))
            throw new IllegalArgumentException();
        this.id = data.getString("id");
    }

    /**
     * Return the FirebaseStructure's id
     * @return the FirebaseStructure's id
     */
    public String getId(){
        return id;
    }

    /**
     * Returned all the required fields to construct a valid FirebaseStructure
     * @return the list of all required fields
     */
    public static List<String> requiredFields(){
        return Collections.singletonList("id");
    }

    /**
     * Return a map of all properties of the class.
     * Usable to put data on the FireStore.
     * @return a map of all fields
     */
    public abstract Map<String, Object> getData();
}
