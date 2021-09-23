package com.webservice.gujumart.recyclerhelperclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.webservice.gujumart.R;

import java.util.ArrayList;

public class MyProfileAdaptor extends RecyclerView.Adapter<MyProfileAdaptor.HolderClass> {

    Context context;
    ArrayList<MyProfileGetSet> profileGetSetArrayList;
    CategoriesClickListener clickListener;

    public MyProfileAdaptor(Context context, ArrayList<MyProfileGetSet> profileGetSetArrayList, CategoriesClickListener clickListener) {
        this.context = context;
        this.profileGetSetArrayList = profileGetSetArrayList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyProfileAdaptor.HolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myprofile_cardview, parent, false);
        final MyProfileAdaptor.HolderClass holderClass = new MyProfileAdaptor.HolderClass(view);
        return holderClass;
    }

    @Override
    public void onBindViewHolder(@NonNull MyProfileAdaptor.HolderClass holder, int position) {
        MyProfileGetSet currentItem = profileGetSetArrayList.get(position);

        String nameurl = currentItem.getName();
        String mobileurl = currentItem.getMobile();
        String emailurl = currentItem.getEmail();
        String pincodeurl = currentItem.getPinode();
        String distriturl = currentItem.getDistrict();
        String fAddressurl = currentItem.getFull_address();

        //Picasso.get().load(imageUrl).fit().centerInside().into(holder.imgage);
        holder.name.setText("Name: "+nameurl);
        holder.mobile.setText("Mobile: "+mobileurl);
        holder.email.setText("E-mail: "+emailurl);
        holder.pincode.setText("Pincode: "+pincodeurl);
        holder.distrit.setText("Distict: "+distriturl);
        holder.fulladdress.setText("Full Address: "+fAddressurl);
    }

    @Override
    public int getItemCount() {
        return profileGetSetArrayList.size();
    }

    public class HolderClass extends RecyclerView.ViewHolder {

        TextView name, mobile, email, pincode, distrit, fulladdress;
        Button update, delete;

        public HolderClass(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.profile_name);
            mobile = itemView.findViewById(R.id.profile_mobile);
            email = itemView.findViewById(R.id.profile_mail);
            pincode = itemView.findViewById(R.id.profile_pincode);
            distrit = itemView.findViewById(R.id.profile_district);
            fulladdress = itemView.findViewById(R.id.profile_fullAddress);
            update = itemView.findViewById(R.id.btn_update_pro);
            delete = itemView.findViewById(R.id.btn_dlt_adds);

            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.buttonClick(view, getAdapterPosition() );
                }
            });
        }
    }
}