package com.teamawesome.geese.activity.debug;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.teamawesome.geese.R;
import com.teamawesome.geese.fragment.SignupFragment;
import com.teamawesome.geese.activity.GeeseActivity;
import com.teamawesome.geese.task.debug.PostRequestTask;

public class DebugMainActivity extends ListActivity {

    String[] values = new String[] { "Actual Application", "Login", "Server", "ViewPager" };
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
        getMenuInflater().inflate(R.menu.debug_menu_home, menu);
        return true;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();

        if (id == 0) {  //Actual app
            Intent i = new Intent(this, GeeseActivity.class);
            startActivity(i);

        } else if (id == 1) {  //UI
            Intent loginIntent = new Intent(this, SignupFragment.class);
            startActivity(loginIntent);

        } else if (id == 2) { //Server
            new PostRequestTask(this).execute(rawCard);

        } else if (id == 3) {  // ViewPager
            Intent i = new Intent( this, DebugViewPagerActivity.class);
            startActivity(i);

        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        // onResume gets called after this to handle the intent
        setIntent(intent);
    }

}

