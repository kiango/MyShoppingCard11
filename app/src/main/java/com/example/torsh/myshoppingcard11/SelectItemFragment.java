package com.example.torsh.myshoppingcard11;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

/**
 * Fragment for user to
 * - insert, update and delete items into database
 * - to select items from the list and insert it into shopping card table
 */

//Todo: listView updates correctly on the emulaor but incorrect on real device when doing insert, update, delete.

public class SelectItemFragment extends Fragment {

    EditText nameEditText, priceEditText;
    Button buttonInsert, buttonUpdate, buttonDelete, buttonCard;
    RelativeLayout relativeLayoutItemFrag;
    SimpleCursorAdapter simpleCursorAdapter;
    ItemDAO itemDAO;
    ListView listView;
    String item_id_clicked, itemName_clicked, itemPrice_clicked;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {

        RelativeLayout relativeLayout1 = (RelativeLayout) inflater.inflate(R.layout.select_item_fragment, container, false);
        this.relativeLayoutItemFrag =relativeLayout1;

        // UI buttons set to objects
        buttonInsert = (Button) relativeLayout1.findViewById(R.id.btn_insert);
        buttonUpdate = (Button) relativeLayout1.findViewById(R.id.btn_update);
        buttonDelete = (Button) relativeLayout1.findViewById(R.id.btn_delete);
        buttonCard = (Button) relativeLayout1.findViewById(R.id.btn_to_card);
        nameEditText = (EditText) relativeLayoutItemFrag.findViewById(R.id.enter_item);
        priceEditText = (EditText) relativeLayoutItemFrag.findViewById(R.id.enter_price);

        // setting listener for each button
        View.OnClickListener listener_insert = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity().getBaseContext(), "inserting", Toast.LENGTH_SHORT).show();
                addItems();
            }
        };
        buttonInsert.setOnClickListener(listener_insert);


        View.OnClickListener listener_update = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity().getBaseContext(), "updating", Toast.LENGTH_SHORT).show();
                updateItem();
            }
        };
        buttonUpdate.setOnClickListener(listener_update);


        View.OnClickListener listener_delete = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity().getBaseContext(), "deleting", Toast.LENGTH_SHORT).show();
                deleteItem();
            }
        };
        buttonDelete.setOnClickListener(listener_delete);

        View.OnClickListener listener_card = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity().getBaseContext(), "adding to card", Toast.LENGTH_SHORT).show();
                putToShoppingCard();
            }
        };
        buttonCard.setOnClickListener(listener_card);

        showItems();
        //refreshList();
        showListItemByClick();

        return relativeLayout1;
    }

    public String validatedItemPrice(){
        // get user input (item-name and price)
        String price_str = priceEditText.getText().toString();
        // -- Regex validation pattern: input string for only numbers between 1 to 2 digit
        String pattern_only_number = "^[0-9]{1,2}$";
        // validate price
        if ( !price_str.matches(pattern_only_number) ) {
            Toast.makeText(relativeLayoutItemFrag.getContext(), "Invalid Price", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            //Toast.makeText(relativeLayoutItemFrag.getContext(), " ok " + price_str, Toast.LENGTH_SHORT).show();
            return price_str;
        }
    }

    public String validatedItemName(){
        // get user input (item-name and price)
        String name = nameEditText.getText().toString();
        // -- Regex validation pattern: input string for all characters, spaces and numbers, no special chars, input string length between 1 to 15 chars
        String pattern = "^[a-zA-Z0-9 ]{1,15}$";
        // validate input item string
        if ( !name.matches(pattern) ) {
            Toast.makeText(relativeLayoutItemFrag.getContext(), "Invalid Item", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            //Toast.makeText(relativeLayoutItemFrag.getContext(), name +" item name ok ", Toast.LENGTH_SHORT).show();
            return name;
        }
    }


    // insert data into database with addItems()
    public void addItems(){
        String itemName = validatedItemName();
        String itemPrice = validatedItemPrice();

        if ( itemName != null && itemPrice != null ){

            int price = Integer.parseInt(itemPrice);

            itemDAO = new ItemDAO(relativeLayoutItemFrag.getContext()); //initialising itemDAO !!
            long id = itemDAO.insertItem(itemName, price);
            //Toast.makeText(relativeLayoutItemFrag.getContext(), "Row id: " + Long.toString(id), Toast.LENGTH_SHORT).show();
            showItems();
        } else
            Toast.makeText(relativeLayoutItemFrag.getContext(), "no item added!", Toast.LENGTH_SHORT).show();
    }


    public void updateItem(){
        Cursor c = ((SimpleCursorAdapter) listView.getAdapter()).getCursor();
        item_id_clicked = c.getString(0);

        String itemName = validatedItemName();
        String itemPrice = validatedItemPrice();

        if ( itemName != null && itemPrice != null ) {
            int price = Integer.parseInt(itemPrice);
            itemDAO.updateItem(itemName, price, item_id_clicked);
            showItems();

        }
    }


    public void deleteItem(){
        Cursor c = ((SimpleCursorAdapter) listView.getAdapter()).getCursor();
        item_id_clicked = c.getString(0);
        itemDAO.deleteItem( Long.parseLong(item_id_clicked) );
        showItems();
    }


    // shows current updated items in the list
    public void showItems(){

        // initialize and open DB
        itemDAO = new ItemDAO(relativeLayoutItemFrag.getContext());
        itemDAO.openDb();

        if (itemDAO.getItems() == null)
            Toast.makeText(relativeLayoutItemFrag.getContext(),"null items!", Toast.LENGTH_SHORT).show();

        // to get the database rows, start by get the returned cursor obj from itemDAO:
        simpleCursorAdapter = new SimpleCursorAdapter(
                relativeLayoutItemFrag.getContext(),
                android.R.layout.simple_list_item_2,
                itemDAO.getItems(),
                new String[]{"name", "price"},
                new int[]{android.R.id.text1, android.R.id.text2},
                0 );

        listView = (ListView) relativeLayoutItemFrag.findViewById(R.id.listViewItem);
        listView.setAdapter(simpleCursorAdapter);

        // ToDo: where to close the cursor?

        refreshList();
        //itemDAO.closeDB();
    }


    // for updating the list after CRUD
    private void refreshList(){
        simpleCursorAdapter.changeCursor(itemDAO.getItems());
        simpleCursorAdapter.notifyDataSetChanged();
        //simpleCursorAdapter.getCursor();
    }

    // catch the item on the list and show them in the EditText for editing
    private void showListItemByClick(){
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor c = ((SimpleCursorAdapter) listView.getAdapter()).getCursor();
                item_id_clicked = c.getString(0);
                itemName_clicked = c.getString(1);
                itemPrice_clicked = c.getString(2);
                //Toast.makeText(relativeLayoutItemFrag.getContext(), "id: "+item_id_clicked.toString(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(relativeLayoutItemFrag.getContext(), listView.getChildAt(1).toString(), Toast.LENGTH_SHORT).show();
                nameEditText.setText(itemName_clicked);
                priceEditText.setText(itemPrice_clicked);
            }
        });
    }

    public void putToShoppingCard() {

        if (item_id_clicked != null) {
            itemDAO.insertToCard(Integer.parseInt(item_id_clicked));
        } else {
            Toast.makeText(getActivity().getBaseContext(), "Select an item from list", Toast.LENGTH_SHORT).show();
        }
    }
}