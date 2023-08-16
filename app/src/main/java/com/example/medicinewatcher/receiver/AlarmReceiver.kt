package com.example.medicinewatcher.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("alarm","Alarm triggered")
        Toast.makeText(context,"GET YOUR MEDICINE!", Toast.LENGTH_SHORT).show()
    }
}