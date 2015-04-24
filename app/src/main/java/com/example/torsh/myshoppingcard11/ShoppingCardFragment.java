package com.example.torsh.myshoppingcard11;

import android.app.ListFragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Fragment for showing the shopping card (show selected items total amount
 */

//ToDo: clear text after insert;

public class ShoppingCardFragment extends Fragment {

    ShoppingCardDAO shoppingCardDAO;
    RelativeLayout relativeLayoutCardFrag;
    ItemDAO itemDAO;
    SimpleCursorAdapter simpleCursorAdapter;
    Button buttonAdd, buttonSubtract, buttonOk, buttonDelete;
    ListView listView_card;
    TextView itemNameTextView, quantityTextView, totalPurchaseTextView;
    String item_id_clicked, itemName_clicked, itemPrice_clicked, itemQuantity_clicked;

    //initialising itemDAO !!
    //itemDAO = new ItemDAO(relativeLayoutItemFrag.getContext());
    //long id = itemDAO.insertItem(itemName, price);


    //ToDo: list view (id, name, price, quantity)

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout relativeLayout2 = (RelativeLayout) inflater.inflate(R.layout.shopping_card_fragment, container, false);
        this.relativeLayoutCardFrag = relativeLayout2;

        buttonAdd = (Button) relativeLayout2.findViewById(R.id.btn_add);
        buttonSubtract = (Button) relativeLayout2.findViewById(R.id.btn_subtract);
        buttonOk = (Button) relativeLayout2.findViewById(R.id.btn_ok);
        buttonDelete = (Button) relativeLayout2.findViewById(R.id.btn_del);

        itemNameTextView = (TextView) relativeLayout2.findViewById(R.id.textView_editItem);
        quantityTextView = (TextView) relativeLayout2.findViewById(R.id.textView_quantity);
        totalPurchaseTextView = (TextView) relativeLayout2.findViewById(R.id.textView_totalPrice);


        // Add
        View.OnClickListener listener1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(quantityTextView.getText().toString());
                //number = number+1;
                number++;
                quantityTextView.setText(String.valueOf(number));
                if ( number > 0 )
                    buttonSubtract.setClickable(true);

            }
        };
        buttonAdd.setOnClickListener(listener1);


        // Subtract
        View.OnClickListener listener2 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = Integer.parseInt(quantityTextView.getText().toString());
                number--;
                quantityTextView.setText(String.valueOf(number));
                if ( number <= 0 ) {
                    deleteFromCard();
                    refreshList();
                    populateTotalPurchase();
                    buttonSubtract.setClickable(false);
                    quantityTextView.setText("");
                    itemNameTextView.setText("");
                }
                //Toast.makeText(relativeLayoutCardFrag.getContext(), "dfgdf", Toast.LENGTH_SHORT).show();
            }
        };
        buttonSubtract.setOnClickListener(listener2);


        // OK (update)
        View.OnClickListener listener3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if ( true ) {
                if ( quantityTextView.getText().toString().equals("")) {
                    Toast.makeText(getActivity().getBaseContext(), "no item selected", Toast.LENGTH_SHORT).show();

                } else {
                    //Toast.makeText(getActivity().getBaseContext(), "in the else...", Toast.LENGTH_SHORT).show();
                    int qnt = Integer.parseInt(quantityTextView.getText().toString());
                    long item_id = Long.parseLong(item_id_clicked);
                    shoppingCardDAO.updateQuantity(item_id, qnt);
                    refreshList();
                    populateTotalPurchase();
                }
            }
        };
        buttonOk.setOnClickListener(listener3);


        // Delete (Del)
        View.OnClickListener listener4 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromCard();
                refreshList();
                populateTotalPurchase();
            }
        };
        buttonDelete.setOnClickListener(listener4);


        // initialize and open DB
        itemDAO = new ItemDAO( relativeLayoutCardFrag.getContext());
        itemDAO.openDb();
        // initialize
        shoppingCardDAO = new ShoppingCardDAO( relativeLayoutCardFrag.getContext() );
        populateShoppingList();
        populateTotalPurchase();
        showListItemByClick();


        return relativeLayout2;
    }


    // populate ListView
    public void populateShoppingList(){
        // id name price quantity subtotal
        Cursor cursor = shoppingCardDAO.getSelectedItems();
        // make an adaptor to map columns of DB to UI (List view)
        // to get the database rows start by get the returned cursor obj from shoppingCardDAO:
        simpleCursorAdapter = new SimpleCursorAdapter(
                relativeLayoutCardFrag.getContext(),
                R.layout.shopping_list_layout,
                cursor,
                new String[]{"_id", "name", "price", "quantity", "subtotal"}, // column name of select query
                new int[]{R.id.txt1, R.id.txt2, R.id.txt3, R.id.txt4, R.id.txt5},
                0);

        // populate shopping list by setting the adaptor for the list view
        listView_card = (ListView) relativeLayoutCardFrag.findViewById(R.id.shopping_card_listView);
        listView_card.setAdapter(simpleCursorAdapter);

    }

    // populate TotalPurchase
    public void populateTotalPurchase(){
        // get returned cursor from total purchase query
        Cursor cursor = shoppingCardDAO.getTotalPurchase();
        String column_name, total = "";

        if (cursor.getCount() == 1){
            cursor.moveToFirst();
            column_name = cursor.getColumnName(0); // get DB column name string
            total = cursor.getString(cursor.getColumnIndex(column_name)); // get the related row (number)
            totalPurchaseTextView.setText(total);
        }
        else Toast.makeText(relativeLayoutCardFrag.getContext(), "card empty", Toast.LENGTH_SHORT).show();
    }

    // catch the item on the list and show them in the EditText fro editing
    private void showListItemByClick(){
        listView_card.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = ((SimpleCursorAdapter) listView_card.getAdapter()).getCursor();
                item_id_clicked = c.getString(0);
                itemName_clicked = c.getString(1);
                itemPrice_clicked = c.getString(2);
                itemQuantity_clicked = c.getString(3);

                Toast.makeText(getActivity().getBaseContext(), item_id_clicked.toString(), Toast.LENGTH_SHORT).show();
                itemNameTextView.setText(itemName_clicked);
                quantityTextView.setText(itemQuantity_clicked);
            }
        });
    }

    public void deleteFromCard(){
        shoppingCardDAO.deleteItemFromCard(Integer.parseInt(item_id_clicked));
    }

    // for updating the list after CRUD
    private void refreshList(){
        simpleCursorAdapter.changeCursor(shoppingCardDAO.getSelectedItems());
        simpleCursorAdapter.getCursor();
    }
}