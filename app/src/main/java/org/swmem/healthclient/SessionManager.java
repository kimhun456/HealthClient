package org.swmem.healthclient;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hyunjae on 16. 8. 2.
 */
public class SessionManager {

    private final String TAG = "SessionManager";
    private final int EXPIRED_DAY = 3;

    private final int SECONDS = 1000;
    private final int MINUTES = 60 * SECONDS;
    private final int HOURS = 60 * MINUTES;
    private final int DAYS = 24 * HOURS;

    private Context mContext;
    private boolean exist;
    private SharedPreferences sharedPreferences;
    private long deviceConnectTime;
    private String deviceID;

    SessionManager(Context context){
        this.mContext = context;
        sharedPreferences =  PreferenceManager
                .getDefaultSharedPreferences(context);
        exist = getExist();
        deviceConnectTime = getDeviceConnectTime();
        deviceID = getDeviceID();

    }

    public String formatDate(long dateInMilliseconds) {
        Date date = new Date(dateInMilliseconds);
        return new SimpleDateFormat("yyyy년 MM월 dd일\n HH시 mm분", Locale.KOREA).format(date);
    }

    public void setExist(boolean exist){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(mContext.getString(R.string.pref_session_exist_key) , exist);
        editor.commit();

    }

    public boolean getExist(){

        exist = sharedPreferences.getBoolean(mContext.getString(R.string.pref_session_exist_key),false);
        return exist;
    }

    public long getDeviceConnectTime() {
        deviceConnectTime = sharedPreferences.getLong(mContext.getString(R.string.pref_session_date_key),0);
        return deviceConnectTime;
    }

    public void setDeviceConnectTime(long deviceConnectTime) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(mContext.getString(R.string.pref_session_date_key) , deviceConnectTime);
        editor.commit();
        this.deviceConnectTime = deviceConnectTime;
    }

    public String getRemainTime(long currentTime , long deviceConnectTime){

        String result;

        long diff = currentTime - deviceConnectTime;
        result = (diff/DAYS) + "일 "
                + (diff/HOURS)  +"시간 "
                + (diff/MINUTES) + "분";

        return result;
    }


    public String getDeviceID() {
        deviceID = sharedPreferences.getString(mContext.getString(R.string.pref_session_device_key),"0");
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(mContext.getString(R.string.pref_session_device_key) , deviceID);
        editor.commit();
        this.deviceID = deviceID;
    }

    public void sync(){

        if(exist){
            long deviceConnectTime = getDeviceConnectTime();
            long currentTime = System.currentTimeMillis();
            if(currentTime - deviceConnectTime >= EXPIRED_DAY * DAYS ){
                setExist(false);
                setDeviceConnectTime(0);
                setDeviceID("");
                return;
            }

        }

    }





}
