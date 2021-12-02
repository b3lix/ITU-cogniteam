package com.app.itu.ui.home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

    public HashMap<String, List<String>> getData(JSONArray jsonArray)
    {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);
                String foodName = object.get("name").toString();

                List<String> foodInfo = new ArrayList<String>();
                String foodSource = object.get("source").toString();
                String numberOfReviews = object.get("reviews").toString();
                JSONObject average = object.getJSONObject("average");
                String averagePrice = average.get("price").toString();
                String averageRating = average.get("rating").toString();
                String favourite = object.get("favourite").toString();

                foodInfo.add(String.format("Zdroj: %s", foodSource));
                foodInfo.add(String.format("Počet recenzií: %s", numberOfReviews));
                foodInfo.add(String.format("Priemerná cena: %s", averagePrice));
                foodInfo.add(String.format("Priemerné hodnotenie: %s/10.00", averageRating));
                foodInfo.add(String.format(favourite));

                expandableListDetail.put(foodName, foodInfo);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return expandableListDetail;
    }

    public HashMap<String, List<String>> refreshData(JSONArray jsonArray)
    {
        expandableListDetail.clear();
        expandableListDetail = getData(jsonArray);
        return expandableListDetail;
    }
}