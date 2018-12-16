package ch.epfl.sweng.zuluzulu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Utility.ImageLoader;

public class ChannelArrayAdapter extends ArrayAdapter<Channel> {

    private Context mContext;
    private List<Channel> channels;

    public ChannelArrayAdapter(@NonNull Context context, List<Channel> list) {
        super(context, 0, list);
        mContext = context;
        channels = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Channel currentChannel = channels.get(position);
        boolean isClickable = currentChannel.isAccessible();

        int layoutResource = isClickable ? R.layout.channel : R.layout.channel_toofar;

        View view = LayoutInflater.from(mContext).inflate(layoutResource, parent, false);

        LinearLayout linearLayout = view.findViewById(R.id.channel_layout);
        int backgroundResource = isClickable ? R.drawable.channel_background : R.drawable.channel_notclickable_background;
        linearLayout.setBackgroundResource(backgroundResource);

        TextView name = view.findViewById(R.id.channel_name);
        name.setText(currentChannel.getName());

        TextView description = view.findViewById(R.id.channel_description);
        description.setText(currentChannel.getShortDescription());

        ImageView icon = view.findViewById(R.id.channel_icon);
        ImageLoader.loadUriIntoImageView(icon, currentChannel.getIconUri(), getContext());

        if (!isClickable) {
            TextView distanceView = view.findViewById(R.id.channel_distance);
            int distance = (int) Math.round(currentChannel.getDistance());
            distanceView.setText("Tu es proche du canal (" + distance + "m).");
        }

        return view;
    }
}
