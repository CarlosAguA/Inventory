package com.example.android.inventory.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.inventory.R;

import static android.R.attr.order;

/**
 * Created by Paviliondm4 on 4/9/2017.
 */


/** {@link InventoryCursorAdapter} is an adapter for a list or grid view
        * that uses a {@link Cursor} of pet data as its data source. This adapter knows
        * how to create list items for each row of pet data in the {@link Cursor}.
        */
public class InventoryCursorAdapter extends CursorAdapter{

    public InventoryCursorAdapter (Context context, Cursor cursor){
        super(context, cursor, 0 );

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        /* Return the listItemView */

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        /*****************************************************************************************
         *                       Cast of the elements from the list_item
         *****************************************************************************************/
        // Find fields to populate in inflated template
        TextView tv_name = (TextView) view.findViewById(R.id.product_name); // Product´s name

        TextView tv_price = (TextView) view.findViewById(R.id.price) ; // Product´s price

        TextView tv_quantity = (TextView) view.findViewById(R.id.quantity); // Product´s availabe quantity

        TextView tv_sold = (TextView) view.findViewById(R.id.tv_sold); // Sold products

        //Find Image view in order to track when the sale button is clicked.
        ImageView saleButton = (ImageView) view.findViewById(R.id.im_view_sale_button); // Shop button

        /*****************************************************************************************
         *                  Get the Indices and values from the footwear table
         *****************************************************************************************/
      // Extract properties from cursor
        String piece_name = cursor.getString(cursor.getColumnIndexOrThrow
                (InventoryContract.footWearEntry.COLUMN_FOOTWEAR_NAME));

        final String piece_quantity = cursor.getString(cursor.getColumnIndexOrThrow
                (InventoryContract.footWearEntry.COLUMN_FOOTWEAR_QUANTITY));

        String piece_price = cursor.getString(cursor.getColumnIndexOrThrow
                (InventoryContract.footWearEntry.COLUMN_FOOTWEAR_PRICE));

        String sold_pieces = cursor.getString(cursor.getColumnIndexOrThrow
               (InventoryContract.footWearEntry.COLUMN_FOOTWEAR_SOLD_ITEMS));

        /*****************************************************************************************
         *               Populate the values from the footwear table on the view
         *****************************************************************************************/

      //Show the information in the TextViews of the list Item.
        tv_name.setText(piece_name);

        tv_quantity.setText(piece_quantity);

        tv_price.setText( "$" + piece_price);

        //tv_sold.setText(sold_pieces);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i ("Adapter ", "Test: " + piece_quantity ) ;

                // Instantiate Content Values object
                // PutContent value for quantity
                // Put content value for sold
                // Update


            }
        });



    }
}
