package com.phr.util

object Time {

    private val timeStarted: Float = System.nanoTime().toFloat();

    fun getTimeInSeconds(): Float {
        return (((System.nanoTime() - timeStarted) * 1E-9).toFloat());
    }
}