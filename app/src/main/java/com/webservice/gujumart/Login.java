package com.webservice.gujumart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.webservice.gujumart.slidesproductview.LoadingDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Login extends AppCompatActivity {

    EditText txtPhone;
    String randomnumber = null;

    //For alet  dialog with progress bar
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R    .layout.activity_login);

        txtPhone = findViewById(R.id.signup_phone_number);

        // Initilizating the progress bar
        loadingDialog = new LoadingDialog(Login.this);
    }

    public void checkOTP(View view) {
        //-----First Start the Progress bar----
        loadingDialog.startLoadingDialog();

        //------Send the OTP via API, as per below------
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        String url = "https://www.gujumart.com/api-sendOtp";

        final String numbers = txtPhone.getText().toString();

        Random random = new Random();
        randomnumber = String.valueOf(100000 + random.nextInt(999999));

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //------First need to lose the progressbar, as per below mentioned-----
                loadingDialog.dismissDialog();

                // show the message as per the below mentioned
                Toast.makeText(Login.this, "msg sent"+ randomnumber, Toast.LENGTH_LONG).show();

                  Intent intent = new Intent(getApplicationContext(), OTPVerification.class);
                  intent.putExtra("otpnumber", randomnumber );
                  intent.putExtra("mobilenumber", numbers);
                  startActivity(intent);
                  finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "msg not sent", Toast.LENGTH_SHORT).show();

            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", numbers);
                params.put("otp", randomnumber);
                return params;
            }
        };
        requestQueue.add(stringRequest);

        //here the Progress bar Alert dialog start for 5 seond
      /*  loadingDialog.startLoadingDialog();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissDialog();

            }
        }, 1000);  */

       /* Intent intent = new Intent(getApplicationContext(), OTPVerification.class);
        startActivity(intent);  */
    }
}
