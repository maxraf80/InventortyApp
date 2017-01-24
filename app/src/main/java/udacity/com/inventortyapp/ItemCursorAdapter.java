package udacity.com.inventortyapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import udacity.com.inventortyapp.data.ItemContract;


public class ItemCursorAdapter extends CursorAdapter {
    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }


    private int mRowsAffected;
    private String mQuantitySold;
    private Context mContext;


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final int mRowId = cursor.getInt(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry._ID));

        TextView nameTextView = (TextView) view.findViewById(R.id.name_cursor);
        TextView referenceTextView = (TextView) view.findViewById(R.id.reference_cursor);
        TextView categoryTextView = (TextView) view.findViewById(R.id.category_cursor);
        TextView priceTextView = (TextView) view.findViewById(R.id.price_cursor);
    final TextView unitsTextView = (TextView) view.findViewById(R.id.units_cursor);
        TextView suplierTextView = (TextView) view.findViewById(R.id.suplier_cursor);
        TextView emailTextView = (TextView) view.findViewById(R.id.email_cursor);
        Button mSellButton = (Button) view.findViewById(R.id.track_sale);




        int nameColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT);
        int referenceColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_REFERENCE);
        int categoryColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_CATEGORY);
        int priceColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRICE);
        int unitsColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_UNITS);
        int suplierColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_SUPLIER);
        int emailColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_EMAIL);


        mContext = context;


        String name = cursor.getString(nameColumnIndex);
        String reference = cursor.getString(referenceColumnIndex);
        String category = cursor.getString(categoryColumnIndex);
        String price = cursor.getString(priceColumnIndex);
        String units = cursor.getString(unitsColumnIndex);
        String supplier = cursor.getString(suplierColumnIndex);
        String email = cursor.getString(emailColumnIndex);


        if (TextUtils.isEmpty(name)) {
            name = "Unknown";
        }
        if (TextUtils.isEmpty(reference)) {
            reference = "Unknown";
        }
        if (TextUtils.isEmpty(category)) {
            category = "Unknown";
        }
        if (TextUtils.isEmpty(price)) {
            price = "0";
        }
        if (TextUtils.isEmpty(units)) {
            units = "0"; }

        if (TextUtils.isEmpty(supplier)) {
            supplier = "Unknown";
        }
        if (TextUtils.isEmpty(email)) {
            email = "Unknown";
        }

        nameTextView.setText(name);
        referenceTextView.setText(reference);
        categoryTextView.setText(category);
        priceTextView.setText(price);
        unitsTextView.setText(units);
        suplierTextView.setText(supplier);
        emailTextView.setText(email);



        mSellButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        productSale(mRowId,Integer.parseInt(unitsTextView.getText().toString()));
        }});}


    public  int  productSale(int mRowId, int units){


        if (units>0){units--;
            ContentValues values = new ContentValues();
            values.put(ItemContract.ItemEntry.COLUMN_ITEM_UNITS,units);
            Uri currentProductUri = ContentUris.withAppendedId(ItemContract.ItemEntry.CONTENT_URI, mRowId);
            mRowsAffected= mContext.getContentResolver().update(currentProductUri,values,null,null);}
        return mRowsAffected;}}
