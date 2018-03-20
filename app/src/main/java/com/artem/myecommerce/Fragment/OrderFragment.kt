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

        view.fragment_order_btn_place_order.setOnClickListener {
            placeOrder()
        }

        view.fragment_order_cl_order_tab.setOnClickListener {
            showHideYourOrder()
        }

        view.fragment_order_cl_billing_info_tab.setOnClickListener {
            showHideBillingInfo()
        }

        view.fragment_order_cl_shipping_info.setOnClickListener {
            showHideShippingInfo()
        }

        var subtotal = calculateSubtotal()
        var shipping: Long = 0
        var pst = calculatePST(subtotal, shipping)
        var gst = calculateGST(subtotal, shipping)
        var total = calculateTotal(pst, gst, subtotal, shipping)

        view.fragment_order_tv_cart_total_amount.text = subtotal.toString()
        view.fragment_order_tv_subtotal_amount.text = subtotal.toString()
        view.fragment_order_tv_shipping_cost.text = shipping.toString()
        view.fragment_order_tv_pst_amount.text = pst.toString()
        view.fragment_order_tv_gst_amount.text = gst.toString()
        view.fragment_order_tv_total_amount.text = total.toString()

        return view
    }

    private fun placeOrder() {

    }

    private fun calculatePST(subtotal: Long, shipping: Long) : Long {
        return 0
    }

    private fun calculateGST(subtotal: Long, shipping: Long) : Long {
        return 0
    }

    private fun calculateSubtotal() : Long {
        return 0
    }

    private fun calculateTotal(pst: Long, gst: Long, subtotal: Long, shipping: Long) : Long{
        //todo split up into PST/GST/Subtotal/Total
        return 0
    }

    private fun showHideYourOrder() {

    }

    private fun showHideShippingInfo() {

    }

    private fun showHideBillingInfo() {

    }
}
