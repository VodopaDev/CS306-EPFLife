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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class Association{
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

    public Association(DocumentReference ref, Context context) {
        this.ref = ref;
        id = 0;
        name = "";
        short_desc = "";
        long_desc = "";

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot result = task.getResult();
                    id = ((Long) result.get("id")).intValue();
                    name = result.get("name").toString();
                    short_desc = result.get("short_desc").toString();
                    long_desc = result.get("long_desc").toString();

                    StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("assos/asso" + id + "_icon.png");
                    mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            icon = uri;
                        }
                    });
                }
            }
        });
    }

}
