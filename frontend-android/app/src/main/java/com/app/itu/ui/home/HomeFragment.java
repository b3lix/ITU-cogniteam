package com.app.itu.ui.home;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListAdapter;
import android.widget.SearchView;
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
import com.google.android.material.snackbar.Snackbar;

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


        SearchView simpleSearchView = binding.searchView; // inititate a search view

// perform set on query text listener event
        simpleSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int operation = 0;
                if (binding.radioButton2.isChecked())
                {
                    operation = 1;
                }
                Singleton.getInstance().setUrlOperation("/food/get");

                jsonRequest.getMethod(getContext(), new JsonRequest.VolleyCallBack() {
                    @Override
                    public void onSuccess() throws JSONException
                    {
                        JSONObject jsonObject = new JSONObject(Singleton.getInstance().jsonOut);
                        JSONArray tmp = jsonObject.getJSONArray("food");

                        expandableListDetail = expandableListDataPump.refreshData(tmp);
                        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                        expandableListAdapter = new CustomExpandableListAdapter(root.getContext(), expandableListTitle, expandableListDetail);
                        expandableListView.setAdapter(expandableListAdapter);
                    }
                }, Integer.toString(operation), query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        expandableListView = (ExpandableListView) binding.expandableListView;

        Singleton.getInstance().setUrlOperation("/food/get");
        jsonRequest.getMethod(root.getContext(), new JsonRequest.VolleyCallBack() {
            @Override
            public void onSuccess() throws JSONException {

                JSONObject jsonObject = new JSONObject(Singleton.getInstance().jsonOut);
                JSONArray tmp = jsonObject.getJSONArray("food");

                expandableListDataPump.getData(tmp, getContext(), new ExpandableListDataPump.DataCallBack() {
                    @Override
                    public void onSuccess() throws JSONException {
                        expandableListDetail = expandableListDataPump.expandableListDetailObject;

                        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                        expandableListAdapter = new CustomExpandableListAdapter(root.getContext(), expandableListTitle, expandableListDetail);
                        expandableListView.setAdapter(expandableListAdapter);
                    }
                });

            }
        });

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
//                Toast.makeText(
//                        root.getContext(),
//                        expandableListTitle.get(groupPosition)
//                                + " -> "
//                                + expandableListDetail.get(
//                                expandableListTitle.get(groupPosition)).get(
//                                childPosition), Toast.LENGTH_SHORT
//                ).show();


                String [] tmp = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).split(":");
                String operation = tmp[0];
                if (operation.equals("PRIDAŤ DO OBĽÚBENÝCH"))
                {
                    if (!Singleton.getInstance().cookieHeader.isEmpty())
                    {
                        Singleton.getInstance().setUrlOperation("/food/favourite/" + id);
                        try {
                            jsonRequest.postMethod(root.getContext(),new JsonRequest.VolleyCallBack() {
                                @Override
                                public void onSuccess() throws JSONException
                                {
                                    Toast.makeText(root.getContext(),"Added to your favorite !",Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(root.getContext(),"You must be logged in for add to favorite !",Toast.LENGTH_SHORT).show();
                    }
                }
                else if (operation.equals("OBĽÚBENÉ"))
                {
                    Singleton.getInstance().setUrlOperation("/food/favourite/" + id);
                    try {
                        jsonRequest.postMethod(root.getContext(),new JsonRequest.VolleyCallBack() {
                            @Override
                            public void onSuccess() throws JSONException
                            {
                                Toast.makeText(root.getContext(),"Removed from your favorite !",Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (operation.equals("PREZERAŤ REZENCIE")) {
                    String jsonOut = Singleton.getInstance().jsonOut;
                    try {
                        JSONObject json = new JSONObject(jsonOut);
                        JSONArray jsonArr = json.getJSONArray("reviews");
                        JSONObject reviews = (JSONObject) jsonArr.get(0);
                        String reviweId = reviews.get("id").toString();
                        String date = reviews.get("date").toString();
                        String user = reviews.get("user").toString();
                        String rating = reviews.get("rating").toString();
                        String description = reviews.get("description").toString();
                        String posPoints = reviews.get("positive_points").toString();
                        String negPoints = reviews.get("negative_points").toString();

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage(reviweId + "\nDátum:\n" + date + "\nPoužívateľ:\n" + user + "\nHodnotenie:\n" + rating + "\nPopis:\n" + description + "\nPlusy:\n" + posPoints + "\nMínusy:\n" + negPoints + "\n").setTitle("Recenzie").show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
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

