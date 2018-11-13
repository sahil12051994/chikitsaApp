package com.chikitsa.root.chikitsa;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**Add event to calendar and make reminder(once / daily)
 *
 */
public class CalenderReminder {

    /**Here we are adding event to device default calendar and then calling reminder one day before event date only
     * reminder type can be email, sms, alert or alarm
     *
     * @param context
     */
    public static void showReminderOneDayBeforeEvent(Context context) {
        String eventTitle = "Ready Android"; //This is event title
        String eventDescription = "Always happy to help u :)"; //This is event description
        String eventDate = "13/11/2013"; //This is the event date
        String eventLocation = "Taj Mahal, Agra, Uttar Pradesh 282001"; //This is the address for your event location

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date dEventDate = dateFormat.parse(eventDate); //Date is formatted to standard format “MM/dd/yyyy”
            cal.setTime(dEventDate);
            cal.add(Calendar.DATE, -1); //It will return one day before calendar of eventDate
        } catch (Exception e) {
            e.printStackTrace();
        }

        String reminderDate = dateFormat.format(cal.getTime());
        Log.e("Day before event start", reminderDate);
        String reminderDayStart = reminderDate + " 00:00:00";
        String reminderDayEnd = reminderDate + " 23:59:59";
        long startTimeInMilliseconds = 0, endTimeInMilliseconds = 0;

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
            Date SDate = formatter.parse(reminderDayStart);
            Date EDate = formatter.parse(reminderDayEnd);
            startTimeInMilliseconds = SDate.getTime();
            endTimeInMilliseconds = EDate.getTime();
            Log.e("StartDate", startTimeInMilliseconds + " " + reminderDayStart);
            Log.e("EndDate", endTimeInMilliseconds + " " + reminderDayEnd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.DTSTART, startTimeInMilliseconds);
        values.put(CalendarContract.Events.DTEND, endTimeInMilliseconds);
        values.put(CalendarContract.Events.TITLE, eventTitle);
        values.put(CalendarContract.Events.DESCRIPTION, eventDescription);
        values.put(CalendarContract.Events.EVENT_LOCATION, eventLocation);

        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.RRULE, "FREQ=HOURLY;COUNT=1");
        values.put(CalendarContract.Events.HAS_ALARM, 1);
        Uri eventUri;

        if (Build.VERSION.SDK_INT >= 8) {
            eventUri = Uri.parse("content://com.android.calendar/events");
        } else {
            eventUri = Uri.parse("content://calendar/events");
        }
// insert event to calendar
        Uri uri = cr.insert(eventUri, values);
        Log.e("EventTest", uri.toString());

//add reminder for event
//This reminder will be show for 12/11/2013, because event date is 13/11/2013
        try {
            Uri REMINDERS_URI;
            long id = -1;
            id = Long.parseLong(uri.getLastPathSegment());
            ContentValues reminders = new ContentValues();
            reminders.put(CalendarContract.Reminders.EVENT_ID, id);
            reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            reminders.put(CalendarContract.Reminders.MINUTES, 1);
            if (Build.VERSION.SDK_INT >= 8) {
                REMINDERS_URI = Uri.parse("content://com.android.calendar/reminders");
            } else {
                REMINDERS_URI = Uri.parse("content://calendar/reminders");
            }
            Uri remindersUri = context.getContentResolver().insert(REMINDERS_URI, reminders);
            Log.e("RemindersTest", remindersUri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**Here we are adding event to device default calendar and then calling reminder till event date daily
     * reminder type can be email, sms, alert or alarm
     *
     * @param context
     */
    public static void showReminderDailyBeforeEventDay(Context context) {
        String eventTitle = "Ready Android"; //This is event title
        String eventDescription = "Always happy to help u :)"; //This is event description
        String eventDate = "13/11/2013"; //This is the event date
        String eventLocation = "Taj Mahal, Agra, Uttar Pradesh 282001"; //This is the address for your event location

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        long startTimeInMilliseconds = System.currentTimeMillis(), endTimeInMilliseconds = 0;
        try {
            Date mDate = formatter.parse(eventDate);
            endTimeInMilliseconds = mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        values.put(CalendarContract.Events.TITLE, eventTitle);
        values.put(CalendarContract.Events.DESCRIPTION, eventDescription);
        values.put(CalendarContract.Events.EVENT_LOCATION, eventLocation);
        values.put(CalendarContract.Events.DTSTART, startTimeInMilliseconds); //This is the time period in millis when i add an event to calendar
        values.put(CalendarContract.Events.DTEND, endTimeInMilliseconds/*System.currentTimeMillis() + 86400000 * 4*/); //This is the event date in milli second
        values.put(CalendarContract.Events.HAS_ALARM, 1);
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.RRULE, "FREQ=DAILY;COUNT=1");
        Uri eventUri;
        if (Build.VERSION.SDK_INT >= 8) {
            eventUri = Uri.parse("content://com.android.calendar/events");
        } else {
            eventUri = Uri.parse("content://calendar/events");
        }
        Uri l_uri = context.getContentResolver().insert(eventUri, values);
        Log.e("EventTest", l_uri.toString());

        try {
            Uri REMINDERS_URI;
            long id = Long.parseLong(l_uri.getLastPathSegment()); //Added event id
            ContentValues reminders = new ContentValues();
            reminders.put(CalendarContract.Reminders.EVENT_ID, id);
//METHOD_DEFAULT = 0, METHOD_ALERT = 1, METHOD_EMAIL = 2, METHOD_SMS = 3, METHOD_ALARM = 4
            reminders.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            reminders.put(CalendarContract.Reminders.MINUTES, 1);
            if (Build.VERSION.SDK_INT >= 8) {
                REMINDERS_URI = Uri.parse("content://com.android.calendar/reminders");
            } else {
                REMINDERS_URI = Uri.parse("content://calendar/reminders");
            }
            Uri uri = context.getContentResolver().insert(REMINDERS_URI, reminders);
            Log.e("RemindersTest", uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

