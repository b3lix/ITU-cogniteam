package com.app.itu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

public class AddItemActivity extends AppCompatActivity
{
    EditText getFoodName;
    EditText getFoodSource;
    RadioButton getFoodType0;
    int getFoodType;
    RadioButton getFoodType1;
    EditText getFoodDescription;
    EditText getFoodReviewMsg;
    RatingBar getFoodReviewRating;
    EditText getFoodReviewRatingNeg;
    EditText getFoodReviewRatingPos;

    Button setFood;

    JsonRequest jsonRequest = new JsonRequest();

    private void setupUI()
    {
        getFoodName = findViewById(R.id.editTextTextFoodName);
        getFoodSource = findViewById(R.id.editTextTextFoodSource);
        getFoodType0 = findViewById(R.id.radioButton);
        getFoodType1 = findViewById(R.id.radioButton2);
        getFoodDescription = findViewById(R.id.editTextTextFoodDescription);
        getFoodReviewMsg = findViewById(R.id.editTextTextFoodReviewMsg);
        getFoodReviewRating = findViewById(R.id.editTextTextFoodReviewRating);
        getFoodReviewRatingNeg = findViewById(R.id.editTextTextFoodReviewRatingNeg);
        getFoodReviewRatingPos = findViewById(R.id.editTextTextFoodReviewRatingPos);
        getFoodType = 0;
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
                if (Singleton.getInstance().cookieHeader.isEmpty())
                {
                    boolean isValid = true;

                    Editable getFoodNameText = getFoodName.getText();
                    if (getFoodNameText.toString().isEmpty())
                    {
                        getFoodName.setError("This field cannot be empty!");
                        isValid = false;
                    }
                    Editable getFoodSourceText = getFoodSource.getText();
                    if (getFoodSourceText.toString().isEmpty())
                    {
                        getFoodSource.setError("This field cannot be empty!");
                        isValid = false;
                    }
                    if (getFoodType0.isChecked())
                    {
                        getFoodType = 0;
                    }
                    else if (getFoodType1.isChecked())
                    {
                        getFoodType = 1;
                    }
                    Editable getFoodDescriptionText = getFoodDescription.getText();
                    if (getFoodDescriptionText.toString().isEmpty())
                    {
                        getFoodDescription.setError("This field cannot be empty!");
                        isValid = false;
                    }
                    Editable getFoodReviewMsgText = getFoodReviewMsg.getText();
                    if (getFoodReviewMsgText.toString().isEmpty())
                    {
                        getFoodReviewMsg.setError("This field cannot be empty!");
                        isValid = false;
                    }
                    float getFoodReviewRatingRate = getFoodReviewRating.getRating();
                    getFoodReviewRatingRate = getFoodReviewRatingRate * 2;
                    int getFoodReviewRatingRateInt = (int) getFoodReviewRatingRate;
                    Editable getFoodReviewRatingNegText = getFoodReviewRatingNeg.getText();
                    if (getFoodReviewRatingNegText.toString().isEmpty())
                    {
                        getFoodReviewRatingNeg.setError("This field cannot be empty!");
                    }
                    Editable getFoodReviewRatingPosText = getFoodReviewRatingPos.getText();
                    if (getFoodReviewRatingPosText.toString().isEmpty())
                    {
                        getFoodReviewRatingPos.setError("This field cannot be empty!");
                    }


                    if (isValid)
                    {
                        String testFood = "{\n" +
                                " \"name\":" +"\"" + getFoodNameText + "\",\n" +
                                " \"source\":\"" + getFoodSourceText + "\",\n" +
                                " \"type\":\"" + getFoodType + "\",\n" +
                                " \"description\": \"" + getFoodDescriptionText + "\",\n" +
                                " \"review\":\n" +
                                "{ \"msg\":\"" + getFoodReviewMsgText +"\",\n" +
                                "\"rating\": \"" + getFoodReviewRatingRateInt +"\",\n" +
                                "\"negative_points\": [\"" + getFoodReviewRatingNegText + "\"],\n" +
                                "\"positive_points\": [\"" + getFoodReviewRatingPosText + "\"]}\n" +
                                "}";

                        Singleton.getInstance().requestBody = testFood;
                        Singleton.getInstance().setUrlOperation("/food/create");
                        try {
                            jsonRequest.postMethod(v.getContext(), new JsonRequest.VolleyCallBack() {
                                @Override
                                public void onSuccess() throws JSONException
                                {
                                    String stringToPassBack = "Success";
                                    Intent intent = new Intent();
                                    intent.putExtra("status_create", stringToPassBack);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Snackbar.make(v, "You must be logged in !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}
