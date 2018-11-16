package ch.epfl.sweng.zuluzulu.Firebase;

public enum FirebaseFields {

    ID("id"),
    NAME("name"),
    SHORT_DESC("short_desc"),
    LONG_DESC("long_desc"),
    ICON_URI("icon_uri"),
    BANNER_URI("banner_uri"),

    FOLLOWED_ASSOCIATIONS("followed_associations"),
    FOLLOWED_EVENTS("followed_events"),
    LIKES("likes"),

    START_DATE("start_date"),
    END_DATE("end_date"),
    RESTRICTIONS("restrictions"),
    // Linked channel id of an Event or Association
    MAIN_CHANNEL_ID("main_channel_id"),
    // Linked association id of an Event or Channel
    MAIN_ASSOCIATION_ID("main_association_id"),
    // All events registered in an Association
    REGISTERED_EVENTS("events");


    private String path;

    FirebaseFields(String path){
        this.path = path;
    }

    public String getPath(){
        return path;
    }
}
