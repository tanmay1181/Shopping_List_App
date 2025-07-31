package me.tutorial.shoppinglistapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "shoppingItems")
data class Item(@PrimaryKey(autoGenerate = true)
                val sNo: Int = 0,
                val name: String,
                val quantityType: Quantity,
                val quantity: Double)