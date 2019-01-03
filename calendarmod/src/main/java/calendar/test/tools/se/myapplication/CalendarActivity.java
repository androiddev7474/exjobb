package calendar.test.tools.se.myapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.StringTokenizer;

import sun.bob.mcalendarview.MarkStyle;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.listeners.OnMonthChangeListener;
import sun.bob.mcalendarview.utils.CalendarUtil;
import sun.bob.mcalendarview.utils.CurrentCalendar;
import sun.bob.mcalendarview.views.ExpCalendarView;
import sun.bob.mcalendarview.vo.DateData;
import test1.sql.se.sqltest.SQLhelper;


public class CalendarActivity extends AppCompatActivity {

    private final static String OK_CALENDAR_TAG = "tag_calendar_ok";
    private final static String CANCEL_CALENDAR_TAG = "tag_calendar_cancel";

    //intent names
    public static final String CALENDAR_RESULT_DATE = "date_calendaractivity";


    private BroadcastReceiver localBroadcastReceiver;

    private CalendarFragment calendarFragment;

    private ActionMenuView amvMenu;

    private DateData selectedDate;
    private YearPickerFragment yearPickerFragment = new YearPickerFragment();


    private TextView label_year, label_month, label_weekday_day;

    ExpCalendarView calendarView;

    private SQLhelper sqLhelper;

    int[][] sqlDates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        amvMenu = (ActionMenuView) toolbar.findViewById(R.id.amvMenu);
        amvMenu.setOnMenuItemClickListener(new ActionMenuView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return onOptionsItemSelected(menuItem);
            }
        });
        toolbar.getNavigationIcon().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);

        initViews();

        String dateString = getIntent().getStringExtra(CALENDAR_RESULT_DATE);
        int[] dateArray = formatDateString(dateString);
        selectedDate = new DateData(dateArray[0], dateArray[1], dateArray[2]);

        label_year.setText("" + dateArray[0]);
        //label_year.setPaintFlags(label_year.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        label_month.setText(DateUtils.getMonth(CalendarActivity.this, dateArray[1]).toUpperCase());

        initSQLdata();
        for (int i = 0; i < sqlDates.length; i++)
            calendarView.markDate(new DateData(sqlDates[i][0],sqlDates[i][1],sqlDates[i][2]).setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.RED))).hasTitle(false);


        calendarView.travelTo(selectedDate);

        setDateStringView(selectedDate);

        createOnDateClickListener();
        createOnMonthChangeListener();

        localBroadcastReceiver = new LocalBroadcastReceiver();

    }


    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(this).registerReceiver(localBroadcastReceiver, new IntentFilter(YearPickerFragment.YEAR_INTENT));


    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadcastReceiver);
    }


    private void initViews() {

        calendarView = ((ExpCalendarView) findViewById(R.id.calendar));
        label_year = findViewById(R.id.date_year_label_id);
        label_weekday_day = findViewById(R.id.date_weekday_label_id);
        label_month = findViewById(R.id.date_month_label_id);
    }

    /**
     * Hämtar sqlData
     *
     */
    private void initSQLdata() {

        sqLhelper = new SQLhelper(this);
        Object[][] tableData = sqLhelper.getSQLdataExpenditure();
        final int N_DATES_PER_ROW = 3;
        sqlDates = new int[tableData.length][N_DATES_PER_ROW];
        final int COLUMN_ID = 1;
        for (int i = 0; i < tableData.length; i++) {

            StringTokenizer stringTokenizer = new StringTokenizer(tableData[i][COLUMN_ID].toString(),"-");
            int counter = 0;
            while (stringTokenizer.hasMoreElements()) {

                try {
                    sqlDates[i][counter] = Integer.parseInt(stringTokenizer.nextElement().toString());
                } catch (NumberFormatException ne) {
                    System.err.println("NumberFormatException CalendarActivity (StringTokennizer)");
                } catch (Exception e) {
                    System.err.println("Exception CalendarActivity (StringTokennizer)");
                }
                counter++;
            }
        }
    }

    private int[] formatDateString(String dateString) {

        StringTokenizer stringTokenizer = new StringTokenizer(dateString,"-");
        int counter = 0;
        int[] dateArray = new int[3];
        while (stringTokenizer.hasMoreElements()) {

            try {
                dateArray[counter] = Integer.parseInt(stringTokenizer.nextElement().toString());
            } catch (NumberFormatException ne) {
                System.err.println("NumberFormatException CalendarActivity (StringTokennizer)");
            } catch (Exception e) {
                System.err.println("Exception CalendarActivity (StringTokennizer)");
            }
            counter++;
        }

        return dateArray;
    }


    private void createOnDateClickListener() {

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {

                calendarView.getMarkedDates().removeAdd();
                selectedDate = date;
                setDateStringView(date);
                //calendarView.markDate(new DateData(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDay()).setMarkStyle(new MarkStyle(MarkStyle.DEFAULT, Color.BLUE))).hasTitle(false);
                calendarView.travelTo(new DateData(selectedDate.getYear(), selectedDate.getMonth(), selectedDate.getDay()));
                for (int i = 0; i < sqlDates.length; i++)
                    calendarView.markDate(new DateData(sqlDates[i][0], sqlDates[i][1], sqlDates[i][2]).setMarkStyle(new MarkStyle(MarkStyle.DOT, Color.RED))).hasTitle(false);
            }
        });
    }


    private void createOnMonthChangeListener() {

        calendarView.setOnMonthChangeListener(new OnMonthChangeListener() {
            @Override
            public void onMonthChange(int year, int month) {

                label_year.setText("" + year);
                label_month.setText(DateUtils.getMonth(CalendarActivity.this, month).toUpperCase());
                System.out.println("month: " + month);
            }
        });
    }


    public void setDateStringView(DateData dateData) {

        String dateString = dateData.getYear() + "-" + dateData.getMonth() + "-" + dateData.getDay();
        int dayOfWeek = DateUtils.getDayOfWeek(dateString);
        String form_date_string = DateUtils.getWeekdayString(this, dayOfWeek) + " " + dateData.getDay() + " " + DateUtils.getMonth(this, dateData.getMonth());
        label_weekday_day.setText(form_date_string);
    }


    public void onClickYear() {

        Bundle bundle = new Bundle();
        bundle.putInt(YearPickerFragment.YEAR_KEY, selectedDate.getYear());
        bundle.putInt(YearPickerFragment.MONTH_KEY, selectedDate.getMonth());
        bundle.putInt(YearPickerFragment.DAY_KEY, selectedDate.getDay());
        yearPickerFragment.setArguments(bundle);
        yearPickerFragment.show(getSupportFragmentManager(), YearPickerFragment.YEARPICKER_FRAGMENT_KEY);

    }


    public void onCalendarOKcancel(View view) {

        Intent returnIntent = new Intent();
        switch (view.getTag().toString()) {

            case OK_CALENDAR_TAG:

                String day = (selectedDate.getDay() > 9) ? "" + selectedDate.getDay() : "0" + selectedDate.getDay();
                String month = (selectedDate.getMonth() > 9) ? "" + selectedDate.getMonth() : "0" + selectedDate.getMonth();

                String dateString = selectedDate.getYear() + "-" + month + "-" + day;
                returnIntent.putExtra(getString(R.string.RESULT_DATE), dateString);

                setResult(Activity.RESULT_OK, returnIntent);
                calendarView.unMarkDate(selectedDate);
                finish();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.calendar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            setResult(Activity.RESULT_CANCELED, new Intent());
            calendarView.unMarkDate(selectedDate);
            finish();
            return true;
        }

        if (id == R.id.action_yearpicker) {
            onClickYear();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent == null || intent.getAction() == null)
                return;

            if (intent.getAction().equals(YearPickerFragment.YEAR_INTENT)) {

                int year = yearPickerFragment.getArguments().getInt(YearPickerFragment.SELECTED_YEAR_KEY);

                DateData date = new DateData(year, 1, 1);
                calendarView.travelTo(date);
                setDateStringView(date);
                yearPickerFragment.dismiss();
                selectedDate = date;


            }



        }
    }


}


