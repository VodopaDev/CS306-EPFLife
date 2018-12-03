package ch.epfl.sweng.zuluzulu.Utility;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Date;
import java.util.List;
import java.util.Random;

import ch.epfl.sweng.zuluzulu.Adapters.AssociationArrayAdapter;
import ch.epfl.sweng.zuluzulu.Adapters.EventArrayAdapter;
import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.Fragments.EventFragment;
import ch.epfl.sweng.zuluzulu.IdlingResource.IdlingResourceFactory;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;

public class FirebaseUtils {

    public static void fillEventFromDatabase(String collectionPath, String order, List<Event> events, EventArrayAdapter adapter) {
        FirebaseFirestore.getInstance().collection(collectionPath).orderBy(order).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snap_list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snap : snap_list) {
                            FirebaseMapDecorator fmap = new FirebaseMapDecorator(snap);
                            if (fmap.hasFields(Event.FIELDS) && fmap.getDate("start_date").compareTo(new Date(System.currentTimeMillis())) >= 0) {
                                Event event = new Event.EventBuilder().build(fmap);
                                events.add(event);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Snackbar.make(EventFragment.getView(), "Loading error, check your connection", 5000).show();
                        Log.e("EVENT_LIST", "Error fetching event date\n" + e.getMessage());
                    }
                });
    }

    private void fillAssociationLists(String sortOption, List<Association> assos, AssociationArrayAdapter adapter) {
        IdlingResourceFactory.incrementCountingIdlingResource();
        FirebaseFirestore.getInstance().collection("assos_info").orderBy(sortOption).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snap_list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snap : snap_list) {
                            FirebaseMapDecorator data = new FirebaseMapDecorator(snap);
                            if (data.hasFields(Association.FIELDS)) {
                                Association asso = new Association(data);
                                assos.add(asso);
                                adapter.notifyDataSetChanged();
                            }
                        }

                        IdlingResourceFactory.decrementCountingIdlingResource();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Snackbar.make(getView(), "Loading error, check your connection", 5000).show();
                        Log.e("ASSO_LIST", "Error fetching association date\n" + e.getMessage());
                        IdlingResourceFactory.decrementCountingIdlingResource();
                    }
                });
    }
}
