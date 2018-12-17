package ch.epfl.sweng.zuluzulu.urlTools;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
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

        List<String> list = new ArrayList<>();

        list.add(sb.toString().replaceAll("<.*?>", ""));

        return list;
    }
}
