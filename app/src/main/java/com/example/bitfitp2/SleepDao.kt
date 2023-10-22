package com.example.bitfitp1

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDao {
    @Query("SELECT * FROM sleep_table")
    fun getAll(): Flow<List<SleepEntity>>

    @Insert
    fun insertAll(sleepData: List<SleepEntity>)

    //@Insert
    //fun insert(sleepData: List<SleepEntity>)

    @Query("DELETE FROM sleep_table")
    fun deleteAll()

    @Query("SELECT AVG(feeling) FROM sleep_table")
    fun getFeelingAvg(): Flow<Double>

    @Query("SELECT AVG(hours) FROM sleep_table")
    fun getHoursAvg(): Flow<Double>
}
