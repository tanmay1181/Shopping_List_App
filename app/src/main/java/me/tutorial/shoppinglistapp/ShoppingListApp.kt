package me.tutorial.shoppinglistapp


import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.tutorial.shoppinglistapp.screens.HomeScreen

@Composable
fun ShoppingListApp(){

    var isAddingItem by rememberSaveable { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val isAtTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex == 0 }
    }
    val fabSize by animateDpAsState(
        targetValue = if(isAtTop) 100.dp else 48.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "FAB Size"
    )
    Scaffold(
        topBar = {ShoppingListTopAppBar()},
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isAddingItem = true
            },
                shape = RoundedCornerShape(16.dp),
                elevation = FloatingActionButtonDefaults.elevation(),
                modifier = Modifier.width(fabSize).height(48.dp)){
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Icon(Icons.Filled.Add, null)
                    if(isAtTop){
                        Text(text = "Add")
                    }
                }
            }
        },
        modifier = Modifier.fillMaxSize()) {innerPadding ->
        HomeScreen(listState = listState,
            isAddingItem = isAddingItem,
            onDismiss = {isAddingItem = false},
            paddingValues =  innerPadding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListTopAppBar(){
    CenterAlignedTopAppBar(
        title = {Text(text = "Listed",
            style = androidx.compose.material3.MaterialTheme.typography.displayMedium)},
    )
}