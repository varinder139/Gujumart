package com.webservice.gujumart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

public class DialogAddToCart implements AdapterView.OnItemSelectedListener {

    Context context;

    Activity activity;
    AlertDialog dialog;
    TextView textView;
    ImageView imageView;
    Button button_cancel, button_submit;
    String ADDTOCARTFILE = "addtoartfile";

    ElegantNumberButton elegantNumberButton;
   // final Activity activitytwo = (Activity) context;
  //  String msg = "select";


    public DialogAddToCart(Activity activity)
    {
        this.activity = activity;
        //this.context = context;
    }

    public void startAddToCartDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        ArrayList<String> temp = new ArrayList<>();
        temp.add("varinder");
        temp.add("varinder");
        temp.add("varinder");
        temp.add("varinder");

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.add_to_cart_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();

     //   SharedPreferences prefs = ((Activity) context)
       //         .getPreferences(Context.MODE_PRIVATE);



        SharedPreferences prefs = activity.getSharedPreferences(ADDTOCARTFILE, Activity.MODE_PRIVATE);
        String template = prefs.getString("productname", "");
        String pimage = prefs.getString("pimage", "");
        String pvariation = prefs.getString("pvariation", "");



        textView  = (TextView) dialog.findViewById(R.id.id_productname);
        textView.setText(template);

        imageView = (ImageView) dialog.findViewById(R.id.id_cartdilalogimage);
        //imageView.setImageURI(Uri.parse(pimage));
        Picasso.get().load(pimage).fit().centerInside().into(imageView);

        elegantNumberButton = (ElegantNumberButton) dialog.findViewById(R.id.id_dilalogqty);

        button_cancel = (Button) dialog.findViewById(R.id.id_cancel);
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        Spinner spinner = (Spinner) dialog.findViewById(R.id.id_spinner);
        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(activity,
                R.array.numbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this); */

        ArrayAdapter simpleAdapter = new ArrayAdapter(activity, R.layout.support_simple_spinner_dropdown_item,
                Collections.singletonList(temp));

        simpleAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        spinner.setSelection(0);
        spinner.setAdapter(simpleAdapter);
        //spinner.setSelected(position);

    }

    public void dismissCartDialog(){
        dialog.dismiss();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /*public void applyTexts(String username) {
        textView  = textView.findViewById(R.id.id_productname);
        //textView.setText(username);

    } */


}
