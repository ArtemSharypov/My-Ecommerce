package com.artem.myecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.artem.myecommerce.R
import com.artem.myecommerce.domain.ProductItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_search_item.view.*

class SearchResultsListAdapter(var context: Context, var productItemsList: ArrayList<ProductItem>) : BaseAdapter() {
    private var inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View

        if(convertView == null){
            view = inflater.inflate(R.layout.row_search_item, parent, false)
        } else {
            view = convertView
        }

        view.setOnClickListener {
            //todo switch to the currentProductFragment with ProductDisplayFragment showing it
        }

        var productItem = productItemsList[position]

        view.row_search_item_tv_product_name.text = productItem.productName
        view.row_search_item_tv_stars.text = productItem.reviewRating.toString()
        view.row_search_item_tv_price.text = "$" + productItem.price

        Glide.with(context).load(productItem.mainImageURL).into(view.row_search_item_iv_product_image)

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