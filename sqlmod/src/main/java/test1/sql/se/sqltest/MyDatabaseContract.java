package test1.sql.se.sqltest;

public class MyDatabaseContract {

    private MyDatabaseContract() {}

    public static class TableCategories {

        public static final String TABLE_CATEGORIES = "categories_table"; // tabellnamn
        public static final String KEY_ID = "id"; // primärnyckel (kategori)
        public static final String IMAGE_ID = "res_id"; // bildreferens

        public static final int N_COLUMNS = 2;

        public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_CATEGORIES + "(" + KEY_ID + " TEXT PRIMARY KEY," + IMAGE_ID + " TEXT" + ")";

    }


    public static class TableExpenditure {

        public static final String TABLE_EXPENDITURE = "expenditure_table";

        public static final String ATTR_PRIMARY_KEY = "pk_expendituretable";
        public static final String ATTR_DATE = "date";
        public static final String ATTR_CATEGORY = "category";
        public static final String ATTR_EXPENDITURE = "expenditure";
        public static final String ATTR_COMMENT = "comment";

        public static final int N_COLUMNS = 5;

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_EXPENDITURE + "(" + ATTR_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT," +  ATTR_DATE + " DATE," + ATTR_CATEGORY + " TEXT," +
                        ATTR_EXPENDITURE + " REAL," + ATTR_COMMENT + " TEXT"  + ")";
    }

}
