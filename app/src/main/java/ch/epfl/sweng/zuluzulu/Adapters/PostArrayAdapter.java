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

import java.util.List;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseProxy;
import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.Utility.Utils;
import ch.epfl.sweng.zuluzulu.User.User;

public class PostArrayAdapter extends ArrayAdapter<Post> {

    private Context mContext;
    private List<Post> posts;
    private User user;

    private Post currentPost;
    private TextView timeAgo;
    private TextView nbUpsText;
    private ImageView upButton;
    private ImageView downButton;

    public PostArrayAdapter(@NonNull Context context, List<Post> list, User user) {
        super(context, 0, list);
        mContext = context;
        posts = list;
        this.user = user;
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
        TextView nbResponsesText = view.findViewById(R.id.post_nb_responses_textview);

        linearLayout.setBackgroundColor(Color.parseColor(currentPost.getColor()));
        message.setText(currentPost.getMessage());

        String name = anonymous ? "Anonymous" : currentPost.getSenderName();
        senderName.setText(name);

        setUpTimeAgoField();

        nbUpsText.setText("" + currentPost.getNbUps());

        int nbResponses = currentPost.getNbResponses();
        nbResponsesText.setText("" + currentPost.getNbResponses());
        if (nbResponses == 0) {
            view.findViewById(R.id.post_responses_linearlayout).setVisibility(LinearLayout.GONE);
        }

        setUpUpDownButtons(currentPost, upButton, downButton, nbUpsText);
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
        } else if (differenceInSeconds < 3600) {
            timeAgo.setText(differenceInSeconds / 60 + "min");
        } else if (differenceInSeconds < 3600 * 24) {
            timeAgo.setText(differenceInSeconds / 3600 + "h");
        } else {
            timeAgo.setText(differenceInSeconds / (3600 * 24) + "d");
        }
    }

    private void setUpUpDownButtons(Post post, ImageView upButton, ImageView downButton, TextView nbUpsText) {
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase(true, post, nbUpsText);
                updateUpsButtons(post, upButton, downButton);
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatabase(false, post, nbUpsText);
                updateUpsButtons(post, upButton, downButton);
            }
        });
    }

    private void updateDatabase(boolean up, Post post, TextView nbUpsText) {
        if(!up && post.downvoteWithUser(user.getSciper()) || up && post.upvoteWithUser(user.getSciper())){
            FirebaseProxy.getInstance().updatePost(post);
            nbUpsText.setText("" + post.getNbUps());
        }
    }

    private void updateUpsButtons(Post post, ImageView upButton, ImageView downButton) {
        if (post.isUpByUser(user.getSciper())) {
            upButton.setImageResource(R.drawable.up_gray);
            downButton.setImageResource(R.drawable.down_transparent);
        }
        else if (post.isDownByUser(user.getSciper())) {
            downButton.setImageResource(R.drawable.down_gray);
            upButton.setImageResource(R.drawable.up_transparent);
        }
    }
}
