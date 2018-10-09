package ch.epfl.sweng.zuluzulu.View;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;

/**
 * A class describing a card-shaped View for an Association
 * This view shows a name, a short description and a picture about an Association
 * You can click it to go to the Association page
 */
public class AssociationCard extends RelativeLayout implements Comparable<AssociationCard> {
    private View rootView;
    private ImageView card_asso_image;
    private TextView card_asso_name;
    private TextView card_asso_short_desc;

    /**
     * Default base constructor (not to be used!)
     * Gives the base View of an Association
     * @param context the context to build the Card
     * @throws IllegalArgumentException if the context is null
     */
    public AssociationCard(Context context){
        super(context);
        if(context == null)
            throw new IllegalArgumentException();

        initViews(context);
    }

    /**
     * Base constructor using an Association to set up texts and picture
     * @param context the context to build the Card
     * @param asso the association to get data from
     * @throws IllegalArgumentException if the context or the Association is null
     */
    public AssociationCard(Context context, Association asso){
        super(context);
        if(asso == null || context == null)
            throw new IllegalArgumentException();

        initViews(context);
        card_asso_name.setText(asso.getName());
        card_asso_short_desc.setText(asso.getShortDesc());

        Uri icon_uri = asso.getIcon();
        if(icon_uri == null){
            FirebaseStorage.getInstance()
                    .getReference("assos/asso"+asso.getId()+"_icon.png")
                    .getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            initIcon(uri);
                        }
                    });
        }
        else{
            initIcon(icon_uri);
        }
        makeClickable();
    }

    /**
     * Initialize all views constituting the AssociationCard
     * @param context the Context to use to create the rootView;
     */
    private void initViews(Context context){
        assert(context != null);
        rootView = inflate(context, R.layout.card_association,this);
        card_asso_image = rootView.findViewById(R.id.card_asso_image);
        card_asso_name = rootView.findViewById(R.id.card_asso_name);
        card_asso_short_desc = rootView.findViewById(R.id.card_asso_short_desc);
    }

    /**
     * Set the picture in the ImageView using an Uri
     * @param uri the picture's Uri
     */
    private void initIcon(Uri uri){
        assert(uri != null);
        Glide.with(getContext())
                .load(uri)
                .centerCrop()
                .into(card_asso_image);
    }

    // TODO: make a click go to an association details page
    /**
     * Create a listener on the the whole view to make a click go to the Association's details page
     */
    private void makeClickable(){
    }

    /**
     * Compare two AssociationCards using their card_asso_name
     * @param o the other AssociationCard
     * @return compareTo between their names
     */
    @Override
    public int compareTo(@NonNull AssociationCard o) {
        return card_asso_name.getText().toString().compareTo(o.card_asso_name.getText().toString());
    }
}
