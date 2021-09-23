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

public class BottomRecyclerAdaptor extends RecyclerView.Adapter<BottomRecyclerAdaptor.BottomRecyclerViewHolder> {

    Context context;
    ArrayList<BottomRecyclerGetterSetter> bottomRecyclerGetterSetterArrayList;

    public BottomRecyclerAdaptor(Context context, ArrayList<BottomRecyclerGetterSetter> bottomRecyclerGetterSetterArrayList) {
        this.context = context;
        this.bottomRecyclerGetterSetterArrayList = bottomRecyclerGetterSetterArrayList;

    }

    @NonNull
    @Override
    public BottomRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scond_receyclerview_cardview, parent, false);
        BottomRecyclerViewHolder bottomRecyclerViewHolder = new BottomRecyclerViewHolder(view);
        return bottomRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BottomRecyclerViewHolder holder, int position) {

        BottomRecyclerGetterSetter currentItem = bottomRecyclerGetterSetterArrayList.get(position);

        String imageUrl = currentItem.getImage();
        String titleurl = currentItem.getTitle();
        String mrpurl = currentItem.getMrpprice();
        String priceurl = currentItem.getPrice();

        Picasso.get().load(imageUrl).fit().centerInside().into(holder.imgage);
        holder.title.setText(titleurl);
        holder.mrpprice.setText(mrpurl);
        holder.rsPrice.setText(priceurl);



       /* holder.img.setImageResource(bottomRecyclerGetterSetter.getImage());
        holder.tagimg.setImageResource(bottomRecyclerGetterSetter.getTagimage());
        holder.title.setText(bottomRecyclerGetterSetter.getTitle());
        holder.mrp.setText(bottomRecyclerGetterSetter.getMrp());
        holder.mrpprice.setText(bottomRecyclerGetterSetter.getMrpprice());
        holder.rs.setText(bottomRecyclerGetterSetter.getRs());
        holder.rsPrice.setText(bottomRecyclerGetterSetter.getPrice());
        //holder.ratingBar.setRating(bottomRecyclerGetterSetter.getRatingBar()); */

    }

    @Override
    public int getItemCount() {
        return bottomRecyclerGetterSetterArrayList.size();
    }

    public static class BottomRecyclerViewHolder extends RecyclerView.ViewHolder{

        ImageView imgage;
        //tagimg;
        TextView title, mrpprice, rsPrice;
        Button button;


        public BottomRecyclerViewHolder(@NonNull View itemView) {
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
