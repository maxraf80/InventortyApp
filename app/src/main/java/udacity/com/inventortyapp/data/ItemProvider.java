package udacity.com.inventortyapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ItemProvider extends ContentProvider{

    public static final String LOG_TAG = ItemProvider.class.getSimpleName();
    private static final int ITEMS = 100;
    private static final int ITEM_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY,ItemContract.PATH_ITEMS,ITEMS);
    sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY,ItemContract.PATH_ITEMS + "/#", ITEM_ID);}
    private ItemDbHelper mDbHelper;


    @Override
    public boolean onCreate() {
        mDbHelper = new ItemDbHelper(getContext());
        return true;}


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
    SQLiteDatabase database = mDbHelper.getReadableDatabase();
    Cursor cursor;
    int match = sUriMatcher.match(uri);
    switch (match) {
        case ITEMS:
            cursor=database.query(ItemContract.ItemEntry.TABLE_NAME,projection, selection, selectionArgs,
            null, null, sortOrder);
            break;
        case ITEM_ID:
        selection = ItemContract.ItemEntry._ID + "=?";
        selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
        cursor = database.query(ItemContract.ItemEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
        break;
        default:
        throw new IllegalArgumentException("Cannot query unknown URI " + uri);}
    return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
