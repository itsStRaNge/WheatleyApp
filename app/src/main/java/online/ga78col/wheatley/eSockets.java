package online.ga78col.wheatley;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class eSockets extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_sockets);

        Button socketOne = (Button) findViewById(R.id.button_socket_1);
        Button socketTwo = (Button) findViewById(R.id.button_socket_2);
        Button socketThree = (Button) findViewById(R.id.button_socket_3);
        Button socketOff = (Button) findViewById(R.id.button_socket_off);
        Button socketSync = (Button) findViewById(R.id.button_socket_sync);

        socketOne.setOnClickListener(this);
        socketTwo.setOnClickListener(this);
        socketThree.setOnClickListener(this);
        socketOff.setOnClickListener(this);
        socketSync.setOnClickListener(this);
    }
    /*******************************************************************************/
    /************MENU***************************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch(item.getItemId()){
            case R.id.action_e_sockets:
                return true;
            case R.id.action_led_control:
                i = new Intent(this, LedControl.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        String cmd="";
        switch(view.getId()){
            case R.id.button_socket_1:
                cmd = "socket,1,";
                break;
            case R.id.button_socket_2:
                cmd = "socket,2,";
                break;
            case R.id.button_socket_3:
                cmd = "socket,3,";
                break;
            case R.id.button_off:
                cmd = "socket,off,";
            case R.id.button_socket_sync:
                //TODO implement sync mechanic
                break;

        }
        NetworkTask network = new NetworkTask();
        network.execute(cmd);
    }
}
