package online.ga78col.wheatley;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Lukas Bernhard on 06.01.2017.
 */

public class NetworkTask extends AsyncTask<String,Void,Void> {

    @Override
    protected Void doInBackground(String[] str) {
        Socket nsocket = null;
        try {
            nsocket = new Socket("192.168.2.110", 5005);
        } catch(UnknownHostException e) {
            System.out.println("Unknown host: www.example.com");

        } catch(IOException e) {
            System.out.println("No I/O");
        }

        if (nsocket != null && nsocket.isConnected()) {
            try {
                DataOutputStream dOut = new DataOutputStream(nsocket.getOutputStream());
                dOut.writeBytes(str[0]);
                dOut.flush();
                dOut.close();
                try {
                    nsocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
