package edu.wsu.harvesttrace.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.wsu.harvesttrace.services.ONNXSegmentationService
import edu.wsu.harvesttrace.services.SegmentationService
import edu.wsu.harvesttrace.services.UserPreferenceService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserPreferenceService(@ApplicationContext context: Context): UserPreferenceService {
        return UserPreferenceService(context)
    }


    @Provides
    @Singleton
    fun provideSegmentationService(@ApplicationContext context: Context): SegmentationService {
        return ONNXSegmentationService(context, "segformer.onnx")
    }


}