package com.example.newphotoapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.photo_rv

class MainActivity : AppCompatActivity() {

  private val rvAdapter: PhotoAdapter = PhotoAdapter()
  private val mainViewModel: MainViewModel by viewModels {
    MainViewModelFactory((application as PhotoApplication).repository)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    handleUI()
    handleObservables()
  }

  private fun handleUI() {
    val gridLayoutManager = GridLayoutManager(this@MainActivity, 3)
    photo_rv.layoutManager = gridLayoutManager
    photo_rv.setHasFixedSize(true)
    photo_rv.adapter = rvAdapter
  }

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
}
