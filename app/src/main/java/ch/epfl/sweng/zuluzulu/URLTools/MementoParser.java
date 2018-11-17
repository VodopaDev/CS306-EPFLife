package ch.epfl.sweng.zuluzulu.URLTools;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class MementoParser implements Parser<List<String>> {
    private final static String TAG = "MEMENTO_PARSER";

    public MementoParser() {
    }

    @Override
    public List<String> parse(BufferedReader in) {
        if (in == null) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        String line;

        try {
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Could not parse datas");
            return null;
        }


        JSONArray jsonarray = null;
        try {
            jsonarray = new JSONArray(sb.toString());
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String title = jsonobject.getString("title");
                System.out.println(title);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "Could not parse json");
            return null;
        }


        return null;
    }
}
