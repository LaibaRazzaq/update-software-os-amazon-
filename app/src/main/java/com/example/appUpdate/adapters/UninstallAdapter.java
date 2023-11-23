package com.example.appUpdate.adapters;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appUpdate.R;
import com.example.appUpdate.activities.AdViewHolder;
import com.example.appUpdate.activities.ItemClass;
import com.example.appUpdate.activities.ItemViewHolder1;
import com.google.android.gms.ads.nativead.NativeAd;



import java.util.ArrayList;
import java.util.List;

public class UninstallAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    ArrayList<Object> objects=new ArrayList<>();
    private static final int IS_AD = 0;
    private static final int NOT_Ad = 1;
    boolean check=false;
    ArrayList<ItemClass> filterAppsArray =new ArrayList<>();
    private SparseBooleanArray checkedItems = new SparseBooleanArray();
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        if(viewType == IS_AD){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_ad_card_view,parent,false);
            return new AdViewHolder(view);
        }else{
            View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.uninstall_item,parent,false);
            return new ItemViewHolder1(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==IS_AD){
            check=true;
            AdViewHolder adv =  (AdViewHolder) holder;
            adv.setNativeAd((NativeAd) objects.get(position));


        }else {
            ItemClass itemClass = (ItemClass) objects.get(position);
          ItemViewHolder1 iv= (ItemViewHolder1) holder;
          iv.appName.setText(itemClass.appname);
          iv.appIcon.setImageDrawable(itemClass.icon);
          iv.appVersion.setText(itemClass.versionName);
          iv.checkBox.setTag(itemClass.packageName);

            iv.checkBox.setTag(itemClass.packageName);
            iv.checkBox.setChecked(checkedItems.get(position)); // set checkbox state based on saved state
            iv.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int position = holder.getAdapterPosition();
                    checkedItems.put(position, isChecked); // save checked state of item
                }
            });
//          iv.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//              @Override
//              public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
//                   boolean isUpdating = false;
//                  if (checked){
//                      itemClass.checkBox.setChecked(true);
//                  }else {
//                      itemClass.checkBox.setChecked(false);
//                  }
//              }
//          });

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

public void removeApp(String packageName){
        for (int i=0;i<objects.size();i++){
            if (objects.get(i) instanceof ItemClass){
                ItemClass itemClass=(ItemClass) objects.get(i);
                if (itemClass.packageName.equals(packageName)){
                    objects.remove(i);
                    notifyDataSetChanged();
                    break;
                }
            }
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
}
