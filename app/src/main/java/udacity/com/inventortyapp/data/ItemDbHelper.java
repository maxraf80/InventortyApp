package udacity.com.inventortyapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = ItemDbHelper.class.getSimpleName();

    private static final String DATABSE_NAME = "shelter.db";
    private static final int DATABASE_VERSION = 2;

    public ItemDbHelper(Context context) {
        super(context, DATABSE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ITEMS_TABLE = "CREATE TABLE " + ItemContract.ItemEntry.TABLE_NAME + " ("
                + ItemContract.ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT + " TEXT NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_ITEM_REFERENCE + " TEXT, "
                + ItemContract.ItemEntry.COLUMN_ITEM_CATEGORY + " INTEGER NOT NULL, "
                + ItemContract.ItemEntry.COLUMN_ITEM_PRICE + " INTEGER NOT NULL DEFAULT 0, "
                + ItemContract.ItemEntry.COLUMN_ITEM_UNITS + " INTEGER NOT NULL DEFAULT 0, "
                + ItemContract.ItemEntry.COLUMN_ITEM_SUPLIER + " TEXT, "
                + ItemContract.ItemEntry.COLUMN_ITEM_EMAIL + " TEXT );";


        db.execSQL(SQL_CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
