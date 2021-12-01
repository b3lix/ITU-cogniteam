package com.app.itu;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonRequest implements VolleyJsonRequest.ResponseListener{
    private static final String TAG = "TAG";
    private Context context;

    public JsonRequest ()
    {

    }

    public void getMethod(Context context)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "https://travelwise.online:8090/auth/info";
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Cookie", Singleton.getInstance().cookieHeader);
                return params;
            }
        };
        queue.add(getRequest);
    }

    public void postMethod(Context context)
    {
        this.context = context;
        RequestQueue queue = Volley.newRequestQueue(context);
        VolleyJsonRequest jsonRequest = null;
        try {
            jsonRequest = new VolleyJsonRequest(Request.Method.POST,Singleton.getInstance().url, new JSONObject(Singleton.getInstance().requestBody), this);
            queue.add(jsonRequest.get());
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(int statusCode, List<Header> headers, JSONObject object, JSONArray array) {
        Log.e(TAG,"-------------------------------");
        for(Header header: headers){
            Log.e(TAG,"Name " + header.getName() + " Value " + header.getValue());
            if (header.getName().equals("Set-Cookie"))
            {
                Singleton.getInstance().cookieHeader = header.getValue().split(";")[0];
            }
        }
        if (object != null)
        {
            // todo handle json
        }
        else if (array != null){
        }
        getMethod(this.context);
    }
}
