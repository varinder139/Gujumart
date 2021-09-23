package com.webservice.gujumart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webservice.gujumart.recyclerhelperclass.CartAdaptor;
import com.webservice.gujumart.recyclerhelperclass.CartDeleteButtonListener;
import com.webservice.gujumart.recyclerhelperclass.CartGetterSetter;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Cart extends AppCompatActivity {
    RecyclerView cartRecycler;
    CartAdaptor cartAdaptor;
    Context mcontext;

    String arrayString;
    ArrayList<CartGetterSetter> cartGetterSetters = new ArrayList<>();

    SharedPreferences sharedPreferences;
    public static final String fileName = "login";
    public static final String Token = "token";
    public static final String ID = "id";
    public static final String Mobile = "mobile";


    int total = 0;
    int count = 0;
    TextView totalpriceTextView, totalItemAdded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        totalpriceTextView = findViewById(R.id.totalPrice);
        totalItemAdded = findViewById(R.id.totalItemCounted);

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);

        //---- hooks recycler view -----
        cartRecycler = (RecyclerView) findViewById(R.id.recycler_cart);

        setCartRecycler();

    }


    private void setCartRecycler() {

        // here setup Recycler View
        cartRecycler.setHasFixedSize(true);
        cartRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mcontext = Cart.this;

        Gson gson = new Gson();
        final String json = sharedPreferences.getString("task list", null);
        Type type = new TypeToken<ArrayList<CartGetterSetter>>() {}.getType();
        cartGetterSetters = gson.fromJson(json, type);

        if (cartGetterSetters == null){
            cartGetterSetters = new ArrayList<>();
        }

        cartAdaptor = new CartAdaptor(mcontext, cartGetterSetters, new CartDeleteButtonListener() {
            @SuppressLint("ApplySharedPref")
            @Override
            public void dltButtonClick(View v, int position) {


                cartGetterSetters.remove(position);
                cartAdaptor.notifyItemRemoved(position);

                arrayString = new Gson().toJson(cartGetterSetters);

                sharedPreferences.edit().putString("task list", arrayString).commit();


                total = 0;
                itemAndPriceCount();


            }
        });

        cartRecycler.setAdapter(cartAdaptor);

        itemAndPriceCount();

    }

      public  void itemAndPriceCount(){

        //here count the total number
          count =  cartGetterSetters.size();
          totalItemAdded.setText(String.valueOf("Total Product Added = "+count));


          // here calculate total Amount
          for(int i = 0; i < cartGetterSetters.size(); i++){
              total = total + cartGetterSetters.get(i).getRsprice();
          }

          totalpriceTextView.setText(String.valueOf("Total Amount = " +total));
      }

     public void backToHome(View view) {
        Intent intent = new Intent(getApplicationContext(), Dashboard.class);
        startActivity(intent);
                   }

    public void checkOut(View view) {
        if (sharedPreferences.contains(Token)) {
            //  headerText.setText(sharedPreferences.getString(Mobile, ""));

            String name = sharedPreferences.getString("id", "No name defined");
            String mmm = sharedPreferences.getString("mobile", "No name defined");
            //Toast.makeText(mcontext, "Your Login Mobile No. "+mmm, Toast.LENGTH_SHORT).show();
            //headerText.setText(mmm);

            Intent intent = new Intent(getApplicationContext(), MyProfileSecond.class);
            startActivity(intent);

            // Toast.makeText(mcontext, "sucess", Toast.LENGTH_SHORT).show();
        } else
        {

            Toast.makeText(mcontext, "Your Not Login please login", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        }


    }
}
