package com.example.appUpdate.activities;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appUpdate.R;



public class ItemViewHolder1 extends RecyclerView.ViewHolder {
   public TextView appName,appVersion;
   public ImageView appIcon;
   public CheckBox checkBox;
    public ItemViewHolder1(@NonNull View itemView) {


        super(itemView);
        appName=(TextView) itemView.findViewById(R.id.appName);
        appVersion=(TextView) itemView.findViewById(R.id.appVersion);
        appIcon=(ImageView)itemView.findViewById(R.id.imageView);
        checkBox=itemView.findViewById(R.id.check);

    }
}
