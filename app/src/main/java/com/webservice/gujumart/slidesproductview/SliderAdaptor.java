package com.webservice.gujumart.slidesproductview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.makeramen.roundedimageview.RoundedImageView;
import com.webservice.gujumart.R;

import java.util.List;

public class SliderAdaptor extends RecyclerView.Adapter<SliderAdaptor.SliderViewHolder> {
    private List<SliderItem> sliderItems;
    private ViewPager2 viewPager2;

    public SliderAdaptor(List<SliderItem> sliderItems, ViewPager2 viewPager2) {
        this.sliderItems = sliderItems;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_item_container,parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(sliderItems.get(position));
        if (position == sliderItems.size()-2){
            viewPager2.post(runnable);
        }

    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }


    /////// View holder Class
    class SliderViewHolder extends RecyclerView.ViewHolder{

        private RoundedImageView imageView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = imageView;

            imageView = itemView.findViewById(R.id.imageSlide);
        }
        void setImage(SliderItem sliderItem){
            // if you want to display image from the internet,
            // you can put code here using glide

            imageView.setImageResource(sliderItem.getImage());
        }
    }
    //Code here for repeate the images
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderItems.addAll(sliderItems);
            notifyDataSetChanged();

        }
    };
}
