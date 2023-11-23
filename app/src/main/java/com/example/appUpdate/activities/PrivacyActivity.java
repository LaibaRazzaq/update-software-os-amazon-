package com.example.appUpdate.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.appUpdate.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class PrivacyActivity extends AppCompatActivity {
  Toolbar toolbar;
    TextView privacyText;
    String htmlText;
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                onBackPressed();

                return true;

            default:

                return super.onOptionsItemSelected(item);

        }

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        toolbar = findViewById(R.id.toolbarPrivacy);
        setSupportActionBar(toolbar);
//        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
//
//        actionBar.setHomeButtonEnabled(true);
//
//        actionBar.setDisplayHomeAsUpEnabled(true);
        //toolbar = findViewById(R.id.toolbarPrivacy);
        privacyText = findViewById(R.id.privacyText);
        htmlText = "        <h3> Privacy Policy </h3>\n" +
                " <p>NZ developer built this app as an Ad Supported app. This SERVICE is provided by nz developer at no cost and\n" +
                "            is intended for use as is.This page is used to inform visitors regarding my policies with the collection, use,\n" +
                "            and disclosure of Personal Information if anyone decided to use my Service.\n" +
                "            If you choose to use my Service, then you agree to the collection and use of information in relation to\n" +
                "            this policy. The Personal Information that I collect is used for providing and improving the Service.\n" +
                "            I will not use or share your information with anyone except as described in this Privacy Policy.\n" +
                "            The terms used in this Privacy Policy have the same meanings as in our Terms and Conditions, which is\n" +
                "            accessible at nz developer unless otherwise defined in this Privacy Policy.</p>\n" +
                "        <h3>Information Collection and Use</h3>\n" +
                "        <p>For a better experience, while using our Service, I may require you to provide us with certain personally\n" +
                "            identifiable information. The information that I request will be retained on your device and is not collected\n" +
                "            by me in any way.\n" +
                "            The app does use third party services that may collect information used to identify you.\n" +
                "            Link to privacy policy of third party service providers used by the app\n" +
                "            <br/>\u2022 Google Play Services\n" +
                "            <br/>\u2022 AdMob\n" +
                "            <br/>\u2022 Firebase Analytics\n" +
                "            <br/>\u2022 Firebase Crashlytics\n" +
                "            <br/>\u2022 Facebook\n" +
                "        </p>\n" +
                "        <h3> Log Data</h3>\n" +
                "        <p>I want to inform you that whenever you use my Service, in a case of an error in the app\n" +
                "            I collect data and information (through third party products) on your phone called Log Data.\n" +
                "            This Log Data may include information such as your device Internet Protocol (IP) address,\n" +
                "            device name, operating system version, the configuration of the app when utilizing my Service,\n" +
                "            the time and date of your use of the Service, and other statistics.</p>\n" +
                "        <h3> Cookies </h3>\n" +
                "        <p> Cookies are files with a small amount of data that are commonly used as anonymous unique identifiers.\n" +
                "            These are sent to your browser from the websites that you visit and are stored on your device's internal\n" +
                "            memory.\n" +
                "            This Service does not use these cookies explicitly. However, the app may use third party code and\n" +
                "            libraries that use cookies to collect information and improve their services.\n" +
                "            You have the option to either accept or refuse these cookies and know when a cookie is being sent to your device.\n" +
                "            If you choose to refuse our cookies, you may not be able to use some portions of this Service.</p>\n" +
                "        <h3> Service Providers </h3>\n" +
                "        <p>I may employ third-party companies and individuals due to the following reasons:\n" +
                "            <br/>\u2022 To facilitate our Service;\n" +
                "            <br/>\u2022 To provide the Service on our behalf;\n" +
                "            <br/>\u2022 To perform Service-related services;\n" +
                "            <br/>\u2022 To assist us in analyzing how our Service is used.\n" +
                "            I want to inform users of this Service that these third parties have access to your Personal Information.\n" +
                "            The reason is to perform the tasks assigned to them on our behalf. However, they are\n" +
                "            obligated not to disclose or use the information for any other purpose.</p>\n" +
                "\n" +
                "        <h3>Security</h3>\n" +
                "        <p>I value your trust in providing us your Personal Information, thus we are striving to use commercially\n" +
                "            acceptable means of protecting it. But remember that no method of transmission over the internet,\n" +
                "            or method of electronic storage is 100% secure and reliable, and I cannot guarantee its absolute security.</p>\n" +
                "        <h3>Links to Other Sites</h3>\n" +
                "        <p>This Service may contain links to other sites. If you click on a third-party link, you will be directed to that site.\n" +
                "            Note that these external sites are not operated by me. Therefore, I strongly advise you to review the Privacy Policy\n" +
                "            of these websites. I have no control over and assume no responsibility for the content, privacy policies,\n" +
                "            or practices of any third-party sites or services.</p>\n" +
                "        <h3>Children’s Privacy</h3>\n" +
                "        <p>These Services do not address anyone under the age of 13. I do not knowingly collect personally identifiable information\n" +
                "            from children under 13. In the case I discover that a child under 13 has provided me with personal information,\n" +
                "            I immediately delete this from our servers. If you are a parent or guardian and you are aware that your child\n" +
                "            has provided us with personal information, please contact me so that I will be able to do necessary actions.</p>\n" +
                "        <h3>Changes to This Privacy Policy</h3>\n" +
                "        <p>I may update our Privacy Policy from time to time. Thus, you are advised to review this page periodically for any changes.\n" +
                "            I will notify you of any changes by posting the new Privacy Policy on this page. These changes are effective immediately\n" +
                "            after they are posted on this page.</p>\n" +
                "        <h3>Contact Us</h3>\n" +
                "        <p>If you have any questions or suggestions about my Privacy Policy,\n" +
                "            do not hesitate to contact me at nextsalution@gmail.com.</p>";
        privacyText.setText(Html.fromHtml(htmlText));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            privacyText.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
        } else {
            // Set justification mode using reflection
            try {
                Method setJustificationMode = TextView.class.getMethod("setJustificationMode", int.class);
                setJustificationMode.invoke(privacyText, 1); // LineBreaker.JUSTIFICATION_MODE_INTER_WORD = 1
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(PrivacyActivity.this, AboutUsActivity.class);
//                finish();
//                startActivity(intent);
//            }
//        });
   }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
