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
        if (channelView == null) {
            channelView = LayoutInflater.from(mContext).inflate(R.layout.channel, parent, false);
        }

        Channel currentChannel = channels.get(position);
        boolean isClickable = currentChannel.isClickable();

        ConstraintLayout constraintLayout = channelView.findViewById(R.id.channel_layout);
        constraintLayout.setBackgroundResource(R.drawable.channel_background);

        TextView name = channelView.findViewById(R.id.channel_name);
        name.setText(currentChannel.getName());

        TextView description = channelView.findViewById(R.id.channel_description);
        description.setText(currentChannel.getDescription());

        ImageView icon = channelView.findViewById(R.id.channel_icon);

        defaultIcon(icon);
        initImageView(currentChannel.getIconUri(), icon);

        return channelView;
    }

    /**
     * Fetch an Image from the Internet and put it in an ImageView
     *
     * @param uri   Uri of the image to fetch
     * @param image ImageView to put the image
     */
    private void initImageView(Uri uri, ImageView image) {
        Glide.with(mContext)
                .load(uri)
                .centerCrop()
                .into(image);
    }

    private void defaultIcon(ImageView icon) {
        Glide.with(mContext)
                .load(new File("res/asso_cache/default_icon.png"))
                .centerCrop()
                .into(icon);
    }
}
