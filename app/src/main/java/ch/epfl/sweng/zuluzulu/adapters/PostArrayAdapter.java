package ch.epfl.sweng.zuluzulu.adapters;

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

import java.util.List;

import ch.epfl.sweng.zuluzulu.firebase.DatabaseFactory;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.structure.Post;
import ch.epfl.sweng.zuluzulu.structure.SuperMessage;
import ch.epfl.sweng.zuluzulu.structure.user.User;
import ch.epfl.sweng.zuluzulu.utility.TimeUtility;
import ch.epfl.sweng.zuluzulu.utility.Utils;

public class PostArrayAdapter extends ArrayAdapter<SuperMessage> {

    private Context mContext;
    private List<SuperMessage> posts;
    private User user;

    private Post currentPost;
    private TextView timeAgo;
    private TextView nbResponsesText;

    public PostArrayAdapter(@NonNull Context context, List<SuperMessage> list, User user) {
        super(context, 0, list);
        mContext = context;
        posts = list;
        this.user = user;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        currentPost = (Post) posts.get(position);
        boolean anonymous = currentPost.isAnonymous();

        View view = LayoutInflater.from(mContext).inflate(R.layout.post, parent, false);

        LinearLayout linearLayout = view.findViewById(R.id.post_linearLayout);
        TextView message = view.findViewById(R.id.post_msg);
        TextView senderName = view.findViewById(R.id.post_senderName);
        timeAgo = view.findViewById(R.id.post_time_ago_textview);
        ImageView upButton = view.findViewById(R.id.post_up_button);
        ImageView downButton = view.findViewById(R.id.post_down_button);
        TextView nbUpsText = view.findViewById(R.id.post_nb_ups_textview);
        nbResponsesText = view.findViewById(R.id.post_nb_responses_textview);

        linearLayout.setBackgroundColor(Color.parseColor(currentPost.getColor()));
        message.setText(currentPost.getMessage());

        String name = anonymous ? "Anonymous" : currentPost.getSenderName();
        senderName.setText(name);

        nbUpsText.setText("" + currentPost.getNbUps());

        if (currentPost.isReply()) {
            view.findViewById(R.id.post_white_line).setVisibility(View.INVISIBLE);
        }

        setUpTimeAgoField();
        setUpNbResponses(view);
        setUpUpDownButtons(currentPost, upButton, downButton, nbUpsText);
        updateUpsButtons(currentPost, upButton, downButton);

        return view;
    }

    private void setUpNbResponses(View view) {
        int nbResponses = currentPost.getNbReplies();
        nbResponsesText.setText("" + currentPost.getNbReplies());
        if (nbResponses == 0 || currentPost.isReply()) {
            view.findViewById(R.id.post_responses_linearlayout).setVisibility(LinearLayout.GONE);
        }
    }

    /**
     * Set up the correct time passed since the creation of the post in the field
     */
    private void setUpTimeAgoField() {
        long differenceInSeconds = TimeUtility.getMillisecondsSince(currentPost.getTime()) / 1000;
        if (differenceInSeconds < 60) {
            timeAgo.setText(differenceInSeconds + "s");
        } else if (differenceInSeconds < 3600) {
            timeAgo.setText(differenceInSeconds / 60 + "min");
        } else if (differenceInSeconds < 3600 * 24) {
            timeAgo.setText(differenceInSeconds / 3600 + "h");
        } else {
            timeAgo.setText(differenceInSeconds / (3600 * 24) + "d");
        }
    }

    private void setUpUpDownButtons(Post post, ImageView upButton, ImageView downButton, TextView nbUpsText) {
        upButton.setOnClickListener(v -> {
            updateDatabase(true, post, nbUpsText);
            updateUpsButtons(post, upButton, downButton);
        });

        downButton.setOnClickListener(v -> {
            updateDatabase(false, post, nbUpsText);
            updateUpsButtons(post, upButton, downButton);
        });
    }

    private void updateDatabase(boolean up, Post post, TextView nbUpsText) {
        if ((!up && post.downvoteWithUser(user.getSciper())) || (up && post.upvoteWithUser(user.getSciper()))) {
            DatabaseFactory.getDependency().updatePost(post);
            nbUpsText.setText("" + post.getNbUps());
        }
    }

    private void updateUpsButtons(Post post, ImageView upButton, ImageView downButton) {
        if (post.isUpByUser(user.getSciper())) {
            upButton.setImageResource(R.drawable.up_gray);
            downButton.setImageResource(R.drawable.down_transparent);
        } else if (post.isDownByUser(user.getSciper())) {
            downButton.setImageResource(R.drawable.down_gray);
            upButton.setImageResource(R.drawable.up_transparent);
        }
    }
}
