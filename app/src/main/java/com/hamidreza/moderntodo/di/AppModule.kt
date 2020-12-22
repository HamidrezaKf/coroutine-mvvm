package com.hamidreza.moderntodo.di

import android.app.Application
import androidx.room.Room
import com.hamidreza.moderntodo.data.db.TaskDataBase
import com.hamidreza.moderntodo.utils.Conts.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskDb(
        app: Application
    ) = Room.databaseBuilder(app, TaskDataBase::class.java, DB_NAME)
        .fallbackToDestructiveMigration().build()

    @Provides
    fun provideTaskDao(db:TaskDataBase) = db.taskDao()
}