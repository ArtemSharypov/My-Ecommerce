package com.artem.myecommerce

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.adapter.PageNumbersAdapter
import kotlinx.android.synthetic.main.fragment_search_results.view.*

class SearchResultsFragment : Fragment() {
    private lateinit var pageNumbersAdapter: PageNumbersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_search_results, null)

        //todo switch this to use the ViewPager's count, then dynamically create the number of pages
        var pageNumbers = ArrayList<String>()

        var horizontalLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        view.fragment_search_results_rv_pages_display.layoutManager = horizontalLayoutManager

        pageNumbersAdapter = PageNumbersAdapter(pageNumbers)
        view.fragment_search_results_rv_pages_display.adapter = pageNumbersAdapter

        return view
    }
}