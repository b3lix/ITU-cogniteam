/*
 *  Projekt ITU
 *      Autori:
 *          xnosko06 (Matúš Nosko)
 */

package com.app.itu;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.app.itu.databinding.ActivityMainBinding;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Kontaktujte nás example.email@smthg.com !", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        View headView = navigationView.getHeaderView(0);
        View buttonView = headView.findViewById(R.id.buttonAcc);
        buttonView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Button tmpButt =(Button)findViewById(R.id.buttonAcc);
                TextView textView = (TextView)findViewById(R.id.textView);
                String textViewString = (String) textView.getText();
                String actionText = (String) tmpButt.getText();

                if (actionText.equals("Log in"))
                {
                    Intent myIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivityForResult(myIntent, SECOND_ACTIVITY_REQUEST_CODE);
                }
                else if (actionText.equals("Log out"))
                {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    //Yes button clicked
                                    JsonRequest jsonRequest = new JsonRequest();
                                    try {
                                        Singleton.getInstance().setUrlOperation("/auth/logout");
                                        Singleton.getInstance().requestBody = "{"+"\"username\":"+ textViewString +"}";
                                        jsonRequest.postMethod(view.getContext(),new JsonRequest.VolleyCallBack() {
                                            @SuppressLint("SetTextI18n")
                                            @Override
                                            public void onSuccess() throws JSONException
                                            {
                                                Button button = (Button)findViewById(R.id.buttonAcc);
                                                button.setText("Log in");
                                                Singleton.getInstance().cookieHeader = "";
                                                button.setBackgroundColor(Color.parseColor("#FF673AB7"));
                                                TextView textView = (TextView)findViewById(R.id.textView);
                                                textView.setText("");
                                                Snackbar.make(view, "Odhlásenie bolo úspešné !", Snackbar.LENGTH_LONG)
                                                        .setAction("Action", null).show();
                                            }

                                            @Override
                                            public void onFail() {

                                            }
                                        });
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Ste si istý, že sa chcete odhlásiť ?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                }

            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
            { // Activity.RESULT_OK

                // get String data from Intent
                String returnString = data.getStringExtra("status_logged_in");
                if (returnString.equals("Log out"))
                {
                    String username = data.getStringExtra("username");
                    Singleton.getInstance().username = username;
                    Button button = (Button)findViewById(R.id.buttonAcc);
                    button.setText(returnString);

                    button.setBackgroundColor(Color.GREEN);
                    TextView textView = (TextView)findViewById(R.id.textView);
                    textView.setText(username);
                    Toast.makeText(getApplicationContext(),"Prihlásenie bolo úspešné !", Toast.LENGTH_SHORT).show();
                }
                else if (returnString.equals("Register"))
                {
                    String username = data.getStringExtra("username");
                    Toast.makeText(getApplicationContext(),"Ahoj " + username + " si prihlásený !", Toast.LENGTH_SHORT).show();
                }

                // set text view with string
            }
        }
    }
}