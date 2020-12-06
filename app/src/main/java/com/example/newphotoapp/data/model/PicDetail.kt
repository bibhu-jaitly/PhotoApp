package com.example.newphotoapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_table")
class PicDetail {

 @PrimaryKey
  var photoID: Long = 0
  var photoUrl: String = ""

  override fun equals(other: Any?): Boolean {
    if (javaClass != other?.javaClass) {
      return false
    }
    other as PicDetail
    if (photoID != other.photoID) {
      return false
    }
    if (photoUrl != other.photoUrl) {
      return false
    }
    return true
  }
}