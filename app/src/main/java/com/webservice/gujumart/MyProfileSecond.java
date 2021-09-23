package com.webservice.gujumart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.webservice.gujumart.recyclerhelperclass.CategoriesClickListenerSecond;
import com.webservice.gujumart.recyclerhelperclass.MyProfileAdaptorSecond;
import com.webservice.gujumart.recyclerhelperclass.MyProfileGetSetSecond;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyProfileSecond extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;

    // for recyclerview
    RecyclerView myprofileRecycler;
    MyProfileAdaptorSecond myprofileAdaptor;

    SharedPreferences sharedPreferences;
    public static final String fileName = "login";
    public static final String Token = "token";
    public static final String ID = "id";
    public static final String Mobile = "mobile";

    String tkn;
    String userid;
    String validmobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_second);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);

        //------------ hooks recycler view -----
        myprofileRecycler = (RecyclerView) findViewById(R.id.viewprofile_recycler_second);
        //categoriesRecycler.setLayoutManager(new LinearLayoutManager(this));

        //Call The Categories Recycler funtion


        //  tkn = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJkYXRhIjp7ImlkIjoiMjUiLCJuYW1lIjoiIiwiZW1haWwiOiIiLCJtb2JpbGUiOiI5OTk5NTg1MDQzIn19.h-EAQTJA6MX1mkBumAEecM91coyIj15DzDw2cP0K7kk";




        tkn = sharedPreferences.getString("token", "No name defined");
        userid = sharedPreferences.getString("id", "No name defined");
        validmobile = sharedPreferences.getString("mobile", "No name defined");
        //  Toast.makeText(MyProfile.this, "i Function "+userid, Toast.LENGTH_SHORT).show();
        setRecyclerView();


       /* TextView textView = findViewById(R.id.myprofile_textView);
        Bundle extra = getIntent().getExtras();
        String username = extra.getString("title");
        textView.setText(username);  */

        bottomNavigationView = findViewById(R.id.bottom_navigationSecond);
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

    }

    public void backToHome(View view) {

        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
        finish();
    }

    public void updateProfileSecond(View view) {
        Intent intent = new Intent(getApplicationContext(), UpdateProfile.class);
        intent.putExtra("shr_userid", userid);
        intent.putExtra("shr_mobile", validmobile);
        intent.putExtra("shr_token", tkn);
        startActivity(intent);
    }
    public  void setRecyclerView(){

        //  Toast.makeText(getApplicationContext(), "function called", Toast.LENGTH_SHORT).show();

        myprofileRecycler.setHasFixedSize(true);
        myprofileRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        final ArrayList<MyProfileGetSetSecond> myProfileGetSets = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String baseurl = "http://gujumart.com/api-getUserAddess?userId=";
        String miniurl = userid;
        String url = baseurl + miniurl;

        // Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //Toast.makeText(MyProfile.this, "response on", Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray jsonArray = response.getJSONArray("addressList");

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject produt = jsonArray.getJSONObject(i);

                                String nameUrl = produt.getString("contactName");
                                String mobileurl = produt.getString("contactMobile");
                                String useridurl = produt.getString("userid");
                                String addressidurl = produt.getString("id");
                                String emailurl = produt.getString("email");
                                String pincodeurl = produt.getString("zipcode");
                                String districturl = produt.getString("district");
                                String fulladdressurl = produt.getString("address");
                                // String longdes = produt.getString("long_description");



                                //Toast.makeText(MyProfile.this, "Done "+nameUrl, Toast.LENGTH_SHORT).show();

                                myProfileGetSets.add(new MyProfileGetSetSecond(nameUrl, mobileurl, emailurl, pincodeurl, districturl, fulladdressurl, useridurl, addressidurl));
                            }
                            myprofileAdaptor = new MyProfileAdaptorSecond(MyProfileSecond.this, myProfileGetSets, new CategoriesClickListenerSecond() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    // Toast.makeText(MyProfile.this, "updated button clicked", Toast.LENGTH_SHORT).show();
                                    MyProfileGetSetSecond clickedItem = myProfileGetSets.get(position);
                                    Intent desIntent = new Intent(getApplicationContext(), UpdateProfile.class);


                                    desIntent.putExtra("userid", clickedItem.getUseid());
                                    desIntent.putExtra("addressid", clickedItem.getAddsid());
                                    desIntent.putExtra("pin_code", clickedItem.getPinode());

                                    desIntent.putExtra("fulladdress", clickedItem.getFull_address());
                                    desIntent.putExtra("e_mail", clickedItem.getEmail());

                                    desIntent.putExtra("district", clickedItem.getDistrict());
                                    desIntent.putExtra("name", clickedItem.getName());
                                    desIntent.putExtra("mobile", clickedItem.getMobile());
                                    desIntent.putExtra("shr_token", tkn);

                                    startActivity(desIntent);
                                    finish();

                                }

                                @Override
                                public void buttonClick(View v, int position) {
                                    Toast.makeText(MyProfileSecond.this, "Delete button cliked", Toast.LENGTH_SHORT).show();
                                    /*
                                    if (sharedPreferences.contains(Token)) {

                                        String tkn = sharedPreferences.getString("token", "No name defined");
                                        String userid = sharedPreferences.getString("id", "No name defined");
                                        String validmobile = sharedPreferences.getString("mobile", "No name defined");
                                        CategoriesGetterSetter buttonclicked = categoriesGetterSetters.get(position);

                                        String productid = buttonclicked.getSkuid();

                                        // here Add to Cart Api Connected

                                        RequestQueue requestQueue = Volley.newRequestQueue(Categories.this);
                                        String url = "https://www.gujumart.com/api-add_cart";



                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {

                                                //Log.v("LOG_VOLLEY", response);

                                                try {
                                                    JSONObject jObj = new JSONObject(response);
                                                    String status = jObj.getString("status");
                                                    String msg = jObj.getString("message");
                                                    //  String cart_item_id = jObj.getString("cart_item_id");

                                                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        }, new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Toast.makeText(getApplicationContext(), "Error occurred, Please Check Internet Connection", Toast.LENGTH_SHORT).show();

                                            }
                                        }) {
                                            public Map<String, String> getHeaders() {
                                                Map<String, String> header = new HashMap<String, String>();
                                                header.put("token", tkn);
                                                return header;
                                            }
                                            protected Map<String, String> getParams() {
                                                Map<String, String> params = new HashMap<String, String>();
                                                // params.put("token", tkn);
                                                params.put("userId", userid);
                                                params.put("product_id", productid);
                                                params.put("no_of_product", "1");
                                                return params;
                                            }

                                        };
                                        requestQueue.add(stringRequest);


                                        //Toast.makeText(getApplicationContext(), "Button Clicked "+productid, Toast.LENGTH_SHORT).show();
                                    } else
                                    {

                                        Toast.makeText(getApplicationContext(), "Please login first", Toast.LENGTH_SHORT).show();
                                    }
                                    // Toast.makeText(mcontext, "Add to Cart Successfully", Toast.LENGTH_LONG).show();  */
                                }
                                public void useAddress(View v, int position) {
                                    MyProfileGetSetSecond clickedItem = myProfileGetSets.get(position);
                                    Intent desIntent = new Intent(getApplicationContext(), OrderPlace.class);


                                    desIntent.putExtra("userid", clickedItem.getUseid());
                                    desIntent.putExtra("addressid", clickedItem.getAddsid());
                                    desIntent.putExtra("pin_code", clickedItem.getPinode());

                                    desIntent.putExtra("fulladdress", clickedItem.getFull_address());
                                    desIntent.putExtra("e_mail", clickedItem.getEmail());

                                    desIntent.putExtra("district", clickedItem.getDistrict());
                                    desIntent.putExtra("name", clickedItem.getName());
                                    desIntent.putExtra("mobile", clickedItem.getMobile());
                                    desIntent.putExtra("shr_token", tkn);

                                    startActivity(desIntent);
                                    finish();

                                }

                            });
                            myprofileRecycler.setAdapter(myprofileAdaptor);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(MyProfileSecond.this, "Please Insert New Address", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyProfileSecond.this, "error ocurred", Toast.LENGTH_SHORT).show();

            }
        }) {
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<String, String>();
                header.put("token", tkn);
                return header;
            }
        };
        requestQueue.add(request);

        //  myprofileRecycler.setAdapter(myprofileAdaptor);
    }


}