import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.tutorial.shoppinglistapp.data.Quantity
import me.tutorial.shoppinglistapp.ui.theme.ShoppingListAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CenteredDropdownDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit
) {
    if (showDialog) {
        var expanded by remember { mutableStateOf(false) }
        val options = listOf("Kg", "Litre", "Pieces")
        var selectedOption by remember { mutableStateOf(options[0]) }

        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(onClick = { onDismiss() }) {
                    Text("OK")
                }
            },
            title = { Text("Select Quantity Type") },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Choose a unit:")

                    // ✅ The best way to align the dropdown properly inside a dialog
                    OutlinedButton(onClick = {expanded = true}) {
                        //Text(text = itemQuantityType.toString())
                        Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "More")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {expanded = false},
                    ) {
                        androidx.compose.material.DropdownMenuItem(onClick = {
                            expanded = false
                        }) {
                            Text(text = "Kilos")
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun MainScreen() {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { showDialog = true }) {
            Text("Open Dialog")
        }

        CenteredDropdownDialog(
            showDialog = showDialog,
            onDismiss = { showDialog = false }
        )
    }
}


@Preview
@Composable
fun TestPreview(){
    ShoppingListAppTheme {
        Scaffold { innerPadding ->
            MainScreen()
        }
    }
}