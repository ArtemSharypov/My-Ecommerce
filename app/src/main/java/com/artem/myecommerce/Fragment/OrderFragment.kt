package com.artem.myecommerce.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.R
import com.artem.myecommerce.adapter.OrderItemsListAdapter
import com.artem.myecommerce.domain.CartItem
import kotlinx.android.synthetic.main.fragment_order.view.*

class OrderFragment : Fragment() {
    private lateinit var adapter: OrderItemsListAdapter
    private var orderItemsList = ArrayList<CartItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_order, null)

        //todo add way to populate orderItemsList

        adapter = OrderItemsListAdapter(context!!, orderItemsList)
        view.fragment_order_lv_order_items.adapter = adapter

        return view
    }
}
