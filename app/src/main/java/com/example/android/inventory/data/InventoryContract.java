package com.example.android.inventory.data;

/**
 * Created by Paviliondm4 on 4/6/2017.
 */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the inventory database.
 */
public final class InventoryContract {

    /* Empty constructor to prevent an accidental instantiating */
    private InventoryContract() {}

    /* Scheme */

    /* Content authority */
    public static final String CONTENT_AUTHORITY = "com.example.android.inventory";

    /* Path table name */
    public static final String PATH_FOOTWEAR = "footwear";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /* Inner class that defines the table contents of the footwear table */
    public static abstract class footWearEntry implements BaseColumns{

        /** The content URI to access the footwear data in the provider */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FOOTWEAR);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of footwear.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FOOTWEAR;

        /**
         * The MIME type of the {@link #CONTENT_URI} for a single footwear piece.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FOOTWEAR;

        /* Constants of the fields from the footwear table */

        // Table name
        public static final String TABLE_NAME = "footwear" ;

        //Id of the footwear piece
        public static final String _ID = BaseColumns._ID ;

    /* Product name, quantity and price must be shown in order to fullfill the project req. */

        //Footwear´s product name
        public static final String COLUMN_FOOTWEAR_NAME = "name" ;
        //Footwear´s price
        public static final String COLUMN_FOOTWEAR_PRICE = "price" ;
        //Amount of availabe pieces
        public static final String COLUMN_FOOTWEAR_QUANTITY = "quantity" ;
        //Image of the footwear piece
        public static final String COLUMN_FOOTWEAR_IMAGE = "image" ;
        //Sold pieces
        public static final String COLUMN_FOOTWEAR_SOLD_ITEMS  = "sold" ;

        //Supplier´s phone
        public static final String COLUMN_FOOTWEAR_SUPPLIER_PHONE = "phone" ;
        //Supplier´s webpage
        public static final String COLUMN_FOOTWEAR_SUPPLIER_WEBPAGE = "webpage" ;
        //Supplier´s email
        public static final String COLUMN_FOOTWEAR_SUPPLIER_EMAIL = "email" ;

     }

}


