package ch.epfl.sweng.zuluzulu.Structure;

import android.os.AsyncTask;
import android.text.Html;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
        HttpURLConnection myURLConnection;
        int code = 0;
        try {
            // Open url
            aURL = openUrl(url);

            // Open connection
            myURLConnection = (HttpURLConnection) aURL.openConnection();
            myURLConnection.connect();

            System.out.println("length : " + myURLConnection.getContent());
            System.out.println("length : " + myURLConnection.getContent().toString());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(myURLConnection.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null)
                System.out.println(inputLine);
            in.close();

            // Get HTTP response code
            code = myURLConnection.getResponseCode();
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



        System.out.println("CONNECTED !");


        return true;
    }

    private URL openUrl(String url) throws MalformedURLException {
        return new URL(url);
    }


}
