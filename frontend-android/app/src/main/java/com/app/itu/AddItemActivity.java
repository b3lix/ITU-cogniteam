/*
 *   Projekt ITU
 *       Autori:
 *           xslesa01 (Michal Šlesár)
 */
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
import android.widget.Toast;

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
                if (!Singleton.getInstance().cookieHeader.isEmpty())
                {
                    boolean isValid = true;

                    Editable getFoodNameText = getFoodName.getText();
                    if (getFoodNameText.toString().isEmpty())
                    {
                        getFoodName.setError("Toto pole  nemôže byť prázdne !");
                        isValid = false;
                    }
                    Editable getFoodSourceText = getFoodSource.getText();
                    if (getFoodSourceText.toString().isEmpty())
                    {
                        getFoodSource.setError("Toto pole  nemôže byť prázdne !");
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
                        getFoodDescription.setError("Toto pole  nemôže byť prázdne !");
                        isValid = false;
                    }
                    Editable getFoodReviewMsgText = getFoodReviewMsg.getText();
                    if (getFoodReviewMsgText.toString().isEmpty())
                    {
                        getFoodReviewMsg.setError("Toto pole  nemôže byť prázdne !");
                        isValid = false;
                    }
                    float getFoodReviewRatingRate = getFoodReviewRating.getRating();
                    getFoodReviewRatingRate = getFoodReviewRatingRate;
                    int getFoodReviewRatingRateInt = (int) getFoodReviewRatingRate;
                    Editable getFoodReviewRatingNegText = getFoodReviewRatingNeg.getText();
                    if (getFoodReviewRatingNegText.toString().isEmpty())
                    {
                        getFoodReviewRatingNeg.setError("Toto pole  nemôže byť prázdne !");
                    }
                    Editable getFoodReviewRatingPosText = getFoodReviewRatingPos.getText();
                    if (getFoodReviewRatingPosText.toString().isEmpty())
                    {
                        getFoodReviewRatingPos.setError("Toto pole  nemôže byť prázdne !");
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

                                @Override
                                public void onFail()
                                {
                                    Toast.makeText(getApplicationContext(),"Toto jedlo už existuje !", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else
                {
                    Snackbar.make(v, "Musíš byť prihlásený !", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}
