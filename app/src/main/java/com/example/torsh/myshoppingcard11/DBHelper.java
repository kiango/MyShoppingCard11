package com.example.torsh.myshoppingcard11;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
* In this class we create 2 tables
* One table (itemTable) for items inserted
* One table (shoppingCard) for selected items into shopping card.
*
* */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "shoppingDB";
    private static final int VERSION = 3;
//    private static final String TABLE_NAME_ITEMS = "itemTable";
//    private static final String TABLE_NAME_SHOPPING_CARD = "shoppingCard";
//
//    private static final String ITEM_ID = "_id";
//    private static final String ITEM_NAME = "name";
//    private static final String PRICE ="price";
//
//    private static final String CARD_ID = "_cID";
//    private static final String ITEM_QUANTITY = "quantity";
//    private static final String FK_ITEM_ID ="item_id";
//
//    private static final String CREATE_ITEMS_TABLE =
//            "CREATE TABLE " +TABLE_NAME_ITEMS+
//                    "(" +ITEM_ID+ "INTEGER PRIMARY KEY AUTOINCREMENT, " +ITEM_NAME+ "VARCHAR(100)," +PRICE+ "INTEGER)";
//
//    private static final String CREATE_SHOPPING_CARD_TABLE =
//            "CREATE TABLE " +TABLE_NAME_SHOPPING_CARD+
//                    " ( " +CARD_ID+  " INTEGER PRIMARY KEY AUTOINCREMENT, "+ITEM_QUANTITY+" INTEGER), FOREIGN KEY("
//                    +FK_ITEM_ID+ ") REFERENCES " +TABLE_NAME_ITEMS+ "(" +ITEM_ID+ "))";

    private static final String DELETE_ITEM_TABLE = "DROP TABLE IF EXISTS itemTable";
    private static final String DELETE_SHOPPING_CARD_TABLE = "DROP TABLE IF EXISTS shoppingCard";

    private Context context;


    public DBHelper(Context context){
        // (context, name of DB, custom cursor OR null for default cursor, DB-version no)
        super(context, DB_NAME, null, VERSION);
        this.context = context;

        // verify:
        //com.example.torsh.my.Message.message(context, "DBHelper const called");

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //Toast.makeText(context, "onCreate called", Toast.LENGTH_SHORT).show();
        //com.example.torsh.mydbexample.Message.message(context, "onCreate DBHelper");

        try {
            //db.execSQL(CREATE_ITEMS_TABLE);
            db.execSQL("CREATE TABLE IF NOT EXISTS itemTable (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER)");
            //Toast.makeText(context, "DBHelper exsql", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL("CREATE TABLE IF NOT EXISTS shoppingCard (_id INTEGER PRIMARY KEY AUTOINCREMENT, quantity INTEGER, item_id INTEGER, FOREIGN KEY(item_id) REFERENCES itemTable(_id))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //com.example.torsh.mydbexample.Message.message(context, "onUpgrade");
        //Toast.makeText(context, "onUpgrade called", Toast.LENGTH_SHORT).show();

        try {
            db.execSQL(DELETE_ITEM_TABLE);
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            db.execSQL(DELETE_SHOPPING_CARD_TABLE);
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}