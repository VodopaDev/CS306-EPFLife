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

    private String url_place_and_room;
    private String website;
    private String contact;
    private String category;
    private String speaker;

    private EventDate date;

    private int channel_id;
    private int assos_id;

    public Event(String id, String name, String shortDesc, String longDesc, EventDate date,
                 int likes, String organizer, String place, String bannerUri, String iconUri,
                 String url_place_and_room, String website, String contact, String category, String speaker) {
        super(id);
        this.name = name;
        this.shortDescription = shortDesc;
        this.longDescription = longDesc;
        setDate(date);
        this.likes = likes;
        this.organizer = organizer;
        this.place = place;
        this.bannerUri = bannerUri;
        this.iconUri = iconUri;
        setWebsite(website);
        setUrlPlaceAndRoom(url_place_and_room);
        setContact(contact);
        setCategory(category);
        setSpeaker(speaker);
    }

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

<<<<<<< HEAD
=======
        id = data.getInteger("id");
        channel = data.getInteger("channel_id");
>>>>>>> master
        name = data.getString("name");
        shortDescription = data.getString("short_description");
        longDescription = data.getString("long_description");
        channelId = data.getString("channel_id");
        likes = data.getInteger("likes");
        organizer = data.getString("organizer");
        place = data.getString("place");
<<<<<<< HEAD
        iconUri = data.getString("icon_uri");
        bannerUri = data.getString("banner_uri");
        startDate = data.getDate("start_date");
=======

        shortDesc = data.getString("short_desc");
        longDesc = data.getString("long_desc");

        setUrlPlaceAndRoom(data.getString("url_place_and_room"));
        setWebsite(data.getString("website"));
        setContact(data.getString("contact"));
        setCategory(data.getString("category"));
        setSpeaker(data.getString("speaker"));


        String icon_str = data.getString("icon_uri");
        Uri uri = icon_str == null ? Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon) : Uri.parse(icon_str);
        iconUri = uri == null ? null : uri.toString();

        // Init the Banner URI
        String banner_str = data.getString("banner_uri");
        uri = banner_str == null ? Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner) : Uri.parse(banner_str);
        bannerUri = uri == null ? null : uri.toString();

        setDate(new EventDate(data.getDate("start_date"), data.getDate("end_date")));

        likes = data.getInteger("likes");

        channel_id = data.get("channel_id") == null ? 0 : data.getInteger("channel_id");

        assos_id = data.get("assos_id") == null ? 0 : data.getInteger("assos_id");
>>>>>>> master
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

<<<<<<< HEAD
    public String getShortDescription() {
        return shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
=======
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException();
        }
        this.name = name.trim().replaceAll("\"", "");
    }

    public int getChannel() {
        return channel;
>>>>>>> master
    }

    public String getShortDesc() {
        return shortDesc;
    }

<<<<<<< HEAD
    public String getStartDateString() {
        return Utils.dateFormat.format(startDate);
    }

    public String getChannelId(){
        return channelId;
    }

    public void setChannelId(String channelId){
        this.channelId = channelId;
=======
    public String getLongDesc() {
        return longDesc;
>>>>>>> master
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

<<<<<<< HEAD
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
=======
    public int getChannelId() {
        return channel_id;
    }

    public int getAssosId() {
        return assos_id;
    }

    public String getUrlPlaceAndRoom() {
        return url_place_and_room;
    }

    public void setUrlPlaceAndRoom(String url_place_and_room) {
        assert(url_place_and_room != null);

        this.url_place_and_room = url_place_and_room;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        assert(website != null);

        this.website = website;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        assert(contact != null);

        this.contact = contact;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        assert(category != null);

        this.category = category;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        assert(speaker != null);

        this.speaker = speaker;
    }


    private void setDate(EventDate date) {
        assert(date != null);
        this.date = date;
    }

    public Date getStartDate() {
        return date.getStartDate();
    }

    public Date getEndDate() {
        return date.getEndDate();
    }

    public String getStartDateString() {
        return date.getStartDateString();
    }

    public String getEndDateString() {
        return date.getEndDateString();
>>>>>>> master
    }
}
