package test1.sql.se.sqltest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 *
 * Klassen innehåller en uppsättning statiska metoder för att ställa sql-frågor till den databas.
 * Björn Hallström
 * Version: 2 (2018-11-30) - vissa funktioner mer generiska än tidigare
 *
 */

public class SqlQueries {

    public static final int DATATYPE_STRING = 0;
    public static final int DATATYPE_INT = 1;
    public static final int DATATYPE_FLOAT = 2;


    /**
     * Uppdatering per 2018-11-29. Metoden är nu mer generisk
     * @param db databas
     * @param table tabell
     * @param attributes attribut (kolumner)
     * @param dataTypes datatyper (t.ex. int eller String)
     * @param contents tabelldata
     * @return
     */
    public static boolean addSqlData(SQLiteDatabase db, String table, String[] attributes, int[] dataTypes, Object[] contents) {

        if (attributes.length == dataTypes.length && dataTypes.length == contents.length) { // längden måste vara densamma

            ContentValues values = new ContentValues();
            for (int col = 0; col < dataTypes.length; col++) {
                switch (dataTypes[col]) {
                    case DATATYPE_STRING:
                        values.put(attributes[col], contents[col].toString());
                        break;
                    case DATATYPE_INT:
                        values.put(attributes[col], Integer.parseInt(contents[col].toString()));
                        break;
                    case DATATYPE_FLOAT:
                        values.put(attributes[col], Float.parseFloat(contents[col].toString()));
                        break;
                }
            }

            // skapa tabellrad
            db.insert(table, null, values);
            //db.close(); // stäng anslutning

            return true;
        } else
            return false;
    }

    /**
     * Räknar antalet rader
     * @return
     */
    public static int getRowCount(SQLiteDatabase db, String table) {
        String countQuery = "SELECT  * FROM " + table;
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }


    /**
     * Hämtar all tabelldata
     * Uppdatering per 2018-11-29. Metoden är nu mer generisk
     * @param db
     * @param tableData
     * @param table
     * @param nColums
     */
    public static void getTableData(SQLiteDatabase db, Object[][] tableData, String table, int nColums) {

        String query = "SELECT * FROM " + table;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null) {
                int row = 0;
                cursor.moveToFirst();
                do {
                    for (int col = 0; col < nColums; col++) {
                        tableData[row][col] = cursor.getString(col);
                    }
                    row++;

                } while (cursor.moveToNext());
            }
        }
        catch (Exception e) {}
    }



    public static void getTableRowDataByID(SQLiteDatabase db, String table, int[] dataTypes, int nCols, ArrayList<Object> tableDataList, String targetCol, Object target) {


        String query = "SELECT * FROM " + table + " WHERE " + targetCol + " =" + "'" + target + "'";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null) {

                int row = 0;
                cursor.moveToFirst();
                do {
                    for (int col = 0; col < nCols; col++) {
                        //tableData[row][col] = cursor.getString(col);
                        Object ob = null;
                        switch (dataTypes[col]) {
                            case DATATYPE_STRING:
                                ob = cursor.getString(col);
                                break;
                            case DATATYPE_INT:
                                ob = cursor.getInt(col);
                                break;
                            case DATATYPE_FLOAT:
                                ob = cursor.getFloat(col);
                                break;
                        }
                        tableDataList.add(ob);
                    }
                    row++;
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }



    public static void getTableRowDataByID(SQLiteDatabase db, String table, int[] dataTypes, int nCols, ArrayList<Object> tableDataList, String[] targetCols, Object[] targets) {


        String query = "SELECT * FROM " + table + " WHERE " + targetCols[0] + " =" + "'" + targets[0] + "'" + " AND " + targetCols[1] + " = " + "'" + targets[1] + "'";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null) {

                int row = 0;
                cursor.moveToFirst();
                do {
                    for (int col = 0; col < nCols; col++) {
                        //tableData[row][col] = cursor.getString(col);
                        Object ob = null;
                        switch (dataTypes[col]) {
                            case DATATYPE_STRING:
                                ob = cursor.getString(col);
                                break;
                            case DATATYPE_INT:
                                ob = cursor.getInt(col);
                                break;
                            case DATATYPE_FLOAT:
                                ob = cursor.getFloat(col);
                                break;
                        }
                        tableDataList.add(ob);
                    }
                    row++;
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }


    /**
     * tar fullständigt bort tabellen från databasen
     * @param db
     * @param table
     */
    public static void dropTable( SQLiteDatabase db, String table) {

        db.execSQL("DROP TABLE IF EXISTS " + table);
    }


    /**
     * tar bort allt innehåll i tabeöllen
     * @param db
     * @param table
     */
    public static void clearTable(SQLiteDatabase db, String table) {

        db.execSQL("delete from " + table);
    }


    /**
     * Tar bort samtliga tuples där ett givet attributs innehåll matchat en given textsträng
     * @param db
     * @param table
     * @param columnName
     * @param value
     */
    public static void deleteTableRow(SQLiteDatabase db, String table, String columnName, String value) {

        db.execSQL("delete from " + table + " WHERE " + columnName + "=" + "'" + value + "'");
    }


    /**
     * Tar bort samtliga tuples där ett givet attributs innehåll matchat ett givet heltal
     * @param db
     * @param table
     * @param columnName
     * @param value
     */
    public static void deleteTableRow(SQLiteDatabase db, String table, String columnName, int value) {

        db.execSQL("delete from " + table + " WHERE " + columnName + "=" + value);
    }


    /**
     * Uppdatering av tabellvärden
     * @param db
     * @param table
     * @param columns
     * @param values
     * @param dataTypes
     * @param targetCol målkolumnen - dvs den kolumnen som måste innehålla ett specifikt värde för att uppdatering av raden ska ske. Målkolumnen är inte sällan PK (Primary Key)
     * @param target
     * @return sant eller falskt beroende på om kolumnlängderna var desamma
     */
    public static boolean updateTableColumn(SQLiteDatabase db, String table, String[] columns, Object[] values, int[] dataTypes, String targetCol, int target) {

        if (columns.length == values.length && values.length == dataTypes.length) { //kolumnlängden måste vara densamma
            String stringConc = "";
            for (int i = 0; i < columns.length; i++) {

                switch (dataTypes[i]) {

                    case DATATYPE_STRING:
                        if (i < columns.length - 1)
                            stringConc += (columns[i] + "=" + "'" + values[i] + "'" + ",");
                        else
                            stringConc += (columns[i] + "=" + "'" + values[i] + "'" + " ");
                        break;
                    case DATATYPE_FLOAT:
                        if (i < columns.length - 1)
                            stringConc += (columns[i] + "=" + values[i] + ",");
                        else
                            stringConc += (columns[i] + "=" + values[i] + " ");
                        break;
                }
            }
            String sqlQuerie = "UPDATE " + table + " SET " + stringConc + "WHERE " + targetCol + "=" + target;
            db.execSQL(sqlQuerie);

            return true;
        } else
            return false;
    }






    /**
     * Uppdatering av tabellvärden
     * @param db
     * @param table
     * @param columns
     * @param values
     * @param dataTypes
     * @param targetCol målkolumnen - dvs den kolumnen som måste innehålla ett specifikt värde för att uppdatering av raden ska ske. Målkolumnen är inte sällan PK (Primary Key)
     * @param target
     * @return sant eller falskt beroende på om kolumnlängderna var desamma
     */
    public static boolean updateTableColumn(SQLiteDatabase db, String table, String[] columns, Object[] values, int[] dataTypes, String targetCol, String target) {

        if (columns.length == values.length && values.length == dataTypes.length) { //kolumnlängden måste vara densamma
            String stringConc = "";
            for (int i = 0; i < columns.length; i++) {

                switch (dataTypes[i]) {

                    case DATATYPE_STRING:
                        if (i < columns.length - 1)
                            stringConc += (columns[i] + "=" + "'" + values[i] + "'" + ",");
                        else
                            stringConc += (columns[i] + "=" + "'" + values[i] + "'" + " ");
                        break;
                    case DATATYPE_FLOAT:
                        if (i < columns.length - 1)
                            stringConc += (columns[i] + "=" + values[i] + ",");
                        else
                            stringConc += (columns[i] + "=" + values[i] + " ");
                        break;
                }
            }
            String sqlQuerie = "UPDATE " + table + " SET " + stringConc + "WHERE " + targetCol + "=" + "'" + target + "'";
            db.execSQL(sqlQuerie);

            return true;
        } else
            return false;
    }



    /**
     * Summering av given kolumn där villkor ställs på 2 andra kolumner
     * @param db
     * @param selection (kolumn att summera, tabell, datum, kategori)
     * @param selectionArgs (tex datumvärde o kategorivärde)
     */
    public static int sumTableColumn2Cond(SQLiteDatabase db, String[] selection, String[] selectionArgs) {


        final int SELECTION_ID1 = 0; //kolumn att summera
        final int SELECTION_ID2 = 1; //aktuell tabell
        final int SELECTION_ID3 = 2; //kolumn1 att undersöka
        final int SELECTION_ID4 = 3; //kolumn2 att undersöka

        //String query = "SELECT SUM(" + sumColumn + ") as Total FROM " + table + " WHERE " + attribute1 + "=" +  '?' + " AND " + attribute2 + "=" + '?';
        String query = "SELECT SUM(" + selection[SELECTION_ID1] + ") as Total FROM " +
                selection[SELECTION_ID2] + " WHERE " +
                selection[SELECTION_ID3] + "=" +  '?' + " AND " +
                selection[SELECTION_ID4] + "=" + '?';


        Cursor cursor = db.rawQuery(query, selectionArgs);

        int total = 0;
        if (cursor.moveToFirst())
            total = cursor.getInt(cursor.getColumnIndex("Total"));// get final total

        return total;
    }

    /**
     * Summering av enskild kolumn med 1 argument
     * @param db
     * @param selection
     * @param selectionArgs
     * @return
     */
    public static int sumTableColumn1Cond(SQLiteDatabase db, String[] selection, String[] selectionArgs) {

        final int SELECTION_ID1 = 0; //kolumn att summera
        final int SELECTION_ID2 = 1; //aktuell tabell
        final int SELECTION_ID3 = 2; //kolumn1 att undersöka

        //String query = "SELECT SUM(" + sumColumn + ") as Total FROM " + table + " WHERE " + attribute1 + "=" +  '?' + " AND " + attribute2 + "=" + '?';
        String query = "SELECT SUM(" + selection[SELECTION_ID1] + ") as Total FROM " +
                selection[SELECTION_ID2] + " WHERE " +
                selection[SELECTION_ID3] + "=" +  '?';


        Cursor cursor = db.rawQuery(query, selectionArgs);

        int total = 0;
        if (cursor.moveToFirst())
            total = cursor.getInt(cursor.getColumnIndex("Total"));// get final total

        return total;

    }


    /**
     * Väljer ut en enskild månad där summan återges för en specifik kolumn
     * @param db
     * @param selection (kolumn att summera, tabell samt datum)
     * @param selectionArgs aktuell månad
     * @return
     */
    public static float sumTableColumnMonth(SQLiteDatabase db, String[] selection, String[] selectionArgs) {

        String query = "SELECT SUM(" + selection[0] + ") AS Total FROM " + selection[1] + " WHERE strftime('%m', " + selection[2] + ")=" + selectionArgs[0];
        Cursor cursor = db.rawQuery(query, null);

        float total = -1;
        if (cursor.moveToFirst())
            total = cursor.getFloat(cursor.getColumnIndex("Total"));// get final total

        return total;
    }


    /**
     * Väljer ut ett enskilt år där summan återges för en specifik kolumn.
     * @param db
     * @param selection (kolumn att summera, tabell samt datum)
     * @param selectionArgs aktuell månad.
     * @return
     */
    public static float sumTableColumnYear(SQLiteDatabase db, String[] selection, String[] selectionArgs) {

        String query = "SELECT SUM(" + selection[0] + ") AS Total FROM " + selection[1] + " WHERE strftime('%Y', " + selection[2] + ")=" + selectionArgs[0];
        Cursor cursor = db.rawQuery(query, null);

        float total = -1;
        if (cursor.moveToFirst())
            total = cursor.getFloat(cursor.getColumnIndex("Total"));// get final total

        return total;
    }


    public static float sumTableColumnYearCat(SQLiteDatabase db, String[] selection, String[] selectionArgs) {

        String query = "SELECT SUM(" + selection[0] + ") AS Total FROM " + selection[1] + " WHERE strftime('%Y', " + selection[2] + ")=" + selectionArgs[0] +
                " AND " + selection[3] + "=" + "'" + selectionArgs[1] + "'";
        Cursor cursor = db.rawQuery(query, null);

        float total = -1;
        if (cursor.moveToFirst())
            total = cursor.getFloat(cursor.getColumnIndex("Total"));// get final total

        return total;
    }


    public static float sumTableColumnYearMonth(SQLiteDatabase db, String[] selection, String[] selectionArgs) {

        String query = "SELECT SUM(" + selection[0] + ") AS Total FROM " + selection[1] + " WHERE strftime('%Y', " + selection[2] + ")=" + selectionArgs[0] +
                " AND strftime('%m', " + selection[2] + ")=" + selectionArgs[1];
        Cursor cursor = db.rawQuery(query, null);

        float total = -1;
        if (cursor.moveToFirst())
            total = cursor.getFloat(cursor.getColumnIndex("Total"));// get final total

        return total;
    }


    public static float sumTableColumnYearMonthCat(SQLiteDatabase db, String[] selection, String[] selectionArgs) {

        String query = "SELECT SUM(" + selection[0] + ") AS Total FROM " + selection[1] + " WHERE strftime('%Y', " + selection[2] + ")=" + selectionArgs[0] +
                " AND strftime('%m', " + selection[2] + ")=" + selectionArgs[1] + " AND " + selection[3] + "=" + "'" + selectionArgs[2] + "'";
        Cursor cursor = db.rawQuery(query, null);

        float total = -1;
        if (cursor.moveToFirst())
            total = cursor.getFloat(cursor.getColumnIndex("Total"));// get final total

        return total;
    }


    public static float sumTableColumnMonthCat(SQLiteDatabase db, String[] selection, String[] selectionArgs) {

        String query = "SELECT SUM(" + selection[0] + ") AS Total FROM " + selection[1] + " WHERE strftime('%m', " + selection[2] + ")=" + selectionArgs[0] +
                " AND " + selection[3] + "=" + "'" + selectionArgs[1] + "'";
        Cursor cursor = db.rawQuery(query, null);

        float total = -1;
        if (cursor.moveToFirst())
            total = cursor.getFloat(cursor.getColumnIndex("Total"));// get final total

        return total;
    }

    public static void sortBy(SQLiteDatabase db, String[] selection, Object[] result) {

        String query = "SELECT * " +  "FROM " + selection[0] + " ORDER BY " + selection[1];
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            if (cursor != null) {

                int row = 0;
                cursor.moveToFirst();
                do {
                    result[row] = cursor.getString(1);
                    row++;
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static void selectNrowsLast(SQLiteDatabase db, String[] selection, Object[][] results) {


        String query = "SELECT *" + " FROM " + selection[1] + " ORDER BY " + selection[2] + " DESC LIMIT " + "'" + 3 + "'";
        Cursor cursor = null;
        try {

            cursor = db.rawQuery(query, null);

            if (cursor != null) {

                int row = 0;
                cursor.moveToFirst();
                do {
                    //results[row] = cursor.getFloat(3);
                    results[0][row] = cursor.getString(2);
                    results[1][row] = cursor.getString(3);
                    row++;
                } while (cursor.moveToNext());
            }

        }  catch (Exception e) {

            e.printStackTrace();
        }
    }

    public static float selectSumNrowsLast(SQLiteDatabase db, String[] selection, String selectionArg, int nRows, Object[] results) {

        //String query = "SELECT SUM(" + selection[0] + ") AS Total FROM " + selection[1] + " WHERE

        String query = "SELECT SUM(" + selection[0] + ") AS Total FROM " + selection[1] + " WHERE " + selection[2] + "= '" + selectionArg + "'" + " ORDER BY " + selection[2] + " DESC LIMIT " + nRows;
                //  " FROM " + selection[1] + " ORDER BY " + selection[2] + " DESC LIMIT " + "'" + 3 + "'";
        Cursor cursor = null;
        float total = -1;
        try {

            cursor = db.rawQuery(query, null);
            if (cursor != null) {

                if (cursor.moveToFirst())
                    total = cursor.getFloat(cursor.getColumnIndex("Total"));// get final total
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }

        return total;
    }


}
