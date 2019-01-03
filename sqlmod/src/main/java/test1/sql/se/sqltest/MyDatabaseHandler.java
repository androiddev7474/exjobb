package test1.sql.se.sqltest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHandler extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyStorage4";
    public static final int DATABASE_VERSION = 4;

    private static MyDatabaseHandler sInstance;

    public static synchronized MyDatabaseHandler getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new MyDatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
    }


    public MyDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(MyDatabaseContract.TableCategories.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(MyDatabaseContract.TableExpenditure.SQL_CREATE_TABLE);
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        onCreate(db);
    }



}


