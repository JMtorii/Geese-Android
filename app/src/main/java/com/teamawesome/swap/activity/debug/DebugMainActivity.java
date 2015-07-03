package com.teamawesome.swap.activity.debug;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.teamawesome.swap.activity.GooseActivity;
import com.teamawesome.swap.task.debug.PostRequestTask;
import com.teamawesome.swap.R;

public class DebugMainActivity extends ListActivity {

    String[] values = new String[] { "Actual Application", "UI", "Server" };
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

        if (id == 0) {  //Actual app
            Intent i = new Intent(this, GooseActivity.class);
            startActivity(i);

        } else if (id == 1) {  //UI
            Intent homeIntent = new Intent(this, DebugHomeActivity.class);
            startActivity(homeIntent);

        } else if (id == 2) { //Server
            new PostRequestTask(this).execute(rawCard);

        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

}

