package com.example.ruletauwu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
//BroadcastReceiver
//notificación
public class BaterryLow extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action != null){
            if(action.equals(Intent.ACTION_BATTERY_LOW)){
                //s’enviarà una notificació al nostre telèfon amb el text: «Bateria baixa, el telèfon es tancarà aviat, guarda les dades de la teva partida»
                Toast.makeText(context, "Bateria baixa, el telèfon es tancarà aviat,guarda les dades de la teva partida", Toast.LENGTH_LONG).show();
            }
        }
     }
}
