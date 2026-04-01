package com.nikhil.flowcus.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<FocusSession>>

    @Query("SELECT * FROM focus_sessions WHERE date >= :since")
    fun getSessionsSince(since: Long): Flow<List<FocusSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: FocusSession)

    @Query("SELECT SUM(durationSeconds) FROM focus_sessions WHERE date >= :startOfDay")
    fun getTotalFocusTimeToday(startOfDay: Long): Flow<Long?>
}
