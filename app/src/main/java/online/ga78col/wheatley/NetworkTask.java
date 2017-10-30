package online.ga78col.wheatley;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * Created by Lukas Bernhard on 06.01.2017.
 */

public class NetworkTask extends AsyncTask<JSONObject,JSONObject, JSONObject> {
    private NetworkInterface m_caller = null;
    public NetworkTask(NetworkInterface caller){
        m_caller = caller;
    }
    public void send(JSONObject packet){
        execute(packet);
    }
    @Override
    protected JSONObject doInBackground(JSONObject... obje) {
        Socket nsocket = null;
        String result = "";
        DataOutputStream dataOutputStream;
        try {
            nsocket = new Socket();
            nsocket.connect(new InetSocketAddress("192.168.2.110", 5005),5000);
            dataOutputStream = new DataOutputStream(nsocket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(nsocket.getInputStream()));
            
            dataOutputStream.writeBytes(obje[0].toString());
            dataOutputStream.flush();

            result = br.readLine();
            nsocket.close();
        } catch(UnknownHostException e) {
            System.out.println("Unknown host: www.example.com");
        } catch(IOException e) {
            System.out.println("No I/O");
        }
        //Log.e("answer", result);
        try {
            return new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onPostExecute(JSONObject result) {
        m_caller.serverResult(result);
    }
}
