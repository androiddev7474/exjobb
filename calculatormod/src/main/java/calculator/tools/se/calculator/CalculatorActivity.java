package calculator.tools.se.calculator;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import test1.sql.se.sqltest.SQLhelper;

/**
 * Aktivitet som visar miniräknaren och som instansierar motorn för att göra beräkningar
 * Björn Hallström
 * Version: 1 (2018-11-23)
 *
 */

public class CalculatorActivity extends AppCompatActivity {

    private ImageView icon;
    private TextView display, categoryLabel, dateLabel;
    private Calculate calculate;
    private TextView comment;
    private CommentFragment commentFragment = new CommentFragment();
    private BroadcastReceiver localBroadcastReceiver;
    private SQLhelper sqLhelper;

    private ActionMenuView amvMenu;

    private String category = "";
    private String date = "";
    private String notes ="";
    private int categoryResID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_);

        createToolbar();

        icon = findViewById(R.id.icon_id);
        categoryLabel = findViewById(R.id.category_label);
        dateLabel = findViewById(R.id.category_date);

        category = getIntent().getStringExtra(getString(R.string.INTENT_PUT_CATEGORY));
        categoryResID = getIntent().getIntExtra(getString(R.string.INTENT_PUT_IMAGE_RES), 0);
        date = getIntent().getStringExtra(getString(R.string.INTENT_PUT_DATE));
        categoryLabel.setText(category);
        dateLabel.setText(date);
        icon.setImageResource(categoryResID);

        sqLhelper = new SQLhelper(this);

        display = (TextView) findViewById(R.id.calc_display);
        calculate = new Calculate();

        comment = findViewById(R.id.comment_id);
        comment.setPaintFlags(comment.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        localBroadcastReceiver = new LocalBroadcastReceiver();


    }

    @Override
    public void onResume() {
        super.onResume();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        String key = getString(R.string.INTENT_GETACTION_NAME);
        LocalBroadcastManager.getInstance(this).registerReceiver(localBroadcastReceiver, new IntentFilter(key));

    }


    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadcastReceiver);

    }


    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_calc, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }


        return super.onOptionsItemSelected(item);
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


    public void onCommentClick(View view) {

        commentFragment.show(getSupportFragmentManager(), "commentfragment");


    }



    /**
     *
     * @param view
     */
    public void onButtonClick(View view) {

        Object o = view.getTag();
        String input = o.toString();
        String newDisplay = "0";
        String displayContent = display.getText().toString();
        String tempContent = "";
        if (input.equals(getString(R.string.CALCULATOR_CLEAR))) {

            clear(displayContent);

        } else {

            tempContent = displayContent + input;
            newDisplay = calculate.checkInput(displayContent, tempContent);
            display.setText(newDisplay);
        }

        if (input.equals(getString(R.string.CALCULATOR_OK))) {

            //Object[] values = {"2018-10-15", "MAT", "3.333", "tjabba"};
            Object[] values = {date, category, parseDisplay(newDisplay), notes};
            sqLhelper.addDataExpenditure(values);
            setResult(Activity.RESULT_OK);
            finish();
            sqLhelper.printExpenditure();
        }

        setDisplayCharacterColor();
    }


    public float parseDisplay(String newDisplay) {

        float value = 0.0f;
        try {
            value = Float.parseFloat(newDisplay);
        } catch (NumberFormatException ne) {
            System.err.println("NumberFormatException i CalculatorActivity (parseDisplay)");
        } catch (Exception e) {
            System.err.println("Exception i CalculatorActivity (parseDisplay)");
        }
        return value;
    }


    /**
     * Tar bort tecken från displayen - ett i taget
     * @param diplayText
     */
    private void clear(String diplayText) {

        if (diplayText.length() > 0) {
            String newDisplay = diplayText.substring(0, diplayText.length() - 1);
            display.setText(newDisplay);
        }

    }


    /**
     * Sätter färger på displaytexten. Skiljer ut operander från talen
     */
    private void setDisplayCharacterColor() {

        final int OPERATORCOLOR = getResources().getColor(R.color.operatorColor);
        final int NUMBERCOLOR = getResources().getColor(R.color.numberColor);
        final int FIRST = 0;
        String displayText = display.getText().toString();
        for (int i = 0; i < displayText.length(); i++) {

            String stringCh = Character.toString(displayText.charAt(i));
            boolean containsOperator = StringFormatter.stringContainsItemFromList(stringCh, Calculate.OPERANDS);
            Spannable spannableWord = new SpannableString(stringCh);

            if (containsOperator)
                spannableWord.setSpan(new ForegroundColorSpan(OPERATORCOLOR), 0, spannableWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            else
                spannableWord.setSpan(new ForegroundColorSpan(NUMBERCOLOR), 0, spannableWord.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (i == FIRST)
                display.setText(spannableWord);
             else
                display.append(spannableWord);
        }
    }


    private class LocalBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent == null || intent.getAction() == null)

                return;
            if (intent.getAction().equals(getString(R.string.INTENT_GETACTION_NAME))) {

                String s = commentFragment.getArguments().getString(getString(R.string.CALCULATOR_COMMENT), "");
                boolean b = commentFragment.getArguments().getBoolean(getString(R.string.COMMENT_OK), false);
                if (b) {
                    notes = s;
                    String info = getString(R.string.toast_notif_save_comment);
                    Toast.makeText(CalculatorActivity.this, info, Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    public void setDate(String date) {

        dateLabel.setText(date);

    }


}