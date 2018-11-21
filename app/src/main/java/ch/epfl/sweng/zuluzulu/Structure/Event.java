package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;


// TODO: Add admin access, ending date, linked-chat id, linked-association id, position
public class Event extends FirebaseStructure {

    private String name;
    private String shortDescription;
    private String longDescription;
    private String channelId;

    private Date startDate;
    private int likes;
    private String organizer;
    private String place;

    private String bannerUri;
    private String iconUri;

    /**
     * Create an event using a FirebaseMap
     *
     * @param data the FirebaseMap
     * @throws IllegalArgumentException if the FirebaseMap isn't an Event's FirebaseMap
     */
    public Event(FirebaseMapDecorator data) {
        super(data);
        if (!data.hasFields(requiredFields()))
            throw new IllegalArgumentException();

        name = data.getString("name");
        shortDescription = data.getString("short_description");
        longDescription = data.getString("long_description");
        channelId = data.getString("channel_id");
        likes = data.getInteger("likes");
        organizer = data.getString("organizer");
        place = data.getString("place");
        iconUri = data.getString("icon_uri");
        bannerUri = data.getString("banner_uri");
        startDate = data.getDate("start_date");
    }


    public static Comparator<Event> nameComparator() {
        return (o1, o2) -> o1.getName().compareTo(o2.getName());
    }

    public static Comparator<Event> dateComparator() {
        return (o1, o2) -> o1.getStartDate().compareTo(o2.getStartDate());
    }

    public static Comparator<Event> likeComparator() {
        return (o1, o2) -> o2.getLikes().compareTo(o1.getLikes());
    }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getStartDateString() {
        return Utils.dateFormat.format(startDate);
    }

    public String getChannelId(){
        return channelId;
    }

    public void setChannelId(String channelId){
        this.channelId = channelId;
    }

    /**
     * Return the Association's icon Uri
     *
     * @return the icon Uri
     */
    public Uri getIconUri() {
        return iconUri == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon) :
                Uri.parse(iconUri);
    }

    /**
     * Return the Association's banner Uri
     *
     * @return the banner Uri
     */
    public Uri getBannerUri() {
        return bannerUri == null ?
                Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner) :
                Uri.parse(bannerUri);
    }

    public String getOrganizer() {
        return organizer;
    }

    public String getPlace() {
        return place;
    }

    public Integer getLikes() {
        return likes;
    }

    public void increaseLikes() {
        likes--;
    }

    public void decreaseLikes() {
        likes++;
    }

    public static List<String> requiredFields() {
        return Arrays.asList("id","name","short_description","long_description","start_date","likes", "channel_id");
    }


    public Map<String,Object> getData(){
        Map<String, Object> map = new HashMap<>();
        map.put("id", getId());
        map.put("name", name);
        map.put("short_description", shortDescription);
        map.put("long_description", longDescription);
        map.put("icon_uri", iconUri);
        map.put("banner_uri", bannerUri);
        map.put("likes", likes);
        map.put("channel_id", channelId);
        map.put("start_date", startDate);
        map.put("place", place);
        map.put("organizer", organizer);
        return map;
    }
}
