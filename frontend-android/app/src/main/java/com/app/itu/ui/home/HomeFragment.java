package com.app.itu.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import com.app.itu.JsonRequest;
import com.app.itu.Singleton;
import com.app.itu.VolleyJsonRequest;
import com.app.itu.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    JsonRequest jsonRequest = new JsonRequest();

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
                // login example
                Singleton.getInstance().setUrlOperation("/auth/login");
                jsonRequest.postMethod(view.getContext());

            }
        });

        ListView listView = (ListView) binding.listView;
        List list = new ArrayList();

        list.add("ahoj");
        list.add("asd");
        list.add("fre");
        list.add("dsf");
        list.add("ahsdfsdfj");
        list.add("asdfsdfj");
        list.add("asdfsdfoj");
        list.add("ahsdddj");
        list.add("ah123oj");
        list.add("a4334hoj");
        list.add("ah53353oj");
        list.add("ah234234o");
        list.add("asdfsdfj");
        list.add("dsf");
        list.add("ahoj");

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}