package com.webservice.gujumart;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import com.webservice.gujumart.SlidesDashboard.SliderAdaptor;
import com.webservice.gujumart.SlidesDashboard.SliderItem;
import com.webservice.gujumart.recyclerhelperclass.CustomItemClickListener;
import com.webservice.gujumart.recyclerhelperclass.FirstRecyclerAdaptor;
import com.webservice.gujumart.recyclerhelperclass.FirstRecyclerGetterSetter;
import com.webservice.gujumart.recyclerhelperclass.SecondRecyclerAdaptor;
import com.webservice.gujumart.recyclerhelperclass.SecondRecyclerGetterSetter;
import com.webservice.gujumart.recyclerhelperclass.ThirdRecyclerAdaptor;
import com.webservice.gujumart.recyclerhelperclass.ThirdRecyclerGetterSetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity{

    TextView headerText;
    SharedPreferences sharedPreferences;
    public static final String fileName = "login";
    public static final String Token = "token";
    public static final String ID = "id";
    public static final String Mobile = "mobile";

    // for slides
    private ViewPager2 viewPager2;
    private Handler sliderHandler = new Handler();

    // for drawer layout
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    androidx.appcompat.widget.Toolbar toolbar;

    // for Bottom Bar
    BottomNavigationView bottomNavigationView;

    // for recyclerview
    RecyclerView firstRecycler, secondRecycler, thirdRecycler;
    FirstRecyclerAdaptor firstRecyclerAdaptor;
    SecondRecyclerAdaptor secondRecyclerAdaptor;
    ThirdRecyclerAdaptor thirdRecyclerAdaptor;
    RecyclerView.Adapter adapter;
    private Context mcontext;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //headerText = findViewById(R.id.headerid);
       sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        /* if (sharedPreferences.contains(Token)) {
            String name = sharedPreferences.getString("id", "No name defined");
            Toast.makeText(mcontext, "text is "+name, Toast.LENGTH_SHORT).show();
        } */

        mcontext = Dashboard.this;

        //-----------Call the slide class here
        slides();

        //-------Hooks- for navigation view and drawer layout and Toolbar-------
        // =======And Navigation View all Coding is here========
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);

        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.colorWhite));
        toggle.syncState();

        // here set up for header text change


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)

            {
                switch (menuItem.getItemId())
                {
                    case R.id.nav_home:
                       // Intent intent = new Intent(Dashboard.this, Dashboard.class);
                       // startActivity(intent);
                       // finish();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Toast.makeText(getApplicationContext(), "Welome to Home Page", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_deliveryAddress:
                        Intent intentzero = new Intent(Dashboard.this, CurrentLocation.class);
                        startActivity(intentzero);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.nav_myOrder:
                        Intent intentTwo = new Intent(Dashboard.this, yourOrders.class);
                        startActivity(intentTwo);
                        break;

                    case R.id.nav_myprofile:
                        Intent intentThree = new Intent(Dashboard.this, MyProfile.class);
                        startActivity(intentThree);
                        break;

                    case R.id.nav_login:
                        Intent intentfour = new Intent(Dashboard.this, Login.class);
                        startActivity(intentfour);
                        break;

                    case R.id.nav_Logout:
                        sharedPreferences.edit().clear().commit();
                        Intent intentlogout = new Intent(Dashboard.this, Dashboard.class);
                        startActivity(intentlogout);
                        Toast.makeText(getApplicationContext(), "Logout Sucessfully", Toast.LENGTH_LONG).show();

                        break;

                    case R.id.nav_contact_us:
                        Intent intentfive = new Intent(Dashboard.this, ContactUs.class);
                        startActivity(intentfive);
                        break;

                    case R.id.nav_rate:
                        break;

                    case R.id.nav_share:
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            }
        });

        //------------ hooks recycler view -----
        firstRecycler = (RecyclerView) findViewById(R.id.first_recycler_id);
       // firstRecycler.setLayoutManager(new LinearLayoutManager(this));
        //-------------Second Recycler View--------
        secondRecycler = (RecyclerView) findViewById(R.id.second_recycler);
        //secondRecycler.setLayoutManager(new LinearLayoutManager(this));
        //-------------Third ecycler View------------------
        thirdRecycler = (RecyclerView) findViewById(R.id.third_recycler);
        //thirdRecycler.setLayoutManager(new LinearLayoutManager(this));

        // -------------Call the Recycler View Funtion
        setFirstRecycler();
        setSecondRecycler();
        setThirdRecycler();

        //   view = findViewById(R.id.nav_Logout);

        //--------- Hide adn shows item--------
        if (sharedPreferences.contains(Token)) {
          //  headerText.setText(sharedPreferences.getString(Mobile, ""));

            String name = sharedPreferences.getString("id", "No name defined");
            String mmm = sharedPreferences.getString("mobile", "No name defined");
            Toast.makeText(mcontext, "Your Login Mobile No. "+mmm, Toast.LENGTH_SHORT).show();
            //headerText.setText(mmm);

            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_login).setVisible(false);
             // Toast.makeText(mcontext, "sucess", Toast.LENGTH_SHORT).show();
        } else
        {
            Menu menu = navigationView.getMenu();
            menu.findItem(R.id.nav_Logout).setVisible(false);
            menu.findItem(R.id.nav_myprofile).setVisible(false);

            Toast.makeText(mcontext, "Your Not Login", Toast.LENGTH_SHORT).show();
        }

        //-----------------Navigation Drawer Menu-------

        //  navigationView.bringToFront();
      /* ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.Navigation_drawer_open, R.string.Navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_home);   */

        // here set the bottom navigation bar
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.bottom_nav_cart:
                        Intent intent = new Intent(Dashboard.this, Cart.class);
                        startActivity(intent);

                        break;
                    case R.id.bottom_nav_dashboard:
                        Toast.makeText(Dashboard.this, "Home Screen Already Open", Toast.LENGTH_SHORT).show();

                        break;

                    case R.id.bottom_nav_wishlist:
                        Intent intentwish = new Intent(Dashboard.this, WishList.class);
                        startActivity(intentwish);

                        break;

                }
                return true;
            }
        });

    }

    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }

    /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent = new Intent(Dashboard.this, Dashboard.class);
                startActivity(intent);
                break;
            case R.id.nav_deliveryAddress:
                Intent intentzero = new Intent(Dashboard.this, DeliveryAddress.class);
                startActivity(intentzero);
                break;

            case R.id.nav_myOrder:
                Intent intentTwo = new Intent(Dashboard.this, yourOrders.class);
                startActivity(intentTwo);
                break;

            case R.id.nav_myprofile:
                Intent intentThree = new Intent(Dashboard.this, MyProfile.class);
                startActivity(intentThree);
                break;

            case R.id.nav_login:
                Intent intentfour = new Intent(Dashboard.this, Login.class);
                startActivity(intentfour);
                break;

            case R.id.nav_Logout:

                Toast.makeText(this, "Logout Sucessfully", Toast.LENGTH_LONG).show();

                break;

            case R.id.nav_contact_us:
                Intent intentfive = new Intent(Dashboard.this, ContactUs.class);
                startActivity(intentfive);
                break;

            case R.id.nav_rate:
                break;

            case R.id.nav_share:
                break;

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }  */



    public void slides(){
          viewPager2 = findViewById(R.id.viewpagerimageslider);

          List<SliderItem> sliderItems = new ArrayList<>();
          sliderItems.add(new SliderItem(R.drawable.imageone));
          sliderItems.add(new SliderItem(R.drawable.imageone));
          sliderItems.add(new SliderItem(R.drawable.imageone));
          sliderItems.add(new SliderItem(R.drawable.imageone));


          viewPager2.setAdapter(new SliderAdaptor(sliderItems, viewPager2));

          viewPager2.setClipToPadding(false);
          viewPager2.setClipChildren(false);
          viewPager2.setOffscreenPageLimit(3);
          viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

          //Code here for highlite the center image
          CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
          compositePageTransformer.addTransformer(new MarginPageTransformer(40));
          compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
              @Override
              public void transformPage(@NonNull View page, float position) {
                  float r = 1 - Math.abs(position);
                  page.setScaleY(0.85f + r * 0.15f);
              }
          });
          viewPager2.setPageTransformer(compositePageTransformer);

          // here is also for autoslide
          viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
              @Override
              public void onPageSelected(int position) {
                  super.onPageSelected(position);
                  sliderHandler.removeCallbacks(sliderRunnable);
                  sliderHandler.postDelayed(sliderRunnable, 2000);  // Slide duration 1.5 second

              }
          });

      }
    //below mentioned code for autoslide
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);

    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    //create function for Recycler View
    private void setFirstRecycler(){

        firstRecycler.setHasFixedSize(true);
        firstRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        ArrayList<FirstRecyclerGetterSetter> firstRecyclerGetterSetters = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "https://www.gujumart.com/api-product";

        String tuku = "https://www.mart.softica.in/teao/uploads/product_images/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("product");

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject produt = jsonArray.getJSONObject(i);

                                String imageUrl = produt.getString("img");
                                String categorietitle = produt.getString("title");

                                String id = produt.getString("id");

                                firstRecyclerGetterSetters.add(new FirstRecyclerGetterSetter(tuku+imageUrl, categorietitle, id));
                            }
                            firstRecyclerAdaptor = new FirstRecyclerAdaptor(getApplicationContext(), firstRecyclerGetterSetters, new CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    FirstRecyclerGetterSetter clickedItem = firstRecyclerGetterSetters.get(position);
                                    Intent desIntent = new Intent(getApplicationContext(), Categories.class);


                                    desIntent.putExtra("postid", clickedItem.getId());

                                    startActivity(desIntent);

                                }
                            });
                            firstRecycler.setAdapter(firstRecyclerAdaptor);

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

        //old data to add the recyclerview
        /*
        ArrayList<FirstRecyclerGetterSetter> firstRecyclerGetterSetters = new ArrayList<>();
        firstRecyclerGetterSetters.add(new FirstRecyclerGetterSetter(R.drawable.imageone,"Best Quality"));
        firstRecyclerGetterSetters.add(new FirstRecyclerGetterSetter(R.drawable.imageone,"Best Quality"));
        firstRecyclerGetterSetters.add(new FirstRecyclerGetterSetter(R.drawable.imageone,"Best Quality"));
        firstRecyclerGetterSetters.add(new FirstRecyclerGetterSetter(R.drawable.imageone,"Best Quality"));
        firstRecyclerGetterSetters.add(new FirstRecyclerGetterSetter(R.drawable.imageone,"Best Quality"));
        firstRecyclerGetterSetters.add(new FirstRecyclerGetterSetter(R.drawable.imageone,"Best Quality"));
        firstRecyclerGetterSetters.add(new FirstRecyclerGetterSetter(R.drawable.imageone,"Best Quality"));
        firstRecyclerGetterSetters.add(new FirstRecyclerGetterSetter(R.drawable.imageone,"Best Quality"));
        firstRecyclerGetterSetters.add(new FirstRecyclerGetterSetter(R.drawable.imageone,"Best Quality"));
        firstRecyclerGetterSetters.add(new FirstRecyclerGetterSetter(R.drawable.imageone,"Best Quality"));
        firstRecyclerGetterSetters.add(new FirstRecyclerGetterSetter(R.drawable.imageone,"Best Quality"));
        firstRecyclerGetterSetters.add(new FirstRecyclerGetterSetter(R.drawable.imageone,"Best Quality"));
        firstRecyclerGetterSetters.add(new FirstRecyclerGetterSetter(R.drawable.imageone,"Best Quality"));
        firstRecyclerGetterSetters.add(new FirstRecyclerGetterSetter(R.drawable.imageone,"Best Quality"));

        adapter = new FirstRecyclerAdaptor(firstRecyclerGetterSetters);
        firstRecycler.setAdapter(adapter);    */
    }

    //create function for Second Recycler View
    public void setSecondRecycler(){

        secondRecycler.setHasFixedSize(true);
        secondRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        ArrayList<SecondRecyclerGetterSetter> secondRecyclerGetterSetters = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "http://gujumart.com/api-bsSellerProd";

        String tuku = "https://www.gujumart.com/teao/uploads/product_variation_images/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("top_brand_prod");

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject produt = jsonArray.getJSONObject(i);

                                String imageUrl = produt.getString("fimage");
                                String categorietitle = produt.getString("attribute");
                                String mrp = produt.getString("price");
                                String price = produt.getString("price");

                                int id = produt.getInt("id");

                                secondRecyclerGetterSetters.add(new SecondRecyclerGetterSetter(tuku+imageUrl, categorietitle, mrp, price));
                            }
                            secondRecyclerAdaptor = new SecondRecyclerAdaptor(getApplicationContext(), secondRecyclerGetterSetters);
                            secondRecycler.setAdapter(secondRecyclerAdaptor);

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

        /*
        secondRecyclerGetterSetters.add(new SecondRecyclerGetterSetter(R.drawable.imageone, "Best Product", "230", "150"));
        secondRecyclerGetterSetters.add(new SecondRecyclerGetterSetter(R.drawable.imageone, "Best Product", "230", "150"));
        secondRecyclerGetterSetters.add(new SecondRecyclerGetterSetter(R.drawable.imageone, "Best Product", "230", "150"));
        secondRecyclerGetterSetters.add(new SecondRecyclerGetterSetter(R.drawable.imageone, "Best Product", "230", "150"));
        secondRecyclerGetterSetters.add(new SecondRecyclerGetterSetter(R.drawable.imageone, "Best Product", "230", "150"));
        secondRecyclerGetterSetters.add(new SecondRecyclerGetterSetter(R.drawable.imageone, "Best Product", "230", "150"));
        secondRecyclerGetterSetters.add(new SecondRecyclerGetterSetter(R.drawable.imageone, "Best Product", "230", "150"));
        secondRecyclerGetterSetters.add(new SecondRecyclerGetterSetter(R.drawable.imageone, "Best Product", "230", "150"));
        secondRecyclerGetterSetters.add(new SecondRecyclerGetterSetter(R.drawable.imageone, "Best Product", "230", "150"));
        secondRecyclerGetterSetters.add(new SecondRecyclerGetterSetter(R.drawable.imageone, "Best Product", "230", "150"));
        secondRecyclerGetterSetters.add(new SecondRecyclerGetterSetter(R.drawable.imageone, "Best Product", "230", "150"));
        secondRecyclerGetterSetters.add(new SecondRecyclerGetterSetter(R.drawable.imageone, "Best Product", "230", "150"));
        secondRecyclerGetterSetters.add(new SecondRecyclerGetterSetter(R.drawable.imageone, "Best Product", "230", "150"));


        adapter = new SecondRecyclerAdaptor(secondRecyclerGetterSetters);
        secondRecycler.setAdapter(adapter);  */
    }

    //third Recycler View Function call here
    private void setThirdRecycler(){

        thirdRecycler.setHasFixedSize(true);
        thirdRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mcontext = Dashboard.this;

        final ArrayList<ThirdRecyclerGetterSetter> thirdRecyclerGetterSetters = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "https://www.gujumart.com/api-product";

        String tuku = "https://www.mart.softica.in/teao/uploads/product_images/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("product");

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject produt = jsonArray.getJSONObject(i);

                                String imageUrl = produt.getString("img");
                                String categorietitle = produt.getString("title");

                                String id = produt.getString("id");

                                thirdRecyclerGetterSetters.add(new ThirdRecyclerGetterSetter(tuku+imageUrl, categorietitle, id));
                            }
                            thirdRecyclerAdaptor = new ThirdRecyclerAdaptor(getApplicationContext(), thirdRecyclerGetterSetters, new CustomItemClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    ThirdRecyclerGetterSetter clickedItem = thirdRecyclerGetterSetters.get(position);
                                    Intent desIntent = new Intent(getApplicationContext(), Categories.class);


                                     desIntent.putExtra("postid", clickedItem.getId());

                                    startActivity(desIntent);

                                }
                            });
                            thirdRecycler.setAdapter(thirdRecyclerAdaptor);

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


        /*thirdRecyclerGetterSetters.add(new ThirdRecyclerGetterSetter(R.drawable.fruts, "Vegetables and Fruits", " All Types of Vegetables are available here"));
        thirdRecyclerGetterSetters.add(new ThirdRecyclerGetterSetter(R.drawable.dairyproducts, "Dairy And Bakery", " All Types of Dairy Product are available here"));
        thirdRecyclerGetterSetters.add(new ThirdRecyclerGetterSetter(R.drawable.staples, "Staples", " Atta, Flours and Suji, Dals, Pulses, Rice"));
        thirdRecyclerGetterSetters.add(new ThirdRecyclerGetterSetter(R.drawable.imageone, "Snack And Branded Food", " Biscuits, Cookies, Noodle, Pasta, Vermicelli....."));
        thirdRecyclerGetterSetters.add(new ThirdRecyclerGetterSetter(R.drawable.beverage, "Beverages", " Tea, Coffee, Fruit, Juices, Energy and Soift Drink......"));
        thirdRecyclerGetterSetters.add(new ThirdRecyclerGetterSetter(R.drawable.fruts, "Personal Care", "Hair Care, Oral Care, Skin Care, Bath and hand wash"));
        thirdRecyclerGetterSetters.add(new ThirdRecyclerGetterSetter(R.drawable.fruts, "Vegetables and Fruits", " All Types of Vegetables are available here"));
        thirdRecyclerGetterSetters.add(new ThirdRecyclerGetterSetter(R.drawable.dairyproducts, "Dairy And Bakery", " All Types of Dairy Product are available here"));
        thirdRecyclerGetterSetters.add(new ThirdRecyclerGetterSetter(R.drawable.staples, "Staples", " Atta, Flours and Suji, Dals, Pulses, Rice"));
        thirdRecyclerGetterSetters.add(new ThirdRecyclerGetterSetter(R.drawable.imageone, "Snack And Branded Food", " Biscuits, Cookies, Noodle, Pasta, Vermicelli....."));
        thirdRecyclerGetterSetters.add(new ThirdRecyclerGetterSetter(R.drawable.beverage, "Beverages", " Tea, Coffee, Fruit, Juices, Energy and Soift Drink......"));
        thirdRecyclerGetterSetters.add(new ThirdRecyclerGetterSetter(R.drawable.fruts, "Personal Care", "Hair Care, Oral Care, Skin Care, Bath and hand wash"));

        adapter = new ThirdRecyclerAdaptor(thirdRecyclerGetterSetters, mcontext, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent desIntent = new Intent(getApplicationContext(), Categories.class);


               // desIntent.putExtra("title", thirdRecyclerGetterSetters.get(position).getTitle());

                startActivity(desIntent);

            }
        }); */
        thirdRecycler.setAdapter(adapter);
    }


    public void searchElement(View view) {

        Intent intentThree = new Intent(Dashboard.this, Search.class);
        startActivity(intentThree);
    }



    public void goToCart(View view) {
        Intent intent = new Intent(getApplicationContext(), Cart.class);
        startActivity(intent);
    }
}
