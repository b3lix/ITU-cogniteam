package com.app.itu.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.itu.JsonRequest;
import com.app.itu.Singleton;
import com.app.itu.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeFragment extends Fragment{

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private ArrayAdapter adapter;
    private ListView listView;
    JsonRequest jsonRequest = new JsonRequest();

    private static final String TAG = HomeFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView listView = (ListView) binding.listView;
        List list = new ArrayList();

        Singleton.getInstance().setUrlOperation("/food/get");
        jsonRequest.getMethod(root.getContext(), new JsonRequest.VolleyCallBack() {
            @Override
            public void onSuccess() throws JSONException {

                JSONObject jsonObject = new JSONObject(Singleton.getInstance().jsonOut);
                JSONArray tmp = jsonObject.getJSONArray("food");

                for (int i =0; i < tmp.length(); i++)
                {
                    JSONObject object = tmp.getJSONObject(i);
                    list.add(object.get("name"));

                }
                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, list);
                listView.setAdapter(adapter);
            }
        });

        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    static public void appendItems(List list, ArrayAdapter adapter)
    {

    }
}