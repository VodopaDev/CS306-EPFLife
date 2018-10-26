package ch.epfl.sweng.zuluzulu.Structure;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AssociationsUrlHandler extends AsyncTask<URL, Void, Long> {

    public AssociationsUrlHandler() {
        System.out.println("hello world");


    }

    @Override
    protected Long doInBackground(URL... urls) {
        return null;
    }

    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);
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
