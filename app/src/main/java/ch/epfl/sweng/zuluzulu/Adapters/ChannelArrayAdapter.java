package ch.epfl.sweng.zuluzulu.Adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
        Channel currentChannel = channels.get(position);
        boolean isClickable = currentChannel.isClickable();

        int layoutResource = isClickable ? R.layout.channel : R.layout.channel_toofar;

        View view = LayoutInflater.from(mContext).inflate(layoutResource, parent, false);

        LinearLayout linearLayout = view.findViewById(R.id.channel_layout);
        int backgroundResource = isClickable ? R.drawable.channel_background : R.drawable.channel_notclickable_background;
        linearLayout.setBackgroundResource(backgroundResource);

        TextView name = view.findViewById(R.id.channel_name);
        name.setText(currentChannel.getName());

        TextView description = view.findViewById(R.id.channel_description);
        description.setText(currentChannel.getDescription());

        ImageView icon = view.findViewById(R.id.channel_icon);

        defaultIcon(icon);
        initImageView(currentChannel.getIconUri(), icon);

        if (!isClickable) {
            TextView distanceView = view.findViewById(R.id.channel_distance);
            int distance = (int) Math.round(currentChannel.getDistance());
            distanceView.setText("You are close to the channel (" + distance + "m).");
        }

        return view;
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
