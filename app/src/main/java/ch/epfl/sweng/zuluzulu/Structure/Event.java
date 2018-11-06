package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;


// TODO: Add admin access, ending date, linked-chat id, linked-association id, position
public class Event implements Serializable {
    public final static List<String> FIELDS = Arrays.asList("id", "name", "short_desc", "long_desc", "start_date");

    private int id;
    private String name;
    private String short_desc;
    private String long_desc;
    private Date start_date;
    private String start_date_string;

    private Uri banner_uri;
    private Uri icon_uri;

    /**
     * Create an event using a FirebaseMap
     *
     * @param data the FirebaseMap
     * @throws IllegalArgumentException if the FirebaseMap isn't an Event's FirebaseMap
     */
    public Event(FirebaseMapDecorator data) {
        if (!data.hasFields(FIELDS))
            throw new NullPointerException();

        id = data.getInteger("id");
        name = data.getString("name");
        short_desc = data.getString("short_desc");
        long_desc = data.getString("long_desc");

        String icon_str = data.getString("icon_uri");
        icon_uri = icon_str == null ?
                Uri.parse("android.ressource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon) :
                Uri.parse(icon_str);

        String banner_str = data.getString("icon_uri");
        banner_uri = banner_str == null ?
                Uri.parse("android.ressource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner) :
                Uri.parse(banner_str);

        start_date = data.getDate("start_date");
        start_date_string = Utils.dateFormat.format(start_date);
    }


    public static Comparator<Event> assoNameComparator() {
        return (o1, o2) -> o1.getName().compareTo(o2.getName());
    }

    public static Comparator<Event> dateComparator() {
        return (o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate());
    }

    // TODO: Add a method to add/remove one User to admins (instead of getting/setting the admins list)
    // TODO: Check inputs before changing fields
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortDesc() {
        return short_desc;
    }

    public String getLongDesc() {
        return long_desc;
    }

    public Date getStartDate() {
        return start_date;
    }

    public String getStartDateString() {
        return start_date_string;
    }

    @Nullable
    public Uri getBannerUri() {
        return banner_uri;
    }

    @Nullable
    public Uri getIconUri() {
        return icon_uri;
    }

}
