package expenditurediary.tool.se.expenditurediary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import test1.sql.se.sqltest.SQLhelper;

public class EditCategoryActivity extends AppCompatActivity {

    //bundle
    public static final String CHOOSED_CATEGORY_KEY = "category to be removed";

    //fragment
    public static final String REMOVE_FRAGMENT_KEY = "removefragment";

    //intent (activityForResult)
    public static final int REQCODE_EDIT = 123;

    private EditCategoryArrayAdapter adapter;
    private RemoveCategoryFragment removeFragment = new RemoveCategoryFragment();

    private ActionMenuView amvMenu;

    private ListView listView;

    private SQLhelper sqLhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        sqLhelper = new SQLhelper(this);

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



        listView = findViewById(R.id.edit_cat_listview_id);

        Object[][] catTable = getSqlData();
        String[] labels = new String[catTable.length];
        int len = labels.length;
        for (int i = 0; i < labels.length; i++)
            labels[i] = catTable[i][0].toString();

        adapter = new EditCategoryArrayAdapter(this, labels);
        listView.setAdapter(adapter);

    }


    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQCODE_EDIT) {

            if (resultCode == RESULT_OK) {

                hardRefreshList();
            }
        }
    }


    private Object[][] getSqlData() {

        SQLhelper sqLhelper = new SQLhelper(this);
        Object[][] catTable = sqLhelper.getSQLdataCategories();

        return catTable;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.edit_remove_cat_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {

            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                return true;

            case R.id.action_remove:
                //Toast.makeText(this, "remove", Toast.LENGTH_SHORT).show();
                Bundle b = new Bundle();
                b.putInt(CHOOSED_CATEGORY_KEY, adapter.getClickedPosition());
                removeFragment.setArguments(b);
                removeFragment.show(getSupportFragmentManager(), REMOVE_FRAGMENT_KEY);
                //b.p
                return true;
            case R.id.action_edit:

                Intent addEditIntent = new Intent(this, AddEditCatActivity.class);
                addEditIntent.putExtra(AddEditCatActivity.INTENT_ADD_EDIT, adapter.getClickedPosition());
                startActivityForResult(addEditIntent, 123);


                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onOKcancelClick(View view) {

        if (view.getTag().equals(RemoveCategoryFragment.REMOVE_OK_TAG)) {

            //Toast.makeText(this, "pos:" + adapter.getClickedPosition(), Toast.LENGTH_SHORT).show();

            String category = sqLhelper.getSQLdataCategories()[adapter.getClickedPosition()][0].toString();
            sqLhelper.deleteCatTableRowsByCategory(category);

            hardRefreshList();
            removeFragment.dismiss();

            sqLhelper.deleteExpTableRowsByCategory(category);

            sqLhelper.printExpenditure();

            Intent startIntent = new Intent(this, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.startActivity(startIntent);

            Intent editCatIntent = new Intent(this, EditCategoryActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(editCatIntent);

        }

        if (view.getTag().equals(RemoveCategoryFragment.REMOVE_CANCEL_TAG)) {

            removeFragment.dismiss();
        }

    }

    private void hardRefreshList() {

        Object[][] catTable = sqLhelper.getSQLdataCategories();
        String[] labels = new String[catTable.length];
        for (int i = 0; i < catTable.length; i++)
            labels[i] = catTable[i][0].toString();

        adapter = new EditCategoryArrayAdapter(this, labels);
        listView.setAdapter(adapter);

    }
}
