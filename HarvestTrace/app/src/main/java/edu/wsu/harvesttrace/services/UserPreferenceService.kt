package edu.wsu.harvesttrace.services

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class UserPreferenceService @Inject constructor(private val context: Context) {


    private val IS_METRIC_KEY = booleanPreferencesKey("is_metric")
    private val FRUIT_DIAMETER_KEY = floatPreferencesKey("fruit_diameter")

    suspend fun saveIsMetric(value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_METRIC_KEY] = value
        }
    }

    val isMetric: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_METRIC_KEY] == true
        }

    suspend fun saveFruitDiameter(value: Float) {
        context.dataStore.edit { preferences ->
            preferences[FRUIT_DIAMETER_KEY] = value
        }
    }

    val fruitDiameter: Flow<Float?> = context.dataStore.data.map { preferences ->
        preferences[FRUIT_DIAMETER_KEY]
    }
}