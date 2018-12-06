package ch.epfl.sweng.zuluzulu.LocalDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.zuluzulu.Structure.EventBuilder;
import ch.epfl.sweng.zuluzulu.User.AuthenticatedUser;
import ch.epfl.sweng.zuluzulu.User.User;

public class UserDatabase {
    private final UserDatabaseHelper mDbHelper;

    public UserDatabase(Context context){
        this.mDbHelper = new UserDatabaseHelper(context);
    }

    public long put(AuthenticatedUser user){
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(UserDatabaseContract.FeedEntry.COLUMN_NAME_SCIPER, user.getSciper());
        values.put(UserDatabaseContract.FeedEntry.COLUMN_NAME_GASPAR, user.getGaspar());
        values.put(UserDatabaseContract.FeedEntry.COLUMN_NAME_SECTION, user.getSection());
        values.put(UserDatabaseContract.FeedEntry.COLUMN_NAME_SEMESTER, user.getSection());
        values.put(UserDatabaseContract.FeedEntry.COLUMN_NAME_FIRST_NAME, user.getFirstNames());
        values.put(UserDatabaseContract.FeedEntry.COLUMN_NAME_EMAIL, user.getEmail());
        values.put(UserDatabaseContract.FeedEntry.COLUMN_NAME_LAST_NAME, user.getLastNames());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(UserDatabaseContract.FeedEntry.TABLE_NAME, null, values);

        System.out.println("addded " + newRowId);

        return newRowId;
    }

    public AuthenticatedUser getUser() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                UserDatabaseContract.FeedEntry.COLUMN_NAME_SCIPER,
                UserDatabaseContract.FeedEntry.COLUMN_NAME_GASPAR,
                UserDatabaseContract.FeedEntry.COLUMN_NAME_SECTION,
                UserDatabaseContract.FeedEntry.COLUMN_NAME_SEMESTER,
                UserDatabaseContract.FeedEntry.COLUMN_NAME_FIRST_NAME,
                UserDatabaseContract.FeedEntry.COLUMN_NAME_EMAIL,
                UserDatabaseContract.FeedEntry.COLUMN_NAME_LAST_NAME
        };

        Cursor cursor = db.query(
                UserDatabaseContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );

        List<AuthenticatedUser> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            System.out.println("HERE");
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(UserDatabaseContract.FeedEntry._ID));
            String semester = cursor.getString(
                    cursor.getColumnIndexOrThrow(UserDatabaseContract.FeedEntry.COLUMN_NAME_SEMESTER));
            String sciper = cursor.getString(
                    cursor.getColumnIndexOrThrow(UserDatabaseContract.FeedEntry.COLUMN_NAME_SCIPER));
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
            System.out.println(email);
            User.UserBuilder builder = new User.UserBuilder();
            AuthenticatedUser user = builder
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

            if(user != null && user.getSciper().length() >= 5) {
                list.add(user);
            }

        }
        cursor.close();

        if(!list.isEmpty()){
            return list.get(0);
        }

        return null;
    }

    public int delete(AuthenticatedUser user){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define 'where' part of query.
        String selection = UserDatabaseContract.FeedEntry.COLUMN_NAME_SCIPER + " LIKE ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { user.getSciper() };
        // Issue SQL statement.
        return db.delete(UserDatabaseContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }
}
