package expenditurediary.tool.se.expenditurediary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import test1.sql.se.sqltest.MyDatabaseContract;
import test1.sql.se.sqltest.SQLhelper;

public class PerDayContentFragment extends DialogFragment {

    //bundle flags
    public static final String CATEGORY_VALUE_FLAG = "category value";
    public static final String DATE_VALUE_FLAG = "date value";

    private Button okButton, cancelButton;

    private View perDayContentView;
    private TextView headerLabel, headerDate;
    private ListView listView;
    private PerDayFragListViewAdapter adapter;

    private SQLhelper sqLhelper;

    private Object[][] dataList;

    String category, date;

    public View onCreateView(LayoutInflater inflater, ViewGroup containter, Bundle onSavedInstanceState) {

        perDayContentView = inflater.inflate(R.layout.per_day_content_fragment, containter, false);
        listView = perDayContentView.findViewById(R.id.per_day_listview_id);
        headerLabel = perDayContentView.findViewById(R.id.header_label_id);
        headerDate = perDayContentView.findViewById(R.id.header_date_id);
        okButton = perDayContentView.findViewById(R.id.ok_perdayfragment);
        cancelButton = perDayContentView.findViewById(R.id.cancel_perdayfragment);

        sqLhelper = new SQLhelper(getContext());

        category = getArguments().getString(CATEGORY_VALUE_FLAG, "no cat");
        date = getArguments().getString(DATE_VALUE_FLAG, "no date");
        String label = category;
        headerLabel.setText(label);
        headerDate.setText(date);

        Object[] targets = {category, date};
        dataList = createListFromSQL(targets);

        adapter = new PerDayFragListViewAdapter(getContext(), dataList);
        listView.setAdapter(adapter);

        initOnClickListeners();

        return perDayContentView;
    }

    private Object[][] createListFromSQL(Object[] targets) {

        ArrayList<Object> list = sqLhelper.getTableDataById(targets);

        final int nCols = MyDatabaseContract.TableExpenditure.N_COLUMNS; // 5 st
        final int nTableRows = list.size() / nCols;
        Object[][] dataList = new Object[nTableRows][nCols];
        //skapa 2d array utifrån 1d array
        int tableCol = 0;
        int tableRow = 0;
        for (int i = 0; i < list.size(); i++) {
            if (i % 5 == 0 && i > 0) {
                tableRow++;
                tableCol = 0;
            }
            dataList[tableRow][tableCol] = list.get(i);
            tableCol++;
        }

        return dataList;
    }


    private void initOnClickListeners() {


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean[] booleanPos = adapter.getListPosToBeRemoved();

                boolean hasItemTobeRemoved = false;
                for (int i = 0; i < booleanPos.length; i++) {

                    if(booleanPos[i]) {

                        int id = Integer.parseInt(dataList[i][0].toString());
                        sqLhelper.deleteRowExpenditureTableById(id);
                        hasItemTobeRemoved = true;
                    }
                }


                if (hasItemTobeRemoved) {
                    Object[] target = {category, date};
                    dataList = createListFromSQL(target);
                    adapter.notifyDataSetChanged();

                    //tillsvidare lösning - inte den snyggaste men lätt att implementera
                    Intent startIntent = new Intent(getActivity(), MainActivity.class);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(startIntent);

                }


                dismiss();

                int y = 1;
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });


    }

}
