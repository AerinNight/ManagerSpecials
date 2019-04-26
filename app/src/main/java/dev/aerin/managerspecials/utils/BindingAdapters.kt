package dev.aerin.managerspecials.utils

import android.graphics.Paint
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso


/**
 * Lazy-loads an image by Uri into an ImageView via DataBinding
 */
@BindingAdapter("imageUri")
fun ImageView.setImageUri(uri: Uri?) {
    if (uri != null) {
        Picasso.get().load(uri).into(this)
    } else {
        setImageResource(0) // null out the image
    }
}

/**
 * Adds XML access to strikethrough support to existing TextViews
 */
@set:BindingAdapter("strikethrough")
var TextView.strikethrough
    get() = (paintFlags and Paint.STRIKE_THRU_TEXT_FLAG) > 0
    set(value) {
        paintFlags = if (value) {
            paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

@BindingAdapter("customWidth")
fun View.setCustomWidth(width: Int) {
    layoutParams.width = width
    requestLayout()
}

@BindingAdapter("customHeight")
fun View.steCustomHeight(height: Int) {
    layoutParams.height = height
    requestLayout()
}
