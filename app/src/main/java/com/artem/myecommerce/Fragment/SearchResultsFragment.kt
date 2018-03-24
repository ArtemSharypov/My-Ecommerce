package com.artem.myecommerce

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.adapter.PageNumbersAdapter
import com.artem.myecommerce.adapter.SearchResultsVPAdapter
import com.artem.myecommerce.domain.ProductItem
import kotlinx.android.synthetic.main.fragment_search_results.*
import kotlinx.android.synthetic.main.fragment_search_results.view.*
import kotlin.math.ceil

class SearchResultsFragment : Fragment() {
    private lateinit var pageNumbersAdapter: PageNumbersAdapter
    private lateinit var searchResultsVPAdapter: SearchResultsVPAdapter
    private val NUMBER_RESULTS_PER_PAGE = 10.0

    companion object {
        private val SEARCH_TEXT = "searchText"

        fun newInstance(searchText: String) : SearchResultsFragment {
            val args = Bundle()
            args.putString(SEARCH_TEXT, searchText)

            val searchResultsFragment = SearchResultsFragment()
            searchResultsFragment.arguments = args

            return searchResultsFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_search_results, null)
        var searchText = ""
        val args = arguments
        var horizontalLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        if(args != null) {
            searchText = args.getString(SEARCH_TEXT)
        }

        //todo do a call to grab the items that are close / match the search text
        var productsList = ArrayList<ProductItem>()
        searchResultsVPAdapter = SearchResultsVPAdapter(childFragmentManager, productsList)


        var pageNumbers = createListOfPageStrings(searchResultsVPAdapter.count)
        view.fragment_search_results_vp_product.adapter = searchResultsVPAdapter
        view.fragment_search_results_rv_pages_display.layoutManager = horizontalLayoutManager

        pageNumbersAdapter = PageNumbersAdapter(pageNumbers) {
            position -> switchToPage(position)
        }

        view.fragment_search_results_rv_pages_display.adapter = pageNumbersAdapter

        return view
    }

    //Switches the ViewPager to the Page number that is passed in
    private fun switchToPage(page: Int) {
        if(page >= 0 && page <= searchResultsVPAdapter.count) {
            fragment_search_results_vp_product.currentItem = page
        }
    }

    //Creates a List of "Page" Strings that are numbered 1 to numResults
    private fun createListOfPageStrings(numResults: Int) : ArrayList<String> {
        var numPages = ceil(searchResultsVPAdapter.count / NUMBER_RESULTS_PER_PAGE).toInt()
        var listOfPageStrings = ArrayList<String>()

        for(i in 1..numPages) {
            listOfPageStrings.add("" + i)
        }

        return listOfPageStrings
    }

}