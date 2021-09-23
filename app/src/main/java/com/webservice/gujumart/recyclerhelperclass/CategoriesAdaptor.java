package com.webservice.gujumart.recyclerhelperclass;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.webservice.gujumart.R;

import java.util.ArrayList;

public class CategoriesAdaptor extends RecyclerView.Adapter<CategoriesAdaptor.CategoriesViewHolder> {

    ArrayList<CategoriesGetterSetter> categoriesGetterSetterArrayList;
    Context mcontext;
    CategoriesClickListener clickListener;

    public CategoriesAdaptor(Context mcontext, ArrayList<CategoriesGetterSetter> categoriesGetterSetterArrayList, CategoriesClickListener clickListener) {
        this.mcontext = mcontext;
        this.categoriesGetterSetterArrayList = categoriesGetterSetterArrayList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CategoriesAdaptor.CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_cardview, parent, false);
        final CategoriesAdaptor.CategoriesViewHolder categoriesViewHolder = new CategoriesAdaptor.CategoriesViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(view, categoriesViewHolder.getPosition());
            }
        });

        return categoriesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesAdaptor.CategoriesViewHolder holder, int position) {

        CategoriesGetterSetter currentItem = categoriesGetterSetterArrayList.get(position);

        String imageUrl = currentItem.getImage();
        String maintitle = currentItem.getTitle();
        String shortdec = currentItem.getShortdes();
        String mrp = currentItem.getMrpprice();
        String price = String.valueOf(currentItem.getRsprice());

        Picasso.get().load(imageUrl).fit().centerInside().into(holder.imgage);
        holder.title.setText(maintitle);
        holder.qty.setText(shortdec);
        holder.mrpprice.setText(mrp);
        holder.rsprice.setText(price);

        /*
        holder.img.setImageResource(categoriesGetterSetter.getImage());
        holder.title.setText(categoriesGetterSetter.getTitle());
        holder.qty.setText(categoriesGetterSetter.getShortdes());
        holder.mrpprice.setText(categoriesGetterSetter.getMrpprice());
        holder.rsprice.setText(categoriesGetterSetter.getRsprice());

         */
        /*holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(mcontext, ProductView.class);
                  int imageurl = categoriesGetterSetter.getImage();
                intent.putExtra("imageurl",imageurl);
                mcontext.startActivity(intent);

            }
        });  */



    }

    @Override
    public int getItemCount() {
        return categoriesGetterSetterArrayList.size();
    }

    public class CategoriesViewHolder extends RecyclerView.ViewHolder{

        ImageView imgage;
        TextView title, qty, mrpprice, rsprice;
        Button button;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);

            imgage = itemView.findViewById(R.id.ccv_mainimage);
            title = itemView.findViewById(R.id.ccv_title);
            qty = itemView.findViewById(R.id.ccv_qty);
            mrpprice = itemView.findViewById(R.id.ccv_mrpprice);
            rsprice = itemView.findViewById(R.id.ccv_rspprice);
            button = itemView.findViewById(R.id.ccv_add_to_cart);

            mrpprice.setPaintFlags(mrpprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                clickListener.buttonClick(view, getAdapterPosition() );

                }
            });
        }
    }
}