package com.example.devrevassignment.utils

import android.content.Context
import com.example.devrevassignment.R

object TimeUtils {
    fun formatMinutes(context: Context, min: Int): String {
        val hours = min / 60
        val minutes = min % 60

        return if (hours <= 1)
            String.format(
                context.resources.getString(R.string.strRunTimeSinglePattern),
                hours.toString(), minutes.toString()
            )
        else
            String.format(
                context.resources.getString(R.string.strRunTimePatternMultiple),
                hours.toString(), minutes.toString()
            )
    }
}