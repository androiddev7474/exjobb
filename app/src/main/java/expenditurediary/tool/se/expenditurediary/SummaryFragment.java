package expenditurediary.tool.se.expenditurediary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import calculator.tools.se.calculator.CalculatorActivity;
import test1.sql.se.sqltest.MyDatabaseContract;
import test1.sql.se.sqltest.SQLhelper;
import test1.sql.se.sqltest.SqlQueries;

import static android.content.Context.MODE_PRIVATE;

public class SummaryFragment extends android.support.v4.app.Fragment {


    public static final String UNITDATEFRAG_INTENT = "unitdatefragintent";
    public static final String IRREGDATEFRAG_INTENT = "irregdatefragintent";

    //sharedpreferences
    public static final String DATA_PREFS = "data_for_charts_lists";
    public static final String BOOLEAN_KEY_DATA_IS_SET = "data_is_set";
    public static final String SWITCH_PREFS = "switch_prefs";
    public static final String BOOLEAN_SWITCH_IS_SET = "switch_is_set";

    private boolean isUnitDate, isIrregulardate;
    private float[] sums;
    private int year, month; // år och månad från UnitDateSpanFragment
    private int position; // position från IrregularDateSpanFragments listview position;
    private ListView listView;
    private View summaryView;
    private TextView monthLabel, yearLabel, periodName, irregPeriod, exp_total;
    private ImageView datespan, yearmonth;
    private RelativeLayout unitDateRL, irregDateRL;
    private PieChart pieChart;
    private Switch listViewSwitch;

    private boolean listViewSwitchIsOn;

    private SQLhelper sqLhelper;

    private BroadcastReceiver localBroadcastReceiver;

    private UnitDateSpanFragment unitDateSpanFragment = new UnitDateSpanFragment();
    private IrregularDateSpanFragment irregularDateSpanFragment = new IrregularDateSpanFragment();

    private ExpenditureDataAdapter expenditureDataAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        summaryView = inflater.inflate(R.layout.summary_fragment, container, false);

        listView = summaryView.findViewById(R.id.expenditure_listview_id);
        listViewSwitch = summaryView.findViewById(R.id.switch_listview);

        datespan = summaryView.findViewById(R.id.datespan_id);
        yearmonth = summaryView.findViewById(R.id.yearmonth_id);

        monthLabel = summaryView.findViewById(R.id.summary_month_id);
        yearLabel = summaryView.findViewById(R.id.summary_year_id);
        periodName = summaryView.findViewById(R.id.period_name_id);
        irregPeriod = summaryView.findViewById(R.id.period_date_id);
        exp_total = summaryView.findViewById(R.id.exp_summary_id);

        unitDateRL = summaryView.findViewById(R.id.rel_layout_unitdate_id);
        irregDateRL = summaryView.findViewById(R.id.rel_layout_irregdate_id);

        pieChart = summaryView.findViewById(R.id.piechart_1);

        sqLhelper = new SQLhelper(getContext());

        initDateClickListeners();
        initSwitchListener();
        initAdapterListView();

        localBroadcastReceiver = new LocalBroadcastReceiver();

        listViewSwitch.setChecked(getSavedSwitchPrefs());

        return summaryView;
    }


    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(localBroadcastReceiver, new IntentFilter(UNITDATEFRAG_INTENT));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(localBroadcastReceiver, new IntentFilter(IRREGDATEFRAG_INTENT));

        refreshAdapter();

    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(localBroadcastReceiver);
    }


    private void refreshAdapter() {

        Object[][] catTable = sqLhelper.getSQLdataCategories();
        String[] labels = new String[catTable.length];
        String[] iconRes = new String[catTable.length];

        for (int i = 0; i < catTable.length; i++) {
            labels[i] = catTable[i][0].toString();
            iconRes[i] = catTable[i][1].toString();
        }
        expenditureDataAdapter.setLabels(labels);
        expenditureDataAdapter.setIconRes(iconRes);
        expenditureDataAdapter.notifyDataSetChanged();
    }

    private void initDateClickListeners() {

        datespan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                irregularDateSpanFragment.show(getActivity().getSupportFragmentManager(), "irregulardatefrag");

            }
        });

        yearmonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                unitDateSpanFragment.show(getActivity().getSupportFragmentManager(), "unitdatefrag");
            }
        });

    }


    private void initSwitchListener() {

        listViewSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isListView) {

                if(isListView) {

                    listView.setVisibility(View.VISIBLE);
                    pieChart.setVisibility(View.GONE);
                    saveSwitchPrefs(true);
                    expenditureDataAdapter.setSums(sums);
                    refreshAdapter();

                } else {
                    pieChart.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    saveSwitchPrefs(false);
                    if (sums != null)
                        generate_picechart(sums);
                }
            }
        });
    }



    public static SummaryFragment newInstance(String text) {

        SummaryFragment summaryFragment = new SummaryFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        summaryFragment.setArguments(b);

        return summaryFragment;
    }



    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {

            if (!getSavedPrefs())
                generateTodaysData();
            else
                generateperiodData();
        }
    }


    /**
     * Perioddata från fragment - anropas och användaren varit där tidigare och kommer tillbaka
     */
    private void generateperiodData() {

        if (isIrregulardate) {

            IrregularDateSpanSQLhelper dataGen = new IrregularDateSpanSQLhelper(getContext());
            dataGen.getSql(position);
            sums = dataGen.sum_expenditure;
        } else if (isUnitDate) {

            ExpandListViewAdaperDataCreator creator = new ExpandListViewAdaperDataCreator(getContext());
            creator.createUnitDateData(sqLhelper);
            final Object[][] catTable = creator.getCatTable();
            sums = new float[catTable.length];
            if (month != - 1) {

                for (int i = 0; i < sums.length; i++)
                    sums[i] = sqLhelper.sumTableColumnYearMonthCat(year, month, catTable[i][0].toString());

            } else if (month == -1) {

                for (int i = 0; i < sums.length; i++)
                    sums[i] = sqLhelper.sumExpTableColumnYearCat(year, catTable[i][0].toString());
            }
        }
        setData();
    }


    /**
     *
     */
    private void generateTodaysData() {

        Object[][] catTable = sqLhelper.getSQLdataCategories();
        sums = new float[catTable.length];
        GregorianCalendar cal = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());
        //dagens datum uppdelat
        int year = Integer.parseInt(today.split("-")[0]);
        int month = Integer.parseInt(today.split("-")[1]);
        int monthJ_API = month - 1;
        int day = Integer.parseInt(today.split("-")[2]);

        cal.set(year, monthJ_API, day);
        int weekday = cal.get((Calendar.DAY_OF_WEEK));

        for (int i = 0; i < sums.length; i++) {
            String[] selArgs = {today, catTable[i][0].toString()};
            sums[i] = sqLhelper.getSumOfExpenditTableCol(selArgs);
        }

        periodName.setText(getContext().getResources().getStringArray(R.array.time_labels)[4].toUpperCase());
        irregPeriod.setText(today);

        setData();
    }


    private void initAdapterListView() {

        Object[][] catTable = sqLhelper.getSQLdataCategories();
        String[] labels = new String[catTable.length];
        String[] iconRes = new String[catTable.length];

        for (int i = 0; i < catTable.length; i++) {
            labels[i] = catTable[i][0].toString();
            iconRes[i] = catTable[i][1].toString();
        }

        expenditureDataAdapter = new ExpenditureDataAdapter(getContext(), labels, iconRes);
        listView.setAdapter(expenditureDataAdapter);
    }



    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent == null || intent.getAction() == null)
                return;

            if (intent.getAction().equals(UNITDATEFRAG_INTENT)) {

                unitDateRL.setVisibility(View.VISIBLE);
                irregDateRL.setVisibility(View.GONE);
                sums = unitDateSpanFragment.getArguments().getFloatArray(UnitDateSpanFragment.EXPENDITURE_RESULTS);
                year = unitDateSpanFragment.getArguments().getInt(UnitDateSpanFragment.EXPENDITURE_YEAR);
                month =  unitDateSpanFragment.getArguments().getInt(UnitDateSpanFragment.EXPENDITURE_MONTH);
                String monthName = "";
                if (month != -1) {
                    monthName = getContext().getResources().getStringArray(R.array.months)[month - 1];
                    monthLabel.setText("" + monthName);
                    yearLabel.setText("" + year);
                } else {
                    monthLabel.setText("");
                    yearLabel.setText("" + year);
                }

                setData();
                savePrefs(true);
                isUnitDate = true;
                isIrregulardate = false;

            } else if (intent.getAction().equals(IRREGDATEFRAG_INTENT)) {

                unitDateRL.setVisibility(View.GONE);
                irregDateRL.setVisibility(View.VISIBLE);
                sums = irregularDateSpanFragment.getArguments().getFloatArray(IrregularDateSpanFragment.EXPENDITURE_RESULTS_ALL);
                String label = irregularDateSpanFragment.getArguments().getString(IrregularDateSpanFragment.EXPENDITURE_IRREGPERIOD_LABEL);
                String period = irregularDateSpanFragment.getArguments().getString(IrregularDateSpanFragment.EXPENDITURE_FIRST_LAST_DATE);
                position = irregularDateSpanFragment.getArguments().getInt(IrregularDateSpanFragment.EXPENDITURE_LIST_POSITION);
                periodName.setText(label.toUpperCase());
                irregPeriod.setText(period);

                setData();
                savePrefs(true);

                isIrregulardate = true;
                isUnitDate = false;
            }
        }
    }


    private void setData() {

        if(sums == null)
            return;

        if (!getSavedSwitchPrefs()) {

            generate_picechart(sums);
            listView.setVisibility(View.GONE);
            pieChart.setVisibility(View.VISIBLE);
        } else {

            listView.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);
            expenditureDataAdapter.setSums(sums);
            expenditureDataAdapter.notifyDataSetChanged();
        }

        float total = 0;
        for (int i = 0; i < sums.length; i++)
            total += sums[i];
        exp_total.setText("" + (int)total);
    }

    private void generate_picechart(float[] sum) {

        pieChart.setUsePercentValues(false);
        pieChart.getDescription().setEnabled(false);

        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);
        ArrayList<PieEntry> yValues = new ArrayList<>();

        Object[][] catTable = sqLhelper.getSQLdataCategories();
        ArrayList <SummaryDataHolder> sumDataList = new ArrayList<>();
        float totalSum = 0;
        for (int i = 0; i < catTable.length; i++) {

            if (sum[i] > 0)
                sumDataList.add(new SummaryDataHolder(catTable[i][0].toString(), sum[i]));

            totalSum += sum[i];
        }

        for (SummaryDataHolder holder: sumDataList) {

            if ( (holder.getData() / totalSum) > 0.05f)
                yValues.add(new PieEntry(holder.getData(),holder.getName()));
        }

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.LIBERTY_COLORS);
        PieData pieData = new PieData((dataSet));
        pieData.setValueTextSize(15f);
        pieData.setValueTextColor(Color.BLACK);
        pieChart.setData(pieData);
    }


    private void savePrefs(boolean isSet) {

        SharedPreferences.Editor editor = getContext().getSharedPreferences(DATA_PREFS, MODE_PRIVATE).edit();
        editor.putBoolean(BOOLEAN_KEY_DATA_IS_SET, isSet);
        editor.apply();
    }

    private boolean getSavedPrefs() {

        SharedPreferences prefs = getContext().getSharedPreferences(DATA_PREFS, MODE_PRIVATE);
        return prefs.getBoolean(BOOLEAN_KEY_DATA_IS_SET, false);
    }

    private void saveSwitchPrefs(boolean isListView) {

        SharedPreferences.Editor editor = getContext().getSharedPreferences(SWITCH_PREFS, MODE_PRIVATE).edit();
        editor.putBoolean(BOOLEAN_SWITCH_IS_SET, isListView);
        editor.apply();
    }

    private boolean getSavedSwitchPrefs() {

        SharedPreferences prefs = getContext().getSharedPreferences(SWITCH_PREFS, MODE_PRIVATE);
        return prefs.getBoolean(BOOLEAN_SWITCH_IS_SET, false);
    }



}
