package com.example.aip;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QrCodeScanner extends AppCompatActivity implements View.OnClickListener {
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
        getSupportActionBar().setIcon(R.drawable.ic_baseline_qr_code_scanner_24);

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