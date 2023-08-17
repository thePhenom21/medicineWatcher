package com.cosulabs.medicinewatcher.views

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.util.Log
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.navigation.NavHostController
import com.cosulabs.medicinewatcher.converters.Converter
import com.cosulabs.medicinewatcher.model.Medicine
import com.cosulabs.medicinewatcher.receiver.AlarmReceiver
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class HomePage(var context: Context,var alarmMgr: AlarmManager){

    var db = Firebase.firestore

    var medicines = SnapshotStateList<Medicine>()

    var added = true

    var user : String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun createDB(){
        db.collection("medicines").get().addOnSuccessListener {
            result -> for(doc in result){
                val map = doc.data
                val medi = Medicine(null,map.get("name") as String,map.get("amount") as String,
                    Converter().fromTimestamp(map.get("time") as String)!!,user!!
                )
                if(medi.userId == user) {
                    medicines.add(medi)
                }
            }
        }
    }
    


    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun mainPage(userName : String, navContoller: NavHostController) {

        var currentName by remember { mutableStateOf("") }
        var currentAmount by remember { mutableStateOf("") }
        var currentTime by remember { mutableStateOf(LocalTime.now()) }
        var showDialog by remember { mutableStateOf(false) }
        var showTime by remember { mutableStateOf(true) }


        user = userName

        Column (horizontalAlignment = Alignment.CenterHorizontally){
            LazyColumn(
                modifier = Modifier.size(
                    width = LocalConfiguration.current.screenWidthDp.dp,
                    height = LocalConfiguration.current.screenHeightDp.dp - 100.dp,
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
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
                .padding(10.dp), onClick = { showDialog = true }) {
                Text("Add Medicine")
            }
        }

    }



    @SuppressLint("UnrememberedMutableState")
    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MedicineCart(medicine: Medicine, medicines: SnapshotStateList<Medicine>) {
        val med = medicine

        var alarmRealSet : MutableState<Boolean> = remember {
            mutableStateOf(false)
        }


        var alarmIntent = Intent(context, AlarmReceiver::class.java)
        var pendingIntent = PendingIntent.getBroadcast(context,0, alarmIntent!!, FLAG_MUTABLE)

        val calendar: Calendar =  Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.MINUTE, medicine.time.minute)
            set(Calendar.HOUR_OF_DAY, medicine.time.hour)
        }



        Card(onClick = { med.alarmSet.value = if (med.alarmSet.value == 1) 0 else 1 }, modifier = Modifier.padding(10.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier
                        .defaultMinSize(
                            minWidth = LocalConfiguration.current.screenWidthDp.dp - 10.dp,
                            minHeight = 50.dp
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

                    if(!alarmRealSet.value) {
                        alarmMgr.setExact(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            pendingIntent
                        )
                        alarmRealSet.value = true
                        println("alarm Setted up bro")
                    }

                    //
                }
                else {
                    Icon(
                        painter = rememberVectorPainter(Icons.Outlined.Info),
                        contentDescription = "info"
                    )
                    alarmMgr?.cancel(pendingIntent)
                    alarmRealSet.value = false
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
                    insertItem(Medicine(null, currentName, currentAmount, t!!,user!!))
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
        med.id!!.delete()
    }

    fun insertItem(med: Medicine) {
        medicines.add(med)
        val medicine = hashMapOf(
            "name" to med.name,
            "amount" to med.amount,
            "time" to Converter().dateToTimestamp(med.time),
            "user" to med.userId
        )


        runBlocking {
            launch {
                val autoID = db.collection("medicines").add(
                    medicine)
                delay(1000L)
                med.id = autoID.result
            }

        }
    }

}