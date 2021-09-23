package com.webservice.gujumart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chaos.view.PinView;
import com.webservice.gujumart.slidesproductview.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OTPVerification extends AppCompatActivity {

    PinView pinView;
    Intent intent;
    SharedPreferences sharedPreferences;

    public static final String fileName = "login";
    public static final String Token = "token";
    public static final String ID = "id";
    public static final String Mobile = "mobile";

    //For alet  dialog with progress bar
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_verification);

        pinView = findViewById(R.id.pin_view);

        // Initilizating the progress bar
        loadingDialog = new LoadingDialog(OTPVerification.this);
    }

    public void goToHomeFromOTP(View view) {

        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }


    public void verifyOTP(View view) {

        final String otpis = pinView.getText().toString();
        intent = getIntent();
        String recivedotp = intent.getStringExtra("otpnumber");
        String recivedmobile = intent.getStringExtra("mobilenumber");

        sharedPreferences = getSharedPreferences(fileName, MODE_PRIVATE);
        if (sharedPreferences.contains(Token)){
            Intent i = new Intent(OTPVerification.this, Dashboard.class);
            startActivity(i);
            Toast.makeText(this, "You are Already Log in", Toast.LENGTH_SHORT).show();
        }


       else if (recivedotp.equals(otpis)) {

            RequestQueue requestQueue = Volley.newRequestQueue(OTPVerification.this);
            String url = " https://www.gujumart.com/api-sign-up";



           StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //Log.v("LOG_VOLLEY", response);

                    try {
                        JSONObject jObj = new JSONObject(response);
                        String token = jObj.getString("token");
                        String id = jObj.getString("id");
                        String mobile = jObj.getString("mobile");

                        //Toast.makeText(OTPVerification.this, mobile, Toast.LENGTH_SHORT).show();


                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(Token, token);
                        editor.putString(ID, id);
                        editor.putString(Mobile, mobile);
                        editor.commit();
                        Toast.makeText(OTPVerification.this, "You are logined successfully"+id, Toast.LENGTH_LONG).show();
                        Intent i = new Intent(OTPVerification.this, Dashboard.class);
                        startActivity(i);
                        finish();

                    } catch (JSONException e) {
                         e.printStackTrace();
                    }




                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(OTPVerification.this, "Error occurred, Please Login again ", Toast.LENGTH_SHORT).show();

                }
            }) {
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("mobile", recivedmobile);
                    //params.put("otp", randomnumber);
                    return params;
                }
            };
            requestQueue.add(stringRequest);

            //here the Progress bar Alert dialog start for 5 seond
            loadingDialog.startLoadingDialog();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.dismissDialog();

                }
            }, 1000);


        } else {
            Toast.makeText(OTPVerification.this, "OTP not Match"+recivedotp, Toast.LENGTH_LONG).show();
        }
    }
}
