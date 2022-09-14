package by.lebedev.nanopoolmonitoring.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import by.lebedev.nanopoolmonitoring.data.db.dao.WalletDao
import by.lebedev.nanopoolmonitoring.data.db.dao.WorkerAutoUpdateDao
import by.lebedev.nanopoolmonitoring.data.db.entity.WalletEntity
import by.lebedev.nanopoolmonitoring.data.db.entity.WorkerAutoUpdateEntity


@Database(
    entities = [WalletEntity::class, WorkerAutoUpdateEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RoomNanopoolDatabase : RoomDatabase(), NanopoolDatabase

interface NanopoolDatabase {
    fun WalletDao(): WalletDao
    fun WorkersDao(): WorkerAutoUpdateDao
}