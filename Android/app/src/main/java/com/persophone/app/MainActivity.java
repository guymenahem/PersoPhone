package com.persophone.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


import com.persophone.collector.CollectorService;
import com.persophone.collector.Logger;
import com.persophone.collector.ProcessStarter;
import com.persophone.persophone_bottom.R;
import com.persophone.shell.ProcessManager;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;


public class MainActivity extends AppCompatActivity implements
        OnTabSelectListener,
        UsageFragment.OnFragmentInteractionListener,
        RecommendationFragment.OnFragmentInteractionListener,
        PreferencesFragment.OnFragmentInteractionListener,
        RateFragment.OnFragmentInteractionListener,
        ShareFragment.OnFragmentInteractionListener{

    public static Context con;
    public int user_id;


    public MainActivity(){
        if (con == null) {
            con = this.getBaseContext();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set bottom bar
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(this);

        // setting first fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, UsageFragment.newInstance("",""), "Wellcome")
                    .commit();
        }

        // if is the first alarm to be set
        if (ProcessStarter.alarmSet == false) {
            Logger.writeToDebugLog("Alarm set for first time activity running");
            Intent i = new Intent(this, CollectorService.class);
            AlarmManager alm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            con = this;
            PendingIntent pi = PendingIntent.getService(con, 0, i, 0);
            alm.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), pi);
            alm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), ProcessStarter.PROC_INTERVAL, pi);
        }

        // Setting UserID
        this.user_id = new UserPref(getApplicationContext()).getUserID();
        Logger.writeToErrorLog("USER ID : " + this.user_id);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onTabSelected(@IdRes int tabId) {
        switch (tabId){

            // choose Usage
            case R.id.bot_my_usage:
                Log.e("CHANGE","usage");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, UsageFragment.newInstance("",""), "Usage")
                        .commit();
                break;

            // choose Recommendation
            case R.id.bot_rec_for_me:
                Log.e("CHANGE","rec");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, RecommendationFragment.newInstance(), "Recommendation")
                        .commit();
                break;

            // choose Prefences
            case R.id.bot_my_perf:
                Log.e("CHANGE","perf");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, PreferencesFragment.newInstance("",""), "Preferences Fragment")
                        .commit();
                break;

            // choose rate
            case R.id.bot_rate:
                Log.e("CHANGE","rate");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, RateFragment.newInstance("",""), "Rate Fragment")
                        .commit();
                break;

            // choose share
            case R.id.bot_share:
                Log.e("CHANGE","share");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, ShareFragment.newInstance("",""), "Share Fragment")
                        .commit();
                break;
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}
