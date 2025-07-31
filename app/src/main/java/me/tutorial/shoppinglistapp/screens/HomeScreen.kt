package me.tutorial.shoppinglistapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import me.tutorial.shoppinglistapp.data.Item
import me.tutorial.shoppinglistapp.data.Quantity
import me.tutorial.shoppinglistapp.ui.model.HomeViewModel
import me.tutorial.shoppinglistapp.ui.model.HomeViewModel.ViewModelProvider
import me.tutorial.shoppinglistapp.ui.theme.ShoppingListAppTheme
import me.tutorial.shoppinglistapp.utils.formatClean

@Composable
fun HomeScreen(viewModel: HomeViewModel = viewModel(factory = ViewModelProvider.Factory),
               listState: LazyListState,
               isAddingItem: Boolean,
               onDismiss: () -> Unit,
               paddingValues: PaddingValues,
               modifier: Modifier = Modifier, ){

    val homeUiState = viewModel.homeUiState.collectAsState()
    val coroutine = rememberCoroutineScope()

    if(homeUiState.value.isLoading){
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    else{
        if(homeUiState.value.items.isEmpty()){
            Text(text = "Click (+)/Add button to add items",
                modifier = modifier.padding(paddingValues).alpha(0.5f),
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        else{
            LazyColumn(modifier = modifier.padding(paddingValues),
                state = listState) {
                items(homeUiState.value.items){item ->
                    ItemCard(item = item,
                        onDelete = {
                            coroutine.launch {
                                viewModel.deleteItem(item)
                            }
                        },
                        onItemEdit = {
                            coroutine.launch {
                                viewModel.updateItem(it)
                            }
                        },
                        modifier = modifier)
                }
            }
        }
    }

    if(isAddingItem) {
        AddItemDialog(
            onDismiss = {onDismiss()},
            onAddItem = {
                coroutine.launch {
                    viewModel.addItem(it)
                }
            }
        )
    }
}


@Composable
fun ItemCard(item: Item,
             onDelete: () -> Unit,
             onItemEdit: (item: Item) -> Unit,
             modifier: Modifier){
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var isEditing by rememberSaveable {mutableStateOf(false) }

    Card(modifier = Modifier
        .fillMaxWidth()
        .height(72.dp)
        .padding(4.dp),//remove later
        shape = RoundedCornerShape(topStart = 16.dp, bottomStart = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background),
        border = BorderStroke(2.dp, Color.LightGray)
    ){
        Row(modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically){
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.width(200.dp)) {
                Button(onClick = {isEditing = true},
                    colors = ButtonColors(containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.onBackground,
                        disabledContainerColor = Color.Transparent,
                        disabledContentColor = Color.Red),
                    ) {
                    Icon(Icons.Filled.Edit, "Edit")
                }

                Column(modifier = modifier.fillMaxHeight(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center) {
                    var itemName = item.name.toCharArray()
                    if(item.name.length > 15){
                        for(i in 13..15){
                            itemName[i] = '.'
                        }
                    }
                    Text(text = String(itemName),
                        overflow = TextOverflow.Visible,
                        fontSize = 16.sp,
                        maxLines = 1)
                    Text(text = "Q: " + item.quantity.formatClean() +" " + item.quantityType.title)
                }
            }
            Button(onClick = {
                showDeleteDialog = true
            },
                colors = ButtonColors(containerColor = Color.Transparent,
                    contentColor = Color.Red,
                    disabledContainerColor = Color.Transparent,
                    disabledContentColor = Color.Red),
            ) {
                Icon(Icons.Filled.Delete, "Delete")
            }
        }

        if(isEditing){
            EditItemDialog(onDismiss = {isEditing = false},
                item = item,
                onItemEdit = onItemEdit)
        }

        if(showDeleteDialog){
            DeleteItemDialog(onDismiss = {showDeleteDialog = false},
                onDelete = {onDelete()},
                title = "Delete " + item.name)
        }

    }
}

@Composable
fun EditItemDialog(onDismiss:() -> Unit, item: Item, onItemEdit: (item: Item) -> Unit){
    var itemName by remember { mutableStateOf(item.name) }
    var itemQuantity by remember { mutableStateOf(item.quantity.toString()) }
    AlertDialog(
        onDismissRequest = {onDismiss()},
        buttons = {
            Row(horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = {
                    onDismiss()
                    val updatedItem = item.copy(name = itemName,
                        quantity = itemQuantity.toDoubleOrNull() ?: item.quantity)
                    if(itemName.isNotEmpty() && itemQuantity.isNotEmpty()){
                        onItemEdit(updatedItem)
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Done, contentDescription = "Yes")
                }
                IconButton(onClick = {onDismiss()}) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "No")
                }
            }
        },
        title = {Text(text = "Edit Item")},
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp),) {
                Text(text = "Save Changes?", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = itemName, onValueChange = { itemName = it }, singleLine = true)
                OutlinedTextField(value = itemQuantity.toString(), onValueChange = {itemQuantity = it}, singleLine = true)
            }
        },
        shape = RoundedCornerShape(8.dp),
        backgroundColor = androidx.compose.material.MaterialTheme.colors.background
    )
}

@Composable
fun DeleteItemDialog(onDismiss: () -> Unit, onDelete:() -> Unit, title: String){
    AlertDialog(
        onDismissRequest = {onDismiss()},
        buttons = {
            Row(horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = {
                    onDelete()
                    onDismiss()
                }) {
                    Icon(imageVector = Icons.Filled.Done, contentDescription = "Yes")
                }
                IconButton(onClick = {onDismiss()}) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "No")
                }
            }
        },
        title = {Text(text = title)},
        text = {Text(text = "Are You Sure?", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())},
        shape = RoundedCornerShape(8.dp),
        backgroundColor = androidx.compose.material.MaterialTheme.colors.background
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AddItemDialog(onDismiss: () -> Unit, onAddItem: (item: Item) -> Unit){
    var itemName by rememberSaveable { mutableStateOf("") }
    var itemQuantity by rememberSaveable { mutableStateOf("") }
    var itemQuantityType by rememberSaveable { mutableStateOf(Quantity.Pieces) }
    var quantityTypeDropDownMenuEXP by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = {onDismiss()},
        buttons = {
            Row(horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = {
                    onDismiss()
                    val item = Item(name = itemName,
                        quantityType = itemQuantityType,
                        quantity = itemQuantity.toDoubleOrNull() ?: 1.0)
                    if(itemName.isNotEmpty() && itemQuantity.isNotEmpty()){
                        onAddItem(item)
                    }
                }) {
                    Icon(imageVector = Icons.Filled.Done, contentDescription = "Yes")
                }
                IconButton(onClick = {onDismiss()}) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "No")
                }
            }
        },
        title = {Text(text = "Add an Item", style = MaterialTheme.typography.bodyLarge)},
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,) {
                Text(text = "Give proper details to Save Item",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = itemName,
                    onValueChange = {itemName = it },
                    singleLine = true,
                    placeholder = {Text("Name")})
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)){
                    Box {
                        OutlinedButton(onClick = {quantityTypeDropDownMenuEXP = true}) {
                            Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = "More")
                        }
                        val scrollState = rememberScrollState()
                        DropdownMenu(
                            expanded = quantityTypeDropDownMenuEXP,
                            onDismissRequest = {quantityTypeDropDownMenuEXP = false},
                            scrollState = scrollState,
                            modifier = Modifier.height(240.dp)
                        ) {
                            val allQuantityTypes = Quantity.entries

                            allQuantityTypes.forEach {quantityType ->
                                DropdownMenuItem(
                                    onClick = {itemQuantityType = quantityType
                                    quantityTypeDropDownMenuEXP = false}
                                ) {
                                    Text(text = quantityType.title)
                                }
                            }
                        }
                    }

                    OutlinedTextField(value = itemQuantityType.title,
                        onValueChange = {},
                        singleLine = true,
                        readOnly = true)

                }
                OutlinedTextField(value = itemQuantity,
                    onValueChange = {itemQuantity = it },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    placeholder = {Text("Quantity")},
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ))
            }
        },
        backgroundColor = androidx.compose.material.MaterialTheme.colors.background
    )
}

@Preview(showBackground = true)
@Composable
fun ShoppingListAppPreview(){
    ShoppingListAppTheme {
        Scaffold {innerPadding ->
//            Column {
//                ItemCard(Item(1, "Candle", Quantity.Pieces, 2),{},{}, Modifier)
//                ItemCard(Item(1, "Dildo", Quantity.cm, 7),{},{}, Modifier)
//                ItemCard(Item(1, "Cucumber, Benidryl", Quantity.KiloGrams, 2),{},{}, Modifier)
//            }
            AddItemDialog({}, {})
        }
    }
}


