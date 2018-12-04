package ch.epfl.sweng.zuluzulu.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Event;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;
import ch.epfl.sweng.zuluzulu.Utility.ImageLoader;

public class UpcomingEventArrayAdapter extends ArrayAdapter<Event>{
    private static final int layout_resource_id = R.layout.card_upcoming_event;

    private final Context context;
    private final List<Event> data;

    private final OnFragmentInteractionListener mListener;

    private AuthenticatedUser user;

    /**
     * Basic constructor of an EventArrayAdapter
     *
     * @param context Context of the Fragment
     * @param data    List of events to view
     */
    public UpcomingEventArrayAdapter(Context context, List<Event> data, OnFragmentInteractionListener mListener, User user) {
        super(context, layout_resource_id, data);
        this.mListener = mListener;
        this.context = context;
        this.data = data;
        if (user.isConnected()) {
            this.user = (AuthenticatedUser) user;
        } else {
            this.user = null;
        }

    }

    /**
     * Return a View of an event in a GroupView in a form of a card with the
     * Event's name, short description and icon image
     *
     * @param position    position in the GroupView
     * @param convertView View where to put the Event's view
     * @param parent      the ListView to put the view
     * @return the Event card view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View event_view = convertView;
        final UpcomingEventArrayAdapter.UpcomingEventHolder holder = event_view == null ?
                new UpcomingEventArrayAdapter.UpcomingEventHolder() : (UpcomingEventArrayAdapter.UpcomingEventHolder) event_view.getTag();

        if (event_view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            event_view = inflater.inflate(layout_resource_id, parent, false);

            holder.name = event_view.findViewById(R.id.card_event_name);
            holder.short_desc = event_view.findViewById(R.id.card_event_desc);
            holder.icon = event_view.findViewById(R.id.card_event_image);
            holder.start_date = event_view.findViewById(R.id.card_event_date);
            holder.likes = event_view.findViewById(R.id.card_event_nb_likes);

            event_view.setTag(holder);
        }

        final Event event = data.get(position);
        holder.name.setText(event.getName());
        holder.short_desc.setText(event.getShortDescription());
        ImageLoader.loadUriIntoImageView(holder.icon, event.getIconUri(), getContext());
        holder.likes.setText(String.valueOf(event.getLikes()));

        event_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FRAG_CHANGE", "Switching to " + event.getName() + "detailed view");
                mListener.onFragmentInteraction(CommunicationTag.OPEN_EVENT_DETAIL_FRAGMENT, event);
            }
        });

        holder.start_date.setText(event.getDateTimeUser());

        return event_view;
    }

    /**
     * Static class to easily create an Event's specific View
     */
    static class UpcomingEventHolder {
        ImageView icon;
        TextView name;
        TextView short_desc;
        TextView start_date;
        TextView likes;
        ImageButton likes_button;
    }
}