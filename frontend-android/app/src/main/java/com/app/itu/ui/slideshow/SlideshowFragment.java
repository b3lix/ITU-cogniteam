package com.app.itu.ui.slideshow;

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

            Singleton.getInstance().setUrlOperation("/food/get");
            jsonRequest.getMethod(root.getContext(), new JsonRequest.VolleyCallBack() {
                @Override
                public void onSuccess() throws JSONException {

                    JSONObject jsonObject = new JSONObject(Singleton.getInstance().jsonOut);
                    JSONArray tmp = jsonObject.getJSONArray("food");

                    expandableListDetail = expandableListDataPump.getData(tmp, true);
                    expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
                    expandableListAdapter = new CustomExpandableListAdapter(root.getContext(), expandableListTitle, expandableListDetail);
                    expandableListView.setAdapter(expandableListAdapter);
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
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}