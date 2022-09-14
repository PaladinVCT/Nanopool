package by.lebedev.nanopoolmonitoring.di

import android.content.Context
import by.lebedev.nanopoolmonitoring.ui.BaseApplication
import by.lebedev.nanopoolmonitoring.ui.MainActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplication(@ApplicationContext appContext: Context): BaseApplication {
        return appContext as BaseApplication
    }
    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }
}