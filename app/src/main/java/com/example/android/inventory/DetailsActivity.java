package com.example.android.inventory;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;

import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventory.data.InventoryContract.footWearEntry;

import static android.R.attr.button;


public class DetailsActivity extends AppCompatActivity
       implements LoaderManager.LoaderCallbacks<Cursor> {

    /**********************************************************************************************
     *                                     GLOBAL VARIABLES
     *********************************************************************************************/
    public static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    /* Loader ID for PET_LOADER */
    private static final int FOOTWEAR_LOADER = 0;

    //Uri Identifier used to determine whether Adding or Updating Product
    private Uri currentUri;

    //Booblean for tracking if values on Edit fields have changed.
    private boolean mFootwearHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mFootwearHasChanged = true;
            return false;
        }
    };

    /*EditText field to enter the footwear's name*/
    private EditText mNameEditText;

    /*EditText field to enter the footwear's price*/
    private EditText mPriceEditText;

    /*TextView field to see available */
    private TextView mQuantityTextView;

    /*EditText field to enter the supplier's phone*/
    private EditText mPhoneEditText;

    /*EditText field to enter the supplier's email*/
    private EditText mEmailEditText;

    /*EditText field to enter the supplier's webpage*/
    private EditText mWebpageEditText;

    /*Button to increase amount of products*/
    private Button increaseProductButton ;

    /*Button to decrease amount of products*/
    private Button decreaseProductButton ;

    /* Global variable to track the quantity */
    int pieceQuantity ;

    /*Image button to call supplier */
    private ImageButton mButtonCall;

    /*************************** Pending for Foto / Peek *****************************************/

    /***********************************************************************************************
     *                            Loader Callback Methods
     **********************************************************************************************/

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        if (currentUri != null) {
            // Since the editor shows all pet attributes, define a projection that contains
            // all columns from the pet table
            String[] projection = {
                    footWearEntry._ID,
                    footWearEntry.COLUMN_FOOTWEAR_NAME,
                    footWearEntry.COLUMN_FOOTWEAR_PRICE,
                    footWearEntry.COLUMN_FOOTWEAR_QUANTITY,
                    footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_PHONE,
                    footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_EMAIL,
                    footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_WEBPAGE};
            // Pending Foto / Image column

            Log.i(LOG_TAG, "TEST : Uri return cursor");
            // This loader will execute the ContentProvider's query method on a background thread
            return new CursorLoader(this,   // Parent activity context
                    currentUri,         // Query the content URI for the current product
                    projection,             // Columns to include in the resulting Cursor
                    null,                   // No selection clause
                    null,                   // No selection arguments
                    null);                  // Default sort order
        }

        Log.i(LOG_TAG, "TEST : Uri return null");
        return null;
    }

    @Override
    public void onLoaderReset(Loader loader) {

        Log.i(LOG_TAG, "TEST : OnLoaderReset() ");
        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityTextView.setText("");
        mEmailEditText.setText("");
        mPhoneEditText.setText("");
        mWebpageEditText.setText("");
        /* Image picture setTExt */

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            Log.i(LOG_TAG, "TEST : onLoadFinished() cursor null or < 1");
            return;
        }


        if (cursor.moveToFirst()) {
            Log.i(LOG_TAG, "TEST : OnLoadFinished() Uri retrieve data");

            // Find the columns of footwear attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(footWearEntry.COLUMN_FOOTWEAR_NAME);
            int priceColumnIndex = cursor.getColumnIndex(footWearEntry.COLUMN_FOOTWEAR_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(footWearEntry.COLUMN_FOOTWEAR_QUANTITY);
            int phoneColumnIndex = cursor.getColumnIndex(footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_PHONE);
            int emailColumnIndex = cursor.getColumnIndex(footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_EMAIL);
            int webpageColumnIndex = cursor.getColumnIndex(footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_WEBPAGE);

            // Extract out the value from the Cursor for the given column index
            String pieceName = cursor.getString(nameColumnIndex);
            int piecePrice = cursor.getInt(priceColumnIndex);
            pieceQuantity = cursor.getInt(quantityColumnIndex);
            String supplierPhone = cursor.getString(phoneColumnIndex);
            String supplierEmail = cursor.getString(emailColumnIndex);
            String supplierWebpage = cursor.getString(webpageColumnIndex);
            /* Pending Image String */

            //Set the retrieved info from the pets table in the editTexts
            mNameEditText.setText(pieceName);
            mPriceEditText.setText(String.valueOf(piecePrice));
            mQuantityTextView.setText(String.valueOf(pieceQuantity));
            mPhoneEditText.setText(supplierPhone);
            mEmailEditText.setText(supplierEmail);
            mWebpageEditText.setText(supplierWebpage);
            /*Pending image setText */

        }

    }

    /***********************************************************************************************
     *                            OnCreate Method()
     **********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Find all relevant views that we will need to read user input from
        /* Product info */
        mNameEditText = (EditText) findViewById(R.id.edit_footwear_name);
        mPriceEditText = (EditText) findViewById(R.id.edit_footwear_price);
        mQuantityTextView = (TextView) findViewById(R.id.tv_quantity);
        increaseProductButton = (Button) findViewById(R.id.button_plus);
        decreaseProductButton = (Button) findViewById(R.id.button_minus);

        /* Supplier Info */
        mEmailEditText = (EditText) findViewById(R.id.edit_supplier_email);
        mPhoneEditText = (EditText) findViewById(R.id.edit_supplier_phone);
        mWebpageEditText = (EditText) findViewById(R.id.edit_supplier_web_page);

        /* Call supplier */
        mButtonCall = (ImageButton) findViewById(R.id.ib_call) ;

        /** PENDING FOTO IMAGE ***********/

        //Register the edit Fields with the mTouchListener
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityTextView.setOnTouchListener(mTouchListener);
        mEmailEditText.setOnTouchListener(mTouchListener);
        mPhoneEditText.setOnTouchListener(mTouchListener);
        mWebpageEditText.setOnTouchListener(mTouchListener);

        /* Get the URI from the CatalogActivity  */
        Intent intent = getIntent();
        currentUri = intent.getData();

        if (currentUri == null) {

            setTitle(getString(R.string.editor_activity_title_new_product));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a product that hasn't been created yet.)
            invalidateOptionsMenu();

        } else {

            Log.i(LOG_TAG, "TEST: " + currentUri.toString());
            setTitle(R.string.editor_activity_title_edit_product);
            Log.i(LOG_TAG, "Test" + currentUri.toString());

        }

        /*Initialize the loader  */
        getLoaderManager().initLoader(FOOTWEAR_LOADER, null, this);

        /* Click Listener for the increase button */
        increaseProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pieceQuantity = pieceQuantity + 1 ;
                mQuantityTextView.setText(String.valueOf(pieceQuantity));
            }
        });

          /* Click Listener for the decrease button */
        decreaseProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pieceQuantity > 0){
                    pieceQuantity = pieceQuantity - 1 ;
                }else{
                    pieceQuantity = 0 ;
                }

                mQuantityTextView.setText(String.valueOf(pieceQuantity));
            }
        });

        mButtonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Retrieve the phone number saved on the db from the PhoneEditText*/
                String phoneToCall = mPhoneEditText.getText().toString();

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phoneToCall));
                startActivity(intent);

            }
        });

    }

    /***********************************************************************************************
     *                         OPTIONS MENU RELATED METHODS
     **********************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                //Save data
                saveProduct();
                //Exit Activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
           /* This case is only accesible if the Activity is on edit mode but not on add mode */
            case R.id.action_delete:
                //add strng for product deleted
                showDeleteConfirmationDialog();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the product hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mFootwearHasChanged) {
                    NavUtils.navigateUpFromSameTask(DetailsActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(DetailsActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new product, hide the "Delete" menu item.
        if (currentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    /**********************************************************************************************
     *                                      General Methods
     *********************************************************************************************/

    private void saveProduct() {

        String footwearName = mNameEditText.getText().toString().trim();
        String footwearPrice = mPriceEditText.getText().toString().trim();
        String footwearQuantity = mQuantityTextView.getText().toString();
        String supplierPhone = mPhoneEditText.getText().toString().trim();
        String supplierEmail = mEmailEditText.getText().toString().trim();
        String supplierWebpage = mWebpageEditText.getText().toString().trim();

        if (currentUri == null && TextUtils.isEmpty(footwearName) && TextUtils.isEmpty(footwearPrice)
                && TextUtils.isEmpty(footwearQuantity) && TextUtils.isEmpty(supplierPhone)
                && TextUtils.isEmpty(supplierEmail) && TextUtils.isEmpty(supplierWebpage)) {
            //Finish the activity if all fields are empty.
            //finish();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(footWearEntry.COLUMN_FOOTWEAR_NAME, footwearName);
        values.put(footWearEntry.COLUMN_FOOTWEAR_PRICE, footwearPrice);
        values.put(footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_PHONE, supplierPhone);
        values.put(footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_EMAIL, supplierEmail);
        values.put(footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_WEBPAGE, supplierWebpage);

        //MAybe do the same for price !

        int quantity = 0;
        if (!TextUtils.isEmpty(footwearQuantity)) {
            quantity = Integer.parseInt(footwearQuantity);
        }

        values.put(footWearEntry.COLUMN_FOOTWEAR_QUANTITY, quantity);

        // Condition Add a product
        if (currentUri == null) {
            // Insert a new product into the provider, returning the content URI for the new product.
            Uri newUri = getContentResolver().insert(footWearEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }

        } else {

            // Otherwise this is an EXISTING product, so update the product with content URI: mCurrentUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentUri will already identify the correct row in the database that
            // we want to modify
            int rowsAffected = getContentResolver().update(
                    currentUri, // the user product content URI
                    values,               // the columns to update
                    null,
                    null);

            //Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_pet_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_pet_successful),
                        Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the product.
                deleteProduct();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the product.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        // If the product hasn't changed, continue with handling back button press
        if (!mFootwearHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the product.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the product in the database.
     */
    private void deleteProduct() {
        // Deletes the product that match the selection criteria
        // Defines a variable to contain the number of rows deleted

        int mRowsDeleted = getContentResolver().delete(
                currentUri,   // the user dictionary content URI
                null,            // the column to select on
                null             // the value to compare to
        );

        // Show a toast message depending on whether or not the delete was successful
        if (mRowsDeleted == 0 ){
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, R.string.editor_delete_pet_failed,
                    Toast.LENGTH_SHORT ).show();
        }else{
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, R.string.editor_delete_pet_successful,
                    Toast.LENGTH_SHORT ).show();
        }

        //Close the activity after deleting
        finish();

    }

}

