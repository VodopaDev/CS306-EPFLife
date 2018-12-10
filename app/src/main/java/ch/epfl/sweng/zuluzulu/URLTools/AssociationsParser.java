package ch.epfl.sweng.zuluzulu.URLTools;

import android.util.Log;

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
public class AssociationsParser implements Parser<List<String>> {
    private final static String TAG = "ASSOCIATIONS_PARSER";

    public AssociationsParser() {

    }

    /**
     * This function will parse the datas and return a arraylist of strings
     *
     * @param in Input stream
     * @return ArrayList of strings , values separated by a comma
     */
    @Override
    public List<String> parse(BufferedReader in) {
        if (in == null) {
            return null;
        }
        // regex
        Pattern p = Pattern.compile("&#\\d+.*<a href=\"(.*?)\".*>(.*)</a>.*\\((.+)\\)<");

        ArrayList<String> results = new ArrayList<>();
        String inputLine;

        try {
            while ((inputLine = in.readLine()) != null) {
                Matcher m = p.matcher(inputLine);
                if (m.find()) {
                    results.add(createResult(m.group(1), m.group(2), m.group(3)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Could not parse datas");
            return null;
        }
        return results;
    }

    private String createResult(String url, String name, String description) {
        // remove the span tag
        String clean_description = description.replaceAll("<.*?>", "");


        // remplace html unicode to char
        String sb = url + ',' + name + ',' + clean_description;
        String result = sb
                .replaceAll("&#8217;", "'").replaceAll("&#8211;", "-")
                .replaceAll("&gt;", ">").replaceAll("&amp;", "&");

        return result;
    }
}
