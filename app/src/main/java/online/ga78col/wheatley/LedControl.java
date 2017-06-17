package online.ga78col.wheatley;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class LedControl extends AppCompatActivity implements View.OnClickListener,SeekBar.OnSeekBarChangeListener{
    private final static int BUTTON_CUSTOM = 0,BUTTON_FADE=1;
    private int BUTTON_STATE = BUTTON_CUSTOM;
    private Button customButton=null,fadeButton=null;
    private SeekBar redSeekbar = null, greenSeekbar = null,blueSeekbar = null;
    private Integer red = 0,green = 0,blue = 0, alpha = 255;
    private String fadeSpeed="100";
    int b_red=0,b_green=0,b_blue=255;
    public String cmd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led_control);

        redSeekbar = (SeekBar) findViewById(R.id.red_seekbar);
        greenSeekbar = (SeekBar) findViewById(R.id.green_seekbar);
        blueSeekbar = (SeekBar) findViewById(R.id.blue_seekbar);
        customButton = (Button) findViewById(R.id.button_custom);
        fadeButton = (Button) findViewById(R.id.button_fade);
        Button offButton = (Button)  findViewById(R.id.button_off);


        fadeButton.setOnClickListener(this);
        customButton.setOnClickListener(this);
        offButton.setOnClickListener(this);
        redSeekbar.setOnSeekBarChangeListener(this);
        greenSeekbar.setOnSeekBarChangeListener(this);
        blueSeekbar.setOnSeekBarChangeListener(this);
        new Fadeing().execute();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button_fade:
                BUTTON_STATE = BUTTON_FADE;
                break;
            case R.id.button_off:
                cmd = "lightsOff,0,";
                NetworkTask network = new NetworkTask();
                network.execute(cmd);
                redSeekbar.setProgress(0);
                greenSeekbar.setProgress(0);
                blueSeekbar.setProgress(0);
                break;
            case R.id.button_custom:
                BUTTON_STATE = BUTTON_CUSTOM;
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        TextView text,view;
        switch(seekBar.getId()){
            case R.id.red_seekbar:
                red = progress;
                text = (TextView) findViewById(R.id.redShowNumber);
                text.setText(red.toString());
                updateCustomButtonColor();
                view = (TextView) findViewById(R.id.r);
                view.setTextColor(Color.rgb(red*2,0,0));
                break;
            case R.id.green_seekbar:
                green = progress;
                text = (TextView) findViewById(R.id.greenShowNumber);
                text.setText(green.toString());
                updateCustomButtonColor();
                view = (TextView) findViewById(R.id.g);
                view.setTextColor(Color.rgb(0,green*2,0));
                break;
            case R.id.blue_seekbar:
                blue = progress;
                text = (TextView) findViewById(R.id.blueShowNumber);
                text.setText(blue.toString());
                updateCustomButtonColor();
                view = (TextView) findViewById(R.id.b);
                view.setTextColor(Color.rgb(0,0,blue*2));
                break;
        }
    }

    private void updateCustomButtonColor(){
        customButton.setBackgroundColor(Color.rgb(red*2,green*2,blue*2));
        customButton.setTextColor(Color.rgb(blue * 2+55, red * 2+55, green * 2+55));
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch(seekBar.getId()) {
            case R.id.red_seekbar:
                cmd = "red," + red.toString() + ",";
                break;
            case R.id.green_seekbar:
                cmd = "green," + green.toString() + ",";

                break;
            case R.id.blue_seekbar:
                cmd = "blue," + blue.toString() + ",";
                break;
            case R.id.button_fade:
                cmd = "fade," + fadeSpeed +",";
                break;
        }
        if(BUTTON_STATE == BUTTON_CUSTOM) {
            NetworkTask network = new NetworkTask();
            network.execute(cmd);
        }
    }

    public class Fadeing extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void voids){
            if(b_blue == 255){
                if(b_green != 0){
                    b_green-=5;
                }else{
                    b_red+=5;
                }
            }
            if(b_red == 255){
                if(b_blue != 0){
                    b_blue-=5;
                }else{
                    b_green+=5;
                }
            }
            if(b_green == 255){
                if(b_red != 0){
                    b_red-=5;
                }else{
                    b_blue+=5;
                }
            }
            fadeButton.setBackgroundColor(Color.rgb(b_red,b_green,b_blue));
            fadeButton.setTextColor(Color.rgb(b_blue,b_red,b_green));
            Fadeing fade = new Fadeing();
            fade.execute(voids);
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
                i = new Intent(this, eSockets.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(i);
                return true;
            case R.id.action_led_control:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

