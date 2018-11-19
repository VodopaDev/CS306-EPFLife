package ch.epfl.sweng.zuluzulu.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.Structure.Utils;

public class PostArrayAdapter extends ArrayAdapter<Post> {

    private Context mContext;
    private List<Post> posts;

    private Post currentPost;
    private TextView timeAgo;
    private TextView nbUpsText;
    private ImageView upButton;
    private ImageView downButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PostArrayAdapter(@NonNull Context context, List<Post> list) {
        super(context, 0, list);
        mContext = context;
        posts = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        currentPost = posts.get(position);
        boolean anonymous = currentPost.isAnonymous();

        View view = LayoutInflater.from(mContext).inflate(R.layout.post, parent, false);

        LinearLayout linearLayout = view.findViewById(R.id.post_linearLayout);
        TextView message = view.findViewById(R.id.post_msg);
        TextView senderName = view.findViewById(R.id.post_senderName);
        timeAgo = view.findViewById(R.id.post_time_ago_textview);
        upButton = view.findViewById(R.id.post_up_button);
        downButton = view.findViewById(R.id.post_down_button);
        nbUpsText = view.findViewById(R.id.post_nb_ups_textview);
        TextView nbResponses = view.findViewById(R.id.post_nb_responses_textview);

        linearLayout.setBackgroundColor(Color.parseColor(currentPost.getColor()));
        message.setText(currentPost.getMessage());

        String name = anonymous ? "Anonymous" : currentPost.getSenderName();
        senderName.setText(name);

        setUpTimeAgoField();

        nbUpsText.setText("" + currentPost.getNbUps());
        nbResponses.setText("" + currentPost.getNbResponses());

        setUpUpDownButtons(currentPost, view);
        updateUpsButtons(currentPost, upButton, downButton);

        return view;
    }

    /**
     * Set up the correct time passed since the creation of the post in the field
     */
    private void setUpTimeAgoField() {
        long differenceInSeconds = Utils.getMillisecondsSince(currentPost.getTime()) / 1000;
        if (differenceInSeconds < 60) {
            timeAgo.setText(differenceInSeconds + "s");
        }
        else if (differenceInSeconds < 3600) {
            timeAgo.setText(differenceInSeconds/60 + "min");
        }
        else if (differenceInSeconds < 3600 * 24){
            timeAgo.setText(differenceInSeconds/3600 + "h");
        }
        else {
            timeAgo.setText(differenceInSeconds/(3600*24) + "d");
        }
    }

    private void setUpUpDownButtons(Post post, View view) {
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase(true, post, view);
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase(false, post, view);
            }
        });
    }

    private void updateDatabase(boolean up, Post post, View view) {
        if (!post.isUpByUser() && !post.isDownByUser()) {
            ImageView upButton = view.findViewById(R.id.post_up_button);
            ImageView downButton = view.findViewById(R.id.post_down_button);
            TextView nbUpsText = view.findViewById(R.id.post_nb_ups_textview);
            int nbUps = post.getNbUps() + (up ? 1 : -1);
            DocumentReference documentReference = db.collection("channels/channel" + post.getChannelId() + "/posts").document(post.getId());
            if (up) {
                List<String> upScipers = post.getUpScipers();
                upScipers.add(post.getUserSciper());

                documentReference.update(
                        "nbUps", nbUps,
                        "upScipers", upScipers
                );
                post.setUpByUser(true);
            } else {
                List<String> downScipers = post.getDownScipers();
                downScipers.add(post.getUserSciper());

                documentReference.update(
                        "nbUps", nbUps,
                        "downScipers", downScipers
                );
                post.setDownByUser(true);
            }
            updateUpsButtons(post, upButton, downButton);
            nbUpsText.setText("" + nbUps);
        }
    }

    private void updateUpsButtons(Post post, ImageView upButton, ImageView downButton) {
        if (post.isUpByUser()) {
            upButton.setImageResource(R.drawable.up_gray);
            downButton.setImageResource(R.drawable.down_transparent);
        }
        else if (post.isDownByUser()) {
            downButton.setImageResource(R.drawable.down_gray);
            upButton.setImageResource(R.drawable.up_transparent);
        }
    }
}
