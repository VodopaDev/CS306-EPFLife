package ch.epfl.sweng.zuluzulu.utility;
import java.util.Calendar;
import java.util.Date;

public interface TimeUtility {
    /**
     * Return the time passed in milliseconds since the given date
     *
     * @param date the date
     * @return the time passed since the given date
     */
    static long getMillisecondsSince(Date date) {
        if (date == null) {
            throw new NullPointerException();
        }
        long dateTime = date.getTime();
        long now = Calendar.getInstance().getTimeInMillis();
        return now - dateTime;
    }

    /**
     * Convert hour and minute from integer to beautiful string
     *
     * @param hour   The hour
     * @param minute The minutes
     * @return The string of the hour and the minutes
     */
    static String hourAndMinutesFrom(int hour, int minute) {
        if (!validHour(hour) || !validMinute(minute)) {
            throw new IllegalArgumentException();
        }
        String strHour = String.valueOf(hour);
        String strMinute = String.valueOf(minute);
        if (strHour.length() < 2) {
            strHour = "0" + strHour;
        }
        if (strMinute.length() < 2) {
            strMinute = "0" + strMinute;
        }
        return strHour + ":" + strMinute;
    }

    /**
     * Check whether a hour is valid
     *
     * @param hour The hour to check
     * @return whether the hour is valid
     */
    static boolean validHour(int hour) {
        return 0 <= hour && hour <= 23;
    }

    /**
     * Check whether a minute is valid
     *
     * @param minute The minute to check
     * @return whether the minute is valid
     */
    static boolean validMinute(int minute) {
        return 0 <= minute && minute <= 59;
    }
}
