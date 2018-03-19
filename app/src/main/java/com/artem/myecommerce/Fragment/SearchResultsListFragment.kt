package com.artem.myecommerce

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.adapter.SearchResultsListAdapter
import com.artem.myecommerce.domain.ProductItem
import kotlinx.android.synthetic.main.fragment_search_results_list.view.*

class SearchResultsListFragment : Fragment() {
    private lateinit var adapter: SearchResultsListAdapter
    private var productItemsList = ArrayList<ProductItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_search_results_list, null)

        //todo add way to populate productItemsList

        adapter = SearchResultsListAdapter(context!!, productItemsList)
        view.fragment_search_results_list_lv_results.adapter = adapter

        return view
    }
}