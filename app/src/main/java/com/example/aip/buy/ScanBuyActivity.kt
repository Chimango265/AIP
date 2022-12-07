package com.example.aip.buy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.aip.R
import com.example.aip.databinding.ActivityScanBuyBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.json.JSONObject

class ScanBuyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBuyBinding
    private var qrScanIntegrator: IntentIntegrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBuyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //setting action bar back button
        supportActionBar!!.title = "scan QR code to buy"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //functions to call the scan intent
        setOnClickListener()
        setupScanner()
    }

    fun launchDetailsActivity(benefiaryName: String, nationalId: String) {

        //lauching DisplayFarmActivity
        val intent = Intent(applicationContext, DisplayFarmActivity::class.java)
        intent.putExtra("beneficiaryName", benefiaryName)
        intent.putExtra("nationalId", nationalId)

        startActivity(intent)
    }

    private fun setupScanner() {
        qrScanIntegrator = IntentIntegrator(this);
        qrScanIntegrator?.setOrientationLocked(false)
    }

    private fun setOnClickListener() {
        binding.scan.setOnClickListener { scanQRCode() }
    }

    private fun scanQRCode() {
        qrScanIntegrator?.initiateScan()

    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            //IF QRCode has no data
            if (result.contents == null) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_LONG).show()
            }

            else {
                var qrDetails = result.contents // data from scanned QR code
                var details: Array<String> = getUsefulStrings2(getUsefulStrings(qrDetails)) //getting only the national id and name from the scanned data
                binding.name.text = details[0] // setting name on the name textfield
                binding.nationalId.text = details[1] // setting national ID on textfield

                launchDetailsActivity(details[0], details[1]) // call function to launch activity and pass data
            }

        }else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    //removing < characters from data scanned from QR code
   fun getUsefulStrings(text: String): String {
       val pattern = "<*";

       val newText = text.replace(pattern.toRegex(), "")

       return newText

   }
     // getting name and national id and returning them as array
    fun getUsefulStrings2(text2: String): Array<String> {
        var text = getUsefulStrings(text2)
        var i: Int = text.length
        var beneficiaryInfo = arrayOf("", " ")
        var name = " "
        var nationalId = ""
        var occur = 0

       for(j in 0..i) {
           if (text.get(j) == '~') {
               occur++;
           }
           if(occur == 4) {
               name += text[j]
           } else if(occur == 5) {
               nationalId += text[j]
           } else if (occur == 6) {
               name = " " + name + text[j]
           } else if (occur == 7) {
               break
           }
       }

        var pattern = "~+"
        name = name.replace(pattern.toRegex(), " ")
        nationalId = nationalId.replace(pattern.toRegex(), " ")

        beneficiaryInfo[0] = name
        beneficiaryInfo[1] = nationalId

        return beneficiaryInfo
    }
}