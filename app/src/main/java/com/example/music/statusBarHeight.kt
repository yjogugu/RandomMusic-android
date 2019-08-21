package com.example.music

import android.content.Context

fun statusBarHeight(context: Context): Int {
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")

    return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId)
    else 0
}