package edu.wsu.harvesttrace.services

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import java.io.File
import java.io.FileNotFoundException
import java.io.OutputStream

private const val IMAGE_TYPE = "image/png"
private val STORAGE_DIR = "DCIM" + File.separator + "HarvestTrace"

enum class StorageService {
    INSTANCE;

    fun handlePermissions(requestPermissions: (Array<String>) -> Unit) {

        // Permission request logic
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            requestPermissions(
                arrayOf(
                    READ_MEDIA_IMAGES,
                    READ_MEDIA_VIDEO,
                    READ_MEDIA_VISUAL_USER_SELECTED
                )
            )
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
        } else {
            requestPermissions(arrayOf(READ_EXTERNAL_STORAGE))
        }
    }

    fun saveImageToStorage(context: Context, image: Bitmap): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, IMAGE_TYPE)
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.RELATIVE_PATH, STORAGE_DIR)
        values.put(MediaStore.Images.Media.IS_PENDING, true)

        val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            saveImageToStream(image, context.contentResolver.openOutputStream(uri))
            values.put(MediaStore.Images.Media.IS_PENDING, false)
            context.contentResolver.update(uri, values, null, null)
        }
        return uri
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun readImageFromStorage(path: String): Bitmap? {
        val file = File(path)
        if (!file.exists()) {
            throw FileNotFoundException("The image file does not exist")
        }
        return BitmapFactory.decodeFile(path)

    }

    fun readImageFromStorage(context: Context, uri: Uri): Bitmap? {

        val inputStream = context.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)

    }

    fun deleteImageInStorage(path: String): Boolean {
        val file = File(path)
        return file.delete()
    }

    fun deleteImageInStorage(context: Context, uri: Uri?): Boolean {
        return if (uri!=null) {
            try {
                val contentResolver: ContentResolver = context.contentResolver
                val deletedRows = contentResolver.delete(uri, null, null)
                deletedRows > 0
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        } else {
            false
        }
    }

    fun getFilePathFromUri(context: Context, uri: Uri?): String? {
        var filePath: String? = null
        if (uri?.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIndex = it.getColumnIndex(OpenableColumns.SIZE)
                it.moveToFirst()
                val fileName = it.getString(nameIndex)
                val fileSize = it.getLong(sizeIndex)
                val tempFile = File(context.cacheDir, fileName)
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    tempFile.outputStream().use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                filePath = tempFile.absolutePath
            }
        } else if (uri?.scheme == "file") {
            filePath = uri.path
        }
        return filePath
    }

}