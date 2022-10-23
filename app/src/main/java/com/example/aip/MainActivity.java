package com.example.aip;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView newAdds;
    List<RecentNames> newNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getData();

        Button button = findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                scanQrCodeActivity(view);

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

    public void getData() {
        String url = "http://192.168.43.205:8080/api/register";
        //String url = "http://10.0.2.2:8080/api/register";
        RequestQueue getRequestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        String []recentNames = new String[response.length()];
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
                            newNames.add(new RecentNames(recentNames[i]));
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
}