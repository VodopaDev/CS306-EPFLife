package ch.epfl.sweng.zuluzulu.Structure;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.opencensus.common.Function;

public class AssociationsUrlHandler extends AsyncTask<String, Void, ArrayList<String>> {

    // Function that will be executed onPostExecute
    private Function<ArrayList<String>, Void> listener;

    public AssociationsUrlHandler(Function<ArrayList<String>, Void> listener) {
        this.listener = listener;
        this.execute("https://associations.epfl.ch/page-16300-fr-html/");
    }


    @Override
    protected ArrayList<String> doInBackground(String... urls) {
        ArrayList<String> result = parseUrl(urls[0]);


        return result;
    }


    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        if(strings != null) {
            listener.apply(strings);
        }
    }

    private ArrayList<String> parseUrl(String url) {

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = connect(url);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        if(urlConnection == null){
            return null;
        }

        ArrayList<String> datas = null;
        try {
            datas = parseData(urlConnection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }

        return datas;
    }

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
            return null;
        }

        return UrlConnection;
    }


    private ArrayList<String> parseData(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(inputStream));


        Pattern p = Pattern.compile("&#\\d+.* <a href=\"(.*?)\".*>(.*)</a>.*\\((.+)\\)<.*br />.*");

        ArrayList<String> results = new ArrayList<>();

        String inputLine;

        while ((inputLine = in.readLine()) != null) {
            Matcher m = p.matcher(inputLine);
            if (m.find()) {
                // remove the span tag
                String description = m.group(3).replaceAll("<.*>", "");


                System.out.println("Name : " + m.group(2));
                System.out.println("D3 : " + description);
                System.out.println("-----------");

                StringBuilder sb = new StringBuilder();

                sb.append(m.group(1));
                sb.append(',');
                sb.append(description);
                sb.append(',');
                sb.append(m.group(3));

                results.add(sb.toString());
            }
        }
        in.close();

        return results;
    }

    private URL openUrl(String url) throws MalformedURLException {
        return new URL(url);
    }


}
