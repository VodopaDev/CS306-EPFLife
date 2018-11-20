package ch.epfl.sweng.zuluzulu.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.Utility.ImageLoader;

/**
 * An ArrayAdapter for Associations
 */
public class AssociationArrayAdapter extends ArrayAdapter<Association> {
    private static final int layout_resource_id = R.layout.card_association;

    private final Context context;
    private final List<Association> data;

    private final OnFragmentInteractionListener mListener;

    /**
     * Basic constructor of an AssociationArrayAdapter
     *
     * @param context Context of the Fragment
     * @param data    List of Associations to view
     */
    public AssociationArrayAdapter(Context context, List<Association> data, OnFragmentInteractionListener mListener) {
        super(context, layout_resource_id, data);
        this.mListener = mListener;
        this.context = context;
        this.data = data;
    }

    /**
     * Return a View of an Association in a GroupView in a form of a card with the
     * Association's name, short description and icon image
     *
     * @param position    position in the GroupView
     * @param convertView View where to put the Association's view
     * @param parent      the ListView to put the view
     * @return the Association card view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View asso_view = convertView;
        final AssociationHolder holder = asso_view == null ? new AssociationHolder() : (AssociationHolder) asso_view.getTag();

        if (asso_view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            asso_view = inflater.inflate(layout_resource_id, parent, false);

            holder.name = asso_view.findViewById(R.id.card_asso_name);
            holder.short_desc = asso_view.findViewById(R.id.card_asso_short_desc);
            holder.icon = asso_view.findViewById(R.id.card_asso_image);

            asso_view.setTag(holder);
        }

        final Association asso = data.get(position);
        holder.name.setText(asso.getName());
        holder.short_desc.setText(asso.getShortDescription());
        ImageLoader.loadUriIntoImageView(holder.icon, asso.getIconUri(), getContext());

        asso_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FRAG_CHANGE", "Switching to " + asso.getName() + " detailed view");
                mListener.onFragmentInteraction(CommunicationTag.OPEN_ASSOCIATION_DETAIL_FRAGMENT, asso);
            }
        });

        return asso_view;
    }

    /**
     * Static class to easily create an Association's specific View
     */
    static class AssociationHolder {
        ImageView icon;
        TextView name;
        TextView short_desc;
    }


}
