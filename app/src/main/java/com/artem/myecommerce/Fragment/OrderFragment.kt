package com.artem.myecommerce.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.R
import com.artem.myecommerce.`interface`.ReplaceFragmentInterface
import com.artem.myecommerce.adapter.OrderItemsListAdapter
import com.artem.myecommerce.domain.CartItem
import com.artem.myecommerce.utility.CalculatorForTotals
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.fragment_order.view.*

class OrderFragment : Fragment() {
    private var replaceFragmentCallback: ReplaceFragmentInterface? = null
    private lateinit var adapter: OrderItemsListAdapter
    private var orderItemsList = ArrayList<CartItem>()
    private var calculator = CalculatorForTotals()

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

        calculateAllTotals()

        return view
    }

    //Calculates all of the totals based on all of the Items in the current Order
    private fun calculateAllTotals() {
        var subtotal = calculator.calculateSubtotalCartItems(orderItemsList)
        var shipping: Long = 0
        var pstAmount = calculator.calculatePST(subtotal, shipping)
        var gstAmount = calculator.calculateGST(subtotal, shipping)
        var total = calculator.calculateTotal(pstAmount, gstAmount, subtotal, shipping)

        fragment_order_tv_cart_total_amount.text = subtotal.toString()
        fragment_order_tv_subtotal_amount.text = subtotal.toString()
        fragment_order_tv_shipping_cost.text = shipping.toString()
        fragment_order_tv_pst_amount.text = pstAmount.toString()
        fragment_order_tv_gst_amount.text = gstAmount.toString()
        fragment_order_tv_total_amount.text = total.toString()
    }

    private fun placeOrder() {
        //todo add a way to place the order
    }

    //Shows/Hides the Order Information section
    private fun showHideYourOrder() {
        if(fragment_order_cl_order_tab.visibility == View.VISIBLE) {
            fragment_order_cl_order_tab.visibility = View.INVISIBLE
            fragment_order_iv_show_hide_order.setImageResource(R.drawable.ic_expand_more)
        } else {
            fragment_order_cl_order_tab.visibility = View.VISIBLE
            fragment_order_iv_show_hide_order.setImageResource(R.drawable.ic_expand_less)
        }
    }

    //Shows/Hides the SHipping Information section
    private fun showHideShippingInfo() {
        if(fragment_order_cl_shipping_info.visibility == View.VISIBLE) {
            fragment_order_cl_shipping_info.visibility = View.INVISIBLE
            fragment_order_iv_show_hide_shipping.setImageResource(R.drawable.ic_expand_more)
        } else {
            fragment_order_cl_shipping_info.visibility = View.VISIBLE
            fragment_order_iv_show_hide_shipping.setImageResource(R.drawable.ic_expand_less)
        }
    }

    //Shows/Hides the Billing Information section
    private fun showHideBillingInfo() {
        if(fragment_order_cl_billing_info.visibility == View.VISIBLE) {
            fragment_order_cl_billing_info.visibility = View.INVISIBLE
            fragment_order_iv_show_hide_billing.setImageResource(R.drawable.ic_expand_more)
        } else {
            fragment_order_cl_billing_info.visibility = View.VISIBLE
            fragment_order_iv_show_hide_billing.setImageResource(R.drawable.ic_expand_less)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            replaceFragmentCallback = context as ReplaceFragmentInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString() + " must implement the necessary Interface")
        }
    }
}
