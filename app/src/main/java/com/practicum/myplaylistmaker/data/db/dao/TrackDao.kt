package com.practicum.myplaylistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.myplaylistmaker.data.db.entity.TrackEntity
import com.practicum.myplaylistmaker.data.search.requestAndResponse.TrackDto
import com.practicum.myplaylistmaker.domain.models.Track

@Dao
interface TrackDao {
    @Insert (entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun insertTrack (track:TrackDto)

    @Delete(entity = TrackEntity::class)
    fun deleteTrack (track:TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY addTime DESC")
    fun queryTrack():ArrayList<TrackEntity>

    @Query("SELECT * FROM track_table WHERE trackId=:searchId")
    fun queryTrackId(searchId:Long):TrackEntity?


}