package com.webservice.gujumart.recyclerhelperclass;

import android.content.Context;
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

public class CartAdaptor extends RecyclerView.Adapter<CartAdaptor.CartRecyclerViewHolder> {

    ArrayList<CartGetterSetter> cartGetterSetterArrayList;
    Context mcontext;
    CartDeleteButtonListener clickListener;

    public CartAdaptor(Context mcontext, ArrayList<CartGetterSetter> cartGetterSetterArrayList, CartDeleteButtonListener clickListener) {
        this.mcontext = mcontext;
        this.cartGetterSetterArrayList = cartGetterSetterArrayList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CartAdaptor.CartRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_cardview, parent, false);
        CartAdaptor.CartRecyclerViewHolder cartRecyclerViewHolder = new CartAdaptor.CartRecyclerViewHolder(view);
        return cartRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdaptor.CartRecyclerViewHolder holder, int position) {

        CartGetterSetter currentItem = cartGetterSetterArrayList.get(position);

        String imageUrl = currentItem.getImage();
        String titleurl = currentItem.getTitle();
        String qtyurl = String.valueOf(currentItem.getQty());
        String priceurl = String.valueOf(currentItem.getRsprice());
        //String priceurl = String.valueOf(currentItem.getRsprice() * currentItem.getQty());

        Picasso.get().load(imageUrl).fit().centerInside().into(holder.img);
        holder.title.setText(titleurl);
        holder.mrpprice.setText(qtyurl);
        holder.rsPrice.setText(priceurl);

       // holder.img.setImageResource(cartGetterSetter.getImage());
      // Picasso.get().load(cartGetterSetter.getImage()).fit().centerInside().into(holder.img);
      // holder.title.setText(cartGetterSetter.getTitle());
        //holder.mrp.setText(cartGetterSetter.getMrp());
        // holder.mrpprice.setText(cartGetterSetter.getMrpprice());
        // holder.rsPrice.setText(cartGetterSetter.getRsprice());

    }

    @Override
    public int getItemCount() {

        return cartGetterSetterArrayList.size();
    }

    public class CartRecyclerViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView title, mrpprice, rsPrice;
        // ElegantNumberButton elegantNumberButton;
        Button cartdeleteBtn;


        public CartRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.img_product);
            title = itemView.findViewById(R.id.txt_product_name);
            mrpprice = itemView.findViewById(R.id.cartqty);
            rsPrice = itemView.findViewById(R.id.price);
            cartdeleteBtn = itemView.findViewById(R.id.cartDeletebtn);

            /* mrp.setPaintFlags(mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            mrpprice.setPaintFlags(mrpprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG); */

            cartdeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clickListener.dltButtonClick(view, getAdapterPosition() );

                }
            });

        }
    }
}
