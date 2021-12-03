package com.app.itu.ui.home;

import android.content.Context;

import com.app.itu.JsonRequest;
import com.app.itu.Singleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
    JsonRequest jsonRequest = new JsonRequest();
    int finalI;
    public HashMap<String, List<String>> expandableListDetailObject = new HashMap<String, List<String>>();
    public HashMap<String, List<String>> getData(JSONArray jsonArray, boolean favoriteFlag)
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

                String id = object.getString("id");

                foodInfo.add(String.format("Zdroj: %s", foodSource));
                foodInfo.add(String.format("Počet recenzií: %s", numberOfReviews));
                foodInfo.add(String.format("Priemerná cena: %s", averagePrice));
                foodInfo.add(String.format("Priemerné hodnotenie: %s/10.00", averageRating));

                String favourite = object.get("favourite").toString();
                if (favourite.equals("null")) {
                    foodInfo.add(String.format("PRIDAŤ DO OBĽÚBENÝCH: %s", id));
                }
                else
                {
                    foodInfo.add(String.format("OBĽÚBENÉ: %s", id));
                }

                expandableListDetail.put(foodName, foodInfo);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return expandableListDetail;
    }
    public void getData(JSONArray jsonArray, Context context, final DataCallBack dataCallBack) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject object = jsonArray.getJSONObject(i);

                String id = object.getString("id");
                Singleton.getInstance().setUrlOperation("/reviews/get/" + id);

                finalI = i;
                jsonRequest.getMethod(context, new JsonRequest.VolleyCallBack() {
                    @Override
                    public void onSuccess() throws JSONException {
                        //tu su reviews
                        String jsonOut = Singleton.getInstance().jsonOut;

                        String foodName = object.get("name").toString();

                        List<String> foodInfo = new ArrayList<String>();
                        String foodSource = object.get("source").toString();
                        String numberOfReviews = object.get("reviews").toString();
                        JSONObject average = object.getJSONObject("average");
                        String averagePrice = average.get("price").toString();
                        String averageRating = average.get("rating").toString();


                        foodInfo.add(String.format("Zdroj: %s", foodSource));
                        foodInfo.add(String.format("Počet recenzií: %s", numberOfReviews));
                        foodInfo.add(String.format("Priemerná cena: %s", averagePrice));
                        foodInfo.add(String.format("Priemerné hodnotenie: %s/10.00", averageRating));

                        String favourite = object.get("favourite").toString();
                        if (favourite.equals("null")) {
                            foodInfo.add(String.format("PRIDAŤ DO OBĽÚBENÝCH: %s", id));
                        } else {
                            foodInfo.add(String.format("OBĽÚBENÉ: %s", id));
                        }

                        expandableListDetailObject.put(foodName, foodInfo);

                        if (jsonArray.length() == expandableListDetailObject.size())
                        {
                            dataCallBack.onSuccess();
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<String, List<String>> refreshData(JSONArray jsonArray)
    {
        expandableListDetail.clear();
        expandableListDetail = getData(jsonArray, false);
        return expandableListDetail;
    }
    public interface DataCallBack
    {
        void onSuccess() throws JSONException;
    }
}