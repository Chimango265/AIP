package com.example.aip.register;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.aip.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    TextView displayName;
    EditText soil, location, size, crop;
    Button btn;
    String name, nationalId;

    Boolean activityDecider = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //setting action bar back button
        getSupportActionBar().setTitle("Register beneficiary");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        displayName = findViewById(R.id.textView5);
        btn = findViewById(R.id.button3);

        soil = findViewById(R.id.soil);
        location = findViewById(R.id.location);
        size = findViewById(R.id.size);
        crop = findViewById(R.id.crop);

        //getting data from QR code scanner activity
        Intent intent = getIntent();
        name = intent.getStringExtra("beneficiary_name");
        nationalId = intent.getStringExtra("national_id");
        displayName.setText("Chimango Ng'oma" + " Farm Details");

        // queue for API request and response using Volley library
        RequestQueue queue = Volley.newRequestQueue(this);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String soilHealth = soil.getText().toString();
                String locationText = location.getText().toString();
                int farmSize = Integer.parseInt(size.getText().toString());
                String mainCrop = crop.getText().toString();

                postData(soilHealth, locationText, mainCrop, farmSize);// calling method to send API requests
            }
        });

    }

    private void postData(String soilHealth, String locationText, String mainCrop, int farmSize) {
        String url = "https://aipbackend.herokuapp.com/api/register"; // url to process the API request

        RequestQueue queue = Volley.newRequestQueue(Registration.this); // volley request Object

        JSONObject farmer = new JSONObject(); // JSON object to hold farmer details
        JSONObject farm = new JSONObject(); // JSON object to hold farm details

        try {
            farm.put("farmSize", farmSize);
            farm.put("mainCrop", mainCrop);
            farm.put("location", location);
            farmer.put("farm", farm); // farmer owns the farm
            farmer.put("bankAccount",667539209);
            farmer.put("fullName",name);
            farmer.put("national_id", nationalId.trim());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, farmer, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Toast.makeText(Registration.this, "Data sent to PI", Toast.LENGTH_SHORT).show();
                try {
                    Intent i = new Intent(Registration.this, ConfirmSubsidy.class);
                    i.putExtra("beneficiary_name", name); // send name to the registration activity
                    i.putExtra("national_id", nationalId.trim()); // send Id number to the registration activity
                    startActivity(i);
                    Log.i(TAG, response.getString("fullName"));
                } catch(JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(Registration.this, "Fail to get response = " + error, Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> headers = new HashMap<String, String>();

                //headers for the POST request
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Accept", "*/*; charset=utf-8");

                return headers;
            }

        };
        queue.add(request);

    }
}