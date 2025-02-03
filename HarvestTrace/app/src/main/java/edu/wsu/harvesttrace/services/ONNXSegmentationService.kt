package edu.wsu.harvesttrace.services

import ai.onnxruntime.*
import ai.onnxruntime.OrtSession
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import java.nio.FloatBuffer
import java.util.Collections

private const val IMAGE_WIDTH = 512
private const val IMAGE_HEIGHT = 512
private const val BATCH_SIZE = 1L
private const val CHANNELS = 3L

class ONNXSegmentationService(context: Context, modelName: String) : SegmentationService {
    private val ortEnv = OrtEnvironment.getEnvironment()
    private val session: OrtSession
    var percentage = 0.0

    init {
        val modelBytes = context.assets.open(modelName).readBytes()
        session = ortEnv.createSession(modelBytes)
    }

    override fun getPercentageValue():Double{
        return percentage
    }

    override fun runInference(bitmap: Bitmap): Bitmap {
        val input = preprocessBitmap(bitmap)
        val inputName = session.inputNames.iterator().next()
        val output = session.run(Collections.singletonMap(inputName, input))
        return postProcessOutput(output)
    }

    fun preprocessBitmap(bitmap: Bitmap): OnnxTensor {
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, IMAGE_WIDTH, IMAGE_HEIGHT, true)

        val floatBuffer = FloatBuffer.allocate(1 * 3 * IMAGE_HEIGHT * IMAGE_WIDTH)
        floatBuffer.rewind()

        val pixels = IntArray(IMAGE_HEIGHT * IMAGE_WIDTH)
        resizedBitmap.getPixels(pixels, 0, IMAGE_WIDTH, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT)

        for (pixel in pixels) {
            val r = (Color.red(pixel) / 255.0f)
            val g = (Color.green(pixel) / 255.0f)
            val b = (Color.blue(pixel) / 255.0f)

            floatBuffer.put(r)
            floatBuffer.put(g)
            floatBuffer.put(b)
        }

        floatBuffer.rewind()
        // (batch_size, channels, height, width)
        val shape = longArrayOf(BATCH_SIZE, CHANNELS, IMAGE_HEIGHT.toLong(), IMAGE_WIDTH.toLong())
        return OnnxTensor.createTensor(OrtEnvironment.getEnvironment(), floatBuffer, shape)
    }

    fun getColorForLabel(label: Int): Int {
        return when (label) {
            0 -> Color.BLACK
            1 -> Color.RED
            2 -> Color.rgb(123, 63, 0)
            3 -> Color.GREEN
            else -> Color.WHITE
        } as Int
    }

    fun postProcessOutput(output: OrtSession.Result): Bitmap {
        val tensorData = (output.get(0) as OnnxTensor).floatBuffer.array() // Extract float array
        val shape = intArrayOf(1, 4, 128, 128) // Assuming known shape (1,4,128,128)
        val numLabels = shape[1] // 4 classes
        val height = shape[2] // 128
        val width = shape[3] // 128

        // Convert tensorData (flat array) into (numLabels, H, W) format
        val logits = Array(numLabels) { FloatArray(height * width) }
        for (c in 0 until numLabels) {
            for (i in 0 until height * width) {
                logits[c][i] = tensorData[c * height * width + i]
            }
        }

        // Compute argmax per pixel
        val predictedMask = IntArray(height * width)
        for (i in 0 until height * width) {
            var maxIndex = 0
            var maxVal = logits[0][i]
            for (c in 1 until numLabels) {
                if (logits[c][i] > maxVal) {
                    maxVal = logits[c][i]
                    maxIndex = c
                }
            }
            predictedMask[i] = maxIndex
        }

        // Create 128x128 grayscale Bitmap
        val maskBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        var count = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                val label = predictedMask[y * width + x]
                if (label == 3) {
                    count++
                }
                maskBitmap.setPixel(x, y, getColorForLabel(label))
            }
        }
        percentage = (count / 16384.0) * 100.0
        return Bitmap.createScaledBitmap(maskBitmap, 512, 512, false)
    }

}