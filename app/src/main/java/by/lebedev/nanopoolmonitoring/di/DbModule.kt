package by.lebedev.nanopoolmonitoring.di

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.coroutineScope
import androidx.room.Room
import by.lebedev.nanopoolmonitoring.data.db.NanopoolDatabase
import by.lebedev.nanopoolmonitoring.data.db.RoomNanopoolDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Qualifier
import javax.inject.Singleton

@Module(
    includes =
    [DatabaseModuleBinds::class]
)
@InstallIn(SingletonComponent::class)
object DbModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): RoomNanopoolDatabase {
        val builder =
            Room.databaseBuilder(
                appContext,
                RoomNanopoolDatabase::class.java,
                "NanopoolDatabase.db"
            )
        return builder.build()
    }

    @Provides
    fun provideWalletDao(db: NanopoolDatabase) = db.WalletDao()

    @Provides
    fun provideWorkersDao(db: NanopoolDatabase) = db.WorkersDao()

    @Provides
    fun provideLongLifetimeScope(): CoroutineScope {
        return ProcessLifecycleOwner.get().lifecycle.coroutineScope
    }

}

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModuleBinds {

    @Binds
    abstract fun bindNanopoolDatabase(context: RoomNanopoolDatabase): NanopoolDatabase
//
//    @Singleton
//    @Binds
//    abstract fun provideDatabaseCleaner(cleaner: RoomNanopoolDatabase): DatabaseCleaner
//
//    @Binds
//    abstract fun provideDatabaseTransactionRunner(runner: RoomNanopoolDatabase): DatabaseTransactionRunner

}


