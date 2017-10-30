package online.ga78col.wheatley;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class eSockets extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, NetworkInterface{

    private static boolean UPDATE_SWITCHS = false;
    ArrayList<Switch> switch_list = new ArrayList<>();
    Switch smartphone;
    Switch bed_lights;
    Switch media_light;
    Switch stereo;
    Switch desktop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_sockets);


        smartphone = (Switch) findViewById(R.id.button_smartphone);
        bed_lights = (Switch) findViewById(R.id.button_bed_lights);
        media_light = (Switch) findViewById(R.id.button_light);
        stereo = (Switch) findViewById(R.id.button_stereo);
        //off = (Switch) findViewById(R.id.button_off);// has to be button
        desktop = (Switch) findViewById(R.id.button_desktop);

        smartphone.setOnCheckedChangeListener(this);
        bed_lights.setOnCheckedChangeListener(this);
        media_light.setOnCheckedChangeListener(this);
        stereo.setOnCheckedChangeListener(this);
        desktop.setOnCheckedChangeListener(this);

        switch_list.add(smartphone);
        switch_list.add(stereo);
        switch_list.add(bed_lights);
        switch_list.add(media_light);
        switch_list.add(desktop);
    }
    @Override
    public void onResume(){
        super.onResume();
        try {
            JSONObject packet= new JSONObject();
            packet.put("command", "socketStates");
            startRequest(packet);
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
    public void startRequest(JSONObject packet) {
        Log.e("cmd",packet.toString());
        NetworkTask network = new NetworkTask(this);
        network.send(packet);
    }

    @Override
    public void serverResult(JSONObject result) {
        UPDATE_SWITCHS = true;
        try {
            String command = result.get("command").toString();
            if(command.equals("socketStates")){
                for(int i=1;i<6;i++) {
                    boolean state = result.getInt(String.valueOf(i))==1;
                    switch_list.get(i).setChecked(state);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UPDATE_SWITCHS = false;
        //Log.e("answer", result.toString());
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(!UPDATE_SWITCHS) {
            JSONObject packet = new JSONObject();
            try {
                packet.put("command", "socket");
                switch (compoundButton.getId()) {
                    case R.id.button_smartphone:
                        packet.put("id", "1");
                        break;
                    case R.id.button_bed_lights:
                        packet.put("id", "3");
                        break;
                    case R.id.button_light:
                        packet.put("id", "4");
                        break;
                    case R.id.button_stereo:
                        packet.put("id", "2");
                        break;
                    case R.id.button_desktop:
                        packet.put("id", "5");
                        break;
                    case R.id.button_off:
                        packet.put("id", "off");
                        break;
                }
                packet.put("state", b ? 1 : 0);
                startRequest(packet);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
