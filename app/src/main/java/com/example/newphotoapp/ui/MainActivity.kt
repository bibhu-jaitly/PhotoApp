package com.example.newphotoapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.newphotoapp.PhotoApplication
import com.example.newphotoapp.R.layout
import kotlinx.android.synthetic.main.activity_main.photo_rv

class MainActivity : AppCompatActivity() {

  //region Instance Variables
  private val rvAdapter: PhotoAdapter = PhotoAdapter()
  private val mainViewModel: MainViewModel by viewModels {
    MainViewModelFactory((application as PhotoApplication).repository)
  }
  //endregion

  //region Activity Methods
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)
    handleUI()
    handleObservables()
  }

  //endregion

  //region Helper Methods

  /**
   * -> Initialises all the components of [photo_rv]
   */
  private fun handleUI() {
    val gridLayoutManager = GridLayoutManager(this@MainActivity, 3)
    photo_rv.layoutManager = gridLayoutManager
    photo_rv.setHasFixedSize(true)
    photo_rv.adapter = rvAdapter
  }

  /**
   * Handles all the observables and updates UI accordingly
   */
  private fun handleObservables() {
    mainViewModel.getPicListAPILiveData().observe(this, {
      rvAdapter.updateList(it)
    })

    mainViewModel.getDBLiveData().observe(this, {
      if (!it.isNullOrEmpty()) {
        rvAdapter.updateList(it)
      } else {
        mainViewModel.getPublicPhotoMetaList()
      }
    })
  }

  //endregion
}
