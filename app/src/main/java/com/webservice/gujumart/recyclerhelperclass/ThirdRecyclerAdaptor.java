package com.webservice.gujumart.recyclerhelperclass;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.webservice.gujumart.Categories;
import com.webservice.gujumart.R;

import java.util.ArrayList;

public class ThirdRecyclerAdaptor extends RecyclerView.Adapter<ThirdRecyclerAdaptor.ThirdRecyclerViewHolder> {

    ArrayList<ThirdRecyclerGetterSetter> thirdRecyclerGetterSetterArrayList;
    Context mcontext;
    CustomItemClickListener clickListener;

    public ThirdRecyclerAdaptor(Context mcontext, ArrayList<ThirdRecyclerGetterSetter> thirdRecyclerGetterSetterArrayList, CustomItemClickListener clickListener) {
        this.thirdRecyclerGetterSetterArrayList = thirdRecyclerGetterSetterArrayList;
        this.mcontext = mcontext;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ThirdRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.third_receyclerview_cardview, parent, false);
        final ThirdRecyclerViewHolder thirdRecyclerViewHolder = new ThirdRecyclerViewHolder(view);

          view.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  clickListener.onItemClick(view, thirdRecyclerViewHolder.getPosition());
              }
          });

        return thirdRecyclerViewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ThirdRecyclerViewHolder holder, int position) {

        ThirdRecyclerGetterSetter currentItem = thirdRecyclerGetterSetterArrayList.get(position);

        String imageUrl = currentItem.getImg();
        String creatorName = currentItem.getTitle();

        Picasso.get().load(imageUrl).fit().centerInside().into(holder.imgage);
        holder.title.setText(creatorName);

        /* holder.img.setImageResource(thirdRecyclerGetterSetter.getImage());
        holder.title.setText(thirdRecyclerGetterSetter.getTitle());
        holder.decs.setText(thirdRecyclerGetterSetter.getDescription()); */


    }

    @Override
    public int getItemCount() {
        return thirdRecyclerGetterSetterArrayList.size();
    }

    public static class ThirdRecyclerViewHolder extends RecyclerView.ViewHolder{

          ImageView imgage;
          TextView title;

        public ThirdRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            imgage = itemView.findViewById(R.id.tcv_mainimage);
            title = itemView.findViewById(R.id.tcv_title);

        }
    }
}
