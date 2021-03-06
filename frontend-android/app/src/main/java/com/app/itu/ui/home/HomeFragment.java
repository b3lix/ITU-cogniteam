/*
 *   Projekt ITU
 *       Autori:
 *           xbelko02 (Belko Erik)
 */
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
import com.app.itu.ui.AddReviewActivity;
import com.app.itu.ui.gallery.GalleryFragment;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    JsonRequest jsonRequest = new JsonRequest();
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 1;
    private static final int ADD_REVIEW_ACTIVITY_REQUEST_CODE = 2;
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
                else if (binding.radioButton.isChecked())
                {
                    operation = 0;
                }
                else
                {
                    Toast.makeText(root.getContext(),"Nebola zvolen?? mo??nos?? re??tar??cia alebo obchod !",Toast.LENGTH_SHORT).show();
                    return false;
                }
                Singleton.getInstance().setUrlOperation("/food/get");

                jsonRequest.getMethod(getContext(), new JsonRequest.VolleyCallBack() {
                    @Override
                    public void onSuccess() throws JSONException
                    {
                        JSONObject jsonObject = new JSONObject(Singleton.getInstance().jsonOut);
                        JSONArray tmp = jsonObject.getJSONArray("food");
                        expandableListDataPump.expandableListDetailObject.clear();
                        if (tmp.length() == 0)
                        {
                            expandableListDataPump.expandableListDetailObject.clear();
                            expandableListDetail = expandableListDataPump.expandableListDetailObject;

                            expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                            expandableListAdapter = new CustomExpandableListAdapter(root.getContext(), expandableListTitle, expandableListDetail);
                            expandableListView.setAdapter(expandableListAdapter);
                            Toast.makeText(root.getContext(),"Nebolo ni?? n??jden?? !",Toast.LENGTH_SHORT).show();
                        }

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

                    @Override
                    public void onFail()
                    {

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

            @Override
            public void onFail() {

            }
        });

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(root.getContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(root.getContext(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();

            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                String [] tmp = expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition).split(":");
                String operation = tmp[0];
                if (operation.equals("PRIDA?? DO OB????BEN??CH"))
                {
                    String idFav = tmp[1].substring(1);
                    if (!Singleton.getInstance().cookieHeader.isEmpty())
                    {
                        Singleton.getInstance().setUrlOperation("/food/favourite/" + idFav);
                        try {
                            jsonRequest.postMethod(root.getContext(),new JsonRequest.VolleyCallBack() {
                                @Override
                                public void onSuccess() throws JSONException
                                {
                                    Toast.makeText(root.getContext(),"Pridan?? do ob????ben??ch !",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFail() {

                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(root.getContext(),"Mus???? by?? prihlasen?? aby si si mohol prida?? do Ob????ben?? !",Toast.LENGTH_SHORT).show();
                    }
                }
                else if (operation.equals("OB????BEN??"))
                {
                    String idFav = tmp[1].substring(1);
                    if (!Singleton.getInstance().cookieHeader.isEmpty())
                    Singleton.getInstance().setUrlOperation("/food/favourite/" + idFav);
                    try {
                        jsonRequest.postMethod(root.getContext(),new JsonRequest.VolleyCallBack() {
                            @Override
                            public void onSuccess() throws JSONException
                            {
                                Toast.makeText(root.getContext(),"Odstranen?? z Ob????ben??ch !",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFail() {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if (operation.equals("PREZERA?? REZENCIE"))
                {
                    String jsonOut = Singleton.getInstance().jsonOut;
                    List<String> childIdList = expandableListDetail.get(expandableListTitle.get(groupPosition));
                    
                    assert childIdList != null;
                    String child = childIdList.get(6);
                    child = child.split(":")[1].substring(1);
                    expandableListView = (ExpandableListView) binding.expandableListView;

                    Singleton.getInstance().setUrlOperation("/reviews/get/" + child);
                    jsonRequest.getMethod(root.getContext(), new JsonRequest.VolleyCallBack() {
                        @Override
                        public void onSuccess() throws JSONException {

                            JSONObject jsonObject = new JSONObject(Singleton.getInstance().jsonOut);
                            JSONArray tmp = jsonObject.getJSONArray("reviews");

                            StringBuilder outLong = new StringBuilder();
                            for (int i = 0; i < tmp.length(); i++)
                            {
                                JSONObject object = tmp.getJSONObject(i);
                                String reviewId = object.get("id").toString();
                                String date = object.get("date").toString();
                                String user = object.get("user").toString();
                                String rating = object.get("rating").toString();
                                String description = object.get("description").toString();
                                StringBuilder posPoints = new StringBuilder();
                                JSONArray jArray = object.getJSONArray("positive_points");

                                for(int j=0; j < jArray.length(); j++) {
                                    String json_data = jArray.getString(j);
                                    posPoints.append(json_data + "\n");

                                }
                                StringBuilder negPoints = new StringBuilder();

                                jArray = object.getJSONArray("negative_points");

                                for(int j=0; j < jArray.length(); j++) {
                                    String json_data = jArray.getString(j);
                                    negPoints.append(json_data + "\n");

                                }
                                String out = (reviewId + "\nD??tum:\n" + date + "\nPou????vate??:\n" + user + "\nHodnotenie:\n" + rating + "\nPopis:\n" + description + "\nPlusy:\n" + posPoints + "\nM??nusy:\n" + negPoints + "\n");
                                outLong.append("------------\n");
                                outLong.append(out);
                            }
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setMessage(outLong).setTitle("Recenzie").show();
                        }

                        @Override
                        public void onFail() {

                        }
                    });


                }
                else if (operation.equals("PRIDA?? RECENZIU")) {
                    List<String> childIdList = expandableListDetail.get(expandableListTitle.get(groupPosition));
                    String childId = "";
                    assert childIdList != null;
                    for (String element : childIdList)
                    {
                        String splitted = element.split(":")[0];
                        if (splitted.equals("OB????BEN??") || splitted.equals("PRIDA?? DO OB????BEN??CH"))
                        {
                            childId = element.split(":")[1];
                            childId = childId.substring(1);
                        }
                    }
                    Intent myIntent = new Intent(getContext(), AddReviewActivity.class);
                    Bundle b = new Bundle();
                    b.putString("food_id", childId);
                    myIntent.putExtras(b);
                    startActivityForResult(myIntent, ADD_REVIEW_ACTIVITY_REQUEST_CODE);
                }
                return false;
            }
        });

        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), AddItemActivity.class);
                startActivityForResult(myIntent, SECOND_ACTIVITY_REQUEST_CODE);

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

                        @Override
                        public void onFail() {

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

