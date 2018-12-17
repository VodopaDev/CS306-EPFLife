package ch.epfl.sweng.zuluzulu.utility;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.LazyHeaders;

public interface ImageLoader {

    /**
     * Load an Uri in an image view
     *
     * @param container the image view
     * @param uri       the uri to load
     * @param context   context to use
     */
    static void loadUriIntoImageView(ImageView container, Uri uri, Context context) {
        if (context == null) {
            Log.e("GLIDE", "Can't load an Uri in an ImageView with a null Context");
            return;
        }
        if (uri.toString().contains("http")) {
            Headers auth = new LazyHeaders.Builder() // This can be cached in a field and reused later.
                    .addHeader("Cookie", "gdpr=accept")
                    .build();
            GlideUrl glideUrl = new GlideUrl(uri.toString(), auth);
            Glide.with(context)
                    .load(glideUrl)
                    .fitCenter()
                    .into(container);
        } else {
            Glide.with(context)
                    .load(uri)
                    .fitCenter()
                    .into(container);
        }
    }

    /**
     * Load a drawable in an image view
     *
     * @param container the image view
     * @param drawable  the drawable to load
     * @param context   context to use
     */
    static void loadDrawableIntoImageView(ImageView container, int drawable, Context context) {
        if (context == null) {
            Log.e("GLIDE", "Can't load a drawable in an ImageView with a null Context");
            return;
        }

        Glide.with(context)
                .load(drawable)
                .fitCenter()
                .into(container);
    }
}