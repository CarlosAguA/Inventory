package com.example.android.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.android.inventory.data.InventoryCursorAdapter;
import com.example.android.inventory.data.InventoryContract.footWearEntry;

import static android.R.attr.id;
import static android.R.id.message;


public class CatalogActivity extends AppCompatActivity implements
        android.app.LoaderManager.LoaderCallbacks<Cursor>{

    InventoryCursorAdapter inventoryAdapter ;

    private static final int FOOTWEAR_LOADER = 0 ;

    /***********************************************************************************************
     *                            Loader Callback Methods
     **********************************************************************************************/
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        inventoryAdapter.swapCursor(cursor);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                footWearEntry._ID,
                footWearEntry.COLUMN_FOOTWEAR_NAME,
                footWearEntry.COLUMN_FOOTWEAR_PRICE,
                footWearEntry.COLUMN_FOOTWEAR_QUANTITY,
                footWearEntry.COLUMN_FOOTWEAR_SOLD_ITEMS};

        //This loader will execute the Content´s provider query method on a background thread.
        return new CursorLoader(this, footWearEntry.CONTENT_URI, projection,
                null, null, null) ;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        inventoryAdapter.swapCursor(null);

    }

    /***********************************************************************************************
     *                            OnCreate Method()
     **********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, DetailsActivity.class);
                intent.putExtra("fab_button", message);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the product data
        ListView inventoryListView = (ListView) findViewById(R.id.lv);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        inventoryListView.setEmptyView(emptyView);

        //Comment
        inventoryAdapter = new InventoryCursorAdapter(this, null) ;

        //Comment
        inventoryListView.setAdapter(inventoryAdapter);

        //Kick off loader
        getLoaderManager().initLoader(FOOTWEAR_LOADER, null, this) ;

        /*
         * Method for opening the Details Activity - Product Info
         */
        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                //Add coment / description
                Uri currentUri = ContentUris.withAppendedId(footWearEntry.CONTENT_URI, id);

                //Open the editor Activity. and set the uri on the data field of the intent.
                Intent intent = new Intent
                        (CatalogActivity.this, DetailsActivity.class).setData(currentUri);
                intent.putExtra("list_item", message);
                startActivity(intent);

            }
        });
        
    }
    /**********************************************************************************************
     *                                      General Methods
     *********************************************************************************************/
    }

