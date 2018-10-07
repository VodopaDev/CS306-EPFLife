package ch.epfl.sweng.zuluzulu.View;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;

public class AssociationCard extends RelativeLayout {
    private View rootView;
    private ImageView card_asso_image;
    private TextView card_asso_name;
    private TextView card_asso_short_desc;

    public AssociationCard(Context context){
        super(context);
    }

    public AssociationCard(Context context, Association asso){
        super(context);
        while(!asso.hasLoaded()){}

        rootView = inflate(context, R.layout.card_association,this);
        card_asso_image = rootView.findViewById(R.id.card_asso_image);
        card_asso_name = rootView.findViewById(R.id.card_asso_name);
        card_asso_short_desc = rootView.findViewById(R.id.card_asso_short_desc);

        card_asso_name.setText(asso.getName());
        card_asso_short_desc.setText(asso.getShortDesc());
        Glide.with(getContext())
            .load(asso.getIcon())
            .centerCrop()
            .into(card_asso_image);

         // TODO: make a click go to an association details page
    }
}
