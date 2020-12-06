package com.example.newphotoapp.data.database

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newphotoapp.data.model.PicDetail
import kotlinx.coroutines.flow.Flow

class PhotoDBRepository(private val photoDao: PhotoDao) {

  // Room executes all queries on a separate thread.
  // Observed Flow will notify the observer when the data has changed.
  val allPhotos: Flow<MutableList<PicDetail>> = photoDao.getPhotoListAscendingManner()

  // By default Room runs suspend queries off the main thread, therefore, we don't need to
  // implement anything else to ensure we're not doing long running database work
  // off the main thread.
  @Suppress("RedundantSuspendModifier")
  @WorkerThread
  suspend fun insert(photo: PicDetail) {
    photoDao.insertPhoto(photo)
  }
}