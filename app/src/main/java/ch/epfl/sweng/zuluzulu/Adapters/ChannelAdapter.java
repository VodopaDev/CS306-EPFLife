package ch.epfl.sweng.zuluzulu.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Channel;
import ch.epfl.sweng.zuluzulu.Structure.User;

public class ChannelAdapter extends ArrayAdapter<Channel> {

    private Context mContext;
    private List<Channel> channels;

    public ChannelAdapter(@NonNull Context context, List<Channel> list) {
        super(context, 0, list);
        mContext = context;
        channels = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View channelView = convertView;
        if(channelView == null) {
            channelView = LayoutInflater.from(mContext).inflate(R.layout.channel, parent, false);
        }

        Channel currentChannel = channels.get(position);

        TextView name = channelView.findViewById(R.id.channel_name);
        name.setText(currentChannel.getName());

        TextView description = channelView.findViewById(R.id.channel_description);
        description.setText(currentChannel.getDescription());

        return channelView;
    }
}
