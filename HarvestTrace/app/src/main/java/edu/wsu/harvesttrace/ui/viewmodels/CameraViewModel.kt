package edu.wsu.harvesttrace.ui.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class CameraViewModel @Inject constructor(private val savedStateHandle: SavedStateHandle) : ViewModel() {


    private val _capturedImage = MutableStateFlow<Bitmap?>(null)
    val capturedImage: StateFlow<Bitmap?> get() = _capturedImage

    private val _capturedImageUri = MutableStateFlow<Uri?>(null)
    val capturedImageUri: StateFlow<Uri?> get() = _capturedImageUri

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)


    fun setCapturedImage(image: Bitmap?) {
        _capturedImage.value = image
    }

    fun setCapturedImageURI(uri: Uri?) {
        _capturedImageUri.value = uri
    }
}
