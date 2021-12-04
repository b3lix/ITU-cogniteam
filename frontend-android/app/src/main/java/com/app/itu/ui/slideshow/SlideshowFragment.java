package com.app.itu.ui.slideshow;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.app.itu.JsonRequest;
import com.app.itu.MainActivity;
import com.app.itu.R;
import com.app.itu.Singleton;
import com.app.itu.databinding.FragmentSlideshowBinding;
import com.app.itu.ui.home.CustomExpandableListAdapter;
import com.app.itu.ui.home.ExpandableListDataPump;
import com.app.itu.ui.home.HomeFragment;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    JsonRequest jsonRequest = new JsonRequest();
    private com.app.itu.ui.home.ExpandableListDataPump expandableListDataPump = new ExpandableListDataPump();

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        slideshowViewModel = new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (Singleton.getInstance().cookieHeader.isEmpty())
        {
            Toast.makeText(getActivity(), "You must be logged in!",
                    Toast.LENGTH_LONG).show();
        }
        else
        {
            expandableListView = (ExpandableListView) binding.expandableListView;

            Singleton.getInstance().setUrlOperation("/reviews/my");
            jsonRequest.getMethod(root.getContext(), new JsonRequest.VolleyCallBack() {
                @Override
                public void onSuccess() throws JSONException {

                    JSONObject jsonObject = new JSONObject(Singleton.getInstance().jsonOut);
                    JSONArray tmp = jsonObject.getJSONArray("reviews");

                    expandableListDetail = expandableListDataPump.getData(tmp, true);
                    expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                    expandableListAdapter = new CustomExpandableListAdapter(root.getContext(), expandableListTitle, expandableListDetail);
                    expandableListView.setAdapter(expandableListAdapter);
                }

                @Override
                public void onFail() {

                }
            });

            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
//                    Toast.makeText(root.getContext(),
//                            expandableListTitle.get(groupPosition) + " List Expanded.",
//                            Toast.LENGTH_SHORT).show();
                }
            });

            expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                @Override
                public void onGroupCollapse(int groupPosition) {
//                    Toast.makeText(root.getContext(),
//                            expandableListTitle.get(groupPosition) + " List Collapsed.",
//                            Toast.LENGTH_SHORT).show();

                }
            });
            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,
                                            int groupPosition, int childPosition, long id) {
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
                                        Toast.makeText(root.getContext(),"Added to your favourites!",Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(root.getContext(),"You must be logged in for adding to favourites!",Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(root.getContext(),"Removed from your favourites!",Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFail() {

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
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}