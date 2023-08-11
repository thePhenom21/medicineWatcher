package com.example.medicinewatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import com.example.medicinewatcher.model.Medicine
import com.example.medicinewatcher.ui.theme.MedicineWatcherTheme
import java.time.temporal.TemporalAmount

class MainActivity : ComponentActivity() {
    var medicines : ArrayList<Medicine> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MedicineWatcherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    mainPage()
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview(showBackground = true)
    @Composable
    fun mainPage(){
        var currentName by remember { mutableStateOf("") }
        var currentAmount by remember { mutableStateOf("") }
        var currentTime by remember { mutableStateOf("") }
        var showDialog by remember { mutableStateOf(false) }

        Column {
            LazyColumn(modifier = Modifier.size(width = LocalConfiguration.current.screenWidthDp.dp-10.dp,height =  LocalConfiguration.current.screenHeightDp.dp-50.dp),
                contentPadding = PaddingValues(5.dp)
            ){
                items(medicines){ medicine ->
                    MedicineCart(medicine = medicine)
                }

            }
            if(showDialog){
                Dialog(onDismissRequest = {
                        showDialog = false
                        medicines.add(Medicine(currentName,currentAmount,currentTime))
                        currentAmount = ""
                        currentTime = ""
                        currentName = ""
                    }) {
                    Column {
                        TextField(value = currentName, onValueChange = {
                                                                       currentName = it
                        }, label = { Text(text = "Name of medicine")})
                        TextField(value = currentAmount, onValueChange = {
                                                                         currentAmount = it
                        }, label = { Text(text = "Amount of medicine")})
                        TextField(value = currentTime, onValueChange = {
                                                                       currentTime = it
                        }, label = {Text("Time to take")})
                    }
                } }

            Button(modifier = Modifier
                .size(width = LocalConfiguration.current.screenWidthDp.dp - 10.dp, 200.dp)
                .padding(10.dp), onClick = {showDialog = true}){}
        }

    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineCart(medicine: Medicine){
    Card(onClick = { /*TODO*/ }) {
        Column {
            Row(modifier = Modifier.padding(5.dp).size(width = LocalConfiguration.current.screenWidthDp.dp-10.dp, height = 100.dp)){
                Text("Name: ${medicine.name}", modifier = Modifier.padding(5.dp))
                Text("Amount: ${medicine.amount}",modifier = Modifier.padding(5.dp))
                Text("Time: ${medicine.time}",modifier = Modifier.padding(5.dp))
            }
            Button(onClick = { /*TODO*/ },modifier = Modifier.align(CenterHorizontally)){
             Text(text = "Took it")   
            }
        }
        
    }
}