package by.lebedev.nanopoolmonitoring.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "workers_auto-update_entity")
data class WorkerAutoUpdateEntity(
    @PrimaryKey(autoGenerate = true) override var id: Long = 0,
    @ColumnInfo(name = "worker_name") val workerName: String,
    @ColumnInfo(name = "worker_is_alive") val isAlive: Boolean
) : NanopoolEntity
