package expenditurediary.tool.se.expenditurediary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import test1.sql.se.sqltest.SQLhelper;

public class AddEditCatActivity extends AppCompatActivity {

    //intent key
    public static final String INTENT_ADD_EDIT = "intent add edit"; //key
    public static final String INTENT_ADD = "intent add"; //key


    public static final String ICONS_FRAG_TAG = "iconsfragment";

    private TextView addEditTextView;
    private EditText input;
    private ImageView iconSet;
    private String imageName = "default_img";

    private ActionMenuView amvMenu;

    private SQLhelper sqLhelper;

    private int categoryPos;
    private String category;

    private boolean isAdd;

    private IconsFragment iconsFragment = new IconsFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_cat);

        addEditTextView = findViewById(R.id.add_edit_textview_id);
        input = findViewById(R.id.input_category_name_id);
        iconSet = findViewById(R.id.cat_icon_id);

        createToolbar();

        sqLhelper = new SQLhelper(this);

        isAdd = getIntent().getBooleanExtra(INTENT_ADD_EDIT, false);
        if (isAdd) {
            addEditTextView.setText(getString(R.string.add_category_label));
        } else {
            addEditTextView.setText(getString(R.string.edit_category_label));
            categoryPos = getIntent().getIntExtra(INTENT_ADD_EDIT, -1);

            Object[][] catTable = sqLhelper.getSQLdataCategories();
            String imageName = catTable[categoryPos][1].toString();
            category = catTable[categoryPos][0].toString();
            int iconResId = MainActivity.getImageId(this, imageName);
            iconSet.setImageResource(iconResId);
            input.setText(category);

        }



    }


    private void createToolbar() {

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
    }

    public void onClickIcon(View view) {

        iconsFragment.show(getSupportFragmentManager(), ICONS_FRAG_TAG);
    }


    /**
     * callback från IconsFragment
     * @param view
     */
    public void onOKcancelClick(View view) {

        if (view.getTag().equals(IconsFragment.OK_TAG_ICONSFRAG)) {

            int resID = iconsFragment.getIconResID();
            int position = iconsFragment.getResPosition();
            iconSet.setImageResource(resID);
            imageName = getResources().getStringArray(R.array.icons_drawable_names)[position];

            //Object[][] catTable = sqLhelper.getSQLdataCategories();
            //sqLhelper.updateCatTableImage(imageName, catTable[categoryPos][0].toString());

            iconsFragment.dismiss();
        }


        if (view.getTag().equals(IconsFragment.CANCEL_TAG_ICONSFRAG)) {


            iconsFragment.dismiss();
        }

    }

    public void onConfirmClick(View view) {

        if (isAdd) {

            sqLhelper.addDataCategory(input.getText().toString(), imageName);
            setResult(Activity.RESULT_OK);
            finish();
            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(startIntent);

        } else {

            String newName = input.getText().toString();
            String oldName = category;
            sqLhelper.updateCatTable(newName, oldName, imageName);
            sqLhelper.updateExpenditure(newName, oldName);

            setResult(Activity.RESULT_OK);
            finish();

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edit_cat_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


}
