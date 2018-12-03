package ch.epfl.sweng.zuluzulu.Structure;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.R;

public final class EventBuilder {
    private static final int SHORT_DESC_MAXLENGTH = 100;

    private String id;
    private String name;
    private String shortDesc;
    private String longDesc;
    private List<String> followers;
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

    private String channel_id;
    private String assos_id;

    /**
     * Use to create an event
     */
    public EventBuilder() {
        // set Default values
        followers = new ArrayList<>();
    }

    /**
     * Build an Event
     *
     * @return Event
     */
    public Event build() {
        return new Event(
                id,
                name,
                shortDesc,
                longDesc,
                channel_id,
                assos_id,
                date,
                followers,
                organizer,
                place,
                bannerUri,
                iconUri,
                url_place_and_room,
                website, contact,
                category,
                speaker
        );
    }


    public EventBuilder setId(String id) {
        if(id == null)
            throw new IllegalArgumentException();
        this.id = id;
        return this;
    }

    public EventBuilder setName(String name) {
        if(name == null)
            throw new IllegalArgumentException();
        this.name = name.trim().replaceAll("\"", "");
        return this;
    }


    public EventBuilder setShortDesc(String shortDesc) {
        if(shortDesc == null){
            throw new IllegalArgumentException();
        }
        if(shortDesc.length() > SHORT_DESC_MAXLENGTH){
            String desc = shortDesc.substring(0, SHORT_DESC_MAXLENGTH);
            int last_space = desc.lastIndexOf(" ");
            // cut at a space
            this.shortDesc = desc.substring(0, last_space).trim() + "...";
        } else {
            this.shortDesc = shortDesc;
        }
        return this;
    }

    public EventBuilder setLongDesc(String longDesc) {
        if(longDesc == null)
            throw new IllegalArgumentException();
        this.longDesc = longDesc;
        return this;
    }


    public EventBuilder setFollowers(List<String> followers) {
        this.followers = new ArrayList<>(followers);
        return this;
    }

    public EventBuilder setOrganizer(String organizer) {
        this.organizer = organizer;
        return this;
    }

    public EventBuilder setPlace(String place) {
        this.place = place;
        return this;
    }

    /**
     * Create banner, default if null given
     *
     * @param bannerUri Banner url
     * @return this
     */
    public EventBuilder setBannerUri(String bannerUri) {
        if (!correctUri(bannerUri))
            this.bannerUri = "android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner;
        else
            this.bannerUri = bannerUri;
        return this;
    }

    /**
     * Create icon, default if null given
     *
     * @param iconUri Icon url
     * @return this
     */
    public EventBuilder setIconUri(String iconUri) {
        if (correctUri(iconUri))
            this.iconUri = iconUri;
        else
            this.iconUri = "android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon;
        return this;
    }

    /**
     * Check if the uri is correct
     * @param uri uri
     * @return boolean
     */
    private boolean correctUri(String uri){
        return uri != null && uri.length() >= 6;
    }

    public EventBuilder setUrlPlaceAndRoom(String url_place_and_room) {
        assert (url_place_and_room != null);
        this.url_place_and_room = url_place_and_room;
        return this;
    }

    public EventBuilder setWebsite(String website) {
        assert (website != null);
        this.website = website;
        return this;
    }

    public EventBuilder setContact(String contact) {
        assert (contact != null);
        this.contact = contact;
        return this;
    }

    public EventBuilder setCategory(String category) {
        assert (category != null);
        this.category = category;
        return this;
    }

    public EventBuilder setSpeaker(String speaker) {
        assert (speaker != null);
        this.speaker = speaker;
        return this;
    }

    public EventBuilder setDate(EventDate date) {
        assert (date != null);
        this.date = date;
        return this;
    }

    public EventBuilder setChannelId(String channel_id) {
        this.channel_id = channel_id;
        return this;
    }

    public EventBuilder setAssosId(String assos_id) {
        this.assos_id = assos_id;
        return this;
    }
}