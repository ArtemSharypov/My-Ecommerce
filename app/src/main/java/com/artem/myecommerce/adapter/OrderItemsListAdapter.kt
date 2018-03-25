package com.artem.myecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.artem.myecommerce.R
import com.artem.myecommerce.domain.CartItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_order_item.view.*

class OrderItemsListAdapter (var context: Context, var cartItemsList: ArrayList<CartItem>) : BaseAdapter() {

    private var inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View

        if(convertView == null){
            view = inflater.inflate(R.layout.row_order_item, parent, false)
        } else {
            view = convertView
        }

        var cartItem = cartItemsList[position]

        view.row_order_item_tv_product_name.text = cartItem.productItem.productName
        view.row_order_item_tv_price_display.text = "$" + cartItem.productItem.price
        view.row_order_item_tv_quantity_amount.text = cartItem.quantity.toString()
        view.row_order_item_tv_total_display.text = "$" + cartItem.productItem.price * cartItem.quantity

        Glide.with(context).load(cartItem.productItem.mainImageURL).into(view.row_order_item_iv_product_image)

        return view
    }

    override fun getCount(): Int {
        return cartItemsList.size
    }

    override fun getItem(position: Int): Any {
        return cartItemsList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}