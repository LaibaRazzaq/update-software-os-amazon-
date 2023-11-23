package com.example.appUpdate.activities;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appUpdate.R;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public  TextView textView;
    public TextView sizetextView ;
    public TextView versiontextView ;
    public CardView checkUpdateBtn ;
    public RelativeLayout listViewLayout;
    public ImageView imageView;
    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);

         textView = (TextView) itemView.findViewById(R.id.textView);
         sizetextView = (TextView) itemView.findViewById(R.id.size_text_view);
         versiontextView = (TextView) itemView.findViewById(R.id.version_text_view);
         checkUpdateBtn = (CardView) itemView.findViewById(R.id.detailApp);
        // listViewLayout = (RelativeLayout) itemView.findViewById(R.id.listViewRelativeLayout);
         imageView = (ImageView) itemView.findViewById(R.id.imageView);


    }
}
