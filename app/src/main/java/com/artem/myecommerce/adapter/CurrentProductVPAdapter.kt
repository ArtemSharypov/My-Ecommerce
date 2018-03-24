package com.artem.myecommerce.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.artem.myecommerce.ProductDisplayFragment
import com.artem.myecommerce.domain.ProductItem


class CurrentProductVPAdapter(fm: FragmentManager, var products: ArrayList<ProductItem>, var currPos: Int) : FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        return ProductDisplayFragment.newInstance(products[currPos])
    }

    override fun getCount(): Int {
        return products.size
    }
}