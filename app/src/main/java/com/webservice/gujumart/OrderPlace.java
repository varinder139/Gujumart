package com.webservice.gujumart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webservice.gujumart.recyclerhelperclass.CartGetterSetter;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class OrderPlace extends AppCompatActivity {

    TextView name, mobile, email, pincode, district, fullAddress;
    String ex_userid, ex_addrssid, ex_pincode, ex_address, ex_email, ex_district, ex_name, ex_mobile;

    Intent intent;

    RadioGroup radioGroup;
    RadioButton radioButton;
    String radioChoie = "Cash On Delivery";
    String payOnline = "Pay Online";
    String deliveryDate;
    String couponId = "0";
    String shippingCost = "0";
    ArrayList<CartGetterSetter> cartGetterSetters = new ArrayList<>();

    int total = 0;
    TextView subTotal;

    SharedPreferences sharedPreferences;
    public static final String fileName = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_place);

        subTotal = findViewById(R.id.final_totalAmount);

        name = findViewById(R.id.op_profile_name);
        mobile = findViewById(R.id.op_profile_mobile);
        email = findViewById(R.id.op_profile_mail);
        pincode = findViewById(R.id.op_profile_pincode);
        district = findViewById(R.id.op_profile_district);
        fullAddress = findViewById(R.id.op_profile_fullAddress);

        radioGroup  = findViewById(R.id.radioGroup);



        //----------Here is get the info from last Activity
        intent = getIntent();
        ex_pincode = intent.getStringExtra("pin_code");
        ex_userid = intent.getStringExtra("userid");
        ex_addrssid = intent.getStringExtra("addressid");
        ex_address = intent.getStringExtra("fulladdress");
        ex_email = intent.getStringExtra("e_mail");
        ex_district = intent.getStringExtra("district");
        ex_name = intent.getStringExtra("name");
        ex_mobile = intent.getStringExtra("mobile");

        Toast.makeText(getApplicationContext(), ""+ex_userid, Toast.LENGTH_SHORT).show();

        name.setText("Name: "+ex_name);
        pincode.setText("Pincode: "+ex_pincode);
        mobile.setText("Mobile: "+ex_mobile);
        fullAddress.setText("Full Address: "+ex_address);
        district.setText("District: "+ex_district);
        email.setText("E-mail: "+ex_email);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        final String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<CartGetterSetter>>() {}.getType();
        cartGetterSetters = gson.fromJson(json, type);

        //below mentioned is good working for current time
       // String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

       // Toast.makeText(getApplicationContext(), date, Toast.LENGTH_LONG).show();

        //below mentioned are good coding for get current date with time zone
       /* Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 5);
        dt = c.getTime();
        Toast.makeText(getApplicationContext(), "date "+dt, Toast.LENGTH_LONG   ).show(); */

       // its working fine to get current date with increase 5 days
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd-MM-yyyy");
        Calendar currentCal = Calendar.getInstance();
        String currentdate=dateFormat.format(currentCal.getTime());
        currentCal.add(Calendar.DATE, 5);
        deliveryDate =dateFormat.format(currentCal.getTime());

       // Toast.makeText(getApplicationContext(), ""+toDate, Toast.LENGTH_SHORT).show();

        itemAndPriceCount();
    }

    public void checkButton(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);

        radioChoie = (String) radioButton.getText();

        Toast.makeText(this, "You Choose " + radioChoie,
                Toast.LENGTH_SHORT).show();
    }
    public  void itemAndPriceCount(){


        // here calculate total Amount
        for(int i = 0; i < cartGetterSetters.size(); i++){
            total = total + cartGetterSetters.get(i).getRsprice();
        }

        subTotal.setText(String.valueOf("Total Payable Amount is = " +total));
       // Toast.makeText(getApplicationContext(), "hi "+total, Toast.LENGTH_SHORT).show();

    }

    public void finalOrderPlaced(View view) {


        if (radioChoie.equals(payOnline) ){

            Toast.makeText(getApplicationContext(), "you pay online", Toast.LENGTH_SHORT).show();
        }
        else {
           // Toast.makeText(getApplicationContext(), "pay by cash", Toast.LENGTH_SHORT).show();

            RequestQueue requestQueue = Volley.newRequestQueue(OrderPlace.this);
            String url = "https://www.gujumart.com/api-place-cod-order";


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Toast.makeText(Login.this, "msg sent"+ randomnumber, Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jObj = new JSONObject(response);
                        String status = jObj.getString("status");
                        String msg = jObj.getString("message");
                        String orderno = jObj.getString("order_id");
                        //  String cart_item_id = jObj.getString("cart_item_id");

                        //  Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), orderno, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(OrderPlace.this, "Error...Please Try Later", Toast.LENGTH_SHORT).show();

                }
            }) {
                protected Map<String, String> getParams() {


                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id", ex_userid);
                    params.put("address_id", ex_addrssid);
                    params.put("usr_mob", ex_mobile);
                    params.put("subtotal", String.valueOf(total));
                    params.put("shhipingCost", shippingCost);
                    params.put("coupon_id", couponId);
                    params.put("total_cost", String.valueOf(total));
                    params.put("email", ex_email);
                    params.put("deliveryDate", deliveryDate);


                    for(int i = 0; i < cartGetterSetters.size(); i++) {
                        params.put("cart_items[" + (i) + "][poduct_id]", String.valueOf(cartGetterSetters.get(i).getSkuid()));
                        params.put("cart_items[" + (i) + "][variation_id]", String.valueOf(cartGetterSetters.get(i).getVariation_selected_itm()));
                        params.put("cart_items[" + (i) + "][total_item]", String.valueOf(cartGetterSetters.get(i).getQty()));

                    }

                    return params;
                }
            };
            requestQueue.add(stringRequest);
        }

    }
}