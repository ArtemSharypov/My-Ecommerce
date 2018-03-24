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

    companion object {
        private val PRODUCT_POS = "productPos"
        private val PRODUCTS_LIST = "productsList"

        fun newInstance(productPos: Int, productsList: ArrayList<ProductItem>) : CurrentProductFragment {
            val args = Bundle()
            args.putInt(PRODUCT_POS, productPos)
            args.putParcelableArrayList(PRODUCTS_LIST, productsList)

            val currentProductFragment = CurrentProductFragment()
            currentProductFragment.arguments = args

            return currentProductFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_current_product, null)
        val args = arguments
        var productPos = 0
        var productsList: ArrayList<ProductItem>

        if(args != null) {
            productPos = args.getInt(PRODUCT_POS)
            productsList = args.getSerializable(PRODUCTS_LIST) as ArrayList<ProductItem>
        } else {
            productsList = ArrayList<ProductItem>()
        }

        if(productPos > 0 && productsList.isNotEmpty()) {
            //Moves the item that was clicked into the first position, if it wasn't there already
            var tempCurrItem = productsList[productPos]
            productsList.remove(tempCurrItem)
            productsList.add(0, tempCurrItem)

            productPos = 0
        }

        currentProductVPAdapter = CurrentProductVPAdapter(fragmentManager!!, productsList, productPos)

        view.fragment_current_product_vp_product.adapter = currentProductVPAdapter

        return view
    }
}