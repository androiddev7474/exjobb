package expenditurediary.tool.se.expenditurediary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import calculator.tools.se.calculator.CalculatorActivity;
import calendar.test.tools.se.myapplication.CalendarActivity;
import test1.sql.se.sqltest.MyDatabaseContract;
import test1.sql.se.sqltest.MyDatabaseHandler;
import test1.sql.se.sqltest.SQLhelper;
import test1.sql.se.sqltest.SqlQueries;

public class MyGridViewBaseAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    //private ArrayList<Integer> iconResList = new ArrayList<>();
    private Object[][] tableData;
    private SQLhelper sqLhelper;

    private String selectedDate = "";


    public MyGridViewBaseAdapter(Context context) {

        this.context = context;
        sqLhelper = new SQLhelper(context);

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        upDateTableData();

        sumExpenditureCategory();



    }

    public void upDateTableData() {

        tableData = sqLhelper.getSQLdataCategories();
    }




    @Override
    public int getCount() {

        return tableData.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private static class ViewHolder {
        protected TextView categoryLabel;
        protected ImageView categoryIcon;
        protected TextView expenditureText;

    }


    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.editfragment_gridviewcontent, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.categoryLabel = convertView.findViewById(R.id.category_label_id);
            viewHolder.categoryIcon = convertView.findViewById(R.id.category_icon_id);
            viewHolder.expenditureText = convertView.findViewById(R.id.category_expenditure_id);
            convertView.setTag(viewHolder);

        } else
            viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.categoryLabel.setText(tableData[position][0].toString());
        //viewHolder.categoryIcon.setImageResource(Integer.parseInt(tableData[position][1].toString()));
        viewHolder.categoryIcon.setImageResource(MainActivity.getImageId(context, tableData[position][1].toString()));
        String[] selectionArgs = {selectedDate, tableData[position][0].toString()};
        viewHolder.expenditureText.setText("" + sqLhelper.getSumOfExpenditTableCol(selectionArgs));


        return convertView;
    }



    private void sumExpenditureCategory() {



        //String[] selectionArgs = {"2018-12-31", "HYRA"};

        /*int total = sqLhelper.getSumOfExpenditTableCol(selectionArgs);

        int y = 1;
        Toast.makeText(context, selectionArgs[1] + ":" + total, Toast.LENGTH_SHORT).show();
        */
    }


    public void setDate(String selectedDate) {

        this.selectedDate = selectedDate;
        //notifyDataSetInvalidated();
        notifyDataSetChanged();
    }


    public Object[][] getCategoryTableData() {

        return this.tableData;
    }


}
