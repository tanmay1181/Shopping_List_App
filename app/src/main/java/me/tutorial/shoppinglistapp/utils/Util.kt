package me.tutorial.shoppinglistapp.utils

fun Double.formatClean(): String {
    return if (this % 1.0 == 0.0) {
        this.toInt().toString()
    } else {
        this.toString()
    }
}