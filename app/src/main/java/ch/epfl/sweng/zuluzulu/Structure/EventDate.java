package ch.epfl.sweng.zuluzulu.Structure;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * represent a date of an event
 */
public class EventDate implements Serializable {
    private static final String DATE_TIME_PATTERN = "dd.MM.yyyy HH:mm:ss";
    private static final String DATE_TIME_READ_HOURS = "dd.MM.yyyy HH:mm";
    private static final String DATE_TIME_READ_NO_HOURS = "dd.MM.yyyy";

    private Date startDate;
    private Date endDate;

    /**
     * Create an Event date
     *
     * @param start_date start date
     * @param end_date   end date
     */
    public EventDate(String start_date, String end_date) {
        this(start_date, "", end_date, "");
    }

    /**
     * Create an EventDate
     *
     * @param start start date
     * @param end   start date
     */
    public EventDate(Date start, Date end) {
        assert (start != null);
        assert (end != null);

        this.setStartDate(start);
        this.setEndDate(end);
    }

    /**
     * Create an EventDate
     *
     * @param start_date start date
     * @param start_time start time
     * @param end_date   end date
     * @param end_time   end time
     */
    public EventDate(String start_date, String start_time, String end_date, String end_time) {
        this(stringToStartDate(start_date, start_time), stringToEndDate(end_date, end_time));
    }

    /**
     * Set start date
     *
     * @param start_date_string start date
     * @param start_time_string start time
     */
    private static Date stringToStartDate(String start_date_string, String start_time_string) {
        if (start_date_string == null || "2018-01-01".length() != start_date_string.length()) {
            throw new IllegalArgumentException();
        }
        assert (start_time_string != null);

        String date = start_date_string;

        if ("hh:mm:ss".length() == start_time_string.length()) {
            date = date + " " + start_time_string;
        } else {
            date = date + " 00:00:01";
        }

        return createDate(date);
    }

    /**
     * Set end date
     *
     * @param end_date_string end date
     * @param end_time_string end time
     */
    private static Date stringToEndDate(String end_date_string, String end_time_string) {
        checkDateStringInput(end_date_string, end_time_string);

        String date = end_date_string;

        if (end_time_string.length() == "hh:mm:ss".length()) {
            date = date + " " + end_time_string;
        } else {
            date = date + " 23:59:59";
        }

        return createDate(date);
    }

    private static void checkDateStringInput(String date, String time) {
        if (date == null || "2018-01-01".length() != date.length()) {
            throw new IllegalArgumentException();
        }
        assert (time != null);
    }

    /**
     * Create date from string
     *
     * @param date Date
     * @return String
     */
    private static Date createDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);

        Date start_date = null;
        try {
            start_date = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException();
        }
        return start_date;
    }

    /**
     * return the start date
     *
     * @return Date
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Set start date
     *
     * @param startDate Date
     */
    private void setStartDate(Date startDate) {
        assert (startDate != null);
        this.startDate = startDate;
    }

    /**
     * Get date string format
     *
     * @return String
     */
    public String getStartDateString() {
        return DateToString(startDate);
    }

    /**
     * getAndAddOnSuccessListener End date as string
     *
     * @return string
     */
    public String getEndDateString() {
        return DateToString(endDate);
    }

    /**
     * Create String from date
     *
     * @param date date
     * @return String
     */
    private String DateToString(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_PATTERN);
        return simpleDateFormat.format(date);
    }

    /**
     * getAndAddOnSuccessListener end date
     *
     * @return Date
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * set end date
     *
     * @param endDate end date
     */
    public void setEndDate(Date endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException();
        }
        this.endDate = endDate;
    }

    public String getDateTimeUser(boolean fullDate) {
        StringBuilder sb = new StringBuilder();

        long nb_hours = (getStartDate().getTime() - new Date().getTime()) / 3600000;
        long nb_days = nb_hours / 24;

        if (nb_days <= 30 && !fullDate) {
            printTimeRemaining(nb_days, nb_hours, sb);
        } else {
            printDateStart(sb);
        }
        return sb.toString();
    }

    private void printTimeRemaining(long nb_days, long nb_hours, StringBuilder sb) {
        long nb_hours_end = (getEndDate().getTime() - new Date().getTime())/3600000;
        long nb_days_end = nb_hours_end/24;

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(Timestamp.now().toDate());
        int dayActu = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(getStartDate());
        int dayOfEvent = calendar.get(Calendar.DAY_OF_MONTH);
        int hourOfEvent = calendar.get(Calendar.HOUR_OF_DAY);
        String minuteOfDay = String.format("%02d", calendar.get(Calendar.MINUTE));

        if (nb_days <= 0) {
            if(nb_hours_end <= 0)
                sb.append("Terminé");
            else if(nb_hours <= 0)
                sb.append("Maintenant - " + dateRemoveZeroHour(getEndDate()));
            else {
                if(dayActu - dayOfEvent == 0)
                    sb.append("Aujourd'hui à " + hourOfEvent + "h" + minuteOfDay + " (dans " + nb_hours + " heures)");
                else
                    sb.append("Demain à " + hourOfEvent + "h" + minuteOfDay + " (dans " + nb_hours + " heures)");
            }
        } else
            sb.append("Dans " + nb_days + " jours");
    }

    private void printDateStart(StringBuilder sb) {
        if (!getEndDateString().equals(getStartDateString()) && endDate.getSeconds() != 59) {
            sb.append("Du ");
        } else {
            sb.append("Le ");
        }

        sb.append(dateRemoveZeroHour(startDate));

        if (!getEndDateString().equals(getStartDateString()) && endDate.getSeconds() != 59) {
            sb.append(" au ");
            sb.append(dateRemoveZeroHour(endDate));
        }
    }

    private String dateRemoveZeroHour(Date date) {
        if (startDate.getHours() != 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_READ_HOURS);
            return (simpleDateFormat.format(date));
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_READ_NO_HOURS);
            return (simpleDateFormat.format(date));
        }
    }
}
