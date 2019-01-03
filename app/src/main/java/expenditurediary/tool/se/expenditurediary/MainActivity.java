package expenditurediary.tool.se.expenditurediary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import calculator.tools.se.calculator.CalculatorActivity;
import calendar.test.tools.se.myapplication.CalendarActivity;
import test1.sql.se.sqltest.MyDatabaseHandler;
import test1.sql.se.sqltest.SQLhelper;

public class MainActivity extends AppCompatActivity {

    private AppPageAdapter appPageAdapter;

    private static final String CALCULATOR_TAG = "calculator_tag";
    private static final String CALENDAR_TAG = "calendar_tag";

    public static final String IMAGE_RES_FOLDER_NAME = "drawable/";

    private ActionMenuView amvMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        appPageAdapter = new AppPageAdapter(getSupportFragmentManager(), this);
        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(appPageAdapter);
        pager.setOffscreenPageLimit(AppPageAdapter.N_FRAGMENTS);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);

        SharedPreferences.Editor editor = getSharedPreferences(SummaryFragment.DATA_PREFS, MODE_PRIVATE).edit();
        editor.putBoolean(SummaryFragment.BOOLEAN_KEY_DATA_IS_SET, false);
        editor.apply();



        //tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight_2));
        /*int colorTab = getResources().getColor(R.color.colorPrimaryLight_2);
        tabLayout.setBackgroundColor(colorTab);
        int colorAction = getResources().getColor(R.color.colorPrimary_2);
        toolbar.setBackgroundColor(colorAction);
        */


        createTabIcons(tabLayout);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        SharedPreferences.Editor editor = getSharedPreferences(SummaryFragment.DATA_PREFS, MODE_PRIVATE).edit();
        editor.putBoolean(SummaryFragment.BOOLEAN_KEY_DATA_IS_SET, false);
        editor.apply();
    }


    private void createTabIcons(TabLayout tabLayout) {


        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_customizer, null);
        tabOne.setText(getString(R.string.edit_tab));
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.edit, 0, 0);
        tabOne.setGravity(Gravity.CENTER);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.tab_customizer, null);
        tabTwo.setText(getString(R.string.summary_tab));
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.summary, 0, 0);
        tabTwo.setGravity(Gravity.CENTER);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                return true;

            case R.id.action_info:

                Toast.makeText(this, "info", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:

                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_rate:

                Toast.makeText(this, "rate", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_share:

                Toast.makeText(this, "share", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static int getImageId(Context context, String imageName) {

        int resID = context.getResources().getIdentifier(IMAGE_RES_FOLDER_NAME + imageName, null, context.getPackageName());

        return resID;
    }




}
