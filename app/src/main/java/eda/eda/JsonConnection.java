package eda.eda;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Piekey on 2015/7/27.
 */

/**
 * Refactored by Fermi on 2015/8/22
 *
 */
public class JsonConnection {
    private URL url;
    private String jsonStr;
    private JSONObject reJson;
    private String requestMethod;

    // Refactored by Fermi on 2015/8/20 Add GET Method
    public JsonConnection(String urlStr, JSONObject json, String requestMethod) {
        try {
            this.url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (json != null) {
            this.jsonStr = json.toString();
        } else {
            this.jsonStr = null;
        }
        this.requestMethod = requestMethod;
    }
    public boolean connectAndGetJson() {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            conn.setRequestMethod(this.requestMethod);
            conn.setDoInput(true);

            if (this.requestMethod == "POST" && this.jsonStr != null) {
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(jsonStr.getBytes().length);
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            }
            // Connect
            conn.connect();
            if (this.requestMethod == "POST" && this.jsonStr != null) {
                OutputStream os = new BufferedOutputStream(conn.getOutputStream());
                os.write(jsonStr.getBytes());
                os.flush();
                os.close();
            }
            // get Response
            int responseCode = conn.getResponseCode();
            InputStream in = null;
            if (responseCode / 100 == 2) {
                in = conn.getInputStream();
            } else {
                return false;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuffer reJsonStr = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                reJsonStr.append(line);
            }
            String rjs = reJsonStr.toString();
            rjs = rjs.replace("\"{", "{").replace("}\"", "}").replace("\\", "");
            try {
                if (rjs != null)
                    reJson = new JSONObject(rjs);
                else
                    Log.d("SERVER ERROR", "Server Respond With Null");
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            reader.close();
            in.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public JSONObject getJson() {
        return reJson;
    }
}

