package com.example.appUpdate.adapters;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appUpdate.R;
import com.example.appUpdate.activities.SensorDetail;


import java.util.ArrayList;
import java.util.List;

public class sensorAdapter extends RecyclerView.Adapter<sensorAdapter.ViewHolder> implements Filterable {
    Context mContext;
    List<Sensor> sensorModelList;
    List<Sensor> sensorsFullList;
    public sensorAdapter(Context mContext, List<Sensor> sensorModelList) {
        this.mContext = mContext;
        this.sensorModelList = new ArrayList<>(sensorModelList);
        sensorsFullList=new ArrayList<>(sensorModelList);
    }




    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.sensor_item, parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull  ViewHolder holder, int position) {
        String sensorName=sensorModelList.get(position).getName();
        holder.textView.setText(sensorName);
          String id=String.valueOf(sensorModelList.get(position).getVersion());
          String vendor=sensorModelList.get(position).getVendor();
          int type= sensorModelList.get(position).getType();
          String resulation= String.valueOf(sensorModelList.get(position).getResolution());
          String power= String.valueOf(sensorModelList.get(position).getPower());
          boolean isWakeUp=sensorModelList.get(position).isWakeUpSensor();
        boolean isDynamic= false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            isDynamic = sensorModelList.get(position).isDynamicSensor();
        }
        String maxRange= String.valueOf(sensorModelList.get(position).getMaximumRange());

        boolean finalIsDynamic = isDynamic;
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  Intent intent=new
                          Intent(mContext, SensorDetail.class);
                  intent.putExtra("sensorName",sensorName);
                  intent.putExtra("vendor",vendor);
                  intent.putExtra("type",type);
                  intent.putExtra("maxRange",maxRange);
                  intent.putExtra("resulation",resulation);
                  intent.putExtra("power",power);
                  intent.putExtra("wakeUp",isWakeUp);
                  intent.putExtra("dynamic", finalIsDynamic);
                  mContext.startActivity(intent);
              }
          });



       holder.type.setText("Version"+" "+ id );
        if (position%5==0){
            holder.imageView.setImageResource(R.drawable.ic_sensor);

        }else if (position%5==1){
            holder.imageView.setImageResource(R.drawable.ic_sensor1);
        }else if (position%5==2){
            holder.imageView.setImageResource(R.drawable.ic_sensor2);
        }else if (position%5==3) {
            holder.imageView.setImageResource(R.drawable.ic_sensor4);
        }else if (position%5==4) {
            holder.imageView.setImageResource(R.drawable.ic_sensor5);
        }

    }

    @Override
    public int getItemCount() {
        return sensorModelList.size();
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Sensor> filterList=new ArrayList<>();
            if (constraint==null||constraint.length()==0){
                filterList.addAll(sensorsFullList);




            }else {
                String filterPattern=constraint.toString().toLowerCase().trim();
                for (Sensor sensor:sensorsFullList){
                    if (sensor.getName().toLowerCase().contains(filterPattern)){
                        filterList.add(sensor);
                    }
                }
            }
           FilterResults results=new FilterResults();
            results.values=filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            sensorModelList.clear();
            sensorModelList.addAll((List<Sensor>)filterResults.values);
            notifyDataSetChanged();

        }
    };

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView, type;
        ImageView imageView;
        RelativeLayout relativeLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.textView);
            type=itemView.findViewById(R.id.type);
            imageView=itemView.findViewById(R.id.imageView);
            relativeLayout=itemView.findViewById(R.id.rl);

        }
    }


}

