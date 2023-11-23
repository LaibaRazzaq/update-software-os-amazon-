package com.example.appUpdate.adapters;

import android.annotation.SuppressLint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appUpdate.R;
import com.example.appUpdate.activities.DuplicatePhotos;


import java.util.ArrayList;
import java.util.List;

public class DuplicatePhotoAdapter extends RecyclerView.Adapter<DuplicatePhotoAdapter.ViewHolder>{
    public List<String> duplicateImages;
    private SparseBooleanArray mSelectedItemsIds;
    public DuplicatePhotoAdapter(){
        duplicateImages=new ArrayList<>();
        mSelectedItemsIds=new SparseBooleanArray();

    }
    public void setData(List<String> duplicateImages){
        this.duplicateImages.clear();
        this.duplicateImages.addAll(duplicateImages);
        notifyDataSetChanged();
    }
    public  void setmSelectedItemsIds(SparseBooleanArray mSelectedItemsIds){
        this.mSelectedItemsIds=mSelectedItemsIds;
        notifyDataSetChanged();
    }
    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.duplicate_photos_item,parent,false);
        return new ViewHolder( view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
           holder.checkBox.setChecked(mSelectedItemsIds.get(position));
        String filePath=duplicateImages.get(position);



        Glide.with(holder.imageView.getContext()).load(filePath).into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return duplicateImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.duplicatePhotos);
            checkBox=itemView.findViewById(R.id.checkItem);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int postion=getAdapterPosition();
                    if (!mSelectedItemsIds.get(postion,false)){

                        mSelectedItemsIds.put(postion,true);
                    }else {
                        mSelectedItemsIds.delete(postion);
                    }
                    notifyDataSetChanged();

                    boolean isAnyItemSelected = false;
                    for (int i = 0; i < mSelectedItemsIds.size(); i++) {
                        if (mSelectedItemsIds.valueAt(i)) {
                            isAnyItemSelected = true;
                            break;
                        }
                    }

                    // Show or hide views based on the selection status
                    if (isAnyItemSelected) {
                        DuplicatePhotos.selectImages.setVisibility(View.GONE);
                        DuplicatePhotos.delBtn.setVisibility(View.VISIBLE);
                    } else {
                        DuplicatePhotos.selectImages.setVisibility(View.VISIBLE);
                        DuplicatePhotos.delBtn.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public  void  removeImage(String imagePath){

        for (int i=0;i<duplicateImages.size();i++){
            if (duplicateImages.get(i).equals(imagePath)){
                duplicateImages.remove(i);
                break;
            }
        }
    }
}
