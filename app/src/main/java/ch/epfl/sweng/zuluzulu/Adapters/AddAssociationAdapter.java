package ch.epfl.sweng.zuluzulu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Association;
import ch.epfl.sweng.zuluzulu.URLTools.OnClickRecyclerView;
import ch.epfl.sweng.zuluzulu.Utility.ImageLoader;

public class AddAssociationAdapter extends RecyclerView.Adapter<AddAssociationAdapter.AddAssociationViewHolder> {
    private final List<Association> associationList;
    private final Context context;
    private final SparseBooleanArray checked;
    private final OnClickRecyclerView listener;

    public AddAssociationAdapter(Context context, List<Association> associationList, OnClickRecyclerView listener) {
        if (context == null || associationList == null) {
            throw new IllegalArgumentException("Null argument");
        }
        this.associationList = associationList;
        this.context = context;
        this.checked = new SparseBooleanArray();
        this.listener = listener;
    }

    @NonNull
    @Override
    public AddAssociationAdapter.AddAssociationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.add_card_association, parent, false);

        AddAssociationViewHolder vh = new AddAssociationViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AddAssociationViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if (position < 0 || position >= associationList.size()) {
            return;
        }
        holder.name.setText(associationList.get(position).getName());
        holder.short_desc.setText(associationList.get(position).getShortDescription());
        ImageLoader.loadUriIntoImageView(holder.icon, associationList.get(position).getIconUri(), context);

        holder.add_button.setEnabled(this.checked.get(position, true));
        holder.add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.add_button.setEnabled(false);
                checked.put(holder.getAdapterPosition(), false);
                listener.onClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return associationList.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class AddAssociationViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        ImageView icon;
        TextView name;
        TextView short_desc;
        ImageButton add_button;

        public AddAssociationViewHolder(View view) {
            super(view);
            this.name = view.findViewById(R.id.add_card_asso_name);
            this.short_desc = view.findViewById(R.id.add_card_asso_short_desc);
            this.icon = view.findViewById(R.id.add_card_asso_image);
            this.add_button = view.findViewById(R.id.add_card_add_button);
        }
    }

}
