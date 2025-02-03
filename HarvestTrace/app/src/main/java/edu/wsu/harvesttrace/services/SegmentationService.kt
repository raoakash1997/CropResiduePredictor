package edu.wsu.harvesttrace.services

import android.graphics.Bitmap

interface SegmentationService {

    fun runInference(bitmap: Bitmap): Bitmap

    fun getPercentageValue(): Double
}