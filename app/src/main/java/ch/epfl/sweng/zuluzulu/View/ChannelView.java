package ch.epfl.sweng.zuluzulu.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import ch.epfl.sweng.zuluzulu.R;

public class ChannelView extends RelativeLayout {

    private View rootView;
    private TextView name;

    private int id;

    public ChannelView(Context context){
        super(context);
    }

    public ChannelView(Context context, DocumentReference ref) {
        super(context);

        rootView = inflate(context, R.layout.channel_view, this);
        name = rootView.findViewById(R.id.channel_name);

        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot result = task.getResult();
                id = ((Long)result.get("id")).intValue();
                name.setText(result.get("name").toString());

                Log.d("CHANNEL_VIEW", "Id and name set for channel " + id);

                // TODO: make a click go to open the channel
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { }
                });
            }
        });
    }
}
