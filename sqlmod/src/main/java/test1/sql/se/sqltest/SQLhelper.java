package test1.sql.se.sqltest;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

public class SQLhelper {

    private Context context;
    static MyDatabaseHandler myDatabaseHandler;

    public SQLhelper(Context context) {

        this.context = context;
        //myDatabaseHandler = new MyDatabaseHandler(context);
        myDatabaseHandler = MyDatabaseHandler.getInstance(context);
    }

    public void setTableData(int res_categories, String[] res_imgs) {


        final String[] attributes = {MyDatabaseContract.TableCategories.KEY_ID, MyDatabaseContract.TableCategories.IMAGE_ID};
        final int[] dataTypes = {SqlQueries.DATATYPE_STRING, SqlQueries.DATATYPE_STRING};

        String[] defs = context.getResources().getStringArray(res_categories);
        /*TypedArray typedThumbsArray = context.getResources().obtainTypedArray(res_imgs);
        int[] resIDimgs = new int[defs.length];
        for (int i = 0; i < resIDimgs.length; i++)
            resIDimgs[i] = typedThumbsArray.getResourceId(i, 0);
        */

        final int N_ATTRIBUTES = 2;
        Object[][] values = new Object[res_imgs.length][N_ATTRIBUTES];
        for (int i = 0; i < values.length; i++) {
            values[i][0] = defs[i];
            values[i][1] = res_imgs[i];
        }


        for (int j = 0; j < values.length; j++)
            SqlQueries.addSqlData(myDatabaseHandler.getReadableDatabase(), MyDatabaseContract.TableCategories.TABLE_CATEGORIES, attributes, dataTypes, values[j]);
    }


    public void printCategory() {

        Object[][] tableData = new Object[SqlQueries.getRowCount(myDatabaseHandler.getReadableDatabase(), MyDatabaseContract.TableCategories.TABLE_CATEGORIES)][MyDatabaseContract.TableCategories.N_COLUMNS];
        SqlQueries.getTableData(myDatabaseHandler.getReadableDatabase(), tableData, MyDatabaseContract.TableCategories.TABLE_CATEGORIES, MyDatabaseContract.TableCategories.N_COLUMNS);
        String[] TableColumnNames = {MyDatabaseContract.TableCategories.KEY_ID, MyDatabaseContract.TableCategories.IMAGE_ID};
        StdOutTable.printTable(tableData, TableColumnNames);
    }

    public void printExpenditure() {

        Object[][] tableData = new Object[SqlQueries.getRowCount(myDatabaseHandler.getReadableDatabase(), MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE)][MyDatabaseContract.TableExpenditure.N_COLUMNS];
        SqlQueries.getTableData(myDatabaseHandler.getReadableDatabase(), tableData, MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE, MyDatabaseContract.TableExpenditure.N_COLUMNS);
        String[] TableColumnNames = {MyDatabaseContract.TableExpenditure.ATTR_PRIMARY_KEY, MyDatabaseContract.TableExpenditure.ATTR_DATE, MyDatabaseContract.TableExpenditure.ATTR_CATEGORY, MyDatabaseContract.TableExpenditure.ATTR_EXPENDITURE, MyDatabaseContract.TableExpenditure.ATTR_COMMENT};
        StdOutTable.printTable(tableData, TableColumnNames);

    }



    public Object[][] getSQLdataCategories() {

        Object[][] tableData = new Object[SqlQueries.getRowCount(myDatabaseHandler.getReadableDatabase(), MyDatabaseContract.TableCategories.TABLE_CATEGORIES)][MyDatabaseContract.TableCategories.N_COLUMNS];
        SqlQueries.getTableData(myDatabaseHandler.getReadableDatabase(), tableData, MyDatabaseContract.TableCategories.TABLE_CATEGORIES, MyDatabaseContract.TableCategories.N_COLUMNS);

        return tableData;
    }


    public Object[][] getSQLdataExpenditure() {

        Object[][] tableData = new Object[SqlQueries.getRowCount(myDatabaseHandler.getReadableDatabase(), MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE)][MyDatabaseContract.TableExpenditure.N_COLUMNS];
        SqlQueries.getTableData(myDatabaseHandler.getReadableDatabase(), tableData, MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE, MyDatabaseContract.TableExpenditure.N_COLUMNS);

        return tableData;
    }


    public ArrayList<Object> getSQLdataByColIDExpenditure(String column, String target) {

        ArrayList<Object> tableDataList = new ArrayList<>();
        int[] dataTypes = {SqlQueries.DATATYPE_INT, SqlQueries.DATATYPE_STRING, SqlQueries.DATATYPE_STRING, SqlQueries.DATATYPE_FLOAT, SqlQueries.DATATYPE_STRING};
        SqlQueries.getTableRowDataByID(myDatabaseHandler.getReadableDatabase(), MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE, dataTypes, MyDatabaseContract.TableExpenditure.N_COLUMNS, tableDataList, column, target);

        return tableDataList;
    }


    public void addDataExpenditure(Object[] values) {

        String[] attributes = {MyDatabaseContract.TableExpenditure.ATTR_DATE, MyDatabaseContract.TableExpenditure.ATTR_CATEGORY, MyDatabaseContract.TableExpenditure.ATTR_EXPENDITURE, MyDatabaseContract.TableExpenditure.ATTR_COMMENT};
        int[] dataTypes = {SqlQueries.DATATYPE_STRING, SqlQueries.DATATYPE_STRING, SqlQueries.DATATYPE_FLOAT, SqlQueries.DATATYPE_STRING};
        //Object[] values = {"2018-10-15", "MAT", "3.333", "tjabba"};
        boolean success = SqlQueries.addSqlData(myDatabaseHandler.getReadableDatabase(), MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE, attributes, dataTypes, values);
        Log.i("SUCCESS", "" + success);
    }


    /**
     *
     * @param selectionArgs (datum först, kategori därefter)
     */
    public int getSumOfExpenditTableCol(String[] selectionArgs) {

        String table = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
        String sumColumn = MyDatabaseContract.TableExpenditure.ATTR_EXPENDITURE;
        String attribute1 = MyDatabaseContract.TableExpenditure.ATTR_DATE;
        String attribute2 = MyDatabaseContract.TableExpenditure.ATTR_CATEGORY;

        String[] selection = {sumColumn, table, attribute1, attribute2};

        return SqlQueries.sumTableColumn2Cond(myDatabaseHandler.getReadableDatabase(), selection, selectionArgs);
    }

    public int getSumOfExpenditTableCol(String selectionArg) {

        String table = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
        String sumColumn = MyDatabaseContract.TableExpenditure.ATTR_EXPENDITURE;
        String attribute1 = MyDatabaseContract.TableExpenditure.ATTR_CATEGORY;

        String[] selection = {sumColumn, table, attribute1};

        String[] selectionArgs = {selectionArg};

        return SqlQueries.sumTableColumn1Cond(myDatabaseHandler.getReadableDatabase(), selection, selectionArgs);
    }

    public float sumExpTableColumnMonth(int month) {

        String expenditure = MyDatabaseContract.TableExpenditure.ATTR_EXPENDITURE;
        String table = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
        String date = MyDatabaseContract.TableExpenditure.ATTR_DATE;

        String[] selection = {expenditure, table, date};
        String[] selArg = new String[1];
        if (month < 10)
            selArg[0] = "'0" + month + "'";
        else
            selArg[0] = "'" + month + "'";

        return SqlQueries.sumTableColumnMonth(myDatabaseHandler.getReadableDatabase(), selection, selArg);
    }


    public float sumExpTableColumnYear(int year) {

        String expenditure = MyDatabaseContract.TableExpenditure.ATTR_EXPENDITURE;
        String table = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
        String date = MyDatabaseContract.TableExpenditure.ATTR_DATE;

        String[] selection = {expenditure, table, date};
        String[] selArg = {"'" + year + "'"};
        return SqlQueries.sumTableColumnYear(myDatabaseHandler.getReadableDatabase(), selection, selArg);
    }

    public float sumExpTableColumnYearCat(int year, String categoryVal) {

        String expenditure = MyDatabaseContract.TableExpenditure.ATTR_EXPENDITURE;
        String table = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
        String date = MyDatabaseContract.TableExpenditure.ATTR_DATE;
        String category = MyDatabaseContract.TableExpenditure.ATTR_CATEGORY;

        String[] selection = {expenditure, table, date, category};
        String[] selArg = {"'" + year + "'", categoryVal};
        return SqlQueries.sumTableColumnYearCat(myDatabaseHandler.getReadableDatabase(), selection, selArg);
    }

    public float sumExpTableColumnYearMonth(int year, int month) {

        String expenditure = MyDatabaseContract.TableExpenditure.ATTR_EXPENDITURE;
        String table = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
        String date = MyDatabaseContract.TableExpenditure.ATTR_DATE;

        String[] selection = {expenditure, table, date};
        String[] selArg = new String[2];
        selArg[0] = "'" + year + "'";

        if (month < 10)
            selArg[1] = "'0" + month + "'";
        else
            selArg[1] = "'" + month + "'";

        return SqlQueries.sumTableColumnYearMonth(myDatabaseHandler.getReadableDatabase(), selection, selArg);
    }

    public float sumTableColumnYearMonthCat(int year, int month, String categoryValue) {

        String expenditure = MyDatabaseContract.TableExpenditure.ATTR_EXPENDITURE;
        String table = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
        String date = MyDatabaseContract.TableExpenditure.ATTR_DATE;
        String category = MyDatabaseContract.TableExpenditure.ATTR_CATEGORY;

        String[] selection = {expenditure, table, date, category};
        String[] selArg = new String[3];
        selArg[0] = "'" + year + "'";

        if (month < 10)
            selArg[1] = "'0" + month + "'";
        else
            selArg[1] = "'" + month + "'";

        selArg[2] = categoryValue;

        return SqlQueries.sumTableColumnYearMonthCat(myDatabaseHandler.getReadableDatabase(), selection, selArg);
    }


    public float sumTableColumnMonthCat(int month, String categoryValue) {

        String expenditure = MyDatabaseContract.TableExpenditure.ATTR_EXPENDITURE;
        String table = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
        String date = MyDatabaseContract.TableExpenditure.ATTR_DATE;
        String category = MyDatabaseContract.TableExpenditure.ATTR_CATEGORY;

        String[] selection = {expenditure, table, date, category};
        String[] selArg = new String[2];

        if (month < 10)
            selArg[0] = "'0" + month + "'";
        else
            selArg[0] = "'" + month + "'";

        selArg[1] = categoryValue;

        return SqlQueries.sumTableColumnMonthCat(myDatabaseHandler.getReadableDatabase(), selection, selArg);
    }


    public Object[] getSortedDays() {

        String table = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
        String date = MyDatabaseContract.TableExpenditure.ATTR_DATE;

        int nRows = SqlQueries.getRowCount(myDatabaseHandler.getReadableDatabase(), table);
        Object[] dates = new Object[nRows];
        String[] selection = {table, date};
        SqlQueries.sortBy(myDatabaseHandler.getReadableDatabase(), selection, dates);

        return dates;
    }

    public Object[][] selectNrowsLastExpTable(int nRows) {

        String expenditure = MyDatabaseContract.TableExpenditure.ATTR_EXPENDITURE;
        String table = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
        String date = MyDatabaseContract.TableExpenditure.ATTR_DATE;
        String[] selection = {expenditure, table, date};


        Object[][] data = new Object[2][nRows];
        SqlQueries.selectNrowsLast(myDatabaseHandler.getReadableDatabase(), selection, data);

        return data;
    }


    public float selectSumNrowsLastExpTable(String categoryValue, int nRows) {

        String expenditure = MyDatabaseContract.TableExpenditure.ATTR_EXPENDITURE;
        String table = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
        String date = MyDatabaseContract.TableExpenditure.ATTR_DATE;
        String category = MyDatabaseContract.TableExpenditure.ATTR_CATEGORY;
        String[] selection = {expenditure, table, category, date};



        Object[] expenditures = new Object[nRows];

        return SqlQueries.selectSumNrowsLast(myDatabaseHandler.getReadableDatabase(), selection, categoryValue, nRows, expenditures);
    }


    /**
     *
     * @param value (aktuell kategori)
     * @return
     */
    public boolean deleteExpTableRowsByCategory(String value) {

        String table = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
        String column = MyDatabaseContract.TableExpenditure.ATTR_CATEGORY;

        SqlQueries.deleteTableRow(myDatabaseHandler.getReadableDatabase(), table, column, value);

        return false;
    }

    /**
     *
     * @param value (aktuell kategori)
     * @return
     */
    public boolean deleteCatTableRowsByCategory(String value) {

        String table = MyDatabaseContract.TableCategories.TABLE_CATEGORIES;
        String column = MyDatabaseContract.TableCategories.KEY_ID;

        SqlQueries.deleteTableRow(myDatabaseHandler.getReadableDatabase(), table, column, value);

        return false;
    }


    public void updateCatTableImage(String value, String category) {

        String table = MyDatabaseContract.TableCategories.TABLE_CATEGORIES;
        String[] columns = {MyDatabaseContract.TableCategories.IMAGE_ID}; //kolumnnamn bild
        Object[] values = {value}; //aktuell kategori
        int[] dataTypes = {SqlQueries.DATATYPE_STRING}; //bilden är av typen sträng
        String targetCol = MyDatabaseContract.TableCategories.KEY_ID; // kolumnnamn kategori

        SqlQueries.updateTableColumn(myDatabaseHandler.getReadableDatabase(), table, columns, values, dataTypes, targetCol, category);
    }


    public void addDataCategory(String categoryName, String imageName) {


        String table = MyDatabaseContract.TableCategories.TABLE_CATEGORIES;
        String[] attributes = {MyDatabaseContract.TableCategories.KEY_ID, MyDatabaseContract.TableCategories.IMAGE_ID};
        int[] dataTypes = {SqlQueries.DATATYPE_STRING, SqlQueries.DATATYPE_STRING};
        Object[] values = {categoryName, imageName};
        SqlQueries.addSqlData(myDatabaseHandler.getReadableDatabase(), table, attributes, dataTypes, values);
    }

    public void updateCatTable(String newCategory, String oldCategory, String imageName) {

        //String[] columns = {ATTR_CATEG_EXP, ATTR_EXPENDITURE, ATTR_COMMENT};
        String table = MyDatabaseContract.TableCategories.TABLE_CATEGORIES;
        String[] columns = {MyDatabaseContract.TableCategories.KEY_ID, MyDatabaseContract.TableCategories.IMAGE_ID};
        Object[] values = {newCategory, imageName};
        int[] dataTypes = {SqlQueries.DATATYPE_STRING, SqlQueries.DATATYPE_STRING};
        SqlQueries.updateTableColumn(myDatabaseHandler.getReadableDatabase(), table, columns, values, dataTypes, columns[0], oldCategory);
    }


    public void updateExpenditure(String newCatName, String oldName) {

        //String[] columns = {ATTR_CATEG_EXP, ATTR_EXPENDITURE, ATTR_COMMENT};


        String table = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
        String[] columns = {MyDatabaseContract.TableExpenditure.ATTR_CATEGORY}; // vill uppdatera denna kolumn
        Object[] values = {newCatName}; //med detta värde
        int[] dataTypes = {SqlQueries.DATATYPE_STRING};
        SqlQueries.updateTableColumn(myDatabaseHandler.getReadableDatabase(), table, columns, values, dataTypes, columns[0], oldName);
    }


    /**
     *
     * @param targets (kategori, datum)
     */
    public ArrayList<Object> getTableDataById(Object[] targets) {

        String table = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
        String category = MyDatabaseContract.TableExpenditure.ATTR_CATEGORY;
        String date = MyDatabaseContract.TableExpenditure.ATTR_DATE;

        String[] targetCols = {category, date};

        ArrayList <Object> tableDataList = new ArrayList<>();
        int[] dataTypes = {SqlQueries.DATATYPE_INT, SqlQueries.DATATYPE_STRING, SqlQueries.DATATYPE_STRING, SqlQueries.DATATYPE_FLOAT, SqlQueries.DATATYPE_STRING};
        SqlQueries.getTableRowDataByID(myDatabaseHandler.getReadableDatabase(), table, dataTypes, MyDatabaseContract.TableExpenditure.N_COLUMNS, tableDataList, targetCols, targets);

        return tableDataList;
    }


    public void deleteRowExpenditureTableById(int id) {

        String table = MyDatabaseContract.TableExpenditure.TABLE_EXPENDITURE;
        String col = MyDatabaseContract.TableExpenditure.ATTR_PRIMARY_KEY;

        SqlQueries.deleteTableRow(myDatabaseHandler.getReadableDatabase(), table, col, id);

    }

}
