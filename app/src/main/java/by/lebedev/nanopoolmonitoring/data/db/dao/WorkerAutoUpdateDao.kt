package by.lebedev.nanopoolmonitoring.data.db.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import by.lebedev.nanopoolmonitoring.data.db.entity.WorkerAutoUpdateEntity


@Dao
abstract class WorkerAutoUpdateDao {

    @Query("SELECT * FROM `workers_auto-update_entity`")
    abstract suspend fun getWorkers(): List<WorkerAutoUpdateEntity>

    @Query("DELETE FROM `workers_auto-update_entity`")
    abstract suspend fun clearWorkers()

    @Insert
    abstract suspend fun insertWorkers(entities: List<WorkerAutoUpdateEntity>)
}