package com.artem.myecommerce.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.R
import com.artem.myecommerce.domain.ThumbnailImageItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_thumbnail.view.*

class ThumbnailsAdapter (var thumbnailImagesList: ArrayList<ThumbnailImageItem>, var context: Context) : RecyclerView.Adapter<ThumbnailsAdapter.ViewHolder>(){
    class ViewHolder(var view: View, var context: Context) : RecyclerView.ViewHolder(view) {
        fun bind(thumbnailImageItem: ThumbnailImageItem, context: Context, position: Int) {
            Glide.with(context).load(thumbnailImageItem.imageURL).into(view.row_thumbnail_iv_item_image_preview)

            view.row_thumbnail_iv_item_image_preview.setOnClickListener {
                //todo switch the main image to this one that was clicked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(parent?.context)
        var view = inflater?.inflate(R.layout.row_thumbnail, parent, false)

        return ViewHolder(view!!, context)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var thumbnailImageItem = thumbnailImagesList[position]

        holder?.bind(thumbnailImageItem, context, position)
    }

    override fun getItemCount(): Int {
        return thumbnailImagesList.size
    }
}