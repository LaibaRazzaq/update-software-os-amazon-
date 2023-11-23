package com.example.appUpdate.activities;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appUpdate.R;
import com.google.android.gms.ads.nativead.NativeAd;

public class AdViewHolder extends RecyclerView.ViewHolder {
    public TemplateView template;

    public AdViewHolder(@NonNull View itemView) {
        super(itemView);

        template = itemView.findViewById(R.id.id_ad_template_view);

        NativeTemplateStyle styles = new
                NativeTemplateStyle.Builder().build();

        template.setStyles(styles);
    }

    public void setNativeAd(NativeAd nativeAd){

        template.setNativeAd(nativeAd);
    }
}
