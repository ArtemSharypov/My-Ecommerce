package com.artem.myecommerce.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.artem.myecommerce.ProductDisplayFragment
import com.artem.myecommerce.domain.ProductItem


class CurrentProductVPAdapter(fm: FragmentManager, var products: ArrayList<ProductItem>) : FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        //todo create a factory method for fragment and pass in products[position]
        return ProductDisplayFragment()
    }

    override fun getCount(): Int {
        return products.size
    }
}