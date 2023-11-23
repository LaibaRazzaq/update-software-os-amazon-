package com.example.appUpdate.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appUpdate.R;


public class SimInfo extends Fragment {
    TextView simOp, simIso, isRoaming, networkType, dataConnection, networkISO, operatorID, operatorName;
     LinearLayout linearLayout;
     TextView text1,text2;
        Button allow;
        RelativeLayout photoEditor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sim_info, container, false);
        simOp = view.findViewById(R.id.simOp);
        text1=view.findViewById(R.id.text1);
        text2=view.findViewById(R.id.text2);
        allow=view.findViewById(R.id.allow);
        simIso = view.findViewById(R.id.simIso);
        linearLayout=view.findViewById(R.id.linear);
        isRoaming = view.findViewById(R.id.isRoaming);
        networkType = view.findViewById(R.id.networkType);
        dataConnection = view.findViewById(R.id.dataConnection);
        networkISO = view.findViewById(R.id.networkCountryIoS);
        operatorID = view.findViewById(R.id.operatorId);
        operatorName = view.findViewById(R.id.operatorName);
        photoEditor=view.findViewById(R.id.photoEditor);
        photoEditor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.ng.collagemaker.photoeditor.photocollage")));


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            // Permission is granted
            linearLayout.setVisibility(View.VISIBLE);
            text1.setVisibility(View.GONE);
            allow.setVisibility(View.GONE);
            text2.setVisibility(View.GONE);
            getInfo();



            // Get phone state information
        } else {

           requestPermissions( new String[] { Manifest.permission.READ_PHONE_STATE }, 202);
        }


        allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
                intent.setData(uri);

// Launch the app settings screen
                startActivityForResult(intent,203);


            }
        });






// Get the phone number





        return view;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == 202 &&grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            linearLayout.setVisibility(View.VISIBLE);
            text1.setVisibility(View.GONE);
            allow.setVisibility(View.GONE);
            text2.setVisibility(View.GONE);
            getInfo();

            // Permission is granted, you can perform your action here
        } else if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_DENIED){

                text1.setVisibility(View.VISIBLE);
                text2.setVisibility(View.VISIBLE);
                allow.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);


                // Permission is not granted, you can show an explanation to the user or disable the feature that depends on the permission.
            }



            // Permission is not granted, you can show an explanation to the user or disable the feature that depends on the permission.

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==203 && ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED){
                linearLayout.setVisibility(View.VISIBLE);
                text1.setVisibility(View.GONE);
                allow.setVisibility(View.GONE);
                text2.setVisibility(View.GONE);
                getInfo();

        }else {
            text1.setVisibility(View.VISIBLE);
            text2.setVisibility(View.VISIBLE);
            allow.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
        }
    }

    private  void getInfo(){
        TelephonyManager telephonyManager = (TelephonyManager) requireActivity().getSystemService(Context.TELEPHONY_SERVICE);
        boolean isRoamingB=telephonyManager.isNetworkRoaming();
        if (isRoamingB){
            isRoaming.setText("Yes");
        }else {
            isRoaming.setText("No");
        }
        String simOperatorName = telephonyManager.getSimOperatorName();
        simOp.setText(simOperatorName);
        int dataConnectionIn = telephonyManager.getDataState();
        String dataConnectionName = null;
        switch (dataConnectionIn) {
            case TelephonyManager.DATA_CONNECTED:
                dataConnectionName = "Connected";
                break;
            case TelephonyManager.DATA_CONNECTING:
                dataConnectionName = "Connecting";
                break;
            case TelephonyManager.DATA_DISCONNECTED:
                dataConnectionName = "Disconnected";
                break;
            case TelephonyManager.DATA_SUSPENDED:
                dataConnectionName = "Suspended";
                break;
            default:
                break;
        }
        dataConnection.setText(dataConnectionName);


         @SuppressLint("MissingPermission") int networkTypeIn=telephonyManager.getNetworkType();
        String networkTypeName = null;
        switch (networkTypeIn) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                networkTypeName = "GPRS";
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                networkTypeName = "EDGE";
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                networkTypeName = "UMTS";
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                networkTypeName = "HSDPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                networkTypeName = "HSUPA";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                networkTypeName = "HSPA";
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                networkTypeName = "CDMA";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                networkTypeName = "EVDO_0";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                networkTypeName = "EVDO_A";
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                networkTypeName = "EVDO_B";
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                networkTypeName = "1xRTT";
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                networkTypeName = "LTE";
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                networkTypeName = "EHRPD";
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                networkTypeName = "HSPAP";
                break;
            case TelephonyManager.NETWORK_TYPE_NR:
                networkTypeName = "5G NR";
                break;
            default:
                break;
        }
        networkType.setText(networkTypeName);



        String countryIso = telephonyManager.getNetworkCountryIso();
        if (countryIso == null || countryIso.isEmpty()) {
            countryIso = "Unknown";
        }
        networkISO.setText(countryIso);
        String networkOperator=telephonyManager.getNetworkOperatorName();
        if (networkOperator==null|| networkOperator.isEmpty()){
            operatorName.setText("None");
        }
        operatorName.setText(networkOperator);
        String operatorId=telephonyManager.getNetworkOperator();
        if (operatorId==null||operatorId.isEmpty()){
            operatorID.setText("Unknown");
        }

        operatorID.setText(operatorId);
        String simCountryIso = telephonyManager.getSimCountryIso();
        simIso.setText(simCountryIso);

    }
}