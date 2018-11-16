package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;

import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;

public abstract class SuperStructure {

    private final static String ID_FIELD = "id";
    private final static String NAME_FIELD = "name";
    private final static String SHORT_DESC_FIELD = "short_desc";
    private final static String ICON_URI_FIELD = "icon_uri";


    public static List<String> REQUIRED_FIELDS = Arrays.asList(ID_FIELD, NAME_FIELD, SHORT_DESC_FIELD);

    private Long id;
    private String name;
    private String shortDesc;
    private Uri iconUri;

    public SuperStructure(FirebaseMapDecorator data){
        if(!data.hasFields(REQUIRED_FIELDS))
            throw new IllegalArgumentException("Incorrect map to build a SuperStructure");

        id = data.getLong(ID_FIELD);
        name = data.getString(NAME_FIELD);
        shortDesc = data.getString(SHORT_DESC_FIELD);

        String iconStr = data.getString("icon_uri");
        iconUri = iconStr == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon) :
                Uri.parse(iconStr);


    }

    public Long getId(){
        return id;
    }
    public Long setId(Long newId){
        if(newId == null)
            throw new NullPointerException("newId must be initialized");
        else
            id = newId;
        return id;
    }

    public String getName(){return name;}
    public String setName(String newName){
        if(newName == null)
            throw new NullPointerException("newName must be initialized");
        else
            name = newName;
        return name;
    }

    public String getShortDesc(){return shortDesc;}

    public Uri getIconUri(){return iconUri;}

}
