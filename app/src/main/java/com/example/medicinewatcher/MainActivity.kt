package com.example.medicinewatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.example.medicinewatcher.model.Medicine
import com.example.medicinewatcher.ui.theme.MedicineWatcherTheme
import java.time.temporal.TemporalAmount

class MainActivity : ComponentActivity() {
    var medicines : ArrayList<Medicine> = ArrayList()
    var currentName:String = ""
    var currentAmount:Float = 0.5f
    var currentTime:String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MedicineWatcherTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    LazyColumn{
                        items(medicines){ medicine ->
                            MedicineCart(medicine = medicine)
                        }
                    }
                    Button(onClick = {}) {

                    }

                }
            }
        }
    }

    @Composable
    fun addNewMedicine(){
        Popup() {

        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicineCart(medicine: Medicine){
    Card(onClick = { /*TODO*/ }) {
        Column {
            Row(modifier = Modifier.padding(5.dp)){
                Text("Name: $medicine.name", modifier = Modifier.padding(5.dp))
                Text("Amount: $medicine.amount",modifier = Modifier.padding(5.dp))
                Text("Time: $medicine.time",modifier = Modifier.padding(5.dp))
            }
            Button(onClick = { /*TODO*/ },modifier = Modifier.align(CenterHorizontally)){
             Text(text = "Took it")   
            }
        }
        
    }
}