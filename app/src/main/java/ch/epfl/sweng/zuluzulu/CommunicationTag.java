package ch.epfl.sweng.zuluzulu;

public enum CommunicationTag {

    // Utils to modify the main activity
    SET_TITLE,
    SET_USER,
    OPENING_WEBVIEW,

    // Communicate to the main fragment IDLING_Ressource
    INCREMENT_IDLING_RESOURCE,
    DECREMENT_IDLING_RESOURCE,

    // Opening fragments
    OPEN_MAIN_FRAGMENT,
    OPEN_SETTINGS_FRAGMENT,
    OPEN_ABOUT_US_FRAGMENT,
    OPEN_PROFILE_FRAGMENT,
    OPEN_LOGIN_FRAGMENT,
    OPEN_CHAT_FRAGMENT,
    OPEN_CHANNEL_FRAGMENT,
    OPEN_EVENT_FRAGMENT,
    OPEN_EVENT_DETAIL_FRAGMENT,
    OPEN_ASSOCIATION_FRAGMENT,
    OPEN_ASSOCIATION_DETAIL_FRAGMENT,
}
