package me.tutorial.shoppinglistapp.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import me.tutorial.shoppinglistapp.ShoppingListApplication
import me.tutorial.shoppinglistapp.data.Item
import me.tutorial.shoppinglistapp.data.ItemRepository

class HomeViewModel(private val itemRepository: ItemRepository): ViewModel() {
    val homeUiState: StateFlow<HomeUiState> = itemRepository.getAllItems().map { HomeUiState(false, it) }
        .onStart { emit(HomeUiState(isLoading = true)) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeUiState(isLoading = true)
        )

    suspend fun deleteItem(item: Item){
        itemRepository.deleteItem(item)
    }

    suspend fun addItem(item: Item){
        itemRepository.insertItem(item)
    }

    suspend fun updateItem(item: Item){
        itemRepository.updateItem(item)
    }

    companion object ViewModelProvider{
        val Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as ShoppingListApplication)
                val appContainer = application.appContainer
                HomeViewModel(appContainer.offlineItemRepository)
            }
        }
    }
}

data class HomeUiState(
    val isLoading: Boolean = true,
    val items: List<Item> = listOf())
