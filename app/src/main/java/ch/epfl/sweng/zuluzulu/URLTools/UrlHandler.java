package ch.epfl.sweng.zuluzulu.URLTools;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import io.opencensus.common.Function;

public class UrlHandler <T> extends AsyncTask<String, Void, Void> {
    private final static String TAG = "UrlHandler";

    // Function that will be executed onPostExecute
    private Function<T, Void> listener;

    // The function that will parse the data
    private Function<BufferedReader, T> parser;

    // The result of doInBackground
    private T result = null;

    /**
     * Create a new UrlHandler
     *
     * @param listener The callback function that will be use on PostExecute
     */
    public UrlHandler(Function<T, Void> listener, Function<BufferedReader, T> parser) {
        this.listener = listener;
        this.parser = parser;
    }


    @Override
    protected Void doInBackground(String... urls) {
        if (urls.length > 0)
            result = parseUrl(urls[0]);
        else
            result = null;

        return null;
    }


    @Override
    protected void onPostExecute(Void value) {
        listener.apply(result);
    }


    /**
     * Connect to the URL, check if the response code is OK (200)
     *
     * @param url Url
     * @return HttpURLConnection or null of response is not OK
     * @throws IOException Throw exception if it cannot connect
     */
    private HttpURLConnection connect(String url) throws IOException {
        // Open url
        URL aURL = openUrl(url);

        // Open connection
        HttpURLConnection UrlConnection = (HttpURLConnection) aURL.openConnection();
        UrlConnection.connect();

        // Get HTTP response code
        int code = UrlConnection.getResponseCode();


        if (code != 200) {
            // Not OK response
            UrlConnection.disconnect();
            Log.d(TAG, "No 200 response code");
            return null;
        }

        return UrlConnection;
    }

    /**
     * Connect to the URL, parse it and return the values found
     *
     * @param url The URL
     * @return T Return object of type T with all the values founded
     */
    private T parseUrl(String url) {

        HttpURLConnection urlConnection;

        try {
            // Connect to the url
            urlConnection = connect(url);
        } catch (IOException e) {
            Log.d(TAG, "Cannot connect to the URL");
            e.printStackTrace();
            return null;
        }

        if (urlConnection == null) {
            Log.d(TAG, "null UrlConnection");
            return null;
        }

        T datas = null;
        try {
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            datas = parser.apply(bufferedReader);
            bufferedReader.close();
        } catch (IOException e) {
            Log.d(TAG, "Cannot read the page");
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return datas;
    }


    /**
     * Open the url
     *
     * @param url url
     * @return a URL object
     * @throws MalformedURLException On bad url
     */
    private URL openUrl(String url) throws MalformedURLException {
        return new URL(url);
    }


}
