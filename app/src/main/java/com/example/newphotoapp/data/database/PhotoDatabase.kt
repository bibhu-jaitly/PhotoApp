package com.example.newphotoapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.newphotoapp.data.model.PicDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [PicDetail::class], version = 1, exportSchema = false)
public abstract class PhotoDatabase : RoomDatabase() {

  abstract fun photoDao(): PhotoDao

  companion object {
    @Volatile
    private var INSTANCE: PhotoDatabase? = null

    fun getDatabase(
      context: Context,
      scope: CoroutineScope
    ): PhotoDatabase {
      // if the INSTANCE is not null, then return it,
      // if it is, then create the database
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          PhotoDatabase::class.java,
          "photo_database"
        )
          .build()
        INSTANCE = instance
        // return instance
        instance
      }
    }

    /*private class PhotoDatabaseCallback(
      private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
      *//**
       * Override the onCreate method to populate the database.
       *//*
      override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        // If you want to keep the data through app restarts,
        // comment out the following line.
        INSTANCE?.let { database ->
          scope.launch(Dispatchers.IO) {
            populateDatabase(database.photoDao())
          }
        }
      }
    }*/

    /**
     * Populate the database in a new coroutine.
     * If you want to start with more words, just add them.
     */
   /* suspend fun populateDatabase(photoDao: PhotoDao) {
      // Start the app with a clean database every time.
      // Not needed if you only populate on creation.
      photoDao.deleteAll()

      var word = Word("Hello")
      wordDao.insert(word)
      word = Word("World!")
      wordDao.insert(word)
    }*/
  }
}