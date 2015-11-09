package com.example.deleterequest;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // DELETE REQUEST WITH BODY
        // NOTES: ok-http 1.6.0 is used instead of the lastest version
        HttpStack httpStack;
        if (Build.VERSION.SDK_INT > 19){
            httpStack = new CustomHurlStack();
        } else if (Build.VERSION.SDK_INT >= 9 && Build.VERSION.SDK_INT <= 19)
        {
            httpStack = new OkHttpHurlStack();
        } else {
            httpStack = new HttpClientStack(AndroidHttpClient.newInstance("Android"));
        }
        RequestQueue requestQueue = Volley.newRequestQueue(this, httpStack);
        String URL = "http://www.power-cellar.eu:5000/api/v1.0/bottles/";
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nombre", 1);
            jsonObject.put("annee", 2010);
            jsonObject.put("cru", "http://www.power-cellar.eu:5000/api/v1.0/crus/4");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest deleteRequest = new JsonObjectRequest(Request.Method.DELETE, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("onResponse", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("onErrorResponse", error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                // Basic Authentication
                String credentials = "gustave@example.com:1234";
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", auth);
                return headers;
            }
			
	    @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                String json;
                if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                    try {
                        json = new String(volleyError.networkResponse.data,
                                HttpHeaderParser.parseCharset(volleyError.networkResponse.headers));
                    } catch (UnsupportedEncodingException e) {
                        return new VolleyError(e.getMessage());
                    }
                    return new VolleyError(json);
                }
                return volleyError;
            }
        };
        requestQueue.add(deleteRequest);
    }
}
