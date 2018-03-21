package com.artem.myecommerce

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.adapter.CurrentProductVPAdapter
import com.artem.myecommerce.domain.ProductItem
import kotlinx.android.synthetic.main.fragment_current_product.view.*

class CurrentProductFragment : Fragment() {
    private lateinit var currentProductVPAdapter: CurrentProductVPAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_current_product, null)

        var productsList = ArrayList<ProductItem>()
        currentProductVPAdapter = CurrentProductVPAdapter(fragmentManager!!, productsList)

        view.fragment_current_product_vp_product.adapter = currentProductVPAdapter

        return view
    }
}