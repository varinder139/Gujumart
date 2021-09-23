package com.webservice.gujumart;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.webservice.gujumart.recyclerhelperclass.CartGetterSetter;
import com.webservice.gujumart.recyclerhelperclass.CategoriesAdaptor;
import com.webservice.gujumart.recyclerhelperclass.CategoriesClickListener;
import com.webservice.gujumart.recyclerhelperclass.CategoriesGetterSetter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;

public class Categories extends AppCompatActivity {

    // for recyclerview
    RecyclerView categoriesRecycler;
    CategoriesAdaptor categoriesAdaptor;
    RecyclerView.Adapter adapter;
    Context mcontext;
    Intent intent;
    String pid;

    BottomNavigationView bottomNavigationView;

    //For Login process
    SharedPreferences sharedPreferences;
    public static final String fileName = "login";
    public static final String Token = "token";
    public static final String ID = "id";
    public static final String Mobile = "mobile";

    //Loading the Dialog
    DialogAddToCart loadingDialog;
    AlertDialog dialog;
    ArrayList<String> pvariation;
    String proname, pimage, pshort_dec, plong_dec, pmrp;
    int p_prie;
    String p_sukid, p_prodid;
    TextView textView;

    ArrayList<CategoriesGetterSetter> categoriesGetterSetters; //= new ArrayList<>();

    //here create the temp array list fo add to cart
    ArrayList<CategoriesGetterSetter>  templist;

    String variation_temp = "0";
    int qty_temp = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        categoriesGetterSetters = new ArrayList<>();

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);

        //------------ hooks recycler view -----
        categoriesRecycler = (RecyclerView) findViewById(R.id.categories_recycler_id);
        //categoriesRecycler.setLayoutManager(new LinearLayoutManager(this));

        //innitilizing the add to cart dialog
        loadingDialog = new DialogAddToCart(Categories.this);

        templist = new ArrayList<>();
        Gson gson = new Gson();
        final String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<CartGetterSetter>>() {}.getType();
        templist = gson.fromJson(json, type);

       // checkCartArryList();

        //Call The Categories Recycler funtion
        setCategoriesRecycler();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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


    }

     /*public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.cart_view);

        SearchView searchView = (SearchView) menuItem.getActionView();
        //searchView.setOnQueryTextListener(this);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            // home button from toolbar clicked
            Toast.makeText(mcontext, "Clicked on menu item", Toast.LENGTH_SHORT).show();
        }
        return true;
    } */


    private void setCategoriesRecycler() {

        categoriesRecycler.setHasFixedSize(true);
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        ArrayList<String> variationlist = new ArrayList<String>();
        variationlist.add(0, "select");

        RequestQueue requestQueue = Volley.newRequestQueue(this);

     // Bundle extra = getIntent().getExtras();
    //  String id = extra.getString("postid");
        intent = getIntent();
        pid = intent.getStringExtra("postid");

       // Toast.makeText(this, "id is " + pid, Toast.LENGTH_LONG).show();



        String baseurl = "https://www.gujumart.com/api-productType?prod_id=";
        String miniurl = pid;
        String url = baseurl + miniurl;

        Toast.makeText(getApplicationContext(), "id is "+pid, Toast.LENGTH_SHORT).show();

        String tuku = "https://www.gujumart.com/teao/uploads/product_variation_images/";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("product");

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject produt = jsonArray.getJSONObject(i);

                                String imageUrl = produt.getString("fimage");
                                String categorietitle = produt.getString("attribute_name");
                                String shortdec = produt.getString("short_description");
                                String mrp = produt.getString("price");
                                int price = produt.getInt("price");

                                String proid = produt.getString("product_id");
                                String skuid = produt.getString("id");
                                String longdes = produt.getString("long_description");

                                JSONArray jsonInnerArray = produt.getJSONArray("variation_details");
                                for (int j = 0; j < jsonInnerArray.length(); j++){
                                    JSONObject variation_produt = jsonInnerArray.getJSONObject(j);
                                   // variationlist.add(variation_produt.getString("varition"));

                                    String v = variation_produt.getString("varition");
                                    variationlist.add(v);
                                    Toast.makeText(Categories.this, "G "+variationlist, Toast.LENGTH_SHORT).show();

                                }
                               // Log.d("thing", "success "+ String.valueOf(variationlist));


                                categoriesGetterSetters.add(new CategoriesGetterSetter(tuku+imageUrl, categorietitle, shortdec, mrp, price, proid, skuid,
                                                                                                              longdes, variation_temp, qty_temp, variationlist));
                            }
                            categoriesAdaptor = new CategoriesAdaptor(getApplicationContext(), categoriesGetterSetters, new CategoriesClickListener() {
                                @Override
                                public void onItemClick(View v, int position) {
                                    CategoriesGetterSetter clickedItem = categoriesGetterSetters.get(position);

                                   // ArrayList<String> aration_msg = clickedItem.getVariationid();
                                   // Log.d("thing", "success "+aration_msg);

                                    Intent desIntent = new Intent(getApplicationContext(), ProductView.class);


                                    desIntent.putExtra("fimage", clickedItem.getImage());
                                    desIntent.putExtra("attribute_name", clickedItem.getTitle());
                                    desIntent.putExtra("short_description", clickedItem.getShortdes());

                                    desIntent.putExtra("price", clickedItem.getMrpprice());
                                    desIntent.putExtra("price", clickedItem.getRsprice());

                                    desIntent.putExtra("productid", clickedItem.getProductid());
                                    desIntent.putExtra("skuid", clickedItem.getSkuid());
                                    desIntent.putExtra("long_description", clickedItem.getLongdesc());

                                    startActivity(desIntent);

                                }

                                @Override
                                public void buttonClick(View v, int position) {
                                         CategoriesGetterSetter buttonclicked = categoriesGetterSetters.get(position);

                                          proname = buttonclicked.getTitle();
                                          pimage = buttonclicked.getImage();
                                          pshort_dec = buttonclicked.getShortdes();
                                        //  plong_dec = buttonclicked.getLongdesc();
                                         plong_dec = "very god produt";
                                          p_sukid = buttonclicked.getSkuid();
                                          p_prodid = buttonclicked.getProductid();
                                         // pmrp = buttonclicked.getMrpprice();
                                          pmrp = "420";
                                          p_prie = buttonclicked.getRsprice();
                                          pvariation = buttonclicked.getVariationid();


                                    templist = new ArrayList<>();
                                    //templist.add(new CategoriesGetterSetter(pimage, proname, pshort_dec, pmrp, p_prie, p_prodid, p_sukid, plong_dec, variation_temp, qty_temp, pvariation));

                                    //now we are going to add produt in Array List
                                 /*   SharedPreferences.Editor editor = sharedPreferences.edit();
                                    Gson gson = new Gson();
                                    String json = gson.toJson(templist);
                                    editor.putString("task list", json);
                                    editor.apply();  */

                                    Gson gson = new Gson();
                                    final String json = sharedPreferences.getString("task list", null);
                                    Type type = new TypeToken<ArrayList<CartGetterSetter>>() {}.getType();
                                    templist = gson.fromJson(json, type);

                                    if (templist == null){
                                        templist = new ArrayList<>();
                                    }

                                    //Toast.makeText(getApplicationContext(),  "hi"+pvariation, Toast.LENGTH_SHORT).show();

                                   setAddToCartDialod();


                               /*     String ADDTOCARTFILE = "addtoartfile";
                                    SharedPreferences settings = getSharedPreferences(ADDTOCARTFILE,
                                            MODE_PRIVATE);
                                    SharedPreferences.Editor prefEditor = settings.edit();
                                    prefEditor.putString("productname", proname);
                                    prefEditor.putString("pimage", pimage);
                                    prefEditor.putString("pvariation", String.valueOf(pvariation));
                                   // prefEditor.putString("location", location);
                                    prefEditor.commit();

                                         loadingDialog.startAddToCartDialog();   */

                                    //Toast.makeText(Categories.this, "H "+pvariation, Toast.LENGTH_SHORT).show();

                                }
                            });
                            categoriesRecycler.setAdapter(categoriesAdaptor);

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
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "1KG", "888", "100"));
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "1KG", "555", "200"));
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "3KG", "1000", "300"));
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "1KG", "888", "100"));
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "1KG", "888", "100"));
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "1KG", "888", "100"));
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "1KG", "888", "100"));
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "1KG", "888", "100"));
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "1KG", "888", "100"));
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "1KG", "888", "100"));
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "1KG", "888", "100"));
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "1KG", "888", "100"));
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "1KG", "888", "100"));
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "1KG", "888", "100"));
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "1KG", "888", "100"));
        categoriesGetterSetters.add(new CategoriesGetterSetter(R.drawable.fruts, "Vegetables and Fruits", "1KG", "888", "100"));


        adapter = new CategoriesAdaptor(categoriesGetterSetters, mcontext, new CustomItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent desIntent = new Intent(getApplicationContext(), ProductView.class);


              //  desIntent.putExtra("title", categoriesGetterSetters.get(position).getTitle());

                startActivity(desIntent);

            }


        }); */
        categoriesRecycler.setAdapter(categoriesAdaptor);
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

   public void setAddToCartDialod(){


        final AlertDialog.Builder alert = new AlertDialog.Builder(Categories.this);
        View mview = getLayoutInflater().inflate(R.layout.add_to_cart_dialog, null);
        alert.setView(mview);

        final AlertDialog alertDialog = alert.create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();


        ImageView imageView = (ImageView) alertDialog.findViewById(R.id.id_cartdilalogimage);
        textView = (TextView) alertDialog.findViewById(R.id.id_productname);
        Spinner spinner = (Spinner) alertDialog.findViewById(R.id.id_spinner);

       Picasso.get().load(pimage).fit().centerInside().into(imageView);
       textView.setText(proname);

       ArrayAdapter simpleAdapter = new ArrayAdapter(Categories.this, R.layout.support_simple_spinner_dropdown_item, pvariation);

       simpleAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

       spinner.setSelection(0);
       spinner.setAdapter(simpleAdapter);

       variation_temp = spinner.getSelectedItem().toString();

       Button button_canel = (Button) alertDialog.findViewById(R.id.id_cancel);
       Button button_stb = (Button) alertDialog.findViewById(R.id.id_submit);


       //here the intilization of elegant number
       ElegantNumberButton elegantNumberButton = (ElegantNumberButton) alertDialog.findViewById(R.id.id_dilalogqty);
       elegantNumberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
           @Override
           public void onClick(View view) {
               qty_temp = Integer.parseInt(elegantNumberButton.getNumber());
           }
       });


       button_stb.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

              // Log.d("dekho", qty_temp);

               Toast.makeText(getApplicationContext(), "Product Added Sucessfully", Toast.LENGTH_SHORT).show();

              templist.add(new CategoriesGetterSetter(pimage, proname, pshort_dec, pmrp, p_prie * qty_temp, p_prodid,
                       p_sukid, plong_dec, variation_temp, qty_temp, pvariation));

           //   templist.add(new CategoriesGetterSetter(pimage, proname, pshort_dec, pmrp, p_prie, p_prodid, p_sukid, plong_dec, variation_temp, qty_temp, pvariation));



               //now we are going to add produt in Array List
               SharedPreferences.Editor editor = sharedPreferences.edit();
               Gson gson = new Gson();
               String json = gson.toJson(templist);
               editor.putString("task list", json);
               editor.apply();

               alertDialog.dismiss();
           }
       });


       //here mentioned the canel button
       button_canel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               alertDialog.dismiss();
           }
       });
   }



}