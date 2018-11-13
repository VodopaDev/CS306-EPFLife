package ch.epfl.sweng.zuluzulu.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
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
        View channelView = convertView;
        if (channelView == null)
            channelView = LayoutInflater.from(mContext).inflate(R.layout.channel, parent, false);

        Channel currentChannel = channels.get(position);

        ConstraintLayout constraintLayout = channelView.findViewById(R.id.channel_layout);
        constraintLayout.setBackgroundResource(R.drawable.channel_background);

        TextView name = channelView.findViewById(R.id.channel_name);
        name.setText(currentChannel.getName());

        TextView description = channelView.findViewById(R.id.channel_description);
        description.setText(currentChannel.getDescription());

        ImageView icon = channelView.findViewById(R.id.channel_icon);
        ImageLoader.loadUriIntoImageView(icon, currentChannel.getIconUri(), getContext());

        return channelView;
    }
}
