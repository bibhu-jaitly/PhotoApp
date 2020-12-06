package com.example.newphotoapp.data.model.resposne.photolistresponse

data class Photo(
  val id: Long,
  val owner: String,
  val secret: String,
  val server: Long,
  val farm: Long,
  val title: String,
  val ispublic: Int,
  val isfriend: Int,
  val isfamily: Int
)