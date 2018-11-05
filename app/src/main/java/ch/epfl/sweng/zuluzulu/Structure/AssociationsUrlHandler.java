package ch.epfl.sweng.zuluzulu.Structure;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.opencensus.common.Function;

public class AssociationsUrlHandler extends AsyncTask<String, Void, List<String>> {
    private final static String TAG = "AssociationsUrlHandler";

    // Function that will be executed onPostExecute
    private Function<List<String>, Void> listener;

    /**
     * Create a new AssociationUrlHandler
     *
     * @param listener The callback function that will be use on PostExecute
     */
    public AssociationsUrlHandler(Function<List<String>, Void> listener) {
        this.listener = listener;
    }


    @Override
    protected List<String> doInBackground(String... urls) {
        List<String> result = parseUrl(urls[0]);

        return result;
    }


    @Override
    protected void onPostExecute(List<String> strings) {
        listener.apply(strings);
    }

    /**
     * Connect to the URL, parse it and return the values found
     *
     * @param url The URL
     * @return ArrayList with all the values founded
     */
    private ArrayList<String> parseUrl(String url) {

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


        ArrayList<String> datas = null;
        try {
            // Parse the datas
            datas = parseData(urlConnection.getInputStream());
        } catch (IOException e) {
            Log.d(TAG, "Cannot parse datas");
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return datas;
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
     * This function will parse the datas and return a arraylist of strings
     *
     * @param inputStream Input stream
     * @return ArrayList of strings , values separated by a comma
     * @throws IOException On error - To be handled
     */
    private ArrayList<String> parseData(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(inputStream, "UTF-8"));

        // regex
        Pattern p = Pattern.compile("&#\\d+.* <a href=\"(.*?)\".*>(.*)</a>.*\\((.+)\\)<.*br />.*");

        ArrayList<String> results = new ArrayList<>();

        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            Matcher m = p.matcher(inputLine);
            if (m.find()) {
                // remove the span tag
                String description = m.group(3).replaceAll("<.*?>", "");

                StringBuilder sb = new StringBuilder();
                sb.append(m.group(1));
                sb.append(',');
                sb.append(m.group(2));
                sb.append(',');
                sb.append(description);

                // remplace html unicode to char
                String result = sb.toString()
                        .replaceAll("&#8217;", "'")
                        .replaceAll("&#8211;", "-")
                        .replaceAll("&gt;", ">")
                        .replaceAll("&amp;", "&");

                results.add(result);
            }
        }
        in.close();

        return results;
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
