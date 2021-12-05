
/*
 *  Projekt ITU
 *      Autori:
 *          xnosko06 (Matúš Nosko)
 */

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
import com.app.itu.ui.home.HomeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonRequest implements VolleyJsonRequest.ResponseListener{
    private static final String TAG = "TAG";
    private Context context;
    private Map<String, String> mParams;
    public JsonRequest ()
    {

    }

    public void getMethod(Context context, final VolleyCallBack volleyCallBack)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, Singleton.getInstance().url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        // response
                        Log.d("Response", response);
                        Singleton.getInstance().jsonOut = response;
                        try {
                            volleyCallBack.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
    public void getMethod(Context context, final VolleyCallBack volleyCallBack, boolean refresh)
    {
        if (refresh)
        {

        }
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest getRequest = new StringRequest(Request.Method.GET, Singleton.getInstance().url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        // response
                        Log.d("Response", response);
                        Singleton.getInstance().jsonOut = response;
                        try {
                            volleyCallBack.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                    }
                });
        queue.add(getRequest);
    }

    public void getMethod(Context context, final VolleyCallBack volleyCallBack, String type, String value)
    {
        RequestQueue queue = Volley.newRequestQueue(context);
        String final_url = Singleton.getInstance().url + "?value=" + value + "&type=" + type;
        StringRequest getRequest = new StringRequest(Request.Method.GET, final_url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        // response
                        Log.d("Response", response);
                        Singleton.getInstance().jsonOut = response;
                        try {
                            volleyCallBack.onSuccess();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    public void postMethod(Context context,  final VolleyCallBack volleyCallBack) throws JSONException {
        this.context = context;
        RequestQueue queue = Volley.newRequestQueue(context);
        VolleyJsonRequest jsonRequest = null;

        JSONObject jsonObject = new JSONObject(Singleton.getInstance().requestBody);
        jsonRequest = new VolleyJsonRequest(Request.Method.POST,Singleton.getInstance().url,jsonObject, this, volleyCallBack);
        queue.add(jsonRequest.get());

    }


    @Override
    public void onResponse(int statusCode, List<Header> headers, JSONObject object, JSONArray array, VolleyCallBack volleyCallBack) throws JSONException {
        Log.e(TAG,"-------------------------------");
        for(Header header: headers){
            Log.e(TAG,"Name " + header.getName() + " Value " + header.getValue());
            if (header.getName().equals("Set-Cookie"))
            {
                Singleton.getInstance().cookieHeader = header.getValue().split(";")[0];
                volleyCallBack.onSuccess();
            }
            if (header.getValue().equals("{\"message\":\"Uživateľ s týmito prihlasovacími údajmi neexistuje\"}"))
            {
                Singleton.getInstance().authFlag = false;
            }
        }
        if (statusCode == 200)
        {
            volleyCallBack.onSuccess();
        }
        if (object != null)
        {
            // todo handle json
        }
        else if (array != null){
        }
    }

    public interface VolleyCallBack
    {
        void onSuccess() throws JSONException;
        void onFail();
    }
}
