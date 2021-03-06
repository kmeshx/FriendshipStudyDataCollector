package edu.cmu.chimps.friendship_study;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Random;
import java.util.Set;

import static edu.cmu.chimps.friendship_study.R.string.shared_preference_key_participant_id;


/**
 * Created by fanglinchen on 3/17/17.
 */

public class Utils {
    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getParticipantID(Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        return sharedPref.getString(context.getResources().getString(shared_preference_key_participant_id),null);
    }

    public static boolean hasStoredPreferences(Context context){
        return getParticipantID(context)!=null&&
                randomlySelectFriendInitial(true,context)!=null
                && randomlySelectFriendInitial(false,context) !=null;
    }

    public static boolean isTrackingEnabled(Context context){
        return isMyServiceRunning(context, TrackingService.class);
    }

    public static void startTracking(Context context){
        Intent serviceIntent = new Intent(context,TrackingService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        context.startService(serviceIntent);
    }

    public static String randomlySelectFriendInitial(boolean reu, Context context){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> set;
        if(reu)
            set = sharedPref.getStringSet(context.getResources().getString(R.string.reu_friends_key),null);
        else
            set = sharedPref.getStringSet(context.getResources().getString(R.string.nreu_friends_key),null);

        if(set==null){
            return null;
        }

        int size = set.size();
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for(String randomlySelected : set)
        {
            if (i == item)
                return randomlySelected;
            i++;
        }
        return null;
    }

}
