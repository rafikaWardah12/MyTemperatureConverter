package com.example.mytemperatureconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTemperatureConverterTheme()
        }
    }
}

@Preview
@Composable
fun MyTemperatureConverterTheme() {
    //A surface container using background color from theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            StatefulTemperatureInput()
            ConverterApp()
            TwoWayConverterApp()
        }
    }
}


@Composable
fun StatefulTemperatureInput(
    modifier: Modifier = Modifier
) {
    var input by remember {
        mutableStateOf("")
    }
    var output by remember {
        mutableStateOf("")
    }
    Column(modifier.padding(16.dp)) {
        Text(
            text = stringResource(R.string.stateful_converter),
            style = MaterialTheme.typography.headlineSmall
        )
        OutlinedTextField(
            value = input,
            onValueChange = { newInput ->
                input = newInput
                output = convertToFahrenheit3(newInput)
            },
            label = { Text(stringResource(id = R.string.enter_celsius)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

            )
        Text(text = stringResource(id = R.string.temperature_fahrenheit, output))

    }
}

//sebagai object
//Single Expression memakai "="
//Apabila memakai lambda jdi function dn hrus dikembalikan nilai dlu brupa object
private fun convertToFahrenheit(celcius: String) =
//secara default value dari text field bertipe string
    //perlu konvert jdi nilai. Jika tidak bertipe angka akan mengembalikan null
    celcius.toDoubleOrNull()?.let {
        (it * 9 / 5) + 32
    }.toString()

//Contoh menggunakan higher order
private val convertToFahrenheit2: (String) -> String = { celcius ->
    celcius.toDoubleOrNull()?.let {
        (it * 9 / 5) + 32
    }?.toString() ?: "Invalid Input"
}

//Harus Extend : String supaya tdk terdeteksi bernilai unit
//Branches harus memerlukan return
private fun convertToFahrenheit3(celcius: String): String {
    return celcius.toDoubleOrNull()?.let {
        (it * 9 / 5) + 32
    }?.toString() ?: "Invalid Input"
}

private fun convertToCelcius(celcius: String) =
    celcius.toDoubleOrNull()?.let {
        (it -32)* 5/9
    }.toString()

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun StatefulTemperatureInputPreview() {
    StatefulTemperatureInput()
}

@Composable
fun StatelessTemperatureInput(
    input: String,
    output: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.stateful_converter),
            style = MaterialTheme.typography.headlineSmall
        )
        OutlinedTextField(
            value = input,
            label = { Text(stringResource(R.string.enter_celsius)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = onValueChange,
        )
        Text(stringResource(R.string.temperature_fahrenheit, output))
    }
}

@Composable
private fun ConverterApp(
    modifier: Modifier = Modifier
) {
    var input by remember { mutableStateOf("") }
    var output by remember { mutableStateOf("") }
    Column(modifier) {
        StatelessTemperatureInput(
            input = input,
            output = output,
            onValueChange = { newInput ->
                input = newInput
                output = convertToFahrenheit2(newInput)
            }
        )
    }
}

@Composable
fun GeneralTemperatureInput(
    scale: Scale,
    input: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        OutlinedTextField(
            value = input,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = stringResource(
                        id = R.string.enter_temperature, scale.scaleName
                    ),
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
fun TwoWayConverterApp(
    modifier: Modifier = Modifier
) {
    var celcius by remember {
        mutableStateOf("")
    }
    var fahrenheit by remember {
        mutableStateOf("")
    }
    Column(modifier.padding(16.dp)) {
        Text(
            text = stringResource(id = R.string.two_way_converter),
            style = MaterialTheme.typography.headlineSmall
        )
        GeneralTemperatureInput(
            scale = Scale.CELCIUS,
            input = celcius,
            onValueChange = { newInput ->
                celcius = newInput
                fahrenheit = convertToFahrenheit(newInput)
            })
        GeneralTemperatureInput(
            scale = Scale.FAHRENHEIT,
            input = fahrenheit,
            onValueChange = { newInput ->
                fahrenheit = newInput
                celcius = convertToCelcius(newInput)
            })
    }
}
