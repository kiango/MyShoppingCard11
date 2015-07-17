package com.example.torsh.myshoppingcard11;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/**
 * Class for querying shopping card table and show the selected items on a list
 */

public class ShoppingCardDAO {

    private static final String TABLE_NAME_SHOPPING_CARD = "shoppingCard";
    private static final String ITEM_ID = "item_id";
    private static final String ITEM_QUANTITY = "quantity";

    private Context context;
    private DBHelper dbHelper;
    private SQLiteDatabase db;


    String query_GetItemForCard =
            "SELECT itemTable._id, name, price, quantity, ROUND(price*quantity,1) 'subtotal' " +
                    "FROM itemTable, shoppingCard " +
                    "WHERE itemTable._id = shoppingCard.item_id";

    String query_TotalPurchase =
            "SELECT SUM (ROUND(price*quantity,1)) 'Total' " +
                    "FROM itemTable, shoppingCard " +
                    "WHERE itemTable._id = shoppingCard.item_id";


    public ShoppingCardDAO(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        openDb();
    }

    public void openDb() {
        db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
        //Toast.makeText(context, "openDB card called", Toast.LENGTH_SHORT).show();
    }

    public Cursor getSelectedItems(){
        Cursor cursor = db.rawQuery(query_GetItemForCard, null);
        //cursor.close();
        return cursor;
    }

    public void deleteItemFromCard(long itemID) {
        //Toast.makeText(context, "deleting: "+ itemID, Toast.LENGTH_SHORT).show();
        db.delete(TABLE_NAME_SHOPPING_CARD, ITEM_ID+" = "+itemID, null);
    }


    public void updateQuantity(long itemID, int qnt) {

        // store a set of values that the ContentValues can process
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_QUANTITY, qnt);
        //openDb();
        db.update(TABLE_NAME_SHOPPING_CARD, contentValues, ITEM_ID + "=" + itemID, null); // Works!
        //db.execSQL("UPDATE itemTable SET "+ITEM_NAME+"='"+itemName+"', "+PRICE+" = '"+itemPrice+"' WHERE " +ITEM_ID+ " = '"+itemID+"' "); // works!
    }


    public Cursor getTotalPurchase(){
        Cursor cursor_totalPurchase = db.rawQuery(query_TotalPurchase, null);
        return cursor_totalPurchase;
    }
}