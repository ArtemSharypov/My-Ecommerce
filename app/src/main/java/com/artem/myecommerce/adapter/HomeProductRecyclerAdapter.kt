package com.artem.myecommerce.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.R
import com.artem.myecommerce.domain.ProductItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_home_product.view.*

class HomeProductRecyclerAdapter(var productItemsList: ArrayList<ProductItem>, var context: Context,
                                 val productClick: (Int, ArrayList<ProductItem>) -> Unit) : RecyclerView.Adapter<HomeProductRecyclerAdapter.ViewHolder>(){
    class ViewHolder(var view: View, var context: Context) : RecyclerView.ViewHolder(view) {
        fun bind(productItem: ProductItem) {
            Glide.with(context).load(productItem.mainImageURL).into(view.row_home_product_iv_product_image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        var inflater = LayoutInflater.from(parent?.context)
        var view = inflater?.inflate(R.layout.row_home_product, parent, false)
        var holder = ViewHolder(view!!, context)

        view.setOnClickListener {
            productClick(holder.adapterPosition, productItemsList)
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        var productItem = productItemsList[position]

        holder?.bind(productItem)
    }

    override fun getItemCount(): Int {
        return productItemsList.size
    }
}