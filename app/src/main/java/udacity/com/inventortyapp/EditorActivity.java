package udacity.com.inventortyapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import udacity.com.inventortyapp.data.ItemContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_ITEM_LOADER=0;
    private Uri mCurrentItemUri;
    private EditText mNameEditText;
    private EditText mReferenceText;
    private Spinner mCategorySpinner;
    private EditText mPriceText;
    private EditText mUnitsText;
    private int mCategory= ItemContract.ItemEntry.CATEGORY_UNKNOWN;
    private boolean mItemHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;
            return false;}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_habit);

    Intent intent = getIntent();
    mCurrentItemUri = intent.getData();

        if (mCurrentItemUri ==null){setTitle("Add an Item");
        invalidateOptionsMenu();}else{
        setTitle("Ediat an Item");}
        getLoaderManager().initLoader(EXISTING_ITEM_LOADER,null,this);

        mNameEditText =(EditText) findViewById(R.id.edit_activity_name);
        mReferenceText=(EditText) findViewById(R.id.edit_bar_code);
        mCategorySpinner = (Spinner)findViewById(R.id.spinner_zone);
        mPriceText=       (EditText)findViewById(R.id.price);
        mUnitsText=       (EditText)findViewById(R.id.units);

        mNameEditText.setOnTouchListener(mTouchListener);
        mReferenceText.setOnTouchListener(mTouchListener);
        mCategorySpinner.setOnTouchListener(mTouchListener);
        mPriceText.setOnTouchListener(mTouchListener);
        mUnitsText.setOnTouchListener(mTouchListener);

        setupSpinner();  }

    private void setupSpinner(){
        ArrayAdapter kindSpinnerAdapter = ArrayAdapter.createFromResource(this,R.array.array_category_options,
        android.R.layout.simple_spinner_item);
        kindSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mCategorySpinner.setAdapter(kindSpinnerAdapter);
        mCategorySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String selection = (String) parent.getItemAtPosition(i);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.category_unknown))) {
                        mCategory = ItemContract.ItemEntry.CATEGORY_UNKNOWN;
                    } else if (selection.equals(getString(R.string.category_cleaners))) {
                        mCategory = ItemContract.ItemEntry.CATEGORY_CLEANERS;
                    } else if (selection.equals(getString(R.string.category_food))) {
                        mCategory = ItemContract.ItemEntry.CATEGORY_FOOD;
                    } else if (selection.equals(getString(R.string.category_pets))) {
                        mCategory = ItemContract.ItemEntry.CATEGORY_PETS;
                    } else if (selection.equals(getString(R.string.category_hygiene))) {
                        mCategory = ItemContract.ItemEntry.CATEGORY_HYGIENE;
                    } else if (selection.equals(getString(R.string.category_fruit))) {
                        mCategory = ItemContract.ItemEntry.CATEGORY_FRUIT;
                    } else {
                        mCategory = ItemContract.ItemEntry.CATEGORY_FISH;}}}

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mCategory = ItemContract.ItemEntry.CATEGORY_UNKNOWN;
            }




        });}}





