package ch.epfl.sweng.zuluzulu.Structure;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;

/**
 * Simple class to describe an Object stored in the Firebase Firestore
 * It must have an ID (to be identifiable), a list of required fields (to verify the data fetched online
 * is corresponding to the class you want to build) and a Firestore path to fetch the online data.
 */
public abstract class FirebaseStructure implements Serializable {
    private Long id;

    /**
     * Construct a FirebaseStructure from a FirebaseMap
     * @param data the FirebaseMap
     */
    public FirebaseStructure(FirebaseMapDecorator data){
        if(!data.hasFields(requiredFields()))
            throw new IllegalArgumentException("The Firebase map is missing an ID");

        id = data.getLong("id");
    }

    /**
     * Return the FirebaseStructure's id
     * @return the FirebaseStructure's id
     */
    public Long getId(){
        return id;
    }

    /**
     * Returned all the required fields to construct a valid FirebaseStructure
     * @return the list of all required fields
     */
    public static List<String> requiredFields(){
        return Collections.singletonList("id");
    }
}
