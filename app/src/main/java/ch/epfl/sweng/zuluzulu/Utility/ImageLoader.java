package ch.epfl.sweng.zuluzulu.Utility;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public interface ImageLoader {

    static void loadUriIntoImageView(ImageView container, Uri uri, Context context){
        if(context == null)
            Log.e("GLIDE","Can't load an Uri in an ImageView with a null Context");
        else{
            Glide.with(context)
                    .load(uri)
                    .centerCrop()
                    .into(container);
        }
    }

}
