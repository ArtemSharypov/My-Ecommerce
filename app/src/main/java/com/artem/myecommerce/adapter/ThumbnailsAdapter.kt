package com.artem.myecommerce.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.R
import com.artem.myecommerce.domain.ThumbnailImageItem

class ThumbnailsAdapter (var thumbnailImagesList: ArrayList<ThumbnailImageItem>) : RecyclerView.Adapter<ThumbnailsAdapter.ViewHolder>(){
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(thumbnailImageItem: ThumbnailImageItem) {
            //todo set all the necessary parts of the holder here
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(parent?.context)
        var view = inflater?.inflate(R.layout.row_home_product, parent, false)

        return ViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var thumbnailImageItem = thumbnailImagesList[position]

        holder?.bind(thumbnailImageItem)
    }

    override fun getItemCount(): Int {
        return thumbnailImagesList.size
    }
}