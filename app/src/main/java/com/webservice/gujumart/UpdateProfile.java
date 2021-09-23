package com.webservice.gujumart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.service.autofill.RegexValidator;
import android.util.Patterns;
import android.view.MenuItem;
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
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.webservice.gujumart.slidesproductview.LoadingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    String ex_userid, ex_addrssid, ex_pincode, ex_address, ex_email, ex_district, ex_name, ex_mobile;
    String mainToken;
    Intent intent;

    String final_name, final_pinode, final_address, final_district, final_email;

    EditText pr_name, pr_email, pr_mobile, pr_address, pr_distict, pr_pincode;

    //For validation proess
    AwesomeValidation awesomeValidation;

    //For alet  dialog with progress bar
    LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        pr_name = findViewById(R.id.id_name);
        pr_email = findViewById(R.id.id_email);
        pr_mobile = findViewById(R.id.id_mobile);
        pr_mobile.setKeyListener(null);
        pr_address = findViewById(R.id.id_fulladdress);
        pr_distict = findViewById(R.id.id_distrit);
        pr_pincode = findViewById(R.id.id_pincode);

        //Initilization the validation
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        //// Add the validation
        awesomeValidation.addValidation(this, R.id.id_name, RegexTemplate.NOT_EMPTY, R.string.Invalid_name);

        awesomeValidation.addValidation(this, R.id.id_email, Patterns.EMAIL_ADDRESS, R.string.Invalid_email);

        awesomeValidation.addValidation(this, R.id.id_fulladdress, RegexTemplate.NOT_EMPTY, R.string.Invalid_fulladdress);

        awesomeValidation.addValidation(this, R.id.id_distrit, RegexTemplate.NOT_EMPTY, R.string.Invalid_district);

        awesomeValidation.addValidation(this, R.id.id_pincode, RegexTemplate.NOT_EMPTY, R.string.Invalid_pincode);

        // Initilizating the progress bar
        loadingDialog = new LoadingDialog(UpdateProfile.this);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.bottom_nav_cart:
                        Intent intent = new Intent(getApplicationContext(), Cart.class);
                        startActivity(intent);

                        break;
                    case R.id.bottom_nav_dashboard:
                        Intent intentdash = new Intent(getApplicationContext(), Dashboard.class);
                        startActivity(intentdash);
                        finish();
                        break;

                    case R.id.bottom_nav_wishlist:
                        Intent intentwish = new Intent(getApplicationContext(), WishList.class);
                        startActivity(intentwish);
                        break;

                }
                return true;
            }
        });


        //----------Here is get the info from last Activity
        intent = getIntent();
        mainToken = intent.getStringExtra("shr_token");
      //  Toast.makeText(getApplicationContext(), mainToken, Toast.LENGTH_SHORT).show();



        ex_pincode = intent.getStringExtra("pin_code");


        if (ex_pincode != null){

            ex_userid = intent.getStringExtra("userid");
            ex_addrssid = intent.getStringExtra("addressid");
            ex_address = intent.getStringExtra("fulladdress");
            ex_email = intent.getStringExtra("e_mail");
            ex_district = intent.getStringExtra("district");
            ex_name = intent.getStringExtra("name");
            ex_mobile = intent.getStringExtra("mobile");

            pr_name.setText(ex_name);
            pr_pincode.setText(ex_pincode);
            pr_mobile.setText(ex_mobile);
            pr_address.setText(ex_address);
            pr_distict.setText(ex_district);
            pr_email.setText(ex_email);
        }
        else{

            ex_userid = intent.getStringExtra("shr_userid");
            ex_mobile = intent.getStringExtra("shr_mobile");
            ex_addrssid = "0";
            Toast.makeText(getApplicationContext(), ex_userid, Toast.LENGTH_SHORT).show();
            pr_mobile.setText(ex_mobile);
        }

    }

    public void insertAddress() {

        Toast.makeText(this, "New Address Updated", Toast.LENGTH_SHORT).show();
    }

    public void profileUpdated(View view) {

        if (awesomeValidation.validate()) {

            final_name = pr_name.getText().toString();
            final_pinode = pr_pincode.getText().toString();
            final_address = pr_address.getText().toString();
            final_district = pr_distict.getText().toString();
            final_email = pr_email.getText().toString();

            RequestQueue requestQueue = Volley.newRequestQueue(UpdateProfile.this);
            String url = "https://www.gujumart.com/api-setUserAddess";


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //Log.v("LOG_VOLLEY", response);

                    try {
                        JSONObject jObj = new JSONObject(response);
                        String status = jObj.getString("status");
                        // String id = jObj.getString("id");
                        //String mobile = jObj.getString("mobile");


                        Toast.makeText(getApplicationContext(), "Successfully Updated ", Toast.LENGTH_SHORT).show();
                        Intent moveintent = new Intent(getApplicationContext(), MyProfile.class);
                        startActivity(moveintent);
                        finish();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(UpdateProfile.this, "Error occurred, Please do again ", Toast.LENGTH_SHORT).show();

                }
            }) {
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<String, String>();
                    header.put("token", mainToken);
                    return header;
                }

                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId", ex_userid);
                    params.put("mobile", ex_mobile);
                    params.put("address_id", ex_addrssid);
                    params.put("name", final_name);
                    params.put("pincode", final_pinode);
                    params.put("address", final_address);
                    params.put("email", final_email);
                    params.put("district", final_district);
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
            }, 2000);

        }
        else{
            Toast.makeText(getApplicationContext(), "Please Fill All Column", Toast.LENGTH_SHORT).show();
        }

    }

    public void backToHome(View view) {

        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
        finish();
    }
}

