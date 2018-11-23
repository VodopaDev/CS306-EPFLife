package ch.epfl.sweng.zuluzulu.Structure;

public final class EventBuilder{
    private int id;
    private String name;
    private String shortDesc;
    private String longDesc;
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

    public EventBuilder(){
        // set Default values
        this.likes = 0;
    }

    public Event build(){
        return new Event(id, name, shortDesc, longDesc, date, likes, organizer, place, bannerUri, iconUri, url_place_and_room, website, contact, category, speaker, channel_id, assos_id);
    }

    public EventBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public EventBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public EventBuilder setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
        return this;
    }

    public EventBuilder setLongDesc(String longDesc) {
        this.longDesc = longDesc;
        return this;
    }


    public EventBuilder setLikes(int likes) {
        this.likes = likes;
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

    public EventBuilder setBannerUri(String bannerUri) {
        this.bannerUri = bannerUri;
        return this;
    }

    public EventBuilder setIconUri(String iconUri) {
        this.iconUri = iconUri;
        return this;
    }

    public EventBuilder setUrlPlaceAndRoom(String url_place_and_room) {
        this.url_place_and_room = url_place_and_room;
        return this;
    }

    public EventBuilder setWebsite(String website) {
        this.website = website;
        return this;
    }

    public EventBuilder setContact(String contact) {
        this.contact = contact;
        return this;
    }

    public EventBuilder setCategory(String category) {
        this.category = category;
        return this;
    }

    public EventBuilder setSpeaker(String speaker) {
        this.speaker = speaker;
        return this;
    }

    public EventBuilder setDate(EventDate date) {
        this.date = date;
        return this;
    }

    public EventBuilder setChannelId(int channel_id) {
        this.channel_id = channel_id;
        return this;
    }

    public EventBuilder setAssosId(int assos_id) {
        this.assos_id = assos_id;
        return this;
    }
}