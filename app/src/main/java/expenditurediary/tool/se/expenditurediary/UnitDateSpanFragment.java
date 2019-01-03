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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import test1.sql.se.sqltest.SQLhelper;

public class UnitDateSpanFragment extends DialogFragment {

    public static final String EXPENDITURE_RESULTS = "expenditure_results";
    public static final String EXPENDITURE_YEAR = "expenditure_year";
    public static final String EXPENDITURE_MONTH = "expenditure_month";

    private View unitDateView;
    private ExpandableListView expandableListView;
    private MyExpandableListViewAdapter myExpandableListViewAdapter;
    private ImageView closeIcon;
    private TextView noContentView;
    private SQLhelper sqLhelper;

    private float[] sum_expenditure;

    private int year, month;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState) {

        unitDateView = inflater.inflate(R.layout.unitdate_fragment, container, false);
        closeIcon = unitDateView.findViewById(R.id.unitfrag_close_icon);
        noContentView = unitDateView.findViewById(R.id.no_listcontent_info_unitgfrag);

        expandableListView = unitDateView.findViewById(R.id.date_exp_listview_id);

        sqLhelper = new SQLhelper(getContext());

        init();

        initClickListeners();

        return unitDateView;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }



    private void init() {


        ExpandListViewAdaperDataCreator creator = new ExpandListViewAdaperDataCreator(getContext());
        creator.createUnitDateData(sqLhelper);
        Object[] base_items = creator.getBase_items();
        Object[][] sub_items_names = creator.getSub_items_names();
        Object[][] sub_items_numbs = creator.getSub_items_numbs();
        final Object[][] catTable = creator.getCatTable();

        final Object[] groupItems = base_items;
        final Object[][] childItems = sub_items_numbs;


        initAdapter(base_items, sub_items_names);
        initOnChildClickListener(groupItems, childItems, catTable);
        initOnGroupExpandListener();

        if (base_items.length == 0) {
            noContentView.setVisibility(View.VISIBLE);
            expandableListView.setVisibility(View.GONE);
        } else {
            noContentView.setVisibility(View.GONE);
            expandableListView.setVisibility(View.VISIBLE);
        }

    }

    private void initAdapter(Object[] base_items, Object[][] sub_items_names) {

        myExpandableListViewAdapter = new MyExpandableListViewAdapter(getContext(), base_items, sub_items_names);



        expandableListView.setAdapter(myExpandableListViewAdapter);
        int baseColor = getActivity().getResources().getColor(R.color.colorPrimaryDark);
        final int subColor = getActivity().getResources().getColor(R.color.colorPrimaryLight);
        int[] colorsBase = {0, baseColor, 0};
        int[] colorsSub = {0, subColor, 0};
        expandableListView.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colorsBase));
        expandableListView.setChildDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colorsSub));
        expandableListView.setDividerHeight(5);

    }


    private void initOnGroupExpandListener() {

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                for (int i = 0; i < myExpandableListViewAdapter.getGroupCount(); i++) {
                    if (i != groupPosition)
                        expandableListView.collapseGroup(i);
                    else System.out.println("expand of group " + i);
                }
            }
        });
    }


    /**
     *
     * @param groupItems
     * @param childItems
     * @param catTable
     */
    private void initOnChildClickListener(final Object[] groupItems, final Object[][] childItems, final Object[][] catTable) {

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                sum_expenditure = new float[catTable.length];
                if (childPosition > 0) {
                    year = Integer.parseInt(groupItems[groupPosition].toString());
                    month = Integer.parseInt(childItems[groupPosition][childPosition].toString());

                    for (int i = 0; i < sum_expenditure.length; i++)
                        sum_expenditure[i] = sqLhelper.sumTableColumnYearMonthCat(year, month, catTable[i][0].toString());

                } else {
                    year = Integer.parseInt(childItems[groupPosition][childPosition].toString());
                    month = -1;
                    for (int i = 0; i < sum_expenditure.length; i++)
                        sum_expenditure[i] = sqLhelper.sumExpTableColumnYearCat(year, catTable[i][0].toString());
                }
                dismiss();

                return false;
            }
        });
    }


    @Override
    public void onDismiss(DialogInterface dialog) {

        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();

        if (sum_expenditure != null) {
            Bundle b = new Bundle();
            b.putFloatArray(EXPENDITURE_RESULTS, sum_expenditure);
            b.putInt(EXPENDITURE_YEAR, year);
            b.putInt(EXPENDITURE_MONTH, month);
            setArguments(b);


            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(SummaryFragment.UNITDATEFRAG_INTENT));
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
