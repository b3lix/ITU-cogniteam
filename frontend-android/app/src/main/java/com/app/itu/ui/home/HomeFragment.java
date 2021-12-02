package com.app.itu.ui.home;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.itu.AddItemActivity;
import com.app.itu.JsonRequest;
import com.app.itu.R;
import com.app.itu.Singleton;
import com.app.itu.databinding.FragmentHomeBinding;
import com.app.itu.ui.gallery.GalleryFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    JsonRequest jsonRequest = new JsonRequest();
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 1;
    private static final String TAG = HomeFragment.class.getSimpleName();
    private ExpandableListDataPump expandableListDataPump = new ExpandableListDataPump();

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        expandableListView = (ExpandableListView) binding.expandableListView;

        Singleton.getInstance().setUrlOperation("/food/get");
        jsonRequest.getMethod(root.getContext(), new JsonRequest.VolleyCallBack() {
            @Override
            public void onSuccess() throws JSONException {

                JSONObject jsonObject = new JSONObject(Singleton.getInstance().jsonOut);
                JSONArray tmp = jsonObject.getJSONArray("food");

                expandableListDetail = expandableListDataPump.getData(tmp);
                expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                expandableListAdapter = new CustomExpandableListAdapter(root.getContext(), expandableListTitle, expandableListDetail);
                expandableListView.setAdapter(expandableListAdapter);
            }
        }, true);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(root.getContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(root.getContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        root.getContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });

        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), AddItemActivity.class);
                startActivityForResult(myIntent, SECOND_ACTIVITY_REQUEST_CODE);


                String name = "TestOnClick";

                Singleton.getInstance().setUrlOperation("/auth/login");
                try {
                    jsonRequest.postMethod(view.getContext(), new JsonRequest.VolleyCallBack() {
                        @Override
                        public void onSuccess() throws JSONException {
                            String testFood = "{\n" +
                                    " \"name\":\"TestFood\",\n" +
                                    " \"source\":\"Source\",\n" +
                                    " \"type\":\"1\",\n" +
                                    " \"description\": \"Test\",\n" +
                                    " \"review\":\n" +
                                    "{ \"msg\":\"Amaziiiiiiiiiiing\",\n" +
                                    "\"type\":\"1\",\n" +
                                    "\"rating\": \"10\",\n" +
                                    "\"negative_points\": [\"-10\"],\n" +
                                    "\"positive_points\": [\"10\"]}\n" +
                                    "}";
                            Singleton.getInstance().requestBody = testFood;
                            Singleton.getInstance().setUrlOperation("/food/create");
                            jsonRequest.postMethod(view.getContext(), new JsonRequest.VolleyCallBack() {
                                @Override
                                public void onSuccess() throws JSONException {

                                }
                            });
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return root;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
            { // Activity.RESULT_OK

                String returnString = data.getStringExtra("status_create");
                if (returnString.equals("Success"))
                {
                    Singleton.getInstance().setUrlOperation("/food/get");
                    jsonRequest.getMethod(root.getContext(), new JsonRequest.VolleyCallBack() {
                        @Override
                        public void onSuccess() throws JSONException {

                            JSONObject jsonObject = new JSONObject(Singleton.getInstance().jsonOut);
                            JSONArray tmp = jsonObject.getJSONArray("food");

                            expandableListDetail = expandableListDataPump.refreshData(tmp);
                            expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                            expandableListAdapter = new CustomExpandableListAdapter(root.getContext(), expandableListTitle, expandableListDetail);
                            expandableListView.setAdapter(expandableListAdapter);
                        }
                    }, true);
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

