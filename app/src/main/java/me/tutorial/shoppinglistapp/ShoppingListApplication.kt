package me.tutorial.shoppinglistapp

import android.app.Application
import me.tutorial.shoppinglistapp.data.AppContainer
import me.tutorial.shoppinglistapp.data.AppDataContainer

class ShoppingListApplication: Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppDataContainer(this)
    }
}