package com.example.torsh.myshoppingcard11;

import android.support.v7.app.ActionBarActivity;
import android.app.FragmentManager;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/*
* In this class we use only Fragment transaction manager to switch between 2 fragments
* by clicking 'Item' and 'Card' buttons on the action bar menu.
*
* DB-connection might be closed after usage
*
* */



public class MainActivity extends ActionBarActivity {

    FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // brings fragment 1 to the activity view by default / onCreate :
        //fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_container, new SelectItemFragment(), "ITEM_FRAGMENT").commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //itemDAO.closeDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
            return true;

        if (id == R.id.item_frag) {
            // show item fragment only if it is not visible
            Toast.makeText(getApplicationContext(), "item Fragment", Toast.LENGTH_SHORT).show();
            Fragment selectItemFragment =  getFragmentManager().findFragmentByTag("ITEM_FRAGMENT");
            //if (selectItemFragment.isHidden())
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new SelectItemFragment(), "ITEM_FRAGMENT").commit();

        }

        if (id == R.id.card_frag) {

            //Toast.makeText(getApplicationContext(), "card Fragment", Toast.LENGTH_SHORT).show();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new ShoppingCardFragment(), "CARD_FRAGMENT").commit();
        }

        return super.onOptionsItemSelected(item);
    }
}