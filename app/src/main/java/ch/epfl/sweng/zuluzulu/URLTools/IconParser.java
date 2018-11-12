package ch.epfl.sweng.zuluzulu.URLTools;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IconParser implements Parser<List<String >> {
    private final static String TAG = "ICON_PARSER";

    public IconParser(){}


    /**
     * This function will parse the datas and return a arraylist of strings
     *
     * @param in Input stream
     * @return ArrayList of strings , values separated by a comma
     */
    @Override
    public List<String> parse(BufferedReader in) {
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
