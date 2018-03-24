package com.artem.myecommerce.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.artem.myecommerce.SearchResultsListFragment
import com.artem.myecommerce.domain.ProductItem
import kotlin.math.min

class SearchResultsVPAdapter(fm: FragmentManager, var results: ArrayList<ProductItem>) : FragmentPagerAdapter(fm){
    private val NUM_RESULTS_PER_PAGE = 10

    override fun getItem(position: Int): Fragment {
        var startPos = position * NUM_RESULTS_PER_PAGE
        var endPos = min(startPos + NUM_RESULTS_PER_PAGE, results.size)
        var currPageResults = results.subList(startPos, endPos) as ArrayList<ProductItem>

        return SearchResultsListFragment.newInstance(currPageResults)
    }

    override fun getCount(): Int {
        return results.size
    }
}