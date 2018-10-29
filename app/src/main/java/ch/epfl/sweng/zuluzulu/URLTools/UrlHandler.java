package ch.epfl.sweng.zuluzulu.URLTools;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import io.opencensus.common.Function;

public class UrlHandler extends AsyncTask<String, Void, List<String>> {
    private final static String TAG = "UrlHandler";

    // Function that will be executed onPostExecute
    private Function<List<String>, Void> listener;

    // The function that will parse the data
    private Function<BufferedReader, List<String>> parser;

    /**
     * Create a new UrlHandler
     *
     * @param listener The callback function that will be use on PostExecute
     */
    public UrlHandler(Function<List<String>, Void> listener, Function<BufferedReader, List<String>> parser) {
        Log.e(TAG, "GERE");
        this.listener = listener;
        this.parser = parser;
    }


    @Override
    protected List<String> doInBackground(String... urls) {
        if (urls.length > 0)
            return parseUrl(urls[0]);
        else
            return null;
    }


    @Override
    protected void onPostExecute(List<String> value) {
        listener.apply(value);
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
    private List<String> parseUrl(String url) {

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

        List<String> datas = null;
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
