package com.example.medicinewatcher.views

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TimePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.medicinewatcher.dao.MedicineDao
import com.example.medicinewatcher.model.Medicine
import com.example.medicinewatcher.receiver.AlarmReceiver
import com.example.medicinewatcher.repo.MedicineRepository
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class HomePage(var context: Context) {


    var alarmIntent = Intent(context, AlarmReceiver::class.java)
    var pendingIntent = PendingIntent.getBroadcast(context,0, alarmIntent!!, FLAG_MUTABLE)
    var alarmMgr = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager


    var db: MedicineRepository? = null

    var medicineDao: MedicineDao? = null

    var medicines = SnapshotStateList<Medicine>()

    var added = true





    fun createDB() {
        db = context?.let { MedicineRepository.getDatabase(it) }
        medicineDao = db!!.medicineDao()
        medicines = SnapshotStateList()

        for (item in medicineDao!!.getAll()) {
            medicines.add(item)
        }
    }




    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun mainPage() {

        var currentName by remember { mutableStateOf("") }
        var currentAmount by remember { mutableStateOf("") }
        var currentTime by remember { mutableStateOf(LocalTime.now()) }
        var showDialog by remember { mutableStateOf(false) }
        var showTime by remember { mutableStateOf(true) }


        Column {
            LazyColumn(
                modifier = Modifier.size(
                    width = LocalConfiguration.current.screenWidthDp.dp - 10.dp,
                    height = LocalConfiguration.current.screenHeightDp.dp - 50.dp
                ),
                contentPadding = PaddingValues(5.dp)
            ) {
                items(medicines) { med ->
                    MedicineCart(medicine = med, medicines)
                }
            }
            if (showDialog) {
                added = false
                Dialog(onDismissRequest = {
                    showDialog = false
                }) {
                    Column {
                        TextField(value = currentName, onValueChange = {
                            currentName = it
                        }, label = { Text(text = "Name of medicine") })
                        TextField(value = currentAmount, onValueChange = {
                            currentAmount = it
                        }, label = { Text(text = "Amount of medicine") })
                    }
                }
            }
            else{
                if(!added){
                    showTimeDialog(med = null, showTime = showTime,currentName,currentAmount)
                    added = true
                    currentAmount = ""
                    currentTime = LocalTime.now()
                    currentName = ""
                }
            }


            Button(modifier = Modifier
                .size(width = LocalConfiguration.current.screenWidthDp.dp - 10.dp, 200.dp)
                .padding(10.dp), onClick = { showDialog = true }) {}
        }

    }



    @SuppressLint("UnrememberedMutableState")
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MedicineCart(medicine: Medicine, medicines: SnapshotStateList<Medicine>) {
        val med = medicine


        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.MINUTE,medicine.time.minute)
        calendar.set(Calendar.HOUR_OF_DAY,medicine.time.hour)



        Card(onClick = { med.alarmSet.value = if (med.alarmSet.value == 1) 0 else 1 }, modifier = Modifier.padding(10.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier
                        .padding(5.dp)
                        .size(
                            width = LocalConfiguration.current.screenWidthDp.dp - 10.dp,
                            height = 100.dp
                        )
                ) {
                    Text("Name: ${medicine.name}", modifier = Modifier.padding(5.dp))
                    Text("Amount: ${medicine.amount}", modifier = Modifier.padding(5.dp))
                    Text(
                        "Time: ${medicine.createdDateFormatted}",
                        modifier = Modifier.padding(5.dp)
                    )
                }

                if(med.alarmSet.value == 1){

                    Icon(painter = rememberVectorPainter(Icons.Outlined.Notifications), contentDescription = "alarm")
                    alarmMgr?.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        pendingIntent
                    )

                    //
                }
                else {
                    Icon(
                        painter = rememberVectorPainter(Icons.Outlined.Info),
                        contentDescription = "info"
                    )
                    alarmMgr?.cancel(pendingIntent)
                }
                Button(onClick = {
                    try {
                        removeItem(med)
                    } catch (e: Exception) {
                    }
                }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text(text = "Took it")
                }
            }


        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun showTimeDialog(med: Medicine?, showTime: Boolean, currentName : String, currentAmount: String) {
        var t : LocalTime? = null
        if(showTime) {
            var picker = TimePicker(context)
            var timeListener: TimePickerDialog.OnTimeSetListener =
                TimePickerDialog.OnTimeSetListener { _, i, i2 ->
                    picker.hour = i
                    picker.minute = i2
                    var l1 = ArrayList<Int>()
                    l1.add(picker.hour)
                    l1.add(picker.minute)

                    if (l1[1] < 10 && l1[0] >= 10) {
                        t = LocalTime.parse(
                            l1[0].toString() + ":0" + l1[1].toString()
                                .format(DateTimeFormatter.ofPattern("HH:mm"))
                        )
                    } else if (l1[0] >= 10 && l1[1] >= 10) {
                        t = LocalTime.parse(
                            l1[0].toString() + ":" + l1[1].toString()
                                .format(DateTimeFormatter.ofPattern("HH:mm"))
                        )
                    } else if (l1[0] < 10 && l1[1] >= 10) {
                        t = LocalTime.parse(
                            "0" + l1[0].toString() + ":" + l1[1].toString()
                                .format(DateTimeFormatter.ofPattern("HH:mm"))
                        )
                    } else {
                        t = LocalTime.parse(
                            "0" + l1[0].toString() + ":0" + l1[1].toString()
                                .format(DateTimeFormatter.ofPattern("HH:mm"))
                        )
                    }

                    if (t == null) {
                        t = LocalTime.now()
                    }
                    insertItem(Medicine(null, currentName, currentAmount, t!!))
                }
            val a: TimePickerDialog = TimePickerDialog(
                context,
                timeListener,
                LocalTime.now().hour.dec(),
                LocalTime.now().minute.dec(),
                true
            )
            a.show()
        }


    }

    fun removeItem(med : Medicine) {
        medicines.remove(med)
        medicineDao!!.deleteMedicineById(med)
    }

    fun insertItem(med: Medicine) {
        medicines.add(med)
        medicineDao!!.insertMedicine(med)
    }

}