package com.teamawesome.swap;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class DebugActivity extends ListActivity {

    String[] values = new String[] { "UI", "Server", "Bluetooth" };
    String rawCard = "{ " +
            "'card': {" +
                "'templateId': 1, " +
                "'user': { " +
                    "'fullName': 'Billy Bee', " +
                    "'email': 'not_today_mofo@gmail.com', " +
                    "'phoneNumber': 4151112222, " +
                "}, " +
                "'imageLogo': { " +
                "'src': 'Images/HappyBee.png', " +
                "'name': 'Haps Bee' " +
                "}, " +
                "'company': { " +
                    "'name': 'Lumosity', " +
                    "'position': 'Useless' " +
                "}" +
            "}" +
        "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
        setTitle("CHOOSE YOUR CHARACTER!!!");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();

        switch(item) {
            case "UI":
                break;
            case "Server":
                new PostRequestTask(this).execute(rawCard);
                break;
            case "Bluetooth":
                break;
            default:
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

}

