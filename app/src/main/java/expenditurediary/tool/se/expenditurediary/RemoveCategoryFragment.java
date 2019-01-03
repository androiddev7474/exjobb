package expenditurediary.tool.se.expenditurediary;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import test1.sql.se.sqltest.SQLhelper;

public class RemoveCategoryFragment extends DialogFragment {

    public static final String REMOVE_OK_TAG = "remove ok";
    public static final String REMOVE_CANCEL_TAG = "remove cancel";


    private ImageView icon;
    private View removeCatView;
    private TextView headerText, contentText;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle onSavedInstanceState) {

        removeCatView = inflater.inflate(R.layout.remove_cat_fragment, container, false);
        icon = removeCatView.findViewById(R.id.remove_icon_id);
        headerText = removeCatView.findViewById(R.id.header_remove_id);
        contentText = removeCatView.findViewById(R.id.remove_info_text_id);


        int position = getArguments().getInt(EditCategoryActivity.CHOOSED_CATEGORY_KEY,-1);

        SQLhelper sqLhelper = new SQLhelper(getContext());
        Object[][] catTable = sqLhelper.getSQLdataCategories();

        String category = catTable[position][0].toString();
        String iconResName = catTable[position][1].toString();
        headerText.setText(category);
        int imageResID = MainActivity.getImageId(getContext(), iconResName);
        icon.setImageResource(imageResID);



        return removeCatView;
    }



}
