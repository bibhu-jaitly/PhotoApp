package com.example.newphotoapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.newphotoapp.Constants.Companion.APIKEY
import com.example.newphotoapp.Constants.Companion.FORMAT
import com.example.newphotoapp.Constants.Companion.METHOD_PHOTO_DETAIL
import com.example.newphotoapp.Constants.Companion.METHOD_PHOTO_LIST
import com.example.newphotoapp.Constants.Companion.NO_JSON_CALLBACK
import com.example.newphotoapp.Constants.Companion.USER_ID
import com.example.newphotoapp.data.database.PhotoDBRepository
import com.example.newphotoapp.data.network.PhotoAPI
import com.example.newphotoapp.data.model.PicDetail
import com.example.newphotoapp.data.model.photodetailresponse.PhotoDetailResponse
import com.example.newphotoapp.data.model.resposne.photolistresponse.PhotoListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val photoDBRepository: PhotoDBRepository) : ViewModel() {

  //region Instance Variables

  private var photoListResponse: PhotoListResponse? = null
  private var picListLiveData: MutableLiveData<MutableList<PicDetail>> = MutableLiveData()
  private var tempPicList: MutableList<PicDetail> = ArrayList()
  private var dbLiveData: LiveData<MutableList<PicDetail>> =
    photoDBRepository.allPhotos.asLiveData()

  //endregion

  //region Helper Methods
  /**
   * Returns [dbLiveData] to View for observation and updation of UI
   */
  fun getDBLiveData(): LiveData<MutableList<PicDetail>> {
    return dbLiveData
  }

  /**
   *Returns [picListLiveData] for observation and updation of UI
   */
  fun getPicListAPILiveData(): MutableLiveData<MutableList<PicDetail>> {
    return picListLiveData
  }

  /**
   * Initial API to fetch the photo meta data and calls [getPhotoDetailsFromServer]
   * to fetch details of all the photos from its ID present in [PhotoListResponse]
   */
  fun getPublicPhotoMetaList() {
    viewModelScope.launch(Dispatchers.IO) {
      val response = PhotoAPI.retrofitService.getPhotoList(
        method = METHOD_PHOTO_LIST,
        apiKey = APIKEY,
        userID = USER_ID,
        format = FORMAT,
        jsonCallback = NO_JSON_CALLBACK
      )
      if (response.isSuccessful) {
        photoListResponse = response.body()
        getPhotoDetailsFromServer(photoListResponse!!)
      } else {
        Log.d("error***", response.errorBody().toString() + "")
      }
    }
  }

  /**
   * Loops through the initial response from the photo list meta data received from the initial API
   * and calls [getSinglePhotoDetailsFromServer] to fetch details of all the photos.
   */
  private suspend fun getPhotoDetailsFromServer(photoListResponse: PhotoListResponse) {
    var list = photoListResponse.photos.photo
    for (photo in list) {
      getSinglePhotoDetailsFromServer(photo.id)
    }
  }

  /**
   * Fetches single photo details from the server using Coroutines
   * and calls [insertPhotoDetail] method to update the DB.
   * using [withContext] method to update the [picListLiveData] on Main thread.
   * Response received is [PhotoDetailResponse]
   */
  private suspend fun getSinglePhotoDetailsFromServer(id: Long) {
    viewModelScope.launch(Dispatchers.IO) {
      val responseAsync = async {
        PhotoAPI.retrofitService.getPhotoDetail(
          method = METHOD_PHOTO_DETAIL,
          photoID = id,
          apiKey = APIKEY,
          userID = USER_ID,
          format = FORMAT,
          jsonCallback = NO_JSON_CALLBACK
        )
      }
      val response = responseAsync.await()
      if (response.isSuccessful && response.body() != null) {
        val photoDetailResponse = response.body() as PhotoDetailResponse
        val photo = withContext(Dispatchers.Default) {
          fetchRequiredPicDetailsFromResponse(
            id, photoDetailResponse
          )
        }
        // Log.d(TAG, "getSinglePhotoDetailsFromServer: ${photo.photoID}")
        tempPicList.add(photo)
        insertPhotoDetail(photo)
        withContext(Dispatchers.Main) {
          picListLiveData.value = tempPicList
        }
      } else {
        Log.d("errorSingle***", "error***$id" + response.isSuccessful)
      }
    }
  }

  /**
   * Fetches details of photo to display from the data received from the server
   */
  private fun fetchRequiredPicDetailsFromResponse(
    id: Long,
    photoDetailResponse: PhotoDetailResponse
  ): PicDetail {
    val picDetail = PicDetail()
    picDetail.photoID = id
    for (size in photoDetailResponse.sizes.size) {
      if (size.label.contentEquals("Thumbnail")) {
        picDetail.photoUrl = size.source
        break
      }
    }
    return picDetail
  }

  /**
   * Updates the database with the latest photo id and photo url
   */
  private fun insertPhotoDetail(picDetail: PicDetail) = viewModelScope.launch {
    photoDBRepository.insert(picDetail)
  }

  //endregion
}

class MainViewModelFactory(private val repository: PhotoDBRepository) : ViewModelProvider.Factory {
  override fun <T : ViewModel> create(modelClass: Class<T>): T {
    if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
      @Suppress("UNCHECKED_CAST")
      return MainViewModel(repository) as T
    }
    throw IllegalArgumentException("Unknown ViewModel class")
  }
}