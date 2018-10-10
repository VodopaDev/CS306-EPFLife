package ch.epfl.sweng.zuluzulu.View;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;

/**
 * An ArrayAdapter for Associations
 */
public class AssociationAdapter extends ArrayAdapter<Association> {
    private static final int layout_resource_id = R.layout.card_association;

    private Context context;
    private List<Association> data;

    /**
     * Basic constructor of an AssociationAdapter
     * @param context Context of the Fragment
     * @param data List of Associations to view
     */
    public AssociationAdapter(Context context, List<Association> data){
        super(context, layout_resource_id, data);
        this.context = context;
        this.data = data;
    }

    /**
     * Return a View of an Association in a GroupView in a form of a card with the
     * Association's name, short description and icon image
     * @param position position in the GroupView
     * @param convertView View where to put the Association's view
     * @param parent the ListView to put the view
     * @return the Association card view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        final AssociationHolder holder = row == null ? new AssociationHolder(): (AssociationHolder)row.getTag();

        if(row == null){
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layout_resource_id, parent, false);

            holder.name = row.findViewById(R.id.card_asso_name);
            holder.short_desc = row.findViewById(R.id.card_asso_short_desc);
            holder.icon = row.findViewById(R.id.card_asso_image);

            row.setTag(holder);
        }

        Association asso = data.get(position);
        holder.name.setText(asso.getName());
        holder.short_desc.setText(asso.getShortDesc());

        if(asso.getIcon() == null) {
            FirebaseStorage.getInstance().getReference("assos/asso" + asso.getId() + "_icon.png")
                    .getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            initIcon(uri, holder.icon);
                        }
                    });
        } else {
            initIcon(asso.getIcon(), holder.icon);
        }

        return row;
    }

    /**
     * Fetch an Image from the Internet and put it in an ImageView
     * @param uri Uri of the image to fetch
     * @param icon ImageView to put the image
     */
    private void initIcon(Uri uri, ImageView icon){
        Glide.with(context)
                .load(uri)
                .centerCrop()
                .into(icon);
    }

    /**
     * Static class to easily create an Association's specific View
     */
    static class AssociationHolder{
        ImageView icon;
        TextView name;
        TextView short_desc;
    }


}
