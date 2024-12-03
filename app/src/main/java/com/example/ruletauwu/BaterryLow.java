package com.example.ruletauwu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BaterryLow extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action != null){
            if(action.equals(Intent.ACTION_BATTERY_LOW)){
                Toast.makeText(context, "sssss", Toast.LENGTH_LONG).show();
            }
        }
     }
}
