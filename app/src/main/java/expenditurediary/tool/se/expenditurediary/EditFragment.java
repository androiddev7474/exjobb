package expenditurediary.tool.se.expenditurediary;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import calculator.tools.se.calculator.CalculatorActivity;
import calendar.test.tools.se.myapplication.CalendarActivity;
import test1.sql.se.sqltest.MyDatabaseContract;
import test1.sql.se.sqltest.SQLhelper;
import test3.AppUtils;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class EditFragment extends android.support.v4.app.Fragment {


    //requestcode
    public static final int REQ_CODE_EDITCATEGORY_ACTIVITY = 555;
    public static final int REQ_CODE_ADDCATEGORY_ACTIVITY = 666;

    private View editView;
    private GridView gridView;
    private ImageView now, calendar, add, edit, backward, forward;
    private TextView selected_date, expenditure_sum;

    private SQLhelper sQLhelper;

    private MyGridViewBaseAdapter myGridViewBaseAdapter;

    private PerDayContentFragment perDayFragment = new PerDayContentFragment();

    private static final int REQUESTCODE_CALENDAR = 1;
    //private static final int REQUESTCODE_CALCULATOR = 2;
    //public static final String INIT_TABLE_CATEGORIES = "default_categories";
    //public static final String TABLE_CATEGORIES_BOOLEAN = "table_categories_boolean";
    //private static final String ICON_CATEGORY_TAG = "category_icon_tag";

    private String displayedDate = "";


    private void saveDate(String date) {

        SharedPreferences.Editor editor = getContext().getSharedPreferences("MY_PREFS_DATE", MODE_PRIVATE).edit();
        editor.putString("DATE", date);
        editor.apply();
    }

    private String getSavedDate() {

        SharedPreferences prefs = getContext().getSharedPreferences("MY_PREFS_DATE", MODE_PRIVATE);
        String restoredSwitchState = prefs.getString("DATE", "");

        return restoredSwitchState;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        sQLhelper = new SQLhelper(getContext());

        if (!categoriesAreInitialized())
            initCategories();


        myGridViewBaseAdapter = new MyGridViewBaseAdapter(getContext());

        String savedDate = getSavedDate();
        if (savedDate.equals(""))
            displayedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        else
            displayedDate = getSavedDate();

        myGridViewBaseAdapter.setDate(displayedDate);

        //sQLhelper.SumExpTableColumnMonth(11);

        sQLhelper.printCategory();

    }

    @Override
    public void onResume() {

        super.onResume();
        gridView.setAdapter(myGridViewBaseAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        editView = inflater.inflate(R.layout.edit_fragment, container, false);

        gridView = editView.findViewById(R.id.edit_gridview);
        gridView.setAdapter(myGridViewBaseAdapter);
        expenditure_sum = editView.findViewById(R.id.total_expenditure_tv);
        expenditure_sum.setText("" + getSumOfExpenditures());


        initOnItemClickListeners();



        selected_date = editView.findViewById(R.id.date_tv);
        selected_date.setText(displayedDate);
        expenditure_sum = editView.findViewById(R.id.total_expenditure_tv);

        initOnClickListeners();

        return editView;
    }



    public static EditFragment newInstance(String text) {

        EditFragment editFragment = new EditFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        editFragment.setArguments(b);

        return editFragment;
    }



    private void initCategories() {


        sQLhelper.setTableData(R.array.default_categories, getResources().getStringArray(R.array.default_categories_imgs));
        SharedPreferences.Editor editor = getContext().getSharedPreferences(getContext().getString(R.string.INIT_TABLE_CATEGORIES), MODE_PRIVATE).edit();
        editor.putBoolean(getContext().getString(R.string.TABLE_CATEGORIES_BOOLEAN), true).apply();
    }



    private boolean categoriesAreInitialized() {

        return getContext().getSharedPreferences(getContext().getString(R.string.INIT_TABLE_CATEGORIES), MODE_PRIVATE).getBoolean(getContext().getString(R.string.TABLE_CATEGORIES_BOOLEAN), false);
    }






    public void initOnClickListeners() {

        now = editView.findViewById(R.id.now_iv);
        calendar = editView.findViewById(R.id.calendar_iv);
        add = editView.findViewById(R.id.add_iv);
        edit = editView.findViewById(R.id.edit_iv);
        backward = editView.findViewById(R.id.backward_id);
        forward = editView.findViewById(R.id.forward_id);

        now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                displayedDate = sdf.format(new Date());
                selected_date.setText(displayedDate);
                myGridViewBaseAdapter.setDate(displayedDate);
                myGridViewBaseAdapter.notifyDataSetChanged();

                expenditure_sum.setText("" + getSumOfExpenditures());

            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resultIntent = new Intent(getActivity(), CalendarActivity.class);
                resultIntent.putExtra(CalendarActivity.CALENDAR_RESULT_DATE, displayedDate);
                startActivityForResult(resultIntent, REQUESTCODE_CALENDAR);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), AddEditCatActivity.class);
                intent.putExtra(AddEditCatActivity.INTENT_ADD_EDIT, true); // sant om lägga till ny kategori

                startActivityForResult(intent, REQ_CODE_ADDCATEGORY_ACTIVITY);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), EditCategoryActivity.class);

                startActivityForResult(intent, REQ_CODE_EDITCATEGORY_ACTIVITY);

            }
        });


        backward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeDateOneUnit(-1);
                expenditure_sum.setText("" + getSumOfExpenditures());
            }
        });

        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeDateOneUnit(1);
                expenditure_sum.setText("" + getSumOfExpenditures());
            }
        });


    }

    private void changeDateOneUnit(int change) {

        GregorianCalendar cal = new GregorianCalendar();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        int year = Integer.parseInt(displayedDate.split("-")[0]);
        int month = Integer.parseInt(displayedDate.split("-")[1]);
        int day = Integer.parseInt(displayedDate.split("-")[2]);

        cal.set(year, month - 1, day + change);
        Date date = cal.getTime();

        displayedDate = sdf.format(date);
        selected_date.setText(displayedDate);

        myGridViewBaseAdapter.setDate(displayedDate);
        myGridViewBaseAdapter.notifyDataSetChanged();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == AppUtils.getIntFromRes(R.string.REQUESTCODE_CALENDAR, getContext())) {
            switch (resultCode) {

                case RESULT_OK:

                    displayedDate = data.getStringExtra(getContext().getString(R.string.RESULT_DATE));
                    selected_date.setText(displayedDate);
                    displayedDate = displayedDate;
                    saveDate(displayedDate);
                    myGridViewBaseAdapter.setDate(displayedDate);
                    Object[][] o = sQLhelper.getSQLdataExpenditure();
                    expenditure_sum.setText("" + getSumOfExpenditures());

                    break;
                case Activity.RESULT_CANCELED:

                    break;
            }
        }

        if (requestCode == AppUtils.getIntFromRes(R.string.REQUESTCODE_CALCULATOR, getContext())) {
            switch (resultCode) {

                case RESULT_OK:

                    myGridViewBaseAdapter.notifyDataSetChanged();
                    expenditure_sum.setText("" + getSumOfExpenditures());
                    break;
                case Activity.RESULT_CANCELED:

                    break;
            }
        }

        if (requestCode == REQ_CODE_ADDCATEGORY_ACTIVITY || requestCode == REQ_CODE_EDITCATEGORY_ACTIVITY) {
            switch (resultCode) {

                case RESULT_OK:
                    myGridViewBaseAdapter.upDateTableData();
                    myGridViewBaseAdapter.notifyDataSetChanged();
                    break;
                case Activity.RESULT_CANCELED:

                    break;
            }
        }

    }

    public float getSumOfExpenditures() {

        Object[][] tableData = sQLhelper.getSQLdataCategories();
        int sum = 0;
        for (int i = 0; i < tableData.length; i++) {

            String[] selectionArgs = {displayedDate, tableData[i][0].toString()};
            sum += sQLhelper.getSumOfExpenditTableCol(selectionArgs);
        }
        return sum;
    }


    private void initOnItemClickListeners() {


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                for(int index = 0; index < ((ViewGroup)view).getChildCount(); ++index) {

                    if ( ((ViewGroup)view).getChildAt(index).getTag().equals(AppUtils.getStringFromRes(R.string.ICON_CATEGORY_TAG, getContext())) ) {

                        Intent intent = new Intent(getActivity(), CalculatorActivity.class);
                        String cat = myGridViewBaseAdapter.getCategoryTableData()[position][0].toString();
                        String imageName = myGridViewBaseAdapter.getCategoryTableData()[position][1].toString();
                        int imageResID = MainActivity.getImageId(getContext(), imageName);
                        intent.putExtra(AppUtils.getStringFromRes(R.string.INTENT_PUT_CATEGORY, getContext()), cat);
                        intent.putExtra(AppUtils.getStringFromRes(R.string.INTENT_PUT_IMAGE_RES, getContext()), imageResID);

                        intent.putExtra(AppUtils.getStringFromRes(R.string.INTENT_PUT_DATE, getContext()), displayedDate);

                        startActivityForResult(intent, AppUtils.getIntFromRes(R.string.REQUESTCODE_CALCULATOR, getContext()));
                    }
                }


            }
        });


        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {

                String category = sQLhelper.getSQLdataCategories()[pos][0].toString();


                Object[] targets = {category, displayedDate};
                ArrayList<Object> list = sQLhelper.getTableDataById(targets);

                Bundle b = new Bundle();
                b.putString(PerDayContentFragment.CATEGORY_VALUE_FLAG, category);
                b.putString(PerDayContentFragment.DATE_VALUE_FLAG, displayedDate);
                perDayFragment.setArguments(b);
                perDayFragment.show(getActivity().getSupportFragmentManager(), "perdayfrag");


                return true;
            }
        });

    }



}
