package com.flysolo.e_appoint.utils

import android.content.Context
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun generateRandomString(length: Int = 15): String {
    val characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    var result = ""

    for (i in 0 until length) {
        val randomIndex = (Math.random() * characters.length).toInt()
        result += characters[randomIndex]
    }

    return result
}


fun generateRandomNumberString(length: Int = 15): String {
    val characters = "0123456789"
    var result = ""

    for (i in 0 until length) {
        val randomIndex = (Math.random() * characters.length).toInt()
        result += characters[randomIndex]
    }

    return result
}


fun Double.toPhp(): String {
    return "₱ %.2f".format(this)
}



fun Date.display(): String {
    val formatter = SimpleDateFormat("MMM dd, hh:mm aa", Locale.getDefault())
    return formatter.format(this)
}

fun Context.shortToast(message : String) {
    Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}
