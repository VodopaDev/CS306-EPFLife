package ch.epfl.sweng.zuluzulu.Structure;

import android.location.Location;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

/**
 * A simple class describing an Association
 * Has diverse getters and some functions to create views
 */
public class Association implements Serializable {

    private int id;
    private String name;
    private String short_desc;
    private String long_desc;

    private Uri icon_uri;

    /**
     * Create an association using a DocumentSnapshot
     * @param snap the document snapshot
     * @throws IllegalArgumentException if the snapshot isn't an Association's snapshot
     */
    public Association(DocumentSnapshot snap) {
        if(!snapshotIsValid(snap))
            throw new NullPointerException();

        id = ((Long) snap.get("id")).intValue();
        name = snap.getString("name");
        short_desc = snap.getString("short_desc");
        long_desc = snap.getString("long_desc");
        icon_uri = Uri.parse(snap.getString("icon_uri"));
    }

    /**
     * Return the Association's id
     * @return the id
     */
    public int getId(){
        return id;
    }

    /**
     * Return the Association's name
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * Return the Association's short description
     * @return the short description
     */
    public String getShortDesc(){
        return short_desc;
    }

    /**
     * Return the Association's long description
     * @return the long description
     */
    public String getLongDesc(){
        return long_desc;
    }

    /**
     * Return the Association's icon Uri
     * @return the icon Uri
     */
    @Nullable
    public Uri getIconUri(){
        return icon_uri;
    }

    /**
     * Check if a DocumentSnapshot correspond to an Association's one
     * @param snap the DocumentSnapshot
     * @return true if it is a valid snapshot, false otherwise
     */
    private boolean snapshotIsValid(DocumentSnapshot snap){
        return !(snap == null
                || snap.get("id") == null
                || snap.getString("short_desc") == null
                || snap.getString("long_desc") == null
                || snap.getString("name") == null
                || snap.getString("icon_uri") == null
                );
    }

    /**
     * Return a Comparator for two Associations using their names
     * @return compareTo of two Associations names
     */
    public static Comparator<Association> getComparator(){
        return new Comparator<Association>() {
            @Override
            public int compare(Association o1, Association o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
    }
}
