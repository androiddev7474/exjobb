package calendar.test.tools.se.myapplication;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Fragment för att välja ett specifikt årtal
 * Björn Hallström
 * Version: 1 (2018-11-27)
 */

public class YearPickerFragment extends DialogFragment {

    public static final String YEARPICKER_FRAGMENT_KEY = "yearpickerfragment";
    public static final String YEAR_KEY = "year";
    public static final String MONTH_KEY = "month";
    public static final String DAY_KEY = "day";

    //intent
    public static final String YEAR_INTENT = "year_intent";

    //bundle
    public static final String SELECTED_YEAR_KEY = "selected_year_key";

    private View yearView;
    private TextView year_label, month_day_label;
    private ImageView closeIcon;
    private ListView yearListView;
    private MyArrayAdapter arrayAdapter;

    private String[] years;
    private int selectedYear;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        yearView = inflater.inflate(R.layout.dialogfragment_yearpicker, container,false);
        year_label = yearView.findViewById(R.id.year_id);
        month_day_label = yearView.findViewById(R.id.day_month_id);
        yearListView = yearView.findViewById(R.id.year_list_id);
        closeIcon = yearView.findViewById(R.id.close_icon_id);

        addViewTreeObserver();

        int[] dates = getDateArguments();
        final int weekday_id = DateUtils.getDayOfWeek(dates[0] + "-" + dates[1] + "-" + dates[2]);
        String weekday = DateUtils.getWeekdayString(getContext(), weekday_id);
        year_label.setText("" + dates[0]);
        month_day_label.setText(weekday + " " + dates[2] + " " + DateUtils.getMonth(getContext(), dates[1]));

        years = getContext().getResources().getStringArray(R.array.years);
        arrayAdapter = new MyArrayAdapter(getContext(), R.layout.year_picker_container, years);
        yearListView.setAdapter(arrayAdapter);
        yearListView.setSelection(getPosition(years, "" + dates[0]));
        int baseColor = getActivity().getResources().getColor(R.color.colorPrimaryDark);
        int[] colorsBase = {0, baseColor, 0};
        yearListView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colorsBase));
        yearListView.setDividerHeight(5);

        initClickListeners();
        initOnItemClickListener();

        return yearView;
    }

    private void addViewTreeObserver() {

        yearView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                ViewGroup.LayoutParams params = yearView.getLayoutParams();
                params.height = (int) (getContext().getResources().getDisplayMetrics().heightPixels * 0.75f);
                yearView.setLayoutParams(params);
            }
        });
    }


    private int[] getDateArguments() {

        final int[] dates = {getArguments().getInt(YEAR_KEY, 0), getArguments().getInt(MONTH_KEY, 0), getArguments().getInt(DAY_KEY, 0)};

        return dates;

    }

    private static int getPosition(String[] values, String target) {

        for (int i = 0; i < values.length; i++) {

            if (values[i].matches(target))
                return i;
        }

        return 0;
    }


    private void initClickListeners() {

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

    }

    private void initOnItemClickListener() {

        yearListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                selectedYear = Integer.parseInt(years[position]);
                Bundle b = new Bundle();
                b.putInt(SELECTED_YEAR_KEY, selectedYear);
                setArguments(b);
                LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(YEAR_INTENT));
                dismiss();

            }
        });


    }


}
