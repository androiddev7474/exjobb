package expenditurediary.tool.se.expenditurediary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import test1.sql.se.sqltest.SQLhelper;

public class IrregularDateSpanFragment extends DialogFragment {


    public static final String EXPENDITURE_RESULTS_ALL = "expenditure_results_all";
    public static final String EXPENDITURE_FIRST_LAST_DATE = "first_last_date_expenditure";
    public static final String EXPENDITURE_IRREGPERIOD_LABEL = "irregular_period_label";
    public static final String EXPENDITURE_LIST_POSITION = "listview_position_irregfrag";


    private int position;

    private View irregularDateView;
    private ListView listView;
    private ImageView closeIcon;
    private TextView noContentView;
    private IrregularDateSpanAdapter adapter;

    private IrregularDateSpanSQLhelper dataGen;



    private String firstLastDates;
    private String periodLabel = "";
    private String[] labels;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle savedInstanceState) {

        dataGen = new IrregularDateSpanSQLhelper(getContext());

        irregularDateView = inflater.inflate(R.layout.irregulardate_fragment, containter, false);
        closeIcon = irregularDateView.findViewById(R.id.irregfrag_close_icon);
        noContentView = irregularDateView.findViewById(R.id.no_listcontent_info_irregfrag);

        initAdapterSetViewPrefs();

        initClickListeners();

        return  irregularDateView;
    }

    private void initAdapterSetViewPrefs() {

        listView = irregularDateView.findViewById(R.id.irregular_listview);
        labels = getContext().getResources().getStringArray(R.array.time_labels);
        adapter = new IrregularDateSpanAdapter(getContext(), labels);
        listView.setAdapter(adapter);

        int baseColor = getActivity().getResources().getColor(R.color.colorPrimaryDark);
        int[] colorsBase = {0, baseColor, 0};
        listView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colorsBase));
        listView.setDividerHeight(5);

        checkIfEmptyList();

        initOnItemClickListener();

    }

    private void checkIfEmptyList() {

        SQLhelper sqLhelper = new SQLhelper(getContext());
        Object[][] expTableData = sqLhelper.getSQLdataExpenditure();

        //måste finnas data
        if (expTableData.length != 0) {
            noContentView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        } else {
            noContentView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

    }


    private void initOnItemClickListener() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position_, long id) {

                position = position_;
                firstLastDates = dataGen.getSql(position);
                periodLabel = labels[position];
                dismiss();
            }
        });
    }



    @Override
    public void onDismiss(DialogInterface dialog) {

        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

        if (dataGen.sum_expenditure != null) {
            Bundle b = new Bundle();
            b.putFloatArray(EXPENDITURE_RESULTS_ALL, dataGen.sum_expenditure);
            b.putString(EXPENDITURE_FIRST_LAST_DATE, firstLastDates);
            b.putString(EXPENDITURE_IRREGPERIOD_LABEL, periodLabel);
            b.putInt(EXPENDITURE_LIST_POSITION, position);
            setArguments(b);

            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SummaryFragment.IRREGDATEFRAG_INTENT));
        }
    }



    private void initClickListeners() {

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });

    }


}
