/*
 *   Projekt ITU
 *       Autori:
 *           xbelko02 (Belko Erik)
 */
package com.app.itu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.itu.JsonRequest;
import com.app.itu.R;
import com.app.itu.Singleton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

public class AddReviewActivity extends AppCompatActivity {

    EditText getFoodPrice;
    EditText getFoodReviewMsg;
    RatingBar getFoodReviewRating;
    EditText getFoodReviewRatingNeg;
    EditText getFoodReviewRatingPos;
    Button addReview;
    String food_id;

    JsonRequest jsonRequest = new JsonRequest();

    private void setupUI()
    {
        getFoodPrice = findViewById(R.id.editTextTextFoodPrice);
        getFoodReviewMsg = findViewById(R.id.editTextTextFoodReviewMsg);
        getFoodReviewRating = findViewById(R.id.editTextTextFoodReviewRating);
        getFoodReviewRatingNeg = findViewById(R.id.editTextTextFoodReviewRatingNeg);
        getFoodReviewRatingPos = findViewById(R.id.editTextTextFoodReviewRatingPos);
        addReview = findViewById(R.id.activityButtonSubmit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addreview);
        Bundle b = getIntent().getExtras();
        if (b != null)
        {
            food_id = b.getString("food_id");
        }
        setupUI();
        setupListeners();
    }


    private void setupListeners()
    {
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!Singleton.getInstance().cookieHeader.isEmpty())
                {
                    boolean isValid = true;

                    Editable getFoodPriceText = getFoodPrice.getText();
                    if (getFoodPriceText.toString().isEmpty())
                    {
                        getFoodPrice.setError("Toto pole  nemôže byť prázdne !");
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
//                        tuto treba dostat id jedla
                        String testFood = "{\n" +
                                " \"food\": " +"\"" + food_id + "\",\n" +
                                " \"description\": \"" + getFoodReviewMsgText +"\",\n" +
                                " \"positive_points\": [\"" + getFoodReviewRatingPosText + "\"],\n" +
                                " \"negative_points\": [\"" + getFoodReviewRatingNegText + "\"],\n" +
                                " \"price\": \"" + getFoodPriceText +"\",\n" +
                                " \"rating\": \"" + getFoodReviewRatingRateInt +"\"\n" +
                                " }";

                        Singleton.getInstance().requestBody = testFood;
                        Singleton.getInstance().setUrlOperation("/reviews/create");
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
                                    Toast.makeText(getApplicationContext(),"Nepodarilo sa pridať recenziu !", Toast.LENGTH_SHORT).show();
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
