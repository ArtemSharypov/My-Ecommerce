package com.artem.myecommerce.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.artem.myecommerce.R
import com.artem.myecommerce.domain.CartItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.row_cart_item.view.*

class CartItemsListAdapter (var context: Context, var cartItemsList: ArrayList<CartItem>, var updateQuantityCallback: (CartItem, Int) -> Unit,
                            var removeItemCallback: (Int) -> Unit) : BaseAdapter() {
    private var inflater = LayoutInflater.from(context)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view: View

        if(convertView == null){
            view = inflater.inflate(R.layout.row_cart_item, parent, false)
        } else {
            view = convertView
        }

        var cartItem = cartItemsList[position]
        var totalText = view.row_cart_item_tv_total_display
        var total = calculateItemSubtotal(cartItem.productItem.price, cartItem.quantity)

        totalText.text = total.toString()
        view.row_search_item_tv_product_name.text = cartItem.productItem.productName
        view.row_cart_item_tv_price_display.text = "$" + cartItem.productItem.price
        view.row_cart_item_et_quantity_input.setText(cartItem.quantity.toString())

        Glide.with(context).load(cartItem.productItem.mainImageURL).into(view.row_search_item_iv_product_image)

        view.row_cart_item_ib_remove_item.setOnClickListener {
            removeItemCallback(position)
        }

        //Updates the totals once a User changes the Quantity of a Item
        view.row_cart_item_et_quantity_input.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                var total = calculateItemSubtotal(cartItem.productItem.price, text.toString().toInt())
                totalText.text = "$" + total.toString()

                var quantity = text.toString().toInt()
                updateQuantityCallback(cartItem, quantity)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        return view
    }

    private fun calculateItemSubtotal(price: Double, quantity: Int) : Double {
        var total = 0.0

        if(quantity != null && quantity >= 0) {
            total = price * quantity
        }

        return total
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