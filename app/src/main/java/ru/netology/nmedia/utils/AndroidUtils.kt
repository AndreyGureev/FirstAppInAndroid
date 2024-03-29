package ru.netology.nmedia.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Nullable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.floor
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AndroidUtils {
    companion object {
        val FILE_STORE = "nmedia_posts.json"
        fun counter(count: Int): String {
            return when (count) {
                in 0..999 -> count.toString()
                in 1000..1099 -> String.format("%d", count / 1000) + "K"
                in 1100..9999 -> String.format("%.1f", (floor(count / 100.toDouble())) / 10) + "K"
                in 10_000..999_999 -> String.format("%d", count / 1000) + "K"
                in 1_000_000..999_999_999 ->
                    String.format("%.1f", (floor(count / 100_000.toDouble())) / 10) + "M"
                else -> "> 1 Bn"
            }
        }

        @SuppressLint("NewApi")
        fun addLocalDataTime(): String {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy в HH:mm")
            return current.format(formatter).toString()
        }

        fun hideKeyboard(view: View) {
            val imm =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun urlValidChecker(link: String): Boolean {
            val videoLink = link.trim()
            return Patterns.WEB_URL.matcher(videoLink).matches()
        }

        fun startIntent(context: Context, @Nullable intent: Intent?): Boolean {
            if (intent == null) {
                return false
            }
            val pm = context.packageManager
            val resolveInfo = pm.resolveActivity(intent, 0)
            if (resolveInfo?.activityInfo != null && !TextUtils.isEmpty(
                    resolveInfo.activityInfo.name
                )
                && !TextUtils.isEmpty(resolveInfo.activityInfo.packageName)
            ) {
                context.startActivity(intent)
                return true
            }
            return false
        }
    }

    object StringArg : ReadWriteProperty<Bundle, String?> {

        override fun setValue(thisRef: Bundle, property: KProperty<*>, value: String?) {
            thisRef.putString(property.name, value)
        }

        override fun getValue(thisRef: Bundle, property: KProperty<*>): String? =
            thisRef.getString(property.name)
    }
}