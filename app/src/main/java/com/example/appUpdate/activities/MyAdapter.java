package com.example.appUpdate.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appUpdate.R;
import com.google.android.gms.ads.nativead.NativeAd;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable, View.OnClickListener {
    private static final int IS_AD = 0;
    private static final int NOT_Ad = 1;
     ArrayList<ItemClass> filterAppsArray =new ArrayList<>();
    LayoutInflater layoutInflater;
    MyAdapter myAdapter;
int positionnew;
boolean check=false;

    private ArrayList<Object> objects = new ArrayList<>();
    //ArrayList<ItemClass> applicationInfoArray;
    ItemClass item = null;
    public void setList(List<ItemClass> list){
        filterAppsArray.addAll(list);
        this.objects.addAll(list);
        check=false;
        Log.d("filter", ""+filterAppsArray.size());
    }

    public void setAd(List<NativeAd> nativeAd){
        this.objects.addAll(nativeAd);
        check=true;
        notifyDataSetChanged();
    }

    public void setObject (ArrayList<Object> object){
        this.objects.clear();
        this.objects.addAll(object);
        check=true;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == IS_AD){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_ad_card_view,parent,false);
            return new AdViewHolder(view);
        }else{
            View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.application_adapter_layout,parent,false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d("testing_position", getItemViewType(position)+"");

        if(getItemViewType(position)==IS_AD){
            check=true;
            AdViewHolder adv =  (AdViewHolder) holder;
            adv.setNativeAd((NativeAd) objects.get(position));


        }else{

            Log.d("testing_new_nm",  objects.get(position) + "");
            ItemClass itemClass = (ItemClass) objects.get(position);
            ItemViewHolder ivh = (ItemViewHolder) holder;
            // ivh.imageView.setOnClickListener(this);

            ivh.imageView.setImageDrawable(itemClass.icon);
            ivh.textView.setText(itemClass.appname);
            ivh.sizetextView.setText(itemClass.versionName);
            ivh.versiontextView.setText(itemClass.lastDate);

            positionnew=holder.getAdapterPosition();


            ivh.checkUpdateBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Context context = v.getContext();

                    Intent intent = new Intent(context, AppDetailActivity.class);
                    // int itemPosition = this.getAdapterPosition();


                    intent.putExtra("appname",  itemClass.appname);
                    intent.putExtra("lastDate",  itemClass.lastDate);
                    intent.putExtra("packageName",  itemClass.packageName);
                    intent.putExtra("versionCode",  itemClass.versionCode);
                    intent.putExtra("versionName",  itemClass.versionName);
                    Bitmap bitmap= getBitmapFromDrawable(itemClass.icon);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] b = baos.toByteArray();
                    intent.putExtra("icon", b);
                   // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }

            });

        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (objects.get(position) instanceof NativeAd) {
            return IS_AD;
        } else {
            return NOT_Ad;
        }
    }
    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ItemClass> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(filterAppsArray);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (ItemClass item : filterAppsArray) {
                    if (item.appname.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
           objects.clear();
            objects.addAll((ArrayList<ItemClass>) results.values);

            check=false;

            notifyDataSetChanged();
        }
    };



    @Override
    public void onClick(View view) {


    }

    static private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {

        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        final Canvas canvas = new Canvas(bmp);

        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());

        drawable.draw(canvas);

        return bmp;

    }
}

