package ch.epfl.sweng.zuluzulu.Structure;

import android.net.Uri;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Firebase.FirebaseMapDecorator;
import ch.epfl.sweng.zuluzulu.R;


// TODO: Add admin access, ending date, linked-chat id, linked-association id, position
public class Event implements Serializable {
    public final static List<String> FIELDS = Arrays.asList("id", "name", "short_desc", "long_desc", "start_date", "end_date", "likes");

    private int id;
    private int likes;
    private int channel_id;
    private int assos_id;
    private String name;
    private String shortDesc;
    private String longDesc;
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


    private Event(int id, String name, String shortDesc, String longDesc, EventDate date, int likes, String organizer, String place, String bannerUri, String iconUri, String url_place_and_room, String website, String contact, String category, String speaker, int channel_id, int assos_id) {
        this.id = id;
        this.name = name;
        this.shortDesc = shortDesc;
        this.longDesc = longDesc;
        this.date = date;
        this.likes = likes;
        this.organizer = organizer;
        this.place = place;
        this.bannerUri = bannerUri;
        this.iconUri = iconUri;
        this.website = website;
        this.url_place_and_room = url_place_and_room;
        this.contact = contact;
        this.category = category;
        this.speaker = speaker;
        this.channel_id = channel_id;
        this.assos_id = assos_id;
    }

    public static Comparator<Event> assoNameComparator() {
        return new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.getName().compareTo(o2.getName());
            }
        };
    }

    public static Comparator<Event> dateComparator() {
        return new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        };
    }

    public static Comparator<Event> likeComparator() {
        return new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                return o2.getLikes().compareTo(o1.getLikes());
            }
        };
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
        return shortDesc;
    }

    public String getLongDesc() {
        return longDesc;
    }

    public String getBannerUri() {
        return bannerUri;
    }

    public String getIconUri() {
        return iconUri;
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
        likes += 1;
    }

    public void decreaseLikes() {
        likes -= 1;
    }

    public int getChannelId() {
        return channel_id;
    }

    public int getAssosId() {
        return assos_id;
    }

    public String getUrlPlaceAndRoom() {
        return url_place_and_room;
    }

    public String getWebsite() {
        return website;
    }

    public String getContact() {
        return contact;
    }

    public String getCategory() {
        return category;
    }

    public String getSpeaker() {
        return speaker;
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
    }

    public static final class EventBuilder {
        private static final int SHORT_DESC_MAXLENGTH = 100;

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

        /**
         * Use to create an event
         */
        public EventBuilder() {
            // set Default values
            this.likes = 0;
        }

        /**
         * Build an Event
         *
         * @return Event
         */
        public Event build() {
            return new Event(id, name, shortDesc, longDesc, date, likes, organizer, place, bannerUri, iconUri, url_place_and_room, website, contact, category, speaker, channel_id, assos_id);
        }

        /**
         * Create an event using a FirebaseMap
         *
         * @param data the FirebaseMap
         * @throws IllegalArgumentException if the FirebaseMap isn't an Event's FirebaseMap
         */
        public Event build(FirebaseMapDecorator data) {
            if (!data.hasFields(FIELDS))
                throw new IllegalArgumentException();

            id = data.getInteger("id");
            name = data.getString("name");
            shortDesc = data.getString("short_desc");
            longDesc = data.getString("long_desc");
            likes = data.getInteger("likes");
            organizer = data.getString("organizer");
            place = data.getString("place");

            shortDesc = data.getString("short_desc");
            longDesc = data.getString("long_desc");

            this.url_place_and_room = data.getString("url_place_and_room");
            this.website = data.getString("website");
            this.contact = data.getString("contact");
            this.category = data.getString("category");
            this.speaker = data.getString("speaker");

            setIconUri(data.getString("icon_uri"));
            setBannerUri(data.getString("banner_uri"));

            this.date = new EventDate(data.getDate("start_date"), data.getDate("end_date"));

            likes = data.getInteger("likes");

            channel_id = data.get("channel_id") == null ? 0 : data.getInteger("channel_id");

            assos_id = data.get("assos_id") == null ? 0 : data.getInteger("assos_id");

            return this.build();
        }

        public EventBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public EventBuilder setName(String name) {
            assert (name != null);
            this.name = name.trim().replaceAll("\"", "");
            return this;
        }


        public EventBuilder setShortDesc(String shortDesc) {
            /***
             *
             * NICO LAISSE CE SETTER IL SERT À LIMITER LA TAILLE DU SHORT DESC
             *
             */
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

        /**
         * Create banner, default if null given
         *
         * @param bannerUri Banner url
         * @return this
         */
        public EventBuilder setBannerUri(String bannerUri) {
            if (bannerUri == null || bannerUri.length() < 5) {
                this.bannerUri = Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_banner).toString();
            } else {
                this.bannerUri = bannerUri;
            }
            return this;
        }

        /**
         * Create icon, default if null given
         *
         * @param iconUri Icon url
         * @return this
         */
        public EventBuilder setIconUri(String iconUri) {
            if (iconUri == null || iconUri.length() < 5) {
                this.iconUri = Uri.parse("android.resource://ch.epfl.sweng.zuluzulu/" + R.drawable.default_icon).toString();
            } else {
                this.iconUri = iconUri;
            }
            return this;
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

        public EventBuilder setChannelId(int channel_id) {
            this.channel_id = channel_id;
            return this;
        }

        public EventBuilder setAssosId(int assos_id) {
            this.assos_id = assos_id;
            return this;
        }
    }
}
