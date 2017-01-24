package udacity.com.inventortyapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ItemContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.items";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_ITEMS = "items";
    private ItemContract() {
    }

    public static final class ItemEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ITEMS);
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_ITEMS;

        public final static String TABLE_NAME = "items";
        public final static String _ID = BaseColumns._ID;


        public final static String COLUMN_ITEM_PRODUCT = "name";
        public final static String COLUMN_ITEM_REFERENCE = "reference";
        public final static String COLUMN_ITEM_CATEGORY = "category";
        public final static String COLUMN_ITEM_PRICE = "price";
        public final static String COLUMN_ITEM_UNITS = "units";
        public final static String COLUMN_ITEM_SUPLIER = "suplier";
        public final static String COLUMN_ITEM_EMAIL = "email";

        public static final int CATEGORY_UNKNOWN = 0;
        public static final int CATEGORY_CLEANERS = 1;
        public static final int CATEGORY_FOOD = 2;
        public static final int CATEGORY_PETS = 3;
        public static final int CATEGORY_HYGIENE = 4;
        public static final int CATEGORY_FRUIT = 5;
        public static final int CATEGORY_FISH = 6;

        public static boolean isValidCategory(int category) {
            if (category == CATEGORY_UNKNOWN || category == CATEGORY_CLEANERS || category == CATEGORY_FOOD ||
                    category == CATEGORY_PETS || category == CATEGORY_HYGIENE || category == CATEGORY_FRUIT || category == CATEGORY_FISH) {
                return true;
            }
            return false;
        }
    }
}
