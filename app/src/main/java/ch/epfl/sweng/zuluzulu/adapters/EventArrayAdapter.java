package ch.epfl.sweng.zuluzulu.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.zuluzulu.CommunicationTag;
import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.OnFragmentInteractionListener;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.structure.Event;
import ch.epfl.sweng.zuluzulu.user.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.user.User;
import ch.epfl.sweng.zuluzulu.utility.ImageLoader;
import ch.epfl.sweng.zuluzulu.utility.Utils;


/**
 * An ArrayAdapter for Event
 */
public class EventArrayAdapter extends ArrayAdapter<Event> {
    private static final int layout_resource_id = R.layout.card_event;

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
    public EventArrayAdapter(Context context, List<Event> data, OnFragmentInteractionListener mListener, User user) {
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
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View event_view = convertView;
        final EventHolder holder = event_view == null ? new EventHolder() : (EventHolder) event_view.getTag();

        if (event_view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            event_view = inflater.inflate(layout_resource_id, parent, false);

            holder.name = event_view.findViewById(R.id.card_event_name);
            holder.short_desc = event_view.findViewById(R.id.card_event_short_desc);
            holder.icon = event_view.findViewById(R.id.card_event_image);
            holder.start_date = event_view.findViewById(R.id.card_event_date);
            holder.likes = event_view.findViewById(R.id.card_event_like_text);
            holder.likes_button = event_view.findViewById(R.id.card_event_like_button);

            event_view.setTag(holder);
        }

        final Event event = data.get(position);
        holder.name.setText(event.getName());
        holder.short_desc.setText(event.getShortDescription());
        ImageLoader.loadUriIntoImageView(holder.icon, event.getIconUri(), getContext());
        holder.start_date.setText(event.getDateTimeUser(false));
        holder.likes.setText(context.getResources().getQuantityString(R.plurals.evend_card_followers, event.getLikes(), event.getLikes()));

        holder.likes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null && user.isConnected()) {
                    if (!user.isFollowedEvent(event.getId())) {
                        event.addFollower(user.getSciper());
                        user.addFollowedEvent(event.getId());
                        user.addFollowedChannel(event.getChannelId());
                        holder.likes_button.setSelected(true);
                        DatabaseFactory.getDependency().addEventToUserFollowedEvents(event, user);
                        Snackbar.make(parent, context.getString(R.string.event_followed), Snackbar.LENGTH_SHORT).show();
                    } else {
                        event.removeFollower(user.getSciper());
                        user.removeFollowedEvent(event.getId());
                        user.removeFollowedChannel(event.getChannelId());
                        holder.likes.setText(String.valueOf(event.getLikes()));
                        holder.likes_button.setSelected(false);
                        DatabaseFactory.getDependency().removeEventFromUserFollowedEvents(event, user);
                        Snackbar.make(parent, context.getString(R.string.event_unfollowed), Snackbar.LENGTH_SHORT).show();
                    }
                    holder.likes.setText(context.getResources().getQuantityString(R.plurals.evend_card_followers, event.getLikes(), event.getLikes()));
                } else {
                    Utils.showConnectSnackbar(parent);
                }
            }
        });

        if (user != null && user.isConnected()) {
            holder.likes_button.setSelected(user.isFollowedEvent(event.getId()));

        } else {
            holder.likes_button.setSelected(false);
        }

        event_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("FRAG_CHANGE", "Switching to " + event.getName() + "detailed view");
                mListener.onFragmentInteraction(CommunicationTag.OPEN_EVENT_DETAIL_FRAGMENT, event);
            }
        });

        return event_view;
    }

    /**
     * Static class to easily create an Event's specific View
     */
    static class EventHolder {
        ImageView icon;
        TextView name;
        TextView short_desc;
        TextView start_date;
        TextView likes;
        ImageButton likes_button;
    }
}
