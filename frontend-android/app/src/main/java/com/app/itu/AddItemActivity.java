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
    EditText getFoodReviewMsg;
    EditText getFoodReviewRating;
    EditText getFoodReviewRatingNeg;
    EditText getFoodReviewRatingPos;

    Button setFood;

    JsonRequest jsonRequest = new JsonRequest();

    private void setupUI()
    {
        getFoodName = findViewById(R.id.editTextTextFoodName);
        getFoodSource = findViewById(R.id.editTextTextFoodSource);
        getFoodType = findViewById(R.id.editTextTextFoodType);
        getFoodDescription = findViewById(R.id.editTextTextFoodDescription);
        getFoodReviewMsg = findViewById(R.id.editTextTextFoodReviewMsg);
        getFoodReviewRating = findViewById(R.id.editTextTextFoodReviewRating);
        getFoodReviewRatingNeg = findViewById(R.id.editTextTextFoodReviewRatingNeg);
        getFoodReviewRatingPos = findViewById(R.id.editTextTextFoodReviewRatingPos);

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
                Editable getFoodSourceText = getFoodSource.getText();
                if (getFoodSourceText.toString().isEmpty())
                {
                    getFoodSource.setError("This field cannot be empty!");
                    isValid = false;
                }
                Editable getFoodTypeText = getFoodType.getText();
                if (getFoodTypeText.toString().isEmpty())
                {
                    getFoodType.setError("This field cannot be empty!");
                    isValid = false;
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
                Editable getFoodReviewRatingText = getFoodReviewRating.getText();
                if (getFoodReviewRatingText.toString().isEmpty())
                {
                    getFoodReviewRating.setError("This field cannot be empty!");
                }
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
                            " \"type\":\"" + getFoodTypeText + "\",\n" +
                            " \"description\": \"" + getFoodDescriptionText + "\",\n" +
                            " \"review\":\n" +
                            "{ \"msg\":\"" + getFoodReviewMsgText +"\",\n" +
                            "\"type\":\""+ getFoodTypeText +"\",\n" +
                            "\"rating\": \"" + getFoodReviewRatingText +"\",\n" +
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
        });

    }
}
