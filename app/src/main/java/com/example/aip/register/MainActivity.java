package com.example.aip.register;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.aip.R;
import com.example.aip.buy.ScanBuyActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    ListView newAdds;
    List<RecentNames> newNames; //Names recently registered

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData(); //get user data from the database

        Button button = findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                scanQrCodeActivity(view); //Open new activity to scan Qr code

            }
        });

        Button buy = findViewById(R.id.button);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanQrCodeBuy(view);

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void scanQrCodeActivity(View v) {

        Intent i = new Intent(this, QrCodeScanner.class);
        startActivity(i);
    }

    public void scanQrCodeBuy(View v) {

        Intent i = new Intent(this, ScanBuyActivity.class);
        startActivity(i);
    }

    //Method to get data from the database using volley api
    public void getData() {
        String url = "https://aipbackend.herokuapp.com/api/register";
        //String url = "http://10.0.2.2:8080/api/register";
        RequestQueue getRequestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        String []recentNames = new String[response.length()]; //Array to store received data
                        for(int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jo = response.getJSONObject(i);
                                recentNames[i] = jo.getString("fullName");
                                Log.i(TAG,recentNames[i]);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        newNames = new ArrayList<>();

                        for(int i = 0; i < response.length(); i++) {
                            newNames.add(new RecentNames((recentNames[i].toLowerCase()).trim())); //List to be used by adapater
                        }

                        newAdds = (ListView) findViewById(R.id.list);

                        RecentAdapter adapter = new RecentAdapter(MainActivity.this, R.layout.recent_adds, newNames);
                        newAdds.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(TAG, "Error :" + error.toString());

                    }
                });
        getRequestQueue.add(jsonArrayRequest);
        //Log.i(TAG, recentNames[1]);

    }

    public static class QrCodeScanner extends AppCompatActivity implements View.OnClickListener {
        ImageView scanIcon;
        TextView messageText, idText;
        Button scanBtn;
        String name;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_qr_code_scanner);

            scanIcon = findViewById(R.id.imageView8);
            messageText = findViewById(R.id.textView4);
            idText = findViewById(R.id.textView6);
            //scanBtn = findViewById(R.id.button3);

            scanIcon.setOnClickListener(this);

            getSupportActionBar().setTitle("Scan QR code");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Text watcher object to get change activity after changing the contents of textView
            messageText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void afterTextChanged(Editable editable) {
                    registrationActivity();
                }
            });
        }


        public void registrationActivity() {

            String [] details = getUsefulDetails2(getUsefulDetails(name)); // get a string array with name and national Id

            Intent i = new Intent(getApplicationContext(), Registration.class);
            i.putExtra("beneficiary_name", details[0]); // send name to the registration activity
            i.putExtra("national_id", details[1]); // send Id number to the registration activity

            startActivity(i);
        }

        @Override
        public void onClick(View v) {
            IntentIntegrator intentIntegrator = new IntentIntegrator(this);
            intentIntegrator.setPrompt("Scan id QR code");
            intentIntegrator.setOrientationLocked(true);
            intentIntegrator.initiateScan();
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

            if (intentResult != null) {
                if (intentResult.getContents() == null) {
                    Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                } else {
                    // if the intentResult is not null we'll set
                    // the content and format of scan message
                    name = intentResult.getContents();

                    String [] details = getUsefulDetails2(getUsefulDetails(name));

                    messageText.setText(details[0]);
                    idText.setText(details[1]);
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }

        }

        //Method to remove <<<< characters from the id
        public static String getUsefulDetails(String text) {
            String pattern = "<*"; //the pattern to be removed with regex
            String replace = ""; // to be replaced with no characters
            Pattern r = Pattern.compile(pattern);

            Matcher m = r.matcher(text);

            text = m.replaceAll(replace);

            return text; // return the new text
        }

        // Method to get Name only
        public static String[] getUsefulDetails2(String text ) {
            int i = text.length();
            String [] beneficiaryInfo = new String[2];
            String name = " ";
            String nationalId = " ";
            int occur = 0; // the occurence of ~ character which separate details on the national ID

            for (int j = 0; j < i; j++) { // loop through the given string

                if (text.charAt(j) == '~') { //check for the ~ character
                    occur++;
                }

                if (occur == 4) { // last name starts at the 4th occurence of the ~ character
                    name = name + text.charAt(j);
                }
                else if(occur == 5) {
                    nationalId = nationalId + text.charAt(j); // the 5th occurence of ~ is for ID number
                }
                else if (occur == 6) { // the 6th occurence of ~ is for first name
                    name = " " + name + text.charAt(j);
                }
                else if (occur == 7){
                    break;
                }
            }

            //remove the all the ~
            String pattern = "~+";
            String replace = " ";
            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(name);
            name = m.replaceAll(replace);

            Matcher n = r.matcher(nationalId);
            nationalId = n.replaceAll(replace);

            beneficiaryInfo[0] = name;
            beneficiaryInfo[1] = nationalId;

            return beneficiaryInfo; //return full name!
        }

    }
}