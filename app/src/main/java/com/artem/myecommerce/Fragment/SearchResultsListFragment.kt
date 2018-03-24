package com.artem.myecommerce

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.`interface`.ReplaceFragmentInterface
import com.artem.myecommerce.adapter.SearchResultsListAdapter
import com.artem.myecommerce.domain.ProductItem
import kotlinx.android.synthetic.main.fragment_search_results_list.view.*

class SearchResultsListFragment : Fragment() {
    private var replaceFragmentCallback: ReplaceFragmentInterface? = null
    private lateinit var adapter: SearchResultsListAdapter
    private lateinit var productItemsList: ArrayList<ProductItem>

    companion object {
        private val PRODUCT_RESULTS_LIST = "searchText"

        fun newInstance(productResultsList: ArrayList<ProductItem>) : SearchResultsListFragment {
            val args = Bundle()
            args.putSerializable(PRODUCT_RESULTS_LIST, productResultsList)

            val searchResultsListFragment = SearchResultsListFragment()
            searchResultsListFragment.arguments = args

            return searchResultsListFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_search_results_list, null)
        val args = arguments

        if(args != null) {
            productItemsList = args.getSerializable(PRODUCT_RESULTS_LIST) as ArrayList<ProductItem>
        } else {
            productItemsList = ArrayList<ProductItem>()
        }

        adapter = SearchResultsListAdapter(context!!, productItemsList) {
            position, productItemsList -> productItemClicked(position, productItemsList)
        }

        view.fragment_search_results_list_lv_results.adapter = adapter

        return view
    }

    private fun productItemClicked(position: Int, productItemsList: ArrayList<ProductItem>) {
        var currentProductFragment = CurrentProductFragment.newInstance(position, productItemsList)
        replaceFragmentCallback?.replaceWithFragment(currentProductFragment)
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