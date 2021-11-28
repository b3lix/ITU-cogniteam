package com.app.itu.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.itu.VolleyJsonRequest;
import com.app.itu.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment implements VolleyJsonRequest.ResponseListener{

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    String cookieHeader;
    private RequestQueue queue;

    String requestBody = "{\n" +
            "  \"username\":\"admin\",\n" +
            "  \"password\":\"admin\",\n" +
            "  \"Content-Type\":\"application/json\",\n"+
            "  \"skipCrossSell\":true\n" +
            "}";
    String url = "https://travelwise.online:8090/auth/login";
    private static final String TAG = HomeFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                queue = Volley.newRequestQueue(view.getContext());
                postMethod();
            }
        });
//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void getMethod()
    {
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
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
                params.put("Cookie", cookieHeader);
                return params;
            }
        };
        queue.add(getRequest);

    }

    private void postMethod(){
        VolleyJsonRequest jsonRequest = null;

        try {
            jsonRequest = new VolleyJsonRequest(Request.Method.POST,url, new JSONObject(requestBody),this);
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
                cookieHeader = header.getValue().split(";")[0];
            }
        }
        if (object != null){
            // todo handle json
        }else if (array != null){
        }
        getMethod();
    }
}