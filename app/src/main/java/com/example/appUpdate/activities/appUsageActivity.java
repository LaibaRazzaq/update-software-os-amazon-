package com.example.appUpdate.activities;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.appUpdate.CustomFeedBack.BackTrackUtils;
import com.example.appUpdate.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public class appUsageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String TAG = "UsageStateActivity";
    private static final boolean localLOGV = false;
    private UsageStatsManager mUsageStatsManager;
    private LayoutInflater mInflater;
    private PackageManager pm;
    ListView listView;
    long totalMobileUsageTime;
    DonutProgress progress;
    private UsageStateAdapter mUsageStateAdapter;

    Admenager admenager;
    ProgressBar progressBar;
    LinearLayout appUsageSrceen;
    LottieAnimationView pleasewait;

    BackTrackUtils backTrackUtils;
    public static class AppNameComparator implements Comparator<UsageStats> {
        private Map<String, String> mAppLableList;

        AppNameComparator(Map<String, String> appList) {
            mAppLableList = appList;
        }

        @Override
        public int compare(UsageStats a, UsageStats b) {
            String aLablel = mAppLableList.get(a.getPackageName());
            String bLablel = mAppLableList.get(b.getPackageName());


            return aLablel.compareTo(bLablel);
        }
    }





    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_usage);
        backTrackUtils=new BackTrackUtils(this,this);
        progress=findViewById(R.id.time_usage_circle);
        progressBar=findViewById(R.id.progress);
        pleasewait=findViewById(R.id.pleaseWait);
        pleasewait.setVisibility(View.VISIBLE);
        appUsageSrceen=findViewById(R.id.appUsageScreen);
        appUsageSrceen.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        ActionBar actionBar=getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Mobile Usage");

        }



        admenager=new Admenager(this,this);
        admenager.load_InterstitialAd("admobe_intertesial_appUsage","admobe_intertesial_appUsage_test");



        Spinner typeSpinner =(Spinner) findViewById(R.id.typeSpinner);
        typeSpinner.setOnItemSelectedListener(this);
        listView =(ListView) findViewById(R.id.pkg_list);
        new asyncTask().execute();






    }

    private long getTotalMobileUsageTime() {
        long usageTime=0;
        UsageStatsManager usageStatsManager= null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        }

        Calendar currentDate = Calendar.getInstance();
        Calendar lastCheckedDate = Calendar.getInstance();

        while (true) {
            currentDate.setTimeInMillis(System.currentTimeMillis());
            if (currentDate.get(Calendar.DAY_OF_MONTH) != lastCheckedDate.get(Calendar.DAY_OF_MONTH)) {
                usageTime = 0;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeZone(TimeZone.getDefault());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            long startTime = calendar.getTimeInMillis();
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.MINUTE, 59);

            long endTime = calendar.getTimeInMillis();
            long st = 0, et = 0;
            long currentTime = startTime - 24 * 60 * 60 * 1000;

            List<UsageStats> usageStatsList = null;
            if (usageStatsManager != null) {
                usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, System.currentTimeMillis());

            for (UsageStats usageStats : usageStatsList) {
                usageTime += usageStats.getTotalTimeInForeground();

            }

            lastCheckedDate.setTimeInMillis(System.currentTimeMillis());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return usageTime;

        }
}
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mUsageStateAdapter.sortList(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        backTrackUtils.backTrack();

        admenager.showadmobeInterstitialAd();
        super.onBackPressed();
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }}
   private class asyncTask extends AsyncTask<Void,Void,Void>{

       @Override
       protected Void doInBackground(Void... voids) {


           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
               mUsageStatsManager=(UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
           }

           mInflater=(LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           pm=getPackageManager();
           mUsageStateAdapter=new UsageStateAdapter();
           listView.setAdapter(mUsageStateAdapter);
           totalMobileUsageTime=getTotalMobileUsageTime();
           return null;
       }

       @Override
       protected void onPostExecute(Void unused) {
           super.onPostExecute(unused);
           appUsageSrceen.setVisibility(View.VISIBLE);
           progressBar.setVisibility(View.GONE);
           pleasewait.setVisibility(View.GONE);
           String usageTvString= mUsageStateAdapter.getBreakDown(totalMobileUsageTime);
           progress.setText(usageTvString);
           long hour=TimeUnit.MILLISECONDS.toHours(totalMobileUsageTime);
           int per= (int) (hour*100/24);
           if (per>=0 && per<=10 ){
               progress.setDonut_progress("10");
               progress.setFinishedStrokeColor(Color.parseColor("#FFF89090"));
           }else if (per>10 && per<=20){
               progress.setDonut_progress("20");
               progress.setFinishedStrokeColor(Color.parseColor("#FFFB6C6C"));

           }else if (per>20&&per<=30){

               progress.setDonut_progress("30");
               progress.setFinishedStrokeColor(Color.parseColor("#FFFA5E5E"));
           }else if (per>30 && per<=40){
               progress.setDonut_progress("40");
               progress.setFinishedStrokeColor(Color.parseColor("#FFF83B3B"));
           }else if (per>40 && per<=50){
               progress.setDonut_progress("50");
               progress.setFinishedStrokeColor(Color.parseColor("#FF2400"));
           }else if (per>50&&per<=60){
               progress.setDonut_progress("60");
               progress.setFinishedStrokeColor(Color.parseColor("#FF2400"));

           }else if (per>60 && per<=70){
               progress.setDonut_progress("70");
               progress.setFinishedStrokeColor(Color.parseColor("#FF2400"));
           }else if (per>70 && per<=80){
               progress.setDonut_progress("80");
               progress.setFinishedStrokeColor(Color.parseColor("#FF2400"));
           }else if (per>80 && per<=90 ){
               progress.setDonut_progress("90");
               progress.setFinishedStrokeColor(Color.parseColor("#FF2400"));

           }else if (per>90 && per <= 100){
               progress.setDonut_progress("100");
               progress.setFinishedStrokeColor(Color.parseColor("#FF2400"));
           }else if (per<100){
               progress.setDonut_progress("100");
               progress.setFinishedStrokeColor(Color.parseColor("#FF2400"));
           }
       }
   }
    public static class LastTimeUsedComparator implements Comparator<UsageStats> {

        @Override
        public int compare(UsageStats a, UsageStats b) {
            return (int) (b.getLastTimeUsed() - a.getLastTimeUsed());
        }
    }

    public static class UsageTimeComparator implements Comparator<UsageStats> {

        @Override
        public int compare(UsageStats a, UsageStats b) {
            return (int) (b.getTotalTimeInForeground() - a.getTotalTimeInForeground());
        }
    }

    static class AppViewHolder {
        TextView pkgName;
        TextView lastTimeUsed;
        TextView usageTime;
        ImageView image;
        TextView usagePercentage;
        ProgressBar progressBar;
    }
    class UsageStateAdapter extends BaseAdapter {
        private static final int _DISPLAY_ORDER_USAGE_TIME = 0;
        private static final int _DISPLAY_ORDER_LAST_TIME_USED = 1;
        private static final int _DISPLAY_ORDER_APP_NAME = 2;


        private int mDisplayOrder = _DISPLAY_ORDER_USAGE_TIME;
        private LastTimeUsedComparator mLastTimeUsedComparator = new LastTimeUsedComparator();
        private UsageTimeComparator mUsageTimeComparator = new UsageTimeComparator();
        private AppNameComparator mAppLabelComparator;
        private final ArrayMap<String, String> mAppLabelMap = new ArrayMap<>();
        private  final ArrayMap<String, Drawable>mAppImage=new ArrayMap<>();
        private final ArrayMap<String, Long> mAppPer=new ArrayMap<>();
        private final ArrayList<UsageStats> mPackgeStates = new ArrayList<>();


        UsageStateAdapter() {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            long startTime = cal.getTimeInMillis();
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.MINUTE, 59);
            long endTime = cal.getTimeInMillis();
            long currentTime = startTime+24 * 60 * 60 * 1000;

            final List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,  System.currentTimeMillis());
            if (stats == null) {
                return;
            }
            ArrayMap<String, UsageStats> map = new ArrayMap<>();
            final int stateCount = stats.size();
            for (int i = 0; i < stateCount; i++) {
                final android.app.usage.UsageStats pkgStats = stats.get(i);

                try {
                    ApplicationInfo appInfo = pm.getApplicationInfo(pkgStats.getPackageName(), 0);
                    String label = appInfo.loadLabel(pm).toString();
                    long total= 0;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        total = stats.stream().map(UsageStats::getTotalTimeInForeground).mapToLong(Long::longValue).sum();
                    }else {
                        long sum = 0;
                        for (UsageStats usageStats : stats) {
                            sum += usageStats.getTotalTimeInForeground();
                        }
                        total = sum;
                    }


                    Drawable icon =appInfo.loadIcon(pm);
                    mAppImage.put(pkgStats.getPackageName(), icon);
                    mAppLabelMap.put(pkgStats.getPackageName(), label);
                    mAppPer.put(pkgStats.getPackageName(),total);

                    UsageStats existingStats = map.get(pkgStats.getPackageName());

                    if (existingStats == null) {
                        map.put(pkgStats.getPackageName(), pkgStats);
                    } else {
                        existingStats.add(pkgStats);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
            mPackgeStates.addAll(map.values());

            mAppLabelComparator = new AppNameComparator(mAppLabelMap);
            sortList();
        }

        @Override
        public int getCount() {
            return mPackgeStates.size();
        }

        @Override
        public Object getItem(int i) {
            return mPackgeStates.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

            AppViewHolder holder;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.usagae_app_item, null);
                holder = new AppViewHolder();
                holder.pkgName = (TextView) convertView.findViewById(R.id.package_name);
                holder.usageTime = (TextView) convertView.findViewById(R.id.usage_time);
                holder.lastTimeUsed = (TextView) convertView.findViewById(R.id.last_time_used);
                holder.image=(ImageView) convertView.findViewById(R.id.image);
                holder.usagePercentage=(TextView) convertView.findViewById(R.id.usage_perc_tv);
                holder.progressBar=(ProgressBar) convertView.findViewById(R.id.progressBar);

                convertView.setTag(holder);

            } else {
                holder = (AppViewHolder) convertView.getTag();
            }

            UsageStats pkgStats = mPackgeStates.get(i);

            if (pkgStats != null) {
                String label = mAppLabelMap.get(pkgStats.getPackageName());



                holder.pkgName.setText(label);
                holder.lastTimeUsed.setText(DateUtils.formatSameDayTime(pkgStats.getLastTimeUsed(), System.currentTimeMillis(), DateFormat.MEDIUM, DateFormat.MEDIUM));
                String duration=getBreakDown(pkgStats.getTotalTimeInForeground());
                Long total=mAppPer.get(pkgStats.getPackageName());
                int per= (int) (pkgStats.getTotalTimeInForeground()*100/total);
                holder.usageTime.setText(duration);
                Drawable icon=mAppImage.get(pkgStats.getPackageName());
                holder.image.setImageDrawable(icon);
                holder.usagePercentage.setText(per +"%");
                holder.progressBar.setProgress(per);


                if (per>=0&&per<10){
                    // holder.progressBar.setProgressTintList(ColorStateList.valueOf(R.color.bgColor));
                }else if (per>=10&&per<20){
                    holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.BLUE));

                }else if (per>=20&&per<30){
                    holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.CYAN));
                }else if (per>=30&&per<40){
                    holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.MAGENTA));
                }else if (per>=40&&per<50){
                    holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.YELLOW));
                }else  if (per == 100){
                    holder.progressBar.setProgressTintList(ColorStateList.valueOf(Color.RED));
                }
            }
            return convertView;
        }

        private String getBreakDown(long millis)

        {
            if (millis<0){
                throw new IllegalArgumentException("Duration must be greater than zero!");

            }
            long hours = TimeUnit.MILLISECONDS.toHours(millis);
            millis -= TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
            millis -= TimeUnit.MINUTES.toMillis(minutes);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

            return (hours + " h " +  minutes + " m " );

        }

        void sortList(int sortOrder) {
            if (mDisplayOrder == sortOrder) {
                return;
            }
            mDisplayOrder = sortOrder;
            sortList();
        }

        private void sortList() {
            if (mDisplayOrder == _DISPLAY_ORDER_USAGE_TIME) {
                if (localLOGV) Log.i(TAG, "Sorting by  usage time");
                Collections.sort(mPackgeStates, mUsageTimeComparator);
            } else if (mDisplayOrder == _DISPLAY_ORDER_LAST_TIME_USED) {
                if (localLOGV) Log.i(TAG, "Sorting by last time used ");
                Collections.sort(mPackgeStates, mLastTimeUsedComparator);
            } else if (mDisplayOrder == _DISPLAY_ORDER_APP_NAME) {
                if (localLOGV) Log.i(TAG, "Sorting by application name");
                Collections.sort(mPackgeStates, mAppLabelComparator);
            }
            notifyDataSetChanged();

        }

    }
}