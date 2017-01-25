package udacity.com.inventortyapp;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import udacity.com.inventortyapp.data.ItemContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = EditorActivity.class.getSimpleName();
    private static final int EXISTING_ITEM_LOADER = 0;
    private static final int PICK_IMAGE_REQUEST = 0;
    private static final String STATE_URI = "STATE_URI";
    private static final int SEND_MAIL_REQUEST = 1;
    private Uri mCurrentItemUri;
    private Uri mUriPhoto;
    private TextView mTextView;

    private ImageView mImageView;
    private ImageView mImageView2;
    private ImageView mOrderView;
    private EditText mNameEditText;
    private EditText mReferenceText;
    private Spinner mCategorySpinner;
    private EditText mPriceText;
    private EditText mUnitsText;
    private EditText mSupplierText;
    private EditText mEmailText;
    private int mCategory = ItemContract.ItemEntry.CATEGORY_UNKNOWN;
    private boolean mItemHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
   @Override public boolean onTouch(View view, MotionEvent motionEvent) {
            mItemHasChanged = true;  return false; }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_habit);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        if (mCurrentItemUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_item));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_edit_item));
            getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
        }

        mNameEditText = (EditText) findViewById(R.id.edit_activity_name);
        mReferenceText = (EditText) findViewById(R.id.edit_bar_code);
        mCategorySpinner = (Spinner) findViewById(R.id.spinner_zone);
        mPriceText = (EditText) findViewById(R.id.price);
        mUnitsText=(EditText)findViewById(R.id.units);
        mSupplierText = (EditText) findViewById(R.id.suplier);
        mEmailText = (EditText) findViewById(R.id.email);

        mNameEditText.setOnTouchListener(mTouchListener);
        mReferenceText.setOnTouchListener(mTouchListener);
        mCategorySpinner.setOnTouchListener(mTouchListener);
        mPriceText.setOnTouchListener(mTouchListener);
        mUnitsText.setOnTouchListener(mTouchListener);
        mSupplierText.setOnTouchListener(mTouchListener);
        mEmailText.setOnTouchListener(mTouchListener);




        mImageView = (ImageView) findViewById(R.id.photo); // Cámara de fotos
        mImageView2 = (ImageView) findViewById(R.id.productImage); // Prefoto seleccionada por la cámara
        mTextView = (TextView) findViewById(R.id.image_uri);  // Ruta de acceso

        mImageView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View view) { openImageSelector(); }});


        mOrderView = (ImageView) findViewById(R.id.action_sendmail);
        mOrderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {sendEmail(); }});

        setupSpinner();}

    private void setupSpinner() {
    ArrayAdapter kindSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_category_options,
    android.R.layout.simple_spinner_item);
    kindSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
    mCategorySpinner.setAdapter(kindSpinnerAdapter);
    mCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
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
                        mCategory = ItemContract.ItemEntry.CATEGORY_FISH; }}}
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mCategory = ItemContract.ItemEntry.CATEGORY_UNKNOWN;
            }});}

    private void saveItem() {
        String nameString = mNameEditText.getText().toString().trim();
        String referenceString = mReferenceText.getText().toString().trim();
        String priceString = mPriceText.getText().toString().trim();
        String unitString = mUnitsText.getText().toString().trim();
        String suplierString = mSupplierText.getText().toString().trim();
        String emailString = mEmailText.getText().toString().trim();

        if (mCurrentItemUri == null && TextUtils.isEmpty(nameString) && TextUtils.isEmpty(referenceString) &&
                TextUtils.isEmpty(priceString) && TextUtils.isEmpty(unitString) && TextUtils.isEmpty(suplierString)
                && TextUtils.isEmpty(emailString) && mCategory == ItemContract.ItemEntry.CATEGORY_UNKNOWN) {
            return;
        }


        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT, nameString);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_REFERENCE, referenceString);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_CATEGORY, mCategory);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_PRICE, priceString);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_UNITS, unitString);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_SUPLIER, suplierString);
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_EMAIL, emailString);

        int price = 0;
        if (!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_PRICE, price);

        int units = 0;
        if (!TextUtils.isEmpty(unitString)) {
            units = Integer.parseInt(unitString);
        }
        values.put(ItemContract.ItemEntry.COLUMN_ITEM_UNITS, units);

        if (mCurrentItemUri == null) {
            Uri newUri = getContentResolver().insert(ItemContract.ItemEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, getString(R.string.editor_insert_item_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_insert_item_successful), Toast.LENGTH_SHORT).show();
            }
        } else {

            Log.e(LOG_TAG, "Imprimiendo " + mCurrentItemUri);
            int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);
            if (rowsAffected == 0) {
                Toast.makeText(this, getString(R.string.editor_update_item_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_update_item_successful), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mUriPhoto != null) outState.putString(STATE_URI, mUriPhoto.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState.containsKey(STATE_URI) &&
                !savedInstanceState.getString(STATE_URI).equals("")) {
            mUriPhoto = Uri.parse(savedInstanceState.getString(STATE_URI));
            mTextView.setText(mUriPhoto.toString());
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentItemUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveItem();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!mItemHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    ;
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);

                return true;
        }
        int id = item.getItemId();
        if (id == R.id.action_sendmail) {
            sendEmail();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendEmail() {

        String[] projection = {
                ItemContract.ItemEntry.COLUMN_ITEM_REFERENCE,
                ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT,
                ItemContract.ItemEntry.COLUMN_ITEM_UNITS,
                ItemContract.ItemEntry.COLUMN_ITEM_SUPLIER,
                ItemContract.ItemEntry.COLUMN_ITEM_EMAIL};

    Cursor cursor = getContentResolver().query(mCurrentItemUri,projection,null,null,null);
    if (cursor.moveToFirst()){

    String productName = cursor.getString(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT));
    String productReference=((getString(R.string.email_message_name)) +" "+  cursor.getString(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_ITEM_REFERENCE)));
    String emailAddress = cursor.getString(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_ITEM_EMAIL));
    String productSubject= cursor.getString(cursor.getColumnIndexOrThrow(ItemContract.ItemEntry.COLUMN_ITEM_REFERENCE));
    String subject = (getString(R.string.orderSubject)+ " " + productSubject);
    String body= (getString(R.string.genericMessageBeggining)+ " " + productName +"  " +productReference + "\n" + "\n" + (getString(R.string.genericMessageMiddle)) + "\n"+ "\n" +(getString(R.string.genericMessageEnd)));

        writeEmail(emailAddress, subject, body);
        Log.e(LOG_TAG, "Item Sold button was pressed" + emailAddress);
    }}



    public void writeEmail(String emailAddress, String subject, String message) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, new String [] {emailAddress});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        if (intent.resolveActivity(getPackageManager()) != null) { startActivity(Intent.createChooser(intent, "Send Email")); } }


    @Override
    public void onBackPressed() {
        if (!mItemHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT,
                ItemContract.ItemEntry.COLUMN_ITEM_REFERENCE,
                ItemContract.ItemEntry.COLUMN_ITEM_CATEGORY,
                ItemContract.ItemEntry.COLUMN_ITEM_PRICE,
                ItemContract.ItemEntry.COLUMN_ITEM_UNITS,
                ItemContract.ItemEntry.COLUMN_ITEM_SUPLIER,
                ItemContract.ItemEntry.COLUMN_ITEM_EMAIL};


        return new android.content.CursorLoader(this,
                mCurrentItemUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {

            int productColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRODUCT);
            int referenceColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_REFERENCE);
            int categoryColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_CATEGORY);
            int priceColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRICE);
            int unitsColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_UNITS);
            int suplierColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PRICE);
            int emailColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_EMAIL);
            //int photoColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_ITEM_PHOTO);

            String product = cursor.getString(productColumnIndex);
            int reference = cursor.getInt(referenceColumnIndex);
            int category = cursor.getInt(categoryColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int units = cursor.getInt(unitsColumnIndex);
            String suplier = cursor.getString(suplierColumnIndex);
            String email = cursor.getString(emailColumnIndex);

            mNameEditText.setText(product);
            mReferenceText.setText(Integer.toString(reference));
            mPriceText.setText(Integer.toString(price));
            mUnitsText.setText(Integer.toString(units));
            mSupplierText.setText(suplier);
            mEmailText.setText(email);

            switch (category) {
                case ItemContract.ItemEntry.CATEGORY_UNKNOWN:
                    mCategorySpinner.setSelection(1);
                    break;
                case ItemContract.ItemEntry.CATEGORY_CLEANERS:
                    mCategorySpinner.setSelection(2);
                    break;
                case ItemContract.ItemEntry.CATEGORY_FOOD:
                    mCategorySpinner.setSelection(3);
                    break;
                case ItemContract.ItemEntry.CATEGORY_PETS:
                    mCategorySpinner.setSelection(4);
                    break;
                case ItemContract.ItemEntry.CATEGORY_HYGIENE:
                    mCategorySpinner.setSelection(5);
                    break;
                case ItemContract.ItemEntry.CATEGORY_FRUIT:
                    mCategorySpinner.setSelection(6);
                    break;
                case ItemContract.ItemEntry.CATEGORY_FISH:
                    mCategorySpinner.setSelection(7);
                    break;
            }
        }
    }

    public void openImageSelector() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                mUriPhoto = resultData.getData();
                Log.i(LOG_TAG, "Uri: " + mUriPhoto.toString());
                mTextView.setText(mUriPhoto.toString());
                mImageView2.setImageBitmap(getBitmapFromUri(mUriPhoto));
            }
        } else if (requestCode == SEND_MAIL_REQUEST && resultCode == Activity.RESULT_OK) {
        }
    }

    public Bitmap getBitmapFromUri(Uri uri) {
        if (uri == null || uri.toString().isEmpty())
            return null;
        int targetW = mImageView2.getWidth();
        int targetH = mImageView2.getHeight();

        InputStream input = null;

        try {
            input = this.getContentResolver().openInputStream(uri);

            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;


            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            input = this.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();
            return bitmap;

        } catch (FileNotFoundException fne) {
            Log.e(LOG_TAG, "Failed to load image.", fne);
            return null;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to load image.", e);
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException ioe) {
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEditText.setText("");
        mReferenceText.setText("");
        mPriceText.setText("");
        mUnitsText.setText("");
        mCategorySpinner.setSelection(0);
        mSupplierText.setText("");
        mEmailText.setText("");
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteItem();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem() {
        if (mCurrentItemUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_item_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.editor_delete_item_ok), Toast.LENGTH_SHORT).show();
            }}
        finish(); }

    }
