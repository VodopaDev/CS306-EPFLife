package ch.epfl.sweng.zuluzulu.Structure;

import android.os.AsyncTask;
import android.text.Html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssociationsUrlHandler extends AsyncTask<String, Void, Long> {

    public AssociationsUrlHandler() {
        System.out.println("hello world");

        this.execute("https://associations.epfl.ch/page-16300-fr-html/");
    }



    @Override
    protected Long doInBackground(String... urls) {
        System.out.println("TEST");
        connect(urls[0]);
        return null;
    }


    public boolean connect(String url) {
        URL aURL = null;
        HttpURLConnection UrlConnection;
        int code = 0;
        try {
            // Open url
            aURL = openUrl(url);

            // Open connection
            UrlConnection = (HttpURLConnection) aURL.openConnection();
            UrlConnection.connect();

            parseData(UrlConnection.getInputStream());

            // Get HTTP response code
            code = UrlConnection.getResponseCode();
        } catch (MalformedURLException e) {
            System.out.println("ERROR ");

            e.printStackTrace();

            return false;
        } catch (IOException e) {
            System.out.println("ERROR ");

            e.printStackTrace();
            return false;
        }

        if(code != 200){
            System.out.println("ERROR ");
            // Not OK response
            return false;
        }

        UrlConnection.disconnect();


        System.out.println("CONNECTED !");


        return true;
    }

    private void parseData(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(inputStream));


        Pattern p = Pattern.compile("&#\\d+.* <a href=\"(.*)\".*>(.*)</a>.*\\((.+)\\)<.*br />.*");

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            Matcher m = p.matcher(inputLine);
            if (m.find()) {
                System.out.println("Name : " + m.group(2));
                System.out.println("Description : " + m.group(3));
                System.out.println("-----------");
            }
        }
        in.close();
    }

    private URL openUrl(String url) throws MalformedURLException {
        return new URL(url);
    }


}
