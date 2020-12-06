package com.example.newphotoapp.data.model.resposne.photodetailresponse

import com.example.newphotoapp.data.model.photodetailresponse.Size

data class Sizes (

	val canblog : Int,
	val canprint : Int,
	val candownload : Int,
	val size : List<Size>
)