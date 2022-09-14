

package by.lebedev.nanopoolmonitoring.utils

import androidx.room.withTransaction
import by.lebedev.nanopoolmonitoring.data.db.RoomNanopoolDatabase
import javax.inject.Inject


//interface DatabaseTransactionRunner {
//    suspend operator fun <T> invoke(block: suspend () -> T): T
//}
//
//class RoomTransactionRunner @Inject constructor(private val db: RoomNanopoolDatabase) :
//    DatabaseTransactionRunner {
//    override suspend operator fun <T> invoke(block: suspend () -> T): T {
//        return db.withTransaction {
//            block()
//        }
//    }
//}
