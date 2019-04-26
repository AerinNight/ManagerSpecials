package dev.aerin.managerspecials.models

import android.net.Uri
import java.text.NumberFormat
import java.util.*

data class Special(val width: Int, val height: Int, val display_name: String,
                   val price: Double, val original_price: Double, val imageUrl: Uri) {

    fun getFormattedPrice(): String {
        return NumberFormat.getCurrencyInstance(Locale.US).format(price)
    } // Hard-coded to Locale.US for now because our endpoint only appears to support USD.

    fun getFormattedOldPrice(): String {
        return NumberFormat.getCurrencyInstance(Locale.US).format(original_price)
    } // Hard-coded to Locale.US for now because our endpoint only appears to support USD.
}
