package com.webservice.gujumart;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.squareup.picasso.Picasso;
import com.webservice.gujumart.SlidesDashboard.SliderAdaptor;
import com.webservice.gujumart.SlidesDashboard.SliderItem;
import com.webservice.gujumart.recyclerhelperclass.BottomRecyclerAdaptor;
import com.webservice.gujumart.recyclerhelperclass.BottomRecyclerGetterSetter;
import com.webservice.gujumart.recyclerhelperclass.CategoriesAdaptor;
import com.webservice.gujumart.recyclerhelperclass.CategoriesGetterSetter;
import com.webservice.gujumart.recyclerhelperclass.CustomItemClickListener;
import com.webservice.gujumart.recyclerhelperclass.SecondRecyclerAdaptor;
import com.webservice.gujumart.recyclerhelperclass.SecondRecyclerGetterSetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductView extends AppCompatActivity {


    FloatingActionButton addToWishList;
    static boolean ALREADY_ADDED_TO_WISHLIST = false;
    ElegantNumberButton elegantNumberButton;
    String qty;

    TextView textView;

    // for expandable view
    LinearLayout expandableview, expandableviewFeatures, expandableviewDisclaimer;
    CardView cardview, cardviewFeatures, cardviewDisclaimer;
    Button arrowbtn, arrowbtnFeatures, arrowbtnDisclaimer;

    // for recyclerview
    RecyclerView bottomRecycler;
    BottomRecyclerAdaptor bottomRecyclerAdaptor;

    RecyclerView.Adapter adapter;
    private Context mcontext;

    Intent intent;
    String extraimg, extratitle, extrashortdes, extramrp, extraprice, pid, skuid, longdescription;

    TextView descshort, longdesc;

    BottomNavigationView bottomNavigationView;

    SharedPreferences sharedPreferences;
    public static final String fileName = "login";
    public static final String Token = "token";
    public static final String ID = "id";
    public static final String Mobile = "mobile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        elegantNumberButton = findViewById(R.id.elegantbutton);


        intent = getIntent();

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);

        // here add the Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        descshort = findViewById(R.id.desc_short);
        longdesc = findViewById(R.id.fulldescription);

        //descshort.setText("how are you");

        // hooks for floating button
        addToWishList = findViewById(R.id.add_to_wishlist);

        // hooks for expandable view of Discriptions
        expandableview = findViewById(R.id.expandableview);
        cardview = findViewById(R.id.cardview);
        arrowbtn = findViewById(R.id.btn_arrow_down);

        // hooks for expandable view of Features and Details
        expandableviewFeatures = findViewById(R.id.expandableviewonfeatures);
        cardviewFeatures = findViewById(R.id.cardviewfeatures);
        arrowbtnFeatures = findViewById(R.id.fdbtn_arrow_down);

        // hooks for expandable view of Disclaimer
        expandableviewDisclaimer = findViewById(R.id.expandableviewdisclaimer);
        cardviewDisclaimer = findViewById(R.id.cardviewdisclaimer);
        arrowbtnDisclaimer = findViewById(R.id.btn_arrow_downdisclaimer);

        //-------------ProductView Bottom Recycler View--------
        bottomRecycler = (RecyclerView) findViewById(R.id.productview_bottom_recycler);
        bottomRecycler.setLayoutManager(new LinearLayoutManager(this));

        //call the slid function
        slides();

        // call the bottom Recycler View in Product view Class

        setBottomRecycler();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
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


    public void slides() {
        //  viewPager2 = findViewById(R.id.viewpagerimageslider);
        //indicator = findViewById(R.id.indicator);

        // List<SliderItem> sliderItems = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // String tuku = "https://www.gujumart.com/teao/uploads/product_variation_images/";


        extraimg = intent.getStringExtra("fimage");
        extratitle = intent.getStringExtra("attribute_name");
        extrashortdes = intent.getStringExtra("short_description");
        extramrp = intent.getStringExtra("price");
        extraprice = intent.getStringExtra("price");

        pid = intent.getStringExtra("productid");
        skuid = intent.getStringExtra("skuid");
        longdescription = intent.getStringExtra("skuid");

        //String url = tuku + extraimg;

        TextView tiltetext = findViewById(R.id.pv_title);
        ImageView mainimage = findViewById(R.id.pv_image);
        TextView desctext = findViewById(R.id.pv_sdesc);
        TextView mrp = findViewById(R.id.pv_mrp);
        TextView price = findViewById(R.id.pv_price);

        tiltetext.setText(extratitle);
        // Picasso.get().load(url).fit().centerInside().into(mainimage);
        Picasso.get().load(extraimg).fit().centerInside().into(mainimage);
        desctext.setText(extrashortdes);
        mrp.setText(extramrp);
        price.setText(extraprice);

        //here set the description
        descshort.setText(extrashortdes);
        Document document = Jsoup.parse(longdescription);
        longdesc.setText(longdescription);


        // Toast.makeText(this, extraimg, Toast.LENGTH_LONG).show();
        //Toast.makeText(this, "SKU is " + skuid, Toast.LENGTH_LONG).show();




      /*  String baseurl = "https://www.gujumart.com/api-productType?prod_id=";
        String miniurl = pid;
        String url = baseurl + miniurl;

        String tuku = "https://www.gujumart.com/teao/uploads/product_variation_images/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("product");

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject produt = jsonArray.getJSONObject(i);

                         //       String imageUrl = produt.getString("fimage");
                           //     String categorietitle = produt.getString("attribute_name");
                             //   String shortdec = produt.getString("short_description");
                                String mrp = produt.getString("price");
                                String price = produt.getString("price");

                                String proid = produt.getString("product_id");
                                String skuid = produt.getString("id");


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }); */
        // requestQueue.add(request);

       /* sliderItems.add(new SliderItem(R.drawable.fruitthree));
        sliderItems.add(new SliderItem(R.drawable.fruitthree));
        sliderItems.add(new SliderItem(R.drawable.fruitthree));
        sliderItems.add(new SliderItem(R.drawable.fruitthree));
        sliderItems.add(new SliderItem(R.drawable.fruitthree));
        sliderItems.add(new SliderItem(R.drawable.fruitthree));


        viewPager2.setAdapter(new SliderAdaptor(sliderItems, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);  */

        //Code here for highlite the center image
     /*  CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);  */

        /* new TabLayoutMediator(indicator, viewPager2,
                (tab, position) -> tab.setText(" " + (position))
        ).attach(); */

    }

    public void addToWishListFun(View view) {

        if (sharedPreferences.contains(Token)) {

            String tkn = sharedPreferences.getString("token", "No name defined");
            String userid = sharedPreferences.getString("id", "No name defined");
            String validmobile = sharedPreferences.getString("mobile", "No name defined");

            skuid = intent.getStringExtra("skuid");

           // qty = elegantNumberButton.getNumber();

            RequestQueue requestQueue = Volley.newRequestQueue(ProductView.this);
            String url = "https://www.gujumart.com/api-add_wish";



            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    //Log.v("LOG_VOLLEY", response);

                    try {
                        JSONObject jObj = new JSONObject(response);
                        //String status = jObj.getString("status");
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
                    params.put("prodVariation_Id", skuid);
                   // params.put("no_of_product", qty);
                    return params;
                }

            };
            requestQueue.add(stringRequest);


            Toast.makeText(getApplicationContext(), "Button Clicked" +qty, Toast.LENGTH_SHORT).show();
        } else
        {

            Toast.makeText(getApplicationContext(), "Please login first", Toast.LENGTH_SHORT).show();
        }


        /////-------End here--------
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setExpandableview(View view) {

        if (expandableview.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(cardview, new AutoTransition());
            expandableview.setVisibility(View.VISIBLE);
            arrowbtn.setBackgroundResource(R.drawable.arrow_up);
        } else {
            TransitionManager.beginDelayedTransition(cardview, new AutoTransition());
            expandableview.setVisibility(View.GONE);
            arrowbtn.setBackgroundResource(R.drawable.arrow_down);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setExpandableviewOnFeatures(View view) {
        if (expandableviewFeatures.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(cardviewFeatures, new AutoTransition());
            expandableviewFeatures.setVisibility(View.VISIBLE);
            arrowbtnFeatures.setBackgroundResource(R.drawable.arrow_up);
        } else {
            TransitionManager.beginDelayedTransition(cardviewFeatures, new AutoTransition());
            expandableviewFeatures.setVisibility(View.GONE);
            arrowbtnFeatures.setBackgroundResource(R.drawable.arrow_down);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void setExpandableviewDisclaimer(View view) {

        if (expandableviewDisclaimer.getVisibility() == View.GONE) {
            TransitionManager.beginDelayedTransition(cardviewDisclaimer, new AutoTransition());
            expandableviewDisclaimer.setVisibility(View.VISIBLE);
            arrowbtnDisclaimer.setBackgroundResource(R.drawable.arrow_up);
        } else {
            TransitionManager.beginDelayedTransition(cardviewDisclaimer, new AutoTransition());
            expandableviewDisclaimer.setVisibility(View.GONE);
            arrowbtnDisclaimer.setBackgroundResource(R.drawable.arrow_down);
        }

    }

    //create function for Recycler View
    public void setBottomRecycler() {

        bottomRecycler.setHasFixedSize(true);
        bottomRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        ArrayList<BottomRecyclerGetterSetter> bottomRecyclerGetterSetters = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "http://gujumart.com/api-bsSellerProd";

        String tuku = "https://www.gujumart.com/teao/uploads/product_variation_images/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("top_brand_prod");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject produt = jsonArray.getJSONObject(i);

                                String imageUrl = produt.getString("fimage");
                                String categorietitle = produt.getString("attribute");
                                String mrp = produt.getString("price");
                                String price = produt.getString("price");

                                int id = produt.getInt("id");

                                bottomRecyclerGetterSetters.add(new BottomRecyclerGetterSetter(tuku + imageUrl, categorietitle, mrp, price));
                            }
                            bottomRecyclerAdaptor = new BottomRecyclerAdaptor(getApplicationContext(), bottomRecyclerGetterSetters);
                            bottomRecycler.setAdapter(bottomRecyclerAdaptor);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);


       /* bottomRecyclerGetterSetters.add(new BottomRecyclerGetterSetter(R.drawable.offer_tag, R.drawable.imageone, 3, "Best Product", "MRP.", "230", "Rs.", "150"));
        bottomRecyclerGetterSetters.add(new BottomRecyclerGetterSetter(R.drawable.offer_tag, R.drawable.imageone, 2, "Best Product", "MRP.", "230", "Rs.", "150"));
        bottomRecyclerGetterSetters.add(new BottomRecyclerGetterSetter(R.drawable.offer_tag, R.drawable.imageone, 1, "Best Product", "MRP.", "230", "Rs.", "150"));
        bottomRecyclerGetterSetters.add(new BottomRecyclerGetterSetter(R.drawable.offer_tag, R.drawable.imageone, 4, "Best Product", "MRP.", "230", "Rs.", "150"));
        bottomRecyclerGetterSetters.add(new BottomRecyclerGetterSetter(R.drawable.offer_tag, R.drawable.imageone, 3, "Best Product", "MRP.", "230", "Rs.", "150"));
        bottomRecyclerGetterSetters.add(new BottomRecyclerGetterSetter(R.drawable.offer_tag, R.drawable.imageone, 2, "Best Product", "MRP.", "230", "Rs.", "150"));
        bottomRecyclerGetterSetters.add(new BottomRecyclerGetterSetter(R.drawable.offer_tag, R.drawable.imageone, 1, "Best Product", "MRP.", "230", "Rs.", "150"));
        bottomRecyclerGetterSetters.add(new BottomRecyclerGetterSetter(R.drawable.offer_tag, R.drawable.imageone, 4, "Best Product", "MRP.", "230", "Rs.", "150"));
        bottomRecyclerGetterSetters.add(new BottomRecyclerGetterSetter(R.drawable.offer_tag, R.drawable.imageone, 4, "Best Product", "MRP.", "230", "Rs.", "150"));
        bottomRecyclerGetterSetters.add(new BottomRecyclerGetterSetter(R.drawable.offer_tag, R.drawable.imageone, 4, "Best Product", "MRP.", "230", "Rs.", "150"));
        bottomRecyclerGetterSetters.add(new BottomRecyclerGetterSetter(R.drawable.offer_tag, R.drawable.imageone, 3, "Best Product", "MRP.", "230", "Rs.", "150"));


        adapter = new BottomRecyclerAdaptor(bottomRecyclerGetterSetters);
        bottomRecycler.setAdapter(adapter); */
    }

    public void backToHome(View view) {

        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
        finish();
    }

    public void goToCart(View view) {
        Intent intent = new Intent(getApplicationContext(), Cart.class);
        startActivity(intent);
    }

    public void addToCartFun(View view) {
        if (sharedPreferences.contains(Token)) {

            String tkn = sharedPreferences.getString("token", "No name defined");
            String userid = sharedPreferences.getString("id", "No name defined");
            String validmobile = sharedPreferences.getString("mobile", "No name defined");

            skuid = intent.getStringExtra("skuid");

            qty = elegantNumberButton.getNumber();

            RequestQueue requestQueue = Volley.newRequestQueue(ProductView.this);
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
                    params.put("product_id", skuid);
                    params.put("no_of_product", qty);
                    return params;
                }

            };
            requestQueue.add(stringRequest);


            Toast.makeText(getApplicationContext(), "Button Clicked" +qty, Toast.LENGTH_SHORT).show();
        } else
        {

            Toast.makeText(getApplicationContext(), "Please login first", Toast.LENGTH_SHORT).show();
        }

    }
}
