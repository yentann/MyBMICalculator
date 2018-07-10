package com.example.a17045679.mybmicalculator;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText Weight;
    EditText Height;
    Button Calculate;
    Button Reset;
    TextView Date;
    TextView BMI;
    TextView Overweight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Weight = findViewById(R.id.editTextWeight);
        Height = findViewById(R.id.editTextHeight);
        Calculate = findViewById(R.id.buttonCalculate);
        Reset = findViewById(R.id.buttonReset);
        Date = findViewById(R.id.textViewDate);
        BMI = findViewById(R.id.textViewBMI);
        Overweight = findViewById(R.id.textViewOverWeight);


        Calendar now = Calendar.getInstance();  //Create a Calendar object with current date and time
        final String datetime = now.get(Calendar.DAY_OF_MONTH) + "/" +
                (now.get(Calendar.MONTH)+1) + "/" +
                now.get(Calendar.YEAR) + " " +
                now.get(Calendar.HOUR_OF_DAY) + ":" +
                now.get(Calendar.MINUTE);



        //Calculate
        Calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float floatWeight = Float.parseFloat(Weight.getText().toString());
                float floatHeight = Float.parseFloat(Height.getText().toString());
                final float Total = floatWeight/(floatHeight*floatHeight);

                Date.setText("Last Calculated Date: " + datetime);
                BMI.setText("Last Calculated BMI: " + Total);




                //The lastest update into the latest Database
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor prefEdit = prefs.edit();
                prefEdit.putString("Date",datetime);
                prefEdit.putFloat("BMI",Total);

                if(Total < 18.5) {
                    Overweight.setText("You are underweight");
                }
                else if(Total >= 18.5 && Total <= 24.9) {
                    Overweight.setText("Your BMI is normal");
                }
                else if(Total >=25 && Total <= 29.9) {
                    Overweight.setText("You are overweight");
                }
                else{
                    Overweight.setText("You are obese");
                }

                Weight.setText("");
                Height.setText("");


            }
        });


        //Reset
        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Weight.setText("");
                Height.setText("");

                //clear the database & commit to the database
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor prefEdit = prefs.edit();
                prefEdit.clear();
                prefEdit.commit();

                Date.setText("Last Calculated Date: ");
                BMI.setText("Last Calculated BMI: ");
                Overweight.setText("");

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();

        // Step 1a: Get the user input from the EditText and store it in a variable
        float weight = Float.parseFloat(Weight.getText().toString());
        float height = Float.parseFloat(Height.getText().toString());
        float bmi = weight/(height*height);


        // Step 1b: Obtain an instance of the SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Step 1c: Obtain an instance of the SharedPreferences Editor for update later
        SharedPreferences.Editor prefEdit = prefs.edit();

        // Step 1d: Add the key-value pair
        prefEdit.putFloat("Weight", weight);
        prefEdit.putFloat("Height", height);
        prefEdit.putString("Date", "");
        prefEdit.putFloat("BMI", bmi);


        // Step 1e: Call commit() method to save the changes into SharedPreferences
        prefEdit.commit();
    }

    //onResume
    @Override
    protected void onResume() {
        super.onResume();


        // Step 2a: Obtain an instance of the SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEdit = prefs.edit();

        // Step 2b: Retrieve the saved data from the SharedPreferences object
        float msg1 = prefs.getFloat("Weight", 0.0f);
        float msg2 = prefs.getFloat("Height", 0.0f);
        String msg3 = prefs.getString("Date", "");
        float msg4 = prefs.getFloat("BMI", 0.0f);


        // Step 2c: Update the UI element with the value

        Date.setText("Last Calculated Date: " + msg3);
        BMI.setText("Last Calculated BMI: " + msg4);

    }

}