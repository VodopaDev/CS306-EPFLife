package ch.epfl.sweng.zuluzulu.URLTools;

import android.util.Log;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains parsers that can be used to parse a web page
 * All methods are static
 */
public class Parsers {
    private final static String TAG = "PARSER";

    private Parsers() {

    }

    /**
     * This function will parse the datas and return a arraylist of strings
     *
     * @param in Input stream
     * @return ArrayList of strings , values separated by a comma
     */
    public static List<String> parseAssociationsData(BufferedReader in) {
        if(in == null){
            return null;
        }
        // regex
        Pattern p = Pattern.compile("&#\\d+.* <a href=\"(.*?)\".*>(.*)</a>.*\\((.+)\\)<.*br />.*");

        ArrayList<String> results = new ArrayList<>();
        String inputLine;

        try {
            while ((inputLine = in.readLine()) != null) {
                Matcher m = p.matcher(inputLine);
                if (m.find()) {
                    // remove the span tag
                    String description = m.group(3).replaceAll("<.*?>", "");

                    StringBuilder sb = new StringBuilder();
                    sb.append(m.group(1)).append(',');
                    sb.append(m.group(2)).append(',');
                    sb.append(description);

                    // remplace html unicode to char
                    String result = sb.toString()
                            .replaceAll("&#8217;", "'").replaceAll("&#8211;", "-")
                            .replaceAll("&gt;", ">").replaceAll("&amp;", "&");

                    results.add(result);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Could not parse datas");
            return null;
        }
        return results;
    }

    /**
     * This function will parse the datas and return a arraylist of strings
     *
     * @param in Input stream
     * @return ArrayList of strings , values separated by a comma
     */
    public static List<String> parseIcon(BufferedReader in) {
        if(in == null){
            return null;
        }

        // regex
        Pattern p = Pattern.compile("<link[^>]+href=\"([^\"]*?(?:jpg|ico|png))\"[^>]*");


        String result = null;
        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {
                Matcher m = p.matcher(inputLine);
                if (m.find()) {
                    // remove the span tag

                    StringBuilder sb = new StringBuilder();
                    sb.append(m.group(1));
                    // remplace html unicode to char
                    result = sb.toString();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Could not parse datas");
            return null;
        }

        ArrayList<String> list = new ArrayList<>();

        if(result != null){
            list.add(result);
        }

        return list;
    }
}
