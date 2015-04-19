package com.teamawesome.swap;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import java.util.GregorianCalendar;


public class HomeActivity extends ListActivity {

    ListView listview;
    String[] values = { "hello", "world" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);*/
        setTitle("Swap!!!");

        CardSimplified card_data[] = new CardSimplified[] {
          new CardSimplified(R.drawable.abc_ab_share_pack_mtrl_alpha, "HELLO", "WORLD", new GregorianCalendar(2015, 1, 24)),
          new CardSimplified(R.drawable.abc_ab_share_pack_mtrl_alpha, "HELLO2", "WORLD2", new GregorianCalendar(2015, 1, 23)),
          new CardSimplified(R.drawable.abc_ab_share_pack_mtrl_alpha, "HELLO3", "WORLD3", new GregorianCalendar(2015, 1, 22)),
          new CardSimplified(R.drawable.abc_ab_share_pack_mtrl_alpha, "HELLO4", "WORLD4", new GregorianCalendar(2015, 1, 21))
        };

        CardSimplifiedAdaptor adapter = new CardSimplifiedAdaptor(this, R.layout.card_simplified, card_data);
        setListAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("Testing2", Long.toString(id));
    }
}
