package calendar.test.tools.se.myapplication;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.views.ExpCalendarView;
import sun.bob.mcalendarviewtest.ExpMainActivity;
import sun.bob.mcalendarviewtest.*;
import sun.bob.*;

public class CalendarFragment extends DialogFragment {

    private View fragmentCalendarView;

    private CalendarView calendarView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentCalendarView = inflater.inflate(R.layout.dialogfragment_calendarview, container, false);



        /*calendarView = (CalendarView) fragmentCalendarView.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {

                Toast.makeText(getContext(), "Selected Date:\n" + "Day = " + i2 + "\n" + "Month = " + i1 + "\n" + "Year = " + i, Toast.LENGTH_LONG).show();
            }
        });
        */

        //MCalendarView calendarView = ((MCalendarView) fragmentCalendarView.findViewById(R.id.calendar_exp));
        final ExpCalendarView calendarView = ((ExpCalendarView) fragmentCalendarView.findViewById(R.id.calendar));





        return fragmentCalendarView;

    }

}
