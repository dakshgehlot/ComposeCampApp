package com.example.customtipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.customtipcalculator.ui.theme.CustomTipCalculatorTheme
import java.text.NumberFormat

/*

Hex Codes:
    Light Green: 0xFFCEF5E6
    Medium Green: 0xFF0D8A53
    Dark Green: 0xFF08502F

 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomTipCalculatorTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainPage()
                }
            }
        }
    }
}

@Composable
fun MainPage() {

    // This is the main page composable in which we will pass the three sections: Heading, Input Section and Result Section

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Heading()
        val listOfBills = billTipSplit()
        FinalAmount(final_bill = listOfBills[0], bill = listOfBills[1], tip = listOfBills[2])
    }
}

// The composable function which defines the layout of the heading.
@Composable
fun Heading() {

    // A box composable is used to stack different elements.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFCEF5E6),
                RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
            ),
    ) {
        Text(
            text = "Tip Calculator",
            style = MaterialTheme.typography.h1,
            modifier = Modifier
                .padding(20.dp)
                .align(Alignment.Center),
            color = Color(0xFF08502F)
        )
    }

}


@Composable
fun LabelText(label: String) {
    Text(
        fontSize = 18.sp,
        text = label,
        color = Color.Gray,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
fun billTipSplit(): List<Double> {
    var billInput by remember { mutableStateOf("") }
    var tipPercent by remember { mutableStateOf(0.0) }
    var splitValue by remember { mutableStateOf(0) }
    val bill = billInput.toDoubleOrNull() ?: 0.0

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(start = 60.dp, end = 60.dp)
    ) {

        // TEXT INPUT FUNCTION CALLED
        LabelText(label = "Enter total bill")
        BillInputField(value = billInput, onValueChange = { billInput = it })

        // Add Spacer to space out the elements:
        Spacer(modifier = Modifier.height(48.dp))

        // CHOOSE TIP BUTTONS CALLED
        LabelText(label = "Choose tip")
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            fun onTipChange(tip: Double): Double {
                if (tipPercent != tip) {
                    tipPercent = tip
                } else {
                    tipPercent = 0.0
                }
                return tipPercent
            }

            TipButton(label = "10%", tipPercent, value = 10.0) { onTipChange(10.0) }
            TipButton(label = "15%", tipPercent, value = 15.0) { onTipChange(15.0) }
            TipButton(label = "20%", tipPercent, value = 20.0) { onTipChange(20.0) }

        }

        // Add Spacer to space out the elements:
        Spacer(modifier = Modifier.height(48.dp))
        Spacer(modifier = Modifier.height(16.dp))
        // SPLIT BUTTONS CALLED
        LabelText(label = "Split")
        SplitBetweenPersons(
            splitValue = splitValue,
            onSplitIncrease = { splitValue++ },
            onSplitDecrease = { splitValue-- }
        )

    }

    return calculateTip(bill, tipPercent, splitValue)


}

@Composable
fun BillInputField(
    value: String,
    onValueChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF08502F),
                cursorColor = Color(0xFF08502F),
            ),
            textStyle = TextStyle(
                fontSize = 36.sp,
                color = Color(0xFF0D8A53)
            ),
        )
    }
}

@Composable
fun TipButton(label: String, tipPercent: Double, value: Double, onTipChange: () -> Unit) {

    Button(
        colors = ButtonDefaults.buttonColors(
            backgroundColor = if (tipPercent == value) Color(0xFF0D8A53) else Color.White
        ),
        onClick = { onTipChange() },
        shape = RoundedCornerShape(50),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 6.dp,
        ),
        modifier = Modifier
            .width(75.dp)
            .height(50.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (tipPercent == value) Color.White else Color(0xFF0D8A53)
        )
    }
}

@Composable
fun SplitBetweenPersons(
    splitValue: Int,
    onSplitIncrease: () -> Unit,
    onSplitDecrease: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                if (splitValue != 0) {
                    onSplitDecrease()
                }
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_remove_24),
                contentDescription = null,
                tint = Color(0xFF0D8A53)
            )
        }
        Text(
            text = splitValue.toString(),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0D8A53),
            fontSize = 36.sp
        )
        IconButton(
            onClick = { onSplitIncrease() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                contentDescription = null,
                tint = Color(0xFF0D8A53)
            )
        }
    }
}


@Composable
fun FinalAmount(final_bill: Double = 0.0, bill: Double = 0.0, tip: Double = 0.0) {

    val finalBillInCurrency = NumberFormat.getCurrencyInstance().format(final_bill)
    val billInCurrency = NumberFormat.getCurrencyInstance().format(bill)
    val tipInCurrency = NumberFormat.getCurrencyInstance().format(tip)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 30.dp, end = 30.dp)
            .fillMaxWidth()
            .background(Color(0xFFCEF5E6), RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total per person",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.Gray,
                modifier = Modifier
                    .padding(top = 24.dp)
            )
            Text(
                text = finalBillInCurrency,
                fontWeight = FontWeight.Bold,
                fontSize = 48.sp,
                color = Color(0xFF0D8A53)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LabelText(label = "Bill")
                Text(
                    text = billInCurrency,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = Color(0xFF0D8A53)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LabelText(label = "Tip")
                Text(
                    text = tipInCurrency,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp,
                    color = Color(0xFF0D8A53)
                )
            }
        }
    }
}

fun calculateTip(bill: Double = 0.0, tipPercent: Double, split: Int): List<Double> {
    val tip = tipPercent / 100 * bill
    val finalBill = bill + tip

    if (split != 0) {
        val finalBillPerPerson = finalBill / split
        val billPerPerson = bill / split
        val tipPerPerson = tip / split
        return listOf(
            finalBillPerPerson,
            billPerPerson,
            tipPerPerson
        )
    } else {
        return listOf(finalBill, bill, tip)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CustomTipCalculatorTheme {

    }
}