package com.example.android.inventory;

import android.Manifest;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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

import static android.R.attr.name;


public class DetailsActivity extends AppCompatActivity
       implements LoaderManager.LoaderCallbacks<Cursor> {

    /**********************************************************************************************
     *                                     GLOBAL VARIABLES
     *********************************************************************************************/

    /* Loader ID for FOOTWEAR_LOADER */
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
     Button increaseProductButton ;
    /*Button to decrease amount of products*/
     Button decreaseProductButton ;
    /* Global variable to track the quantity */
     int pieceQuantity ;
    /*Image button to call supplier */
     ImageButton mButtonCall;
    /* Open image from gallery */
     ImageButton mbuttonPhoto;
    /* integer to get Activity result when choosing image from gallery*/
     private static int RESULT_LOAD_IMG = 1;
    /* Global image Uri assigned when uri from a image in gallery is retrieved*/
     Uri imageUri ;
    /* ImageView to populate product image */
     ImageView mProductImageView ;
    /* Text view for saying the user about image button state*/
     TextView mFotoTextView ;


    /***********************************************************************************************
     *                            Loader Callback Methods
     **********************************************************************************************/
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {

        if (currentUri != null) {
            // Since the editor shows all footwear attributes, define a projection that contains
            // all columns from the footwear table
            String[] projection = {
                    footWearEntry._ID,
                    footWearEntry.COLUMN_FOOTWEAR_NAME,
                    footWearEntry.COLUMN_FOOTWEAR_PRICE,
                    footWearEntry.COLUMN_FOOTWEAR_QUANTITY,
                    footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_PHONE,
                    footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_EMAIL,
                    footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_WEBPAGE,
                    footWearEntry.COLUMN_FOOTWEAR_IMAGE};

            // This loader will execute the ContentProvider's query method on a background thread
            return new CursorLoader(this,   // Parent activity context
                    currentUri,         // Query the content URI for the current product
                    projection,             // Columns to include in the resulting Cursor
                    null,                   // No selection clause
                    null,                   // No selection arguments
                    null);                  // Default sort order
        }

        return null;
    }

    @Override
    public void onLoaderReset(Loader loader) {

        mNameEditText.setText("");
        mPriceEditText.setText("");
        mQuantityTextView.setText("");
        mEmailEditText.setText("");
        mPhoneEditText.setText("");
        mWebpageEditText.setText("");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        if (cursor.moveToFirst()) {

            // Find the columns of footwear attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex(footWearEntry.COLUMN_FOOTWEAR_NAME);
            int priceColumnIndex = cursor.getColumnIndex(footWearEntry.COLUMN_FOOTWEAR_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(footWearEntry.COLUMN_FOOTWEAR_QUANTITY);
            int phoneColumnIndex = cursor.getColumnIndex(footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_PHONE);
            int emailColumnIndex = cursor.getColumnIndex(footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_EMAIL);
            int webpageColumnIndex = cursor.getColumnIndex(footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_WEBPAGE);
            int imageColumnIndex = cursor.getColumnIndex(footWearEntry.COLUMN_FOOTWEAR_IMAGE) ;

            // Extract out the value from the Cursor for the given column index
            String pieceName = cursor.getString(nameColumnIndex);
            int piecePrice = cursor.getInt(priceColumnIndex);
            pieceQuantity = cursor.getInt(quantityColumnIndex);
            String supplierPhone = cursor.getString(phoneColumnIndex);
            String supplierEmail = cursor.getString(emailColumnIndex);
            String supplierWebpage = cursor.getString(webpageColumnIndex);
            imageUri= Uri.parse(cursor.getString(imageColumnIndex) ) ;

            //Set the retrieved info from the footwear table in the editTexts
            mNameEditText.setText(pieceName);
            mPriceEditText.setText(String.valueOf(piecePrice));
            mQuantityTextView.setText(String.valueOf(pieceQuantity));
            mPhoneEditText.setText(supplierPhone);
            mEmailEditText.setText(supplierEmail);
            mWebpageEditText.setText(supplierWebpage);
            mProductImageView.setImageURI(imageUri);

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

        /*Product Image */
        mbuttonPhoto = (ImageButton) findViewById(R.id.ib_photo);
        mProductImageView = (ImageView) findViewById(R.id.iv_product);
        mFotoTextView = (TextView) findViewById(R.id.tv_foto);

        //Register the edit Fields with the mTouchListener
        mNameEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mQuantityTextView.setOnTouchListener(mTouchListener);
        mEmailEditText.setOnTouchListener(mTouchListener);
        mPhoneEditText.setOnTouchListener(mTouchListener);
        mWebpageEditText.setOnTouchListener(mTouchListener);

        //Register the Buttons with the mTouchListener
        increaseProductButton.setOnTouchListener(mTouchListener);
        decreaseProductButton.setOnTouchListener(mTouchListener);

        /* Get the URI from the CatalogActivity  */
        Intent intent = getIntent();
        currentUri = intent.getData();

        if (currentUri == null) {

            setTitle(getString(R.string.editor_activity_title_new_product));

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a product that hasn't been created yet.)
            invalidateOptionsMenu();

        } else {

            setTitle(R.string.editor_activity_title_edit_product);

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

        /* Click Listener for Calling Supplier button - Order more */
        mButtonCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Retrieve the phone number saved on the db from the PhoneEditText*/
                String phoneToCall = mPhoneEditText.getText().toString();

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneToCall));
                startActivity(intent);

            }
        });

        /*Click Listener for Adding product image */
        mbuttonPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isStoragePermissionGranted()){
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    // Start the Intent
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                }else{
                    mbuttonPhoto.setEnabled(false);
                }
            }
        });

    }
    /***********************************************************************************************
     *              ACCESING GALLERY AND CHECKING PERMISSIONS METHODS
     **********************************************************************************************/
    /*
     * Get the result from opening the image gallery if a image is chosen and set it on an ImageView
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {
            imageUri = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(imageUri,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
                // Set the Image in ImageView after decoding the String
                mProductImageView.setImageBitmap(BitmapFactory
                        .decodeFile(picturePath));
        }
    }

    /*  Method for  requesting permission and check out the SDK Version */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //resume tasks needing this permission
            // permission was granted, yay! Do the
            // read-related task you need to do.
            // Create intent to Open Image applications like Gallery, Google Photos
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            // Start the Intent
            startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
        }else{
            mbuttonPhoto.setVisibility(View.GONE);
            mFotoTextView.setText(R.string.denied_access_gallery);
        }
    }

    /* Method boolean to check if permission was granted, denied or is already granted */
    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else {
            //permission is automatically granted on sdk<23 upon installation
            return true;
        }
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

        String productImage ;
        if(imageUri != null){

             productImage = imageUri.toString();

        }else{
            productImage = "android.resource://com.example.android.inventory/drawable/no_image_placeholder" ;
        }

        if (currentUri == null && TextUtils.isEmpty(footwearName) && TextUtils.isEmpty(footwearPrice)
                && TextUtils.isEmpty(footwearQuantity) && TextUtils.isEmpty(supplierPhone)
                && TextUtils.isEmpty(supplierEmail) && TextUtils.isEmpty(supplierWebpage)) {
            return;
        }

        if( TextUtils.isEmpty(footwearName) ){
            displayToast("Name Field is required.");
            return;
        }

        ContentValues values = new ContentValues();
        values.put(footWearEntry.COLUMN_FOOTWEAR_NAME, footwearName);
        values.put(footWearEntry.COLUMN_FOOTWEAR_PRICE, footwearPrice);
        values.put(footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_PHONE, supplierPhone);
        values.put(footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_EMAIL, supplierEmail);
        values.put(footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_WEBPAGE, supplierWebpage);
        values.put(footWearEntry.COLUMN_FOOTWEAR_IMAGE,productImage);

        int quantity = 1;
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
                displayToast(getString(R.string.editor_insert_product_failed) ) ;
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                displayToast(getString(R.string.editor_insert_product_successful)) ;
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
                Toast.makeText(this, getString(R.string.editor_update_product_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_product_successful),
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
     * Perform product deleting in the database.
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
            Toast.makeText(this, R.string.editor_delete_product_failed,
                    Toast.LENGTH_SHORT ).show();
        }else{
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, R.string.editor_delete_product_successful,
                    Toast.LENGTH_SHORT ).show();
        }

        //Close the activity after deleting
        finish();

    }

    private void displayToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}

