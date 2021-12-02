package com.app.itu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

public class AddItemActivity extends AppCompatActivity
{
    EditText getFoodName;
    EditText getFoodSource;
    EditText getFoodType;
    EditText getFoodDescription;
    EditText getFoodReview;
    Button setFood;

    JsonRequest jsonRequest = new JsonRequest();

    private void setupUI()
    {
        getFoodName = findViewById(R.id.editTextTextFoodName);
        getFoodSource = findViewById(R.id.editTextTextFoodSource);
        getFoodType = findViewById(R.id.editTextTextFoodType);
        getFoodDescription = findViewById(R.id.editTextTextFoodDescription);
        getFoodReview = findViewById(R.id.editTextTextFoodReview);
        setFood = findViewById(R.id.activityButtonSubmit);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        setupUI();
        setupListeners();
    }

    private void setupListeners()
    {
        setFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                boolean isValid = true;

                Editable getFoodNameText = getFoodName.getText();
                if (getFoodNameText.toString().isEmpty())
                {
                    getFoodName.setError("This field cannot be empty!");
                    isValid = false;
                }
                Editable getFoodSourceText = getFoodName.getText();
                if (getFoodSourceText.toString().isEmpty())
                {
                    getFoodSource.setError("This field cannot be empty!");
                    isValid = false;
                }
                Editable getFoodTypeText = getFoodName.getText();
                if (getFoodTypeText.toString().isEmpty())
                {
                    getFoodType.setError("This field cannot be empty!");
                    isValid = false;
                }
                Editable getFoodDescriptionText = getFoodName.getText();
                if (getFoodDescriptionText.toString().isEmpty())
                {
                    getFoodDescription.setError("This field cannot be empty!");
                    isValid = false;
                }
                Editable getFoodReviewText = getFoodName.getText();
                if (getFoodReviewText.toString().isEmpty())
                {
                    getFoodReview.setError("This field cannot be empty!");
                    isValid = false;
                }

                if (isValid)
                {
                    String testFood = "{\n" +
                            " \"name\":" +"\"" + getFoodNameText + "\",\n" +
                            " \"source\":\"" + getFoodSourceText + "\",\n" +
                            " \"type\":\"" + getFoodTypeText + "\",\n" +
                            " \"description\": \"" + getFoodDescriptionText + "\",\n" +
                            " \"review\":\n" +
                            "{ \"msg\":\"Amaziiiiiiiiiiing\",\n" +
                            "\"type\":\"1\",\n" +
                            "\"rating\": \"10\",\n" +
                            "\"negative_points\": [\"-10\"],\n" +
                            "\"positive_points\": [\"10\"]}\n" +
                            "}";
                    Singleton.getInstance().requestBody = testFood;
                    Singleton.getInstance().setUrlOperation("/food/create");
                    try {
                        jsonRequest.postMethod(v.getContext(), new JsonRequest.VolleyCallBack() {
                            @Override
                            public void onSuccess() throws JSONException {

                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
