package com.example.newphotoapp.data.network

import com.example.newphotoapp.data.model.photodetailresponse.PhotoDetailResponse
import com.example.newphotoapp.data.model.resposne.photolistresponse.PhotoListResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.flickr.com/"

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
  .add(KotlinJsonAdapterFactory())
  .build()

private fun getHttpClient(): OkHttpClient {
  val logging = HttpLoggingInterceptor()
 // logging.level = Level.BODY;
  val builder = OkHttpClient().newBuilder()
    .addInterceptor(logging)
  return builder.build()
}

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
  .addConverterFactory(MoshiConverterFactory.create(moshi))
  .client(getHttpClient())
  .baseUrl(BASE_URL)
  .build()

interface PhotoApiService {

  @GET("services/rest/")
  suspend fun getPhotoList(
    @Query("method") method: String,
    @Query("api_key") apiKey: String,
    @Query("user_id") userID: String,
    @Query("format") format: String,
    @Query("nojsoncallback") jsonCallback: Int
  ): Response<PhotoListResponse>

  @GET("services/rest/")
  suspend fun getPhotoDetail(
    @Query("method") method: String,
    @Query("photo_id") photoID: Long,
    @Query("api_key") apiKey: String,
    @Query("user_id") userID: String,
    @Query("format") format: String,
    @Query("nojsoncallback") jsonCallback: Int
  ): Response<PhotoDetailResponse>
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object PhotoAPI {
  val retrofitService: PhotoApiService by lazy { retrofit.create(PhotoApiService::class.java) }
}

