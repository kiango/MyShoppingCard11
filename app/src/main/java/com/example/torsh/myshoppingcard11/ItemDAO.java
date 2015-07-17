package com.example.torsh.myshoppingcard11;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

/**
 * In this class we query the created tables for
 * insert (insertItem), select (getItems) and update (updateItem)
 */

public class ItemDAO {

    private static final String TABLE_NAME_ITEMS = "itemTable";
    private static final String TABLE_NAME_SHOPPING_CARD = "shoppingCard";
    private static final String ITEM_ID = "_id";
    private static final String ITEM_NAME = "name";
    private static final String PRICE = "price";
    private static final String QUANTITY = "quantity";
    private static final String ITEM_ID_SHOPPING_CARD = "item_id";

    private DBHelper dbHelper;
    public SQLiteDatabase db;
    private Context context;

    public ItemDAO(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        openDb();
    }

    public void openDb() {
        db = dbHelper.getWritableDatabase();
        dbHelper.onCreate(db);
        //Toast.makeText(context, "openDB called", Toast.LENGTH_SHORT).show();
    }

    public void closeDB() {
        dbHelper.close();
    }


    public long insertItem(String itemName, int itemPrice) {

        // store a set of values that the ContentValues can process
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_NAME, itemName);
        contentValues.put(PRICE, itemPrice); // check Type int vs string!
        long id = db.insert(TABLE_NAME_ITEMS, null, contentValues);
        //Toast.makeText(context, "inserted id:  " + ((int) id), Toast.LENGTH_SHORT).show();

        return id;
    }


    public void updateItem(String itemName, int itemPrice, String itemID) {

        // store a set of values that the ContentValues can process
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_NAME, itemName);
        contentValues.put(PRICE, itemPrice); // check Type int vs string!
        //openDb();
        db.update("itemTable", contentValues, ITEM_ID + "=" + itemID, null); // Works!
        //db.execSQL("UPDATE itemTable SET "+ITEM_NAME+"='"+itemName+"', "+PRICE+" = '"+itemPrice+"' WHERE " +ITEM_ID+ " = '"+itemID+"' "); // works!
    }



    public void deleteItem(long itemID) {
        //Toast.makeText(context, "Deleting: "+ itemID, Toast.LENGTH_SHORT).show();
        db.delete(TABLE_NAME_ITEMS, ITEM_ID+" = "+itemID, null);
    }



    // ToDo: large load of data ?
    public Cursor getItems(){
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT _id, name, price FROM itemTable", null);
            //cursor = db.query("itemTable", new String[] {"_id", "name", "price"}, null, null, null, null, null, "name");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("DB PROBLEM", e.getMessage());
        }
        return cursor;
    }



    public boolean insertToCard( int itemId ){

        //db.rawQuery("INSERT INTO shoppingCard (quantity, item_id) VALUES (15, 33)", null);
        Cursor cursor = db.rawQuery(
                "SELECT "+ITEM_ID_SHOPPING_CARD+" " +
                        "FROM "+TABLE_NAME_SHOPPING_CARD+" " +
                        "WHERE "+ITEM_ID_SHOPPING_CARD+" = "+itemId+"",
                null );

        if ( cursor.moveToFirst() ) {
            Toast.makeText(context, "Item is in the card!", Toast.LENGTH_SHORT).show();
            return true;

        } else {
            // insert to shopping card
            ContentValues contentValues = new ContentValues();
            contentValues.put(QUANTITY, 1);
            contentValues.put(ITEM_ID_SHOPPING_CARD, itemId); // check Type int vs string!
            db.insert(TABLE_NAME_SHOPPING_CARD, null, contentValues);
            Toast.makeText(context, "inserted to shopping card", Toast.LENGTH_SHORT).show();

            return false;
        }
    }
}