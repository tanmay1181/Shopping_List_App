package me.tutorial.shoppinglistapp.data

import android.content.Context
import kotlinx.coroutines.flow.Flow

interface ItemRepository{
    suspend fun insertItem(item: Item)
    suspend fun deleteItem(item: Item)
    suspend fun updateItem(item: Item)
    fun getAllItems(): Flow<List<Item>>
    fun getItem(sNo: Int): Flow<Item>
}