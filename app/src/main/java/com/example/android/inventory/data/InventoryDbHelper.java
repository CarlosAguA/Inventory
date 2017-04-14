package com.example.android.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.inventory.data.InventoryContract.footWearEntry;



/**
 * Created by Paviliondm4 on 4/6/2017.
 */

  /*Database helper for Inventory app. Manages database creation and version management.*/
public class InventoryDbHelper extends SQLiteOpenHelper{

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    public static final int DATABASE_VERSION = 1;

    /** Name of the database file */
    public static final String DATABASE_NAME = "clothes" ;

    /**
     * Constructs a new instance of {@link InventoryDbHelper}.
     *
     * @param context of the app
     */
    public InventoryDbHelper(Context context){
        super(context, DATABASE_NAME , null , DATABASE_VERSION);

    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create a String that contains the SQL statement to create the footwear table
        String SQL_ENTRIES_FOOTWEAR_TABLE =
                "CREATE TABLE "
                        + footWearEntry.TABLE_NAME + " (" + footWearEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + footWearEntry.COLUMN_FOOTWEAR_NAME + " TEXT NOT NULL, "
                        + footWearEntry.COLUMN_FOOTWEAR_PRICE + " INTEGER NOT NULL , "
                        + footWearEntry.COLUMN_FOOTWEAR_QUANTITY + " INTEGER NOT NULL DEFAULT 0 , "
                        + footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_EMAIL + " TEXT , "
                        + footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_PHONE + " TEXT , "
                        + footWearEntry.COLUMN_FOOTWEAR_SUPPLIER_WEBPAGE + " TEXT , "
                        + footWearEntry.COLUMN_FOOTWEAR_SOLD_ITEMS  + " INTEGER NOT NULL DEFAULT 0 , "
                        + footWearEntry.COLUMN_FOOTWEAR_IMAGE + " TEXT NOT NULL );" ;

        // Execute the SQL statement
        db.execSQL(SQL_ENTRIES_FOOTWEAR_TABLE);

    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

}
/*
SOURCES
Image as a blob or as an integer ?
 1. How to store(bitmap image) and retrieve image from sqlite database in android? [closed]
 http://stackoverflow.com/questions/11790104/how-to-storebitmap-image-and-retrieve-image-from-sqlite-database-in-android
 */