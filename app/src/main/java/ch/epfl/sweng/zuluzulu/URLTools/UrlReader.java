package ch.epfl.sweng.zuluzulu.URLTools;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class UrlReader implements Reader {
    private static final String TAG = "URL_READER";

    private HttpURLConnection urlConnection;

    protected UrlReader(){
        this.urlConnection = null;
    }


    @Override
    public BufferedReader read(String url) {
        try {
            // Connect to the url
            urlConnection = connect(url);
        } catch (IOException e) {
            Log.d(TAG, "Cannot connect to the URL");
            e.printStackTrace();
            return null;
        }

        if (urlConnection == null) {
            Log.d(TAG, "UrlConnection is null");
            return null;
        }

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            Log.d(TAG, "Cannot read the page");
            e.printStackTrace();
            urlConnection.disconnect();
        }

        return bufferedReader;
    }

    /**
     * Disconnect the http connection
     */
    public void disconnect(){
        if(urlConnection != null) {
            urlConnection.disconnect();
            urlConnection = null;
        }
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
        URL aURL = new URL(url);

        // Open connection
        HttpURLConnection UrlConnection = (HttpURLConnection) aURL.openConnection();

        // to open some epfl pages
        UrlConnection.setRequestProperty("Cookie", "gdpr=accept");

        // Get HTTP response code
        int code = UrlConnection.getResponseCode();

        // Redirect if needed
        if (code == HttpURLConnection.HTTP_MOVED_TEMP || code == HttpURLConnection.HTTP_MOVED_PERM) {
            String newUrl = UrlConnection.getHeaderField("Location");
            UrlConnection = (HttpURLConnection) new URL(newUrl).openConnection();
            UrlConnection.setRequestProperty("Cookie", "gdpr=accept");
            code = UrlConnection.getResponseCode();
        }

        if (code != HttpURLConnection.HTTP_OK) {
            Log.d(TAG, "No 200 response code");
            return null;
        }
        UrlConnection.connect();

        return UrlConnection;
    }
}
