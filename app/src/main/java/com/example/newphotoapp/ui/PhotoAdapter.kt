package com.example.newphotoapp.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.newphotoapp.R.drawable
import com.example.newphotoapp.R.layout
import com.example.newphotoapp.ui.PhotoAdapter.ViewHolder
import com.example.newphotoapp.data.model.PicDetail

class PhotoAdapter : RecyclerView.Adapter<ViewHolder>() {

  private var photoList: MutableList<PicDetail> = ArrayList()
  private var context: Context? = null

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    context = parent.context
    val view = layoutInflater
      .inflate(layout.photo_item, parent, false) as ImageView
    return ViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    val photo = photoList[position]
    if (!photo.photoUrl.isNullOrEmpty()) {
      Glide.with(context!!)
        .load(photo.photoUrl)
        .centerCrop()
        .apply(
          RequestOptions()
            .placeholder(drawable.loading_animation)
            .error(drawable.ic_broken_image)
        )
        .into(holder.itemView as ImageView)
    }
  }

  override fun getItemCount(): Int {
    if (photoList.isNullOrEmpty()) {
      return 0
    }
    return photoList.size
  }

  public fun updateList(list: MutableList<PicDetail>) {
    photoList = list
    notifyDataSetChanged()
  }

  class ViewHolder(itemView: ImageView) : RecyclerView.ViewHolder(itemView)
}