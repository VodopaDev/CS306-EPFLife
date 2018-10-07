package ch.epfl.sweng.zuluzulu.Structure;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.PropertyResourceBundle;

import ch.epfl.sweng.zuluzulu.View.AssociationCard;

public class Association implements Comparable<Association> {
    private static final String DOCUMENT_PATH = "assos_info/asso";
    private static final String IMAGE_PATH = "assos/asso";
    private static final String ICON_EXT = "_icon.png";

    private final DocumentReference ref;

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

    private boolean hasLoaded;

    public Association(DocumentReference ref) {
        this.ref = ref;
        hasLoaded = false;

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot result = task.getResult();
                    id = ((Long) result.get("id")).intValue();
                    name = result.get("name").toString();
                    short_desc = result.get("short_desc").toString();
                    long_desc = result.get("long_desc").toString();

                    StorageReference mStorage = FirebaseStorage.getInstance().getReference().child(IMAGE_PATH + id + ICON_EXT);
                    mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            icon = uri;
                            hasLoaded = true;
                        }
                    });
                }
            }
        });
    }

    public String getName(){
        return name;
    }

    public String getShortDesc(){
        return short_desc;
    }

    public String getLongDesc(){
        return long_desc;
    }

    public Uri getIcon(){
        return icon;
    }

    public static Association fromRef(DocumentReference ref){
        return new Association(ref);
    }

    public static Association fromId(int id){
        return fromRef(FirebaseFirestore.getInstance().document(DOCUMENT_PATH + id));
    }

    public boolean hasLoaded(){
        return hasLoaded;
    }

    public AssociationCard getCardView(Context context){
        while (!hasLoaded){}
        return new AssociationCard(context, this);
    }

    @Override
    public int compareTo(@NonNull Association asso) {
        return name.compareTo(asso.name);
    }
}
