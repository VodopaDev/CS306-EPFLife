package ch.epfl.sweng.zuluzulu.View;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import ch.epfl.sweng.zuluzulu.R;

public class AssociationCard extends RelativeLayout {
    private int id;
    private DocumentReference ref;

    private View rootView;
    private ImageView card_asso_image;
    private TextView card_asso_name;
    private TextView card_asso_short_desc;

    public AssociationCard(Context context){
        super(context);
    }

    public AssociationCard(Context context, DocumentReference ref){
        super(context);
        this.ref = ref;

        rootView = inflate(context, R.layout.card_association,this);
        card_asso_image = rootView.findViewById(R.id.card_asso_image);
        card_asso_name = rootView.findViewById(R.id.card_asso_name);
        card_asso_short_desc = rootView.findViewById(R.id.card_asso_short_desc);

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot result = task.getResult();
                id = ((Long)result.get("id")).intValue();
                card_asso_name.setText(result.get("name").toString());
                card_asso_short_desc.setText(result.get("short_desc").toString());


                StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("assos/asso"+id+"_icon.png");
                mStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Glide.with(getContext())
                                .load(uri)
                                .centerCrop()
                                .into(card_asso_image);
                    }
                });

                // TODO: make a click go to an association details page
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
        });
    }

    private View getView(){
        return rootView;
    }
}
