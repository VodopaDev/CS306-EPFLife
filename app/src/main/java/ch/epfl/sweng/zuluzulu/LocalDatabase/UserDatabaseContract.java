package ch.epfl.sweng.zuluzulu.LocalDatabase;

import android.provider.BaseColumns;

public class UserDatabaseContract {
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_SCIPER + " TEXT," +
                    FeedEntry.COLUMN_NAME_FIRST_NAME + " TEXT," +
                    FeedEntry.COLUMN_NAME_LAST_NAME + " TEXT," +
                    FeedEntry.COLUMN_NAME_SECTION + " TEXT," +
                    FeedEntry.COLUMN_NAME_SEMESTER + " TEXT," +
                    FeedEntry.COLUMN_NAME_EMAIL + " EMAIL," +
                    FeedEntry.COLUMN_NAME_GASPAR + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    private UserDatabaseContract(){}


    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_SCIPER = "sciper";
        public static final String COLUMN_NAME_LAST_NAME = "last_name";
        public static final String COLUMN_NAME_FIRST_NAME = "first_name";
        public static final String COLUMN_NAME_GASPAR = "gaspar";
        public static final String COLUMN_NAME_SECTION = "section";
        public static final String COLUMN_NAME_SEMESTER = "semestre";
        public static final String COLUMN_NAME_EMAIL = "email";
    }
}
