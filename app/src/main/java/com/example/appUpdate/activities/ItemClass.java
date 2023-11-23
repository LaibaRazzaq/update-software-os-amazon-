package com.example.appUpdate.activities;

import android.graphics.drawable.Drawable;
import android.widget.CheckBox;

public class ItemClass {

    public String appname = "";
    public  String packageName = "";
    public String versionName = "";
    int versionCode = 0;
    public Drawable icon;
    public String lastDate;
    public CheckBox checkBox;


    public ItemClass(String appname, String packageName, String versionName, int versionCode, Drawable icon, String lastDate, CheckBox checkBox) {
        this.appname = appname;
        this.packageName = packageName;
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.icon = icon;
        this.lastDate = lastDate;
        this.checkBox = checkBox;
    }

    public ItemClass(){}
}
