package ru.netology.nmedia.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

object AndroidUtils {
    fun hideKeyboard(view: View) {
        val imn = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imn.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Context.showToast(text: Int, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(
            this,
            getString(text),
            length
        ).show()
    }
}