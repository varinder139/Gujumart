package com.webservice.gujumart.recyclerhelperclass;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.webservice.gujumart.R;

import java.util.ArrayList;

public class FirstRecyclerAdaptor extends RecyclerView.Adapter<FirstRecyclerAdaptor.FirstRecyclerViewHolder> {

    Context context;
    ArrayList<FirstRecyclerGetterSetter> firstRecyclerGetterSetterArrayList;
    CustomItemClickListener clickListener;

    public FirstRecyclerAdaptor(Context context, ArrayList<FirstRecyclerGetterSetter> firstRecyclerGetterSetterArrayList, CustomItemClickListener clickListener) {
        this.context = context;
        this.firstRecyclerGetterSetterArrayList = firstRecyclerGetterSetterArrayList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FirstRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_firstrecyclerview_cardview, parent, false);
        FirstRecyclerViewHolder firstRecyclerViewHolder = new FirstRecyclerViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(view, firstRecyclerViewHolder.getPosition());
            }
        });

        return firstRecyclerViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull FirstRecyclerViewHolder holder, int position) {

        FirstRecyclerGetterSetter currentItem = firstRecyclerGetterSetterArrayList.get(position);

        String imageUrl = currentItem.getImg();
        String creatorName = currentItem.getTitle();

        Picasso.get().load(imageUrl).fit().centerInside().into(holder.imgage);
        holder.title.setText(creatorName);


        //holder.img.setImageResource(firstRecyclerGetterSetter.getImg());
        //holder.title.setText(firstRecyclerGetterSetter.getTitle());


    }

    @Override
    public int getItemCount() {
        return firstRecyclerGetterSetterArrayList.size();
    }

    public static class FirstRecyclerViewHolder extends RecyclerView.ViewHolder{

          ImageView imgage;
          TextView title;

        public FirstRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);

            imgage = itemView.findViewById(R.id.firstrecycler_image);
            title = itemView.findViewById(R.id.firstrecycler_title);
        }
    }
}
