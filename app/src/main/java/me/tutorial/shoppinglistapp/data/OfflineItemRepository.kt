package me.tutorial.shoppinglistapp.data

import kotlinx.coroutines.flow.Flow

class OfflineItemRepository(val itemDAO: ItemDAO): ItemRepository {
    override suspend fun insertItem(item: Item) = itemDAO.insert(item)

    override suspend fun deleteItem(item: Item) = itemDAO.delete(item)

    override suspend fun updateItem(item: Item) = itemDAO.update(item)

    override fun getAllItems(): Flow<List<Item>> = itemDAO.getAllItems()

    override fun getItem(sNo: Int): Flow<Item> = itemDAO.getItem(sNo)
}