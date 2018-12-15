package ch.epfl.sweng.zuluzulu.LocalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

public class UserDatabase {
    private final UserDatabaseHelper mDbHelper;

    /**
     * Create the user database
     *
     * @param context Activity context
     */
    public UserDatabase(Context context) {
        this.mDbHelper = new UserDatabaseHelper(context);
    }

    /**
     * put the user to the local databaes
     *
     * @param user user
     * @return -1 id error, otherwise the row
     */
    public long put(AuthenticatedUser user) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserDatabaseContract.FeedEntry.COLUMN_NAME_SCIPER, user.getSciper());
        values.put(UserDatabaseContract.FeedEntry.COLUMN_NAME_GASPAR, user.getGaspar());
        values.put(UserDatabaseContract.FeedEntry.COLUMN_NAME_SECTION, user.getSection());
        values.put(UserDatabaseContract.FeedEntry.COLUMN_NAME_SEMESTER, user.getSemester());
        values.put(UserDatabaseContract.FeedEntry.COLUMN_NAME_FIRST_NAME, user.getFirstNames());
        values.put(UserDatabaseContract.FeedEntry.COLUMN_NAME_EMAIL, user.getEmail());
        values.put(UserDatabaseContract.FeedEntry.COLUMN_NAME_LAST_NAME, user.getLastNames());

        // Insert the new row, returning the primary key value of the new row

        return db.insert(UserDatabaseContract.FeedEntry.TABLE_NAME, null, values);
    }

    /**
     * return the user if in database, null otherwise
     *
     * @return User or null
     */
    public AuthenticatedUser getUser() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {BaseColumns._ID,
                UserDatabaseContract.FeedEntry.COLUMN_NAME_SCIPER, UserDatabaseContract.FeedEntry.COLUMN_NAME_GASPAR,
                UserDatabaseContract.FeedEntry.COLUMN_NAME_SECTION,
                UserDatabaseContract.FeedEntry.COLUMN_NAME_SEMESTER, UserDatabaseContract.FeedEntry.COLUMN_NAME_FIRST_NAME,
                UserDatabaseContract.FeedEntry.COLUMN_NAME_EMAIL, UserDatabaseContract.FeedEntry.COLUMN_NAME_LAST_NAME
        };

        ArrayList<AuthenticatedUser> list = new ArrayList<>();
        try (Cursor cursor = db.query(UserDatabaseContract.FeedEntry.TABLE_NAME, projection, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                AuthenticatedUser user = createUser(cursor);

                if (user != null && user.getSciper().length() >= 5) {
                    list.add(user);
                }
            }
        }

        if (!list.isEmpty()) {
            return list.get(0);
        }

        return null;
    }

    /**
     * Create a user in the local database using a cursor
     *
     * @param cursor cursor containing the user's data
     * @return the newly created user
     */
    private AuthenticatedUser createUser(Cursor cursor) {
        String semester = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseContract.FeedEntry.COLUMN_NAME_SEMESTER));

        String sciper = cursor.getString(cursor.getColumnIndexOrThrow(UserDatabaseContract.FeedEntry.COLUMN_NAME_SCIPER));

        String section = cursor.getString(
                cursor.getColumnIndexOrThrow(UserDatabaseContract.FeedEntry.COLUMN_NAME_SECTION));
        String gaspar = cursor.getString(
                cursor.getColumnIndexOrThrow(UserDatabaseContract.FeedEntry.COLUMN_NAME_GASPAR));
        String last_name = cursor.getString(
                cursor.getColumnIndexOrThrow(UserDatabaseContract.FeedEntry.COLUMN_NAME_LAST_NAME));
        String first_name = cursor.getString(
                cursor.getColumnIndexOrThrow(UserDatabaseContract.FeedEntry.COLUMN_NAME_FIRST_NAME));
        String email = cursor.getString(
                cursor.getColumnIndexOrThrow(UserDatabaseContract.FeedEntry.COLUMN_NAME_EMAIL));

        return new User.UserBuilder()
                .setEmail(email)
                .setFirst_names(first_name)
                .setLast_names(last_name)
                .setSciper(sciper)
                .setSection(section)
                .setSemester(semester)
                .setGaspar(gaspar)
                .setFollowedAssociations(new ArrayList<>())
                .setFollowedEvents(new ArrayList<>())
                .setFollowedChannels(new ArrayList<>())
                .buildAuthenticatedUser();
    }

    /**
     * Delete the user from the database
     *
     * @param user User
     * @return row on success, or -1
     */
    public int delete(AuthenticatedUser user) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define 'where' part of query.
        String selection = UserDatabaseContract.FeedEntry.COLUMN_NAME_SCIPER + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = {user.getSciper()};
        // Issue SQL statement.
        return db.delete(UserDatabaseContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }
}
