package com.artem.myecommerce.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.R
import kotlinx.android.synthetic.main.row_page_number.view.*

class PageNumbersAdapter(var pageNumbersList: ArrayList<String>) : RecyclerView.Adapter<PageNumbersAdapter.ViewHolder>(){
    class ViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        fun bind(pageNumber: String) {
            view.row_page_number_tv_page.text = pageNumber
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(parent?.context)
        var view = inflater?.inflate(R.layout.row_page_number, parent, false)

        return ViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var pageNumber = pageNumbersList[position]

        holder?.bind(pageNumber)
    }

    override fun getItemCount(): Int {
        return pageNumbersList.size
    }
}