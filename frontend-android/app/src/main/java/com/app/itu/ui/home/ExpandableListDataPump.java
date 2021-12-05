/*
 *   Projekt ITU
 *       Autori:
 *           xbelko02 (Belko Erik)
 */
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
                JSONObject food = object.getJSONObject("food");
                String foodName = food.get("name").toString();
                List<String> reviewInfo = new ArrayList<String>();
                String foodId = food.get("id").toString();
                String foodSource = food.get("source").toString();
                String foodType = food.get("type").toString();
                String foodPrice = object.get("price").toString();
                String reviewId = object.get("id").toString();
                String date = object.get("date").toString();
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
                reviewInfo.add(String.format("ID jedla: %s", foodId));
                reviewInfo.add(String.format("Zdroj: %s", foodSource));
                reviewInfo.add(String.format("Typ jedla: %s", foodType));
                reviewInfo.add(String.format("Cena: %s", foodPrice));
                reviewInfo.add(String.format("ID recenzie: %s", reviewId));
                reviewInfo.add(String.format("Dátum recenzie %s", date));
                reviewInfo.add(String.format("Hodnotenie: %s/5.00", rating));
                reviewInfo.add(String.format("Popis: %s", description));
                reviewInfo.add(String.format("Plusy: %s", posPoints));
                reviewInfo.add(String.format("Mínusy: %s", negPoints));

                expandableListDetail.put(foodName, reviewInfo);

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
                        foodInfo.add(String.format("Priemerné hodnotenie: %s/5.00", averageRating));
                        foodInfo.add(String.format("PREZERAŤ REZENCIE"));
                        foodInfo.add(String.format("PRIDAŤ RECENZIU"));

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

                    @Override
                    public void onFail() {

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