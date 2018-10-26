package ch.epfl.sweng.zuluzulu.Structure;

import java.util.HashMap;

public class FirebaseMap extends HashMap<String, Object> {

    public int getInteger(String field){
        return ((Long)get(field)).intValue();
    }

    public int getString(String field){
        return (String)get(field);
    }

    public int getInteger(String fields){
        return ((Long)get(fields)).intValue();
    }

    public int getInteger(String fields){
        return ((Long)get(fields)).intValue();
    }


}
