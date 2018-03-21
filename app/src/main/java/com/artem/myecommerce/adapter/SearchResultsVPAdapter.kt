package com.artem.myecommerce.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.artem.myecommerce.SearchResultsListFragment
import com.artem.myecommerce.domain.ProductItem

class SearchResultsVPAdapter(fm: FragmentManager, var results: ArrayList<ProductItem>) : FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        //todo create a factory method for fragment and pass in results from the 10 slots
        return SearchResultsListFragment()
    }

    override fun getCount(): Int {
        return results.size
    }
}