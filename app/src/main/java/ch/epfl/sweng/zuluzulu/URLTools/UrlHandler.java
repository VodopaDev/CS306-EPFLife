package ch.epfl.sweng.zuluzulu.URLTools;

import android.os.AsyncTask;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import io.opencensus.common.Function;

public class UrlHandler extends AsyncTask<String, Void, Pair<String, List<String>>> {
    private final static String TAG = "UrlHandler";

    // Function that will be executed onPostExecute
    private Function<Pair<String, List<String>>, Void> listener;

    private UrlReader urlReader;

    // The function that will parse the data
    private Parser<List<String>> parser;

    /**
     * Create a new UrlHandler
     *
     * @param listener The callback function that will be use on PostExecute
     */
    public UrlHandler(Function<Pair<String, List<String>>, Void> listener, Parser<List<String>> parser) {
        this.listener = listener;
        this.parser = parser;
        this.urlReader = UrlReaderFactory.getDependency();
    }


    @Override
    protected Pair<String, List<String>> doInBackground(String... urls) {
        if (urls.length > 0) {
            Pair<String, List<String>> result = new Pair<>(urls[0], parseUrl(urls[0]));
            return result;
        } else
            return null;
    }

    @Override
    protected void onPostExecute(Pair<String, List<String>> value) {
        if (null != value && value.second != null) {
            listener.apply(value);
        } else {
            listener.apply(null);
        }
    }

    /**
     * Connect to the URL, parse it and return the values found
     *
     * @param url The URL
     * @return T Return object of type T with all the values founded
     */
    private List<String> parseUrl(String url) {
        BufferedReader bf = urlReader.read(url);

        if (bf == null) {
            return null;
        }

        List<String> result = parser.parse(bf);

        try {
            bf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

}
