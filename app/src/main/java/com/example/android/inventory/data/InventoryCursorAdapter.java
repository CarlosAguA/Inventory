package com.example.android.inventory.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.inventory.R;

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

        // Find fields to populate in inflated template
        TextView tv_name = (TextView) view.findViewById(R.id.product_name);

        TextView tv_quantity = (TextView) view.findViewById(R.id.quantity);

        TextView tv_price = (TextView) view.findViewById(R.id.price) ;

        // Extract properties from cursor
        String piece_name = cursor.getString(cursor.getColumnIndexOrThrow
                (InventoryContract.footWearEntry.COLUMN_FOOTWEAR_NAME));

        String piece_quantity = cursor.getString(cursor.getColumnIndexOrThrow
                (InventoryContract.footWearEntry.COLUMN_FOOTWEAR_QUANTITY));

        String piece_price = cursor.getString(cursor.getColumnIndexOrThrow
                (InventoryContract.footWearEntry.COLUMN_FOOTWEAR_PRICE));

        //Show the information in the TextViews of the list Item.
        tv_name.setText(piece_name);

        tv_quantity.setText(piece_quantity);

        tv_price.setText(piece_price);

    }
}
