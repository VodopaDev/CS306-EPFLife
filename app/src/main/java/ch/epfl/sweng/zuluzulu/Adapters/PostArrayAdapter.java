package ch.epfl.sweng.zuluzulu.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Post;

public class PostArrayAdapter extends ArrayAdapter<Post> {

    private Context mContext;
    private List<Post> posts;

    public PostArrayAdapter(@NonNull Context context, List<Post> list) {
        super(context, 0, list);
        mContext = context;
        posts = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Post currentPost = posts.get(position);
        boolean anonymous = currentPost.isAnonymous();

        View view = LayoutInflater.from(mContext).inflate(R.layout.post, parent, false);

        LinearLayout linearLayout = view.findViewById(R.id.post_linearLayout);
        linearLayout.setBackgroundColor(Color.parseColor(currentPost.getColor()));

        TextView message = view.findViewById(R.id.post_msg);
        TextView senderName = view.findViewById(R.id.post_senderName);

        senderName.setText(currentPost.getSenderName());
        message.setText(currentPost.getMessage());

        return view;
    }
}
