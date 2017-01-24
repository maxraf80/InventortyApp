package udacity.com.inventortyapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class ItemProvider extends ContentProvider {

    public static final String LOG_TAG = ItemProvider.class.getSimpleName();
    private static final int ITEMS = 100;
    private static final int ITEM_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS, ITEMS);
        sUriMatcher.addURI(ItemContract.CONTENT_AUTHORITY, ItemContract.PATH_ITEMS + "/#", ITEM_ID);
    }

    private ItemDbHelper mDbHelper;


    @Override
    public boolean onCreate() {
        mDbHelper = new ItemDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor;
        int match = sUriMatcher.match(uri);
        switch (match) {

            case ITEMS:
                cursor = database.query(ItemContract.ItemEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case ITEM_ID:
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(ItemContract.ItemEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {

            case ITEMS:
                return insertItem(uri, contentValues);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertItem(Uri uri, ContentValues values) {

        String name = values.getAsString(ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT);
        if (name == null) {
            throw new IllegalArgumentException("Item requires a name");
        }

        Integer category = values.getAsInteger(ItemContract.ItemEntry.COLUMN_ITEM_CATEGORY);
        if (category == null || !ItemContract.ItemEntry.isValidCategory(category)) {
            throw new IllegalArgumentException("Item requires a category");
        }

        String reference = values.getAsString(ItemContract.ItemEntry.COLUMN_ITEM_REFERENCE);
        if (reference == null) {
            throw new IllegalArgumentException("Item requires a reference");
        }

        Integer price = Integer.parseInt(values.getAsString(ItemContract.ItemEntry.COLUMN_ITEM_PRICE));
        if (price < 0) {  throw new IllegalArgumentException("Price needs to be defined");
        }

        Integer units = Integer.parseInt(values.getAsString(ItemContract.ItemEntry.COLUMN_ITEM_UNITS));
        if (units <0) {  throw new IllegalArgumentException("Units needs to be defined");   }

        String suplier = values.getAsString(ItemContract.ItemEntry.COLUMN_ITEM_SUPLIER);
        if (suplier == null) {
            throw new IllegalArgumentException("Suplier cannot be an empty field");
        }

        String email = values.getAsString(ItemContract.ItemEntry.COLUMN_ITEM_SUPLIER);
        if (email == null) {
            throw new IllegalArgumentException("Suplier cannot be an empty field");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(ItemContract.ItemEntry.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return updateItem(uri, contentValues, selection, selectionArgs);
            case ITEM_ID:
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs= new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateItem(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateItem(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT)) {
            String name = values.getAsString(ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT);
            if (name == null) {
                throw new IllegalArgumentException("Item requires a name");
            }
        }

        if (values.containsKey((ItemContract.ItemEntry.COLUMN_ITEM_PRICE))) {
            Integer price = Integer.parseInt(values.getAsString(ItemContract.ItemEntry.COLUMN_ITEM_PRICE));
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Price needs to be defined");
            }
        }


        if (values.containsKey((ItemContract.ItemEntry.COLUMN_ITEM_UNITS))) {
            Integer units = Integer.parseInt(values.getAsString(ItemContract.ItemEntry.COLUMN_ITEM_UNITS));
            if (units != null && units < 0) {
                throw new IllegalArgumentException("Units needs to be defined");
            }
        }

        if (values.containsKey((ItemContract.ItemEntry.COLUMN_ITEM_SUPLIER))) {
            String suplier = values.getAsString(ItemContract.ItemEntry.COLUMN_ITEM_SUPLIER);
            if (suplier == null) {
                throw new IllegalArgumentException("Suplier needs to be defined");
            }
        }

        if (values.containsKey((ItemContract.ItemEntry.COLUMN_ITEM_EMAIL))) {
            String email = values.getAsString(ItemContract.ItemEntry.COLUMN_ITEM_EMAIL);
            if (email == null) {
                throw new IllegalArgumentException("Suplier needs to be defined");
            }
        }


        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mDbHelper.getWritableDatabase();


        int rowsUpdated = database.update(ItemContract.ItemEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                rowsDeleted = database.delete(ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case ITEM_ID:
                selection = ItemContract.ItemEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ItemContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ITEMS:
                return ItemContract.ItemEntry.CONTENT_LIST_TYPE;
            case ITEM_ID:
                return ItemContract.ItemEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}


































