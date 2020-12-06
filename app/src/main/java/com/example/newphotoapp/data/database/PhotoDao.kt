package com.example.newphotoapp.data.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newphotoapp.data.model.PicDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface PhotoDao {

  @Query("SELECT * FROM photo_table ORDER BY photoID ASC")
  fun getPhotoListAscendingManner(): Flow<MutableList<PicDetail>>

  @Insert(onConflict = OnConflictStrategy.IGNORE)
  suspend fun insertPhoto(picDetail: PicDetail)

  @Query("DELETE FROM photo_table")
  suspend fun deleteAll()
}