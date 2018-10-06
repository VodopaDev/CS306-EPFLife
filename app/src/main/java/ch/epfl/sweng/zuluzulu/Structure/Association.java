package ch.epfl.sweng.zuluzulu.Structure;

import android.content.Context;
import android.location.Location;
import android.media.Image;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Text;

import java.util.List;

import ch.epfl.sweng.zuluzulu.R;

public class Association{
    private boolean isLoaded;
    DocumentReference ref;

    private int id;
    private String name;
    private String short_desc;
    private String long_desc;
    private Image icon;
    private Location pos;
    private List<Integer> admins;

    private int main_chat_id;
    private List<Integer> chats;
    private List<Integer> events;

    private View card_view;

    // TODO: Get data from cloud service using the id
    public Association(DocumentReference ref, Context context) {
        this.ref = ref;
        id = 0;
        name = "";
        short_desc = "";
        long_desc = "";
        card_view = View.inflate(context, R.layout.card_association, null);

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot result = task.getResult();
                id = ((Long) result.get("id")).intValue();
                name = result.get("name").toString();
                short_desc = result.get("short_desc").toString();
                long_desc = result.get("long_desc").toString();

                ((TextView)card_view.findViewById(R.id.card_asso_name)).setText(name);
                ((TextView)card_view.findViewById(R.id.card_asso_short_desc)).setText(short_desc);
            }
        });
    }

    public void loadOnlineValues(){

    }

    // TODO: Add a method to add/remove one User to admins, same for chats and events
    // TODO: Check inputs before changing fields
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getShortDesc(){
        return short_desc;
    }
    public String getLongDesc(){
        return long_desc;
    }
    public View getCardView(){
        return card_view;
    }
}
