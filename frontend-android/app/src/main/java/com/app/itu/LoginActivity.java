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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity
{
    private static final int REGISTER_ACTIVITY_REQUEST_CODE = 11;
    EditText username;
    EditText password;
    Button login;
    Button register;
    JsonRequest jsonRequest = new JsonRequest();

    private void setupUI()
    {
        username = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPassword);
        login = findViewById(R.id.activityButtonLogin);
        register = findViewById(R.id.activityButtonRegister);
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

                                String stringToPassBack = "Log out";
                                Intent intent = new Intent();
                                intent.putExtra("status_logged_in", stringToPassBack);
                                intent.putExtra("username", usernameText.toString());

                                setResult(RESULT_OK, intent);

                                finish();

                            }

                            @Override
                            public void onFail()
                            {
                                Toast.makeText(getApplicationContext(),"Nesprávne prihlasovacie údaje skúste ešte raz !", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
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
                if (isValid)
                {
                    Singleton.getInstance().setUrlOperation("/auth/register");
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

                                String stringToPassBack = "Register";
                                Intent intent = new Intent();
                                intent.putExtra("status_logged_in", stringToPassBack);
                                intent.putExtra("username", usernameText.toString());

                                setResult(RESULT_OK, intent);

                                finish();

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
