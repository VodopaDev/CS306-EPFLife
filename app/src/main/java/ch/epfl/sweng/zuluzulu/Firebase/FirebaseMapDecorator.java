package ch.epfl.sweng.zuluzulu.Firebase;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Adapt a map from a DocumentSnapshot
 * Allow the use of specific getters for easier use
 */
public class FirebaseMapDecorator {

    // the adapted Map
    private final Map<String, Object> map;

    /**
     * Adapt a map
     *
     * @param map the Map to adapt
     */
    public FirebaseMapDecorator(Map<String, Object> map) {
        this.map = map;
    }

    /**
     * Adapt a map from a DocumentSnapshot
     *
     * @param snap the DocumentSnapshot to adapt
     */
    public FirebaseMapDecorator(DocumentSnapshot snap) {
        this.map = snap.getData();
    }

    /**
     * Get an Integer-casted value from the adapted map
     *
     * @param field key to use on the map
     * @return Integer-casted value
     */
    public Integer getInteger(String field) {
        return ((Long) get(field)).intValue();
    }

    /**
     * Get a Long-casted value from the adapted map
     *
     * @param field key to use on the map
     * @return Long-casted value
     */
    public Long getLong(String field) {
        return (Long) get(field);
    }

    /**
     * Get a String-casted value from the adapted map
     *
     * @param field key to use on the map
     * @return String-casted value
     */
    public String getString(String field) {
        return (String) get(field);
    }

    /**
     * Get a Date-casted value from the adapted map
     *
     * @param field key to use on the map
     * @return Date-casted value
     */
    public Date getDate(String field) {
        return (Date) get(field);
    }

    /**
     * Get a GeoPoint-casted value from the adapted map
     *
     * @param field key to use on the map
     * @return GeoPoint-casted value
     */
    public GeoPoint getGeoPoint(String field) {
        return (GeoPoint) get(field);
    }

    /**
     * Get a Map-casted value from the adapted map
     *
     * @param field key to use on the map
     * @return Map-casted value
     */
    public Map<String, Object> getMap(String field) {
        return (Map<String, Object>) get(field);
    }

    /**
     * Get a List-casted value from the adapted map
     *
     * @param field key to use on the map
     * @return List-casted value
     */
    public List<Object> getList(String field) {
        return (List<Object>) get(field);
    }

    /**
     * Get a LongList-casted value from the adapted map
     *
     * @param field key to use on the map
     * @return LongList-casted value
     */
    public List<Long> getLongList(String field) {
        return (List<Long>) get(field);
    }

    /**
     * Get a list of string from the adapted map
     *
     * @param field key to use on map
     * @return String list casted value
     */
    public List<String> getStringList(String field) {
        return (List<String>) get(field);
    }

    /**
     * Get a IntegerList-casted value from the adapted map
     *
     * @param field key to use on the map
     * @return IntegerList-casted value
     */
    public List<Integer> getIntegerList(String field) {
        List<Long> intermediate = getLongList(field);
        List<Integer> result = new ArrayList<>();
        if (intermediate == null)
            return result;

        for (Long item : intermediate)
            result.add(item.intValue());

        return result;
    }

    /**
     * Get the default type of a value
     *
     * @param field key to use on the map
     * @return value
     */
    public Object get(String field) {
        if(map.containsKey(field))
            return map.get(field);
        return null;
    }

    /**
     * Check if the map has required fields
     *
     * @param fields fields to be checked in the map
     * @return true if the map has all required fields (ie: not null), false otherwise
     */
    public boolean hasFields(List<String> fields) {
        for (String key : fields) {
            if (get(key) == null)
                return false;
        }
        return true;
    }
}
