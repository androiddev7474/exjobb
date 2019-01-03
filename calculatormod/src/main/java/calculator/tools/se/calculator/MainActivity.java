package calculator.tools.se.calculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import test1.sql.se.sqltest.SQLhelper;
import test1.sql.se.sqltest.SqlQueries;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, CalculatorActivity.class);
        startActivity(intent);



    }
}
