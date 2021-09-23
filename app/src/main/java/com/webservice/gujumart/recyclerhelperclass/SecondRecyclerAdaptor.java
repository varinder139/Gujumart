package com.webservice.gujumart.recyclerhelperclass;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.webservice.gujumart.R;

import java.util.ArrayList;

public class SecondRecyclerAdaptor extends RecyclerView.Adapter<SecondRecyclerAdaptor.SecondRecyclerViewHolder> {

    Context context;
    ArrayList<SecondRecyclerGetterSetter> secondRecyclerGetterSetterArrayList;

    public SecondRecyclerAdaptor(Context context, ArrayList<SecondRecyclerGetterSetter> secondRecyclerGetterSetterArrayList) {
        this.context = context;
        this.secondRecyclerGetterSetterArrayList = secondRecyclerGetterSetterArrayList;
    }

    @NonNull
    @Override
    public SecondRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scond_receyclerview_cardview, parent, false);
        SecondRecyclerViewHolder secondRecyclerViewHolder = new SecondRecyclerViewHolder(view);
        return secondRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SecondRecyclerViewHolder holder, int position) {

        SecondRecyclerGetterSetter currentItem = secondRecyclerGetterSetterArrayList.get(position);

        String imageUrl = currentItem.getImage();
        String titleurl = currentItem.getTitle();
        String mrpurl = currentItem.getMrpprice();
        String priceurl = currentItem.getPrice();

        Picasso.get().load(imageUrl).fit().centerInside().into(holder.imgage);
        holder.title.setText(titleurl);
        holder.mrpprice.setText(mrpurl);
        holder.rsPrice.setText(priceurl);

        /*
        holder.img.setImageResource(secondRecyclerGetterSetter.getImage());
        // holder.tagimg.setImageResource(secondRecyclerGetterSetter.getTagimage());
        holder.title.setText(secondRecyclerGetterSetter.getTitle());
      //  holder.mrp.setText(secondRecyclerGetterSetter.getMrp());
        holder.mrpprice.setText(secondRecyclerGetterSetter.getMrpprice());
        //holder.rs.setText(secondRecyclerGetterSetter.getRs());
        holder.rsPrice.setText(secondRecyclerGetterSetter.getPrice());
        //holder.ratingBar.setRating(secondRecyclerGetterSetter.getRatingBar()); */

    }

    @Override
    public int getItemCount() {
        return secondRecyclerGetterSetterArrayList.size();
    }

    public static class SecondRecyclerViewHolder extends RecyclerView.ViewHolder{

          ImageView imgage;
                  //tagimg;
          TextView title, mrpprice, rsPrice;
          Button button;

        public SecondRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            imgage = itemView.findViewById(R.id.scv_product_image);
            //tagimg = itemView.findViewById(R.id.scv_tag);
            title = itemView.findViewById(R.id.scv_product_title);
            mrpprice = itemView.findViewById(R.id.scv_mrpprice);
            rsPrice = itemView.findViewById(R.id.scv_mrp);
            button = itemView.findViewById(R.id.scv_add_to_cart);
            mrpprice.setPaintFlags(mrpprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        }
    }
}
