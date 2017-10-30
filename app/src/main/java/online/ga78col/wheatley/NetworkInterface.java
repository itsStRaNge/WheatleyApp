package online.ga78col.wheatley;

import org.json.JSONObject;

/**
 * Created by Lukas Bernhard on 13.07.2017.
 */

public interface NetworkInterface {
    void startRequest(JSONObject packet);
    void serverResult(JSONObject result);
}
