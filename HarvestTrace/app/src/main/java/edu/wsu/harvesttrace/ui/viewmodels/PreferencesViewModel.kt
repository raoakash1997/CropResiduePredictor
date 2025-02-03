package edu.wsu.harvesttrace.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.wsu.harvesttrace.services.SegmentationService
import edu.wsu.harvesttrace.services.UserPreferenceService
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferences: UserPreferenceService,
    private val _segmentationService: SegmentationService
) : ViewModel() {

    private val _isMetric = preferences.isMetric
    val isMetric get() = _isMetric

    val segmentationService get() = _segmentationService

    fun setIsMetric(value: Boolean) {
        viewModelScope.launch {
            preferences.saveIsMetric(value)
        }
    }

}