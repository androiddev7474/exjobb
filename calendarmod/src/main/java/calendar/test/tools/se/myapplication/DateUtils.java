package calendar.test.tools.se.myapplication;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Utilklass för att tex räkna ut veckodag utifrån datum eller få ut namnet på en månad utifrån ett heltal
 * Björn Hallström
 * Version: 1 (2018-11-27)
 *
 */

public class DateUtils {

    private static final String DATE_PATTERN = "yyyy-MM-dd";


    /**
     * Returnerar namnet på en veckodag utifrån ett inkommande heltal (1 - 7)
     * @param weekday_id
     * @return
     */
    public static String getWeekdayString(Context context, int weekday_id) {

        return context.getResources().getStringArray(R.array.weekday)[weekday_id - 1];
    }


    /**
     * Returnerar namnet på en månad utifrån dess heltalsvärde
     * @param month_id
     * @return
     */
    public static String getMonth(Context context, int month_id) {

        return context.getResources().getStringArray(R.array.months)[month_id - 1];
    }


    /**
     * Returnerar veckodag i heltalsform (1 - 7) utifrån en datumsträng
     * @param date
     * @return
     */
    public static int getDayOfWeek(String date) {

        try {
            int day = 0;
            SimpleDateFormat simpleDateformat = new SimpleDateFormat(DATE_PATTERN);
            Date now = simpleDateformat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            day = calendar.get(Calendar.DAY_OF_WEEK);
            return day;
        } catch (Exception e) {
            return 0;
        }
    }


}
