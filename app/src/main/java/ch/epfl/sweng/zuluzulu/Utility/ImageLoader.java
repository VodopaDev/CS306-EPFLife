package ch.epfl.sweng.zuluzulu.Utility;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.LazyHeaders;

public interface ImageLoader {

    static void loadUriIntoImageView(ImageView container, Uri uri, Context context) {
        if (context == null)
            Log.e("GLIDE", "Can't load an Uri in an ImageView with a null Context");
        else {
            if (uri.toString().length() > 0) {
                Headers auth = new LazyHeaders.Builder() // This can be cached in a field and reused later.
                        .addHeader("Cookie", "gdpr=accept")
                        .build();
                GlideUrl glideUrl = new GlideUrl(uri.toString(), auth);
                Glide.with(context)
                        .load(glideUrl)
                        .fitCenter()
                        .into(container);
            }
        }
    }

}
