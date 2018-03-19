package com.artem.myecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.artem.myecommerce.R
import com.artem.myecommerce.domain.ProductItem

class SearchResultsListAdapter(context: Context, var productItemsList: ArrayList<ProductItem>) : BaseAdapter() {
    private var inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View

        if(convertView == null){
            view = inflater.inflate(R.layout.row_search_item, parent, false)
        } else {
            view = convertView
        }

        var productItem = productItemsList[position]
        //todo setup onClickListeners & setting any necessary data here

        return view
    }

    override fun getCount(): Int {
        return productItemsList.size
    }

    override fun getItem(position: Int): Any {
        return productItemsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}