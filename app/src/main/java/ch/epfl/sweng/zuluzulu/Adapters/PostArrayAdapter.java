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

import java.util.List;

import ch.epfl.sweng.zuluzulu.R;
import ch.epfl.sweng.zuluzulu.Structure.Post;
import ch.epfl.sweng.zuluzulu.Structure.Utils;

public class PostArrayAdapter extends ArrayAdapter<Post> {

    private Context mContext;
    private List<Post> posts;

    private Post currentPost;
    private TextView timeAgo;
    private ImageView upButton;
    private ImageView downButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference;

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
        TextView nbUps = view.findViewById(R.id.post_nb_ups_textview);
        TextView nbResponses = view.findViewById(R.id.post_nb_responses_textview);

        collectionReference = db.collection("channels/channel" + currentPost.getChannel().getId() + "/posts");

        linearLayout.setBackgroundColor(Color.parseColor(currentPost.getColor()));
        message.setText(currentPost.getMessage());

        String name = anonymous ? "Anonymous" : currentPost.getSenderName();
        senderName.setText(name);

        setUpTimeAgoField();

        nbUps.setText("" + currentPost.getNbUps());
        nbResponses.setText("" + currentPost.getNbResponses());

        setUpUpDownButtons();

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

    /**
     * Set up an onClick listener on the up and down buttons
     */
    private void setUpUpDownButtons() {
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newNbUps = currentPost.getNbUps() + 1;
                List<String> upScipers = currentPost.getUpScipers();
                upScipers.add(currentPost.getUserReading().getSciper());

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = db.collection("channels/channel" + currentPost.getChannel().getId() + "/posts");
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
