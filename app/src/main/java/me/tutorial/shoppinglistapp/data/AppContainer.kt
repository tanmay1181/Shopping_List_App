package me.tutorial.shoppinglistapp.data

import android.content.Context

interface AppContainer {
    val offlineItemRepository: OfflineItemRepository
}

class AppDataContainer(val context: Context): AppContainer{
    override val offlineItemRepository by lazy {
        OfflineItemRepository(ItemDatabase.getInstance(context).itemDAO())
    }
}