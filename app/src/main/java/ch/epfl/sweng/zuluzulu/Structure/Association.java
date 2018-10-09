package ch.epfl.sweng.zuluzulu.Structure;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

import ch.epfl.sweng.zuluzulu.View.AssociationCard;

/**
 * A simple class describing an Association
 * Has diverse getters and some functions to create views
 */
public class Association implements Comparable<Association> {
    private static final String IMAGE_PATH = "assos/asso";
    private static final String ICON_EXT = "_icon.png";

    private int id;
    private String name;
    private String short_desc;
    private String long_desc;

    private Uri icon;
    private Location pos;
    private List<Integer> admins;

    private int main_chat_id;
    private List<Integer> chats;
    private List<Integer> events;

    public Association(DocumentSnapshot snap){
        this(snap, null);
    }

    /**
     * Create an association using a DocumentSnapshot
     * @param snap the document snapshot
     * @throws IllegalArgumentException if the snapshot isn't an Association's snapshot
     */
    public Association(DocumentSnapshot snap, Uri iconUri) {
        if(!snapshotIsValid(snap))
            throw new IllegalArgumentException(snap.toString());

        id = ((Long) snap.get("id")).intValue();
        name = snap.getString("name");
        short_desc = snap.getString("short_desc");
        long_desc = snap.getString("long_desc");

        if(iconUri == null) {
            FirebaseStorage.getInstance()
                    .getReference()
                    .child(IMAGE_PATH + id + ICON_EXT)
                    .getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            icon = uri;
                        }
                    });
        }
        else{
            icon = iconUri;
        }
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
    public Uri getIcon(){
        return icon;
    }

    // TODO: add a test about this in CardViewTest
    /**
     * Return an AssociationCard view of the Association
     * @param context context to build the View
     * @return the AssociationCard
     */
    public AssociationCard getCardView(Context context){
        return new AssociationCard(context, this);
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
                );
    }

    /**
     * Compare two Associations using their name
     * @param asso the other Association to compare
     * @return compareTo of both Associations names
     */
    @Override
    public int compareTo(@NonNull Association asso) {
        return name.compareTo(asso.name);
    }
}
