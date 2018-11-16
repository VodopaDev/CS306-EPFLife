package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;

/**
 * A simple structure to hold an Association, Event or Channel
 */
public abstract class SuperStructure implements Serializable{

    private final static String ID_FIELD = "id";
    private final static String NAME_FIELD = "name";
    private final static String SHORT_DESC_FIELD = "short_desc";
    private final static String ICON_URI_FIELD = "icon_uri";

    public static List<String> REQUIRED_FIELDS = Arrays.asList(ID_FIELD, NAME_FIELD, SHORT_DESC_FIELD);

    private Long id;
    private String name;
    private String shortDesc;
    private Uri iconUri;

    /**
     * Create a SuperStructure using a FirebaseMapDecorator
     * @param data the Firebase map
     */
    public SuperStructure(FirebaseMapDecorator data){
        if(!data.hasFields(REQUIRED_FIELDS))
            throw new IllegalArgumentException("Incorrect map to build a SuperStructure");

        id = data.getLong(ID_FIELD);
        name = data.getString(NAME_FIELD);
        shortDesc = data.getString(SHORT_DESC_FIELD);

        String iconStr = data.getString(ICON_URI_FIELD);
        iconUri = iconStr == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon) :
                Uri.parse(iconStr);


    }

    /**
     * Return the structure's id
     * @return the structure's id
     */
    public Long getId(){
        return id;
    }

    /**
     * Set the structure's id
     * @param newId new id to use
     * @return the new structure's id
     */
    public Long setId(Long newId){
        if(newId == null)
            throw new NullPointerException("newId must be initialized");
        else
            id = newId;
        return id;
    }

    /**
     * Return the structure's name
     * @return the structure's name
     */
    public String getName(){
        return name;
    }

    /**
     * Set the structure's name
     * @param newName new name to use
     * @return the new structure's name
     */
    public String setName(String newName){
        if(newName == null)
            throw new NullPointerException("newName must be initialized");
        else
            name = newName;
        return name;
    }

    /**
     * Return the structure's short description
     * @return the structure's short description
     */
    public String getShortDesc(){
        return shortDesc;
    }

    /**
     * Set the structure's short description
     * @param newShortDesc new short description to use
     * @return the new structure's short description
     */
    public String setShortDesc(String newShortDesc){
        if(newShortDesc == null)
            throw new NullPointerException("newShortDesc must be initialized");
        else
            shortDesc = newShortDesc;
        return shortDesc;
    }

    /**
     * Return the structure's icon uri
     * @return the structure's icon uri
     */
    public Uri getIconUri(){
        return iconUri;
    }

    /**
     * Set the structure's icon uri
     * @param newIconUri new icon uro to use
     * @return the new structure's icon uri
     */
    public Uri setIconUri(Uri newIconUri){
        if(newIconUri == null)
            throw new NullPointerException("newShortDesc must be initialized");
        else
            iconUri = newIconUri;
        return iconUri;
    }

}
