package in.apnacare.android.medicationalertsystem.activity;

import android.support.v7.app.ActionBar;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.widget.ExpandableListView;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import in.apnacare.android.medicationalertsystem.R;
import in.apnacare.android.medicationalertsystem.adapter.ExpandableListAdapter;

public class FaqActivity extends BaseActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.apnacareBlue));
        toolbar.setTitle("FAQ");
        setSupportActionBar(toolbar);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);


    }


    /*
     * Preparing the list data
     */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("What is Smart Pill Reminder ?");
        listDataHeader.add("How does it work ?");
        listDataHeader.add("Forgetting medications on time ?");
        listDataHeader.add("Ran out of medicines ?");

        // Adding child data
        List<String> top250 = new ArrayList<String>();


        String text1 = "<html><body>"
                      + "<p align=\"justify\">"
                     + "Smart pill reminder is a multipurpose app for people under any age group,who tend to forget taking their medications on proper scheduled times."
                       + "</p> "
                         + "</body></html>";

        top250.add(text1);


        String text2 = "<html><body>"
                + "<p align=\"justify\">"
                + "The usage is quite simple"
                + "</p><p align=\"left\"> - Schedule all your required medications at your prescribed timings and set the alarms.</p>"
                + "<p align=\"left\"> - Voila !! You get VOICE assisted alarms to remind you of the medications.</p>"
                + "<p align=\"left\"> - You may enter the details of your pharmacist,doctor,and kins to avail services like refiling Medications,Sending Prescriptions,Alert provision to Kins/caregivers.</p>"
                + "<p align=\"left\">Note : <i>Entering the above details are not mandatory, but we encourage you to put them, to use the app to its fullest potential.</i></p>"
                + "</body></html>";

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add(text2);



        String text3 ="<html><body"
                +"<p align=\"justify\" style= \"margin-left:5px\">"
                +"This is where the app does the magic, we will make sure you keep taking your medications on time.Every alarm can be snoozed twice,and on the third attempt your Kins/Caregivers will be getting an email and a message regarding the medication you skipped.</p>"
                +"<p align=\"justify\" style= \"margin-left:5px\">So that even they too could remind you back.It is very helpful for people who stay away from their homes and parents.</p>"
                +"</body></html>";
        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add(text3);

        String text4 = "<html><body"
                +"<p align=\"left\">"
                +"We understand especially the ageing problems of elderly people,who cannot go out every time to get their medicines,so we have come up with an innovative way to do it.</p>"
                +"<p align=\"left\">"
                +"Just save your pharmacy details, next go to Menu->Pharmacy List->Choose \"SEND PRESCRIPTION\" on your desired pharmacy.</p>"
                +"<p align=\"left\">"
                +"Then you can just take a snapshot of the Prescription and Email to them.Once they get the email they will be getting the medicines to your doorstep.</p>"
                +"</body></html>";
        List<String> faq4 = new ArrayList<String>();

        faq4.add(text4);


        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
        listDataChild.put(listDataHeader.get(3), faq4);
    }
}
