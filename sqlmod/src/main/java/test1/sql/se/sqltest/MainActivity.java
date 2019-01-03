package test1.sql.se.sqltest;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHandler myDatabaseHandler;
    private SQLiteDatabase sqlDB;

    //kategoritabellen
    private static final String TABLE_CATEGORIES = MyDatabaseContract.TableCategories.TABLE_CATEGORIES;
    private static final String CATEGORY = MyDatabaseContract.TableCategories.KEY_ID;
    private static final String RES_ID = MyDatabaseContract.TableCategories.IMAGE_ID;

    //utgiftstabellen
    private static final String TABLE_EXPENDITURE = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
    private static final String ATTR_PRIMARY_KEY = MyDatabaseContract.TableExpenditure.ATTR_PRIMARY_KEY;
    private static final String ATTR_DATE = MyDatabaseContract.TableExpenditure.ATTR_DATE;
    private static final String ATTR_CATEG_EXP = MyDatabaseContract.TableExpenditure.ATTR_CATEGORY;
    private static final String ATTR_EXPENDITURE = MyDatabaseContract.TableExpenditure.ATTR_EXPENDITURE;
    private static final String ATTR_COMMENT = MyDatabaseContract.TableExpenditure.ATTR_COMMENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDatabaseHandler = new MyDatabaseHandler(this);
        sqlDB = myDatabaseHandler.getReadableDatabase();



        //int count = SqlQueries.getRowCount(sqlDB, TABLE_EXPENDITURE);
        //System.out.println("count = " + count);
        //SqlQueries.deleteTableRow(sqlDB, TABLE_EXPENDITURE, ATTR_CATEG_EXP, "NÖJEN");

        //updateExpenditure();


        //getTableDataById();

        addDataExpenditure();

        SqlQueries.deleteTableRow(sqlDB, TABLE_EXPENDITURE, ATTR_CATEG_EXP, "MAT");

        printExpenditure();

    }

    private void addDataExpenditure() {

        String[] attributes = {ATTR_DATE, ATTR_CATEG_EXP, ATTR_EXPENDITURE, ATTR_COMMENT};
        int[] dataTypes = {SqlQueries.DATATYPE_STRING, SqlQueries.DATATYPE_STRING, SqlQueries.DATATYPE_FLOAT, SqlQueries.DATATYPE_STRING};
        Object[] values = {"2018-10-15", "MAT", "3.333", "tjabba"};
        boolean success = SqlQueries.addSqlData(sqlDB, TABLE_EXPENDITURE, attributes, dataTypes, values);
        Log.i("SUCCESS", "" + success);
    }

    private void addDataCategory() {

        String[] attributes = {CATEGORY, RES_ID};
        int[] dataTypes = {SqlQueries.DATATYPE_STRING, SqlQueries.DATATYPE_INT};
        Object[] values = {"NÖJEN", 233};
        SqlQueries.addSqlData(sqlDB, TABLE_CATEGORIES, attributes, dataTypes, values);
    }

    private void printCategory() {

        Object[][] tableData = new Object[SqlQueries.getRowCount(sqlDB, TABLE_CATEGORIES)][MyDatabaseContract.TableCategories.N_COLUMNS];
        SqlQueries.getTableData(sqlDB, tableData, TABLE_CATEGORIES, MyDatabaseContract.TableCategories.N_COLUMNS);
        String[] TableColumnNames = {MyDatabaseContract.TableCategories.KEY_ID, MyDatabaseContract.TableCategories.IMAGE_ID};
        StdOutTable.printTable(tableData, TableColumnNames);
    }


    private void printExpenditure() {

        Object[][] tableData = new Object[SqlQueries.getRowCount(sqlDB, TABLE_EXPENDITURE)][MyDatabaseContract.TableExpenditure.N_COLUMNS];
        SqlQueries.getTableData(sqlDB, tableData, TABLE_EXPENDITURE, MyDatabaseContract.TableExpenditure.N_COLUMNS);
        String[] TableColumnNames = {ATTR_PRIMARY_KEY, ATTR_DATE, ATTR_CATEG_EXP, ATTR_EXPENDITURE, ATTR_COMMENT};
        StdOutTable.printTable(tableData, TableColumnNames);

    }


    private void updateExpenditure() {

        //String[] columns = {ATTR_CATEG_EXP, ATTR_EXPENDITURE, ATTR_COMMENT};
        String[] columns = {ATTR_EXPENDITURE, ATTR_COMMENT};
        Object[] values = {5.555, "Maten var super, inte så stark :p"};
        int[] dataTypes = {SqlQueries.DATATYPE_FLOAT, SqlQueries.DATATYPE_STRING};
        SqlQueries.updateTableColumn(sqlDB, TABLE_EXPENDITURE, columns, values, dataTypes, ATTR_PRIMARY_KEY, 5);
    }

    private void updateCatTable(int value) {

        //String[] columns = {ATTR_CATEG_EXP, ATTR_EXPENDITURE, ATTR_COMMENT};
        String[] columns = {MyDatabaseContract.TableCategories.IMAGE_ID};
        Object[] values = {value};
        int[] dataTypes = {SqlQueries.DATATYPE_INT};
        SqlQueries.updateTableColumn(sqlDB, TABLE_EXPENDITURE, columns, values, dataTypes, ATTR_PRIMARY_KEY, 5);
    }


    private void getTableDataById() {

        ArrayList <Object> tableDataList = new ArrayList<>();
        int[] dataTypes = {SqlQueries.DATATYPE_INT, SqlQueries.DATATYPE_STRING, SqlQueries.DATATYPE_STRING, SqlQueries.DATATYPE_FLOAT, SqlQueries.DATATYPE_STRING};
        SqlQueries.getTableRowDataByID(sqlDB, TABLE_EXPENDITURE, dataTypes, MyDatabaseContract.TableExpenditure.N_COLUMNS, tableDataList, ATTR_CATEG_EXP, "MAT");

        int y = 1;
    }


}
