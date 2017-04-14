package com.example.android.inventory.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.Nullable;
import android.util.Log;

import java.net.URI;
import java.security.Provider;

import static android.icu.util.MeasureUnit.FOOT;
import static com.example.android.inventory.data.InventoryContract.CONTENT_AUTHORITY;

import com.example.android.inventory.data.InventoryContract.footWearEntry;

/**
 * Created by Paviliondm4 on 4/9/2017.
 */

public class InventoryProvider extends ContentProvider{

    private InventoryDbHelper mDbHelper ;

    /** URI matcher code for the content URI for the footwear table */
    private static final int FOOTWEAR = 100;

    /** URI matcher code for the content URI for a single product in the footwear table */
    private static final int FOOTWEAR_ID = 101;

    /** Tag for the log messages */
    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_FOOTWEAR + "/#", FOOTWEAR_ID);
        sUriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_FOOTWEAR , FOOTWEAR);

    }

    @Override
    public boolean onCreate() {
        // Make sure the variable is a global variable, so it can be referenced from other
        // ContentProvider methods.
        mDbHelper = new InventoryDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

         /*  Get database object */
        SQLiteDatabase database = mDbHelper.getReadableDatabase() ;

        Cursor cursor ;

        /* Help us find out what kind of input uri is passing to us */
        int match =  sUriMatcher.match(uri) ;

        switch (match){
            case FOOTWEAR:
                // use the query() method to retrieve at least one column of data.
                // For the FOOTWEAR code, query the footwear table directly with the given
                // projection, selection, selection arguments, and sort order. The cursor
                // could contain multiple rows of the footwear table.
                cursor = database.query(
                        footWearEntry.TABLE_NAME,
                        projection,
                        selection,        //all columns
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break ;
            case FOOTWEAR_ID:
                // SQLITE statement: SELECT id, name FROM footwear WHERE _id=5 ;
                selection = footWearEntry._ID + "=?" ;
                /*Projection :{ "_id" , "name" } */
                //Just using id and name for query given by the programmer
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))} ;
                Log.i(LOG_TAG, "Test: provider" + uri + selection)  ;
                cursor = database.query(
                        footWearEntry.TABLE_NAME,  //FROM
                        projection,   //SELECT
                        selection,    //WHERE
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown: " + uri);
        }

        //Register to watch a content URI for changes.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor ;

    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FOOTWEAR:
                return insertFootwear(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertFootwear(Uri uri, ContentValues values) {

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new piece with the given values
        long id = database.insert(footWearEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FOOTWEAR:
                return footWearEntry.CONTENT_LIST_TYPE;
            case FOOTWEAR_ID:
                return footWearEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FOOTWEAR:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case FOOTWEAR_ID:
                // For the FOOTWEAR_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = footWearEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link footwearEntry#COLUMN_FOOTWEAR_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(footWearEntry.COLUMN_FOOTWEAR_NAME)) {
            String name = values.getAsString(footWearEntry.COLUMN_FOOTWEAR_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Footwear requires a name");
            }
        }

        // If the {@link Entry#COLUMN_FOOTWEAR_QUANTITY} key is present,
        // check that the quantity value is valid.
        if (values.containsKey(footWearEntry.COLUMN_FOOTWEAR_QUANTITY)) {
            // Check that the quantity is greater than or equal to 0 pieces
            Integer quantity = values.getAsInteger(footWearEntry.COLUMN_FOOTWEAR_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Footwear requires valid quantity");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(footWearEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated ;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Track the number of rows that were deleted
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FOOTWEAR:

                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(footWearEntry.TABLE_NAME, selection, selectionArgs);

                // If 1 or more rows were deleted, then notify all listeners that the data at the
                // given URI has changed
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsDeleted ;

            case FOOTWEAR_ID:

                selection = footWearEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // Delete a single row given by the ID in the URI
                rowsDeleted = database.delete(footWearEntry.TABLE_NAME, selection, selectionArgs);

                // If 1 or more rows were deleted, then notify all listeners that the data at the
                // given URI has changed
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsDeleted ;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }
}
