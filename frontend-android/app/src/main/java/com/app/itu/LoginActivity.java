package com.app.itu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button login;
    JsonRequest jsonRequest = new JsonRequest();

    private void setupUI() {
        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.activityButtonLogin);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setupUI();
        setupListeners();
    }

    private void setupListeners() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isValid = true;

                Editable usernameText = username.getText();
                if (usernameText.toString().isEmpty() || usernameText.toString().length() < 3)
                {
                    username.setError("You must enter valid username ! At least 3 chars long !");
                    isValid = false;
                }

                Editable passwordText = password.getText();
                if(passwordText.toString().isEmpty() || passwordText.toString().length() < 3)
                {
                    password.setError("You must enter valid password ! At least 3 chars long !");
                    isValid = false;
                }
                if(isValid)
                {
                    Singleton.getInstance().setUrlOperation("/auth/login");
                    Singleton.getInstance().requestBody = "{\n" +
                            "  \"username\":" + "\"" + usernameText.toString() + "\",\n" +
                            "  \"password\":" + "\"" + passwordText.toString() + "\",\n" +
                            "  \"Content-Type\":\"application/json\",\n" +
                            "  \"skipCrossSell\":true\n" +
                            "}";
                    try {
                        jsonRequest.postMethod(v.getContext(), new JsonRequest.VolleyCallBack() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onSuccess() throws JSONException {
                                Snackbar snack = Snackbar.make(v, "OK Logged in!", Snackbar.LENGTH_LONG);
                                View view = snack.getView();
                                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
                                params.gravity = Gravity.TOP;
                                view.setLayoutParams(params);
                                snack.show();

                                String stringToPassBack = "Logged in";
                                Intent intent = new Intent();
                                intent.putExtra("status_logged_in", stringToPassBack);
                                intent.putExtra("username", usernameText.toString());

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

//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Intent i = new Intent(LoginActivity.this, RegistrationActivity.class);
//                //startActivity(i);
//            }
//        });
    }

}
