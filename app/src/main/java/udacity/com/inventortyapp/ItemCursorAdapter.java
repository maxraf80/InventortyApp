package udacity.com.inventortyapp;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import udacity.com.inventortyapp.data.ItemContract;

public class ItemCursorAdapter extends CursorAdapter{
    public ItemCursorAdapter(Context context, Cursor c){super(context,c,0);}

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView referenceTextView = (TextView) view.findViewById(R.id.reference);
        TextView categoryTextView = (TextView) view.findViewById(R.id.category);
        TextView priceTextView = (TextView) view.findViewById(R.id.price);
        TextView unitsTextView = (TextView) view.findViewById(R.id.units);


        int nameColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT);
        int referenceColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_REFERENCE);
        int categoryColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_CATEGORY);
        int priceColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRICE);
        int unitsColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_UNITS);

        String name =cursor.getString(nameColumnIndex);
        String reference = cursor.getString(referenceColumnIndex);
        String category =cursor.getString(categoryColumnIndex);
        String price =cursor.getString(priceColumnIndex);
        String units =cursor.getString(unitsColumnIndex);

        if (TextUtils.isEmpty(reference)){reference = "Unknown";}
        if (TextUtils.isEmpty(category)){category = "Unknown";}
        if (TextUtils.isEmpty(price)){price = "0";}
        if (TextUtils.isEmpty(units)){units = "0";}

        nameTextView.setText(name);
        referenceTextView.setText(reference);
        categoryTextView.setText(category);
        priceTextView.setText(price);
        unitsTextView.setText(units);




    }
}
