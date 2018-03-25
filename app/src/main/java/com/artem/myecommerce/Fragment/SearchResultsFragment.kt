package com.artem.myecommerce

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.artem.myecommerce.adapter.PageNumbersAdapter
import com.artem.myecommerce.adapter.SearchResultsVPAdapter
import com.artem.myecommerce.domain.ProductItem
import com.artem.myecommerce.domain.ReviewItem
import com.artem.myecommerce.domain.ThumbnailImageItem
import com.shopify.buy3.*
import kotlinx.android.synthetic.main.fragment_search_results.*
import kotlinx.android.synthetic.main.fragment_search_results.view.*
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.math.ceil

class SearchResultsFragment : Fragment() {
    private lateinit var pageNumbersAdapter: PageNumbersAdapter
    private lateinit var searchResultsVPAdapter: SearchResultsVPAdapter
    private val NUMBER_RESULTS_PER_PAGE = 10.0
    private lateinit var graphClient: GraphClient
    private var searchText = ""
    private var productsList = ArrayList<ProductItem>()
    private var pageNumbers = ArrayList<String>()

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
        val args = arguments
        var horizontalLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        if(args != null) {
            searchText = args.getString(SEARCH_TEXT)
        }

        searchResultsVPAdapter = SearchResultsVPAdapter(childFragmentManager, productsList)

        pageNumbers = createListOfPageStrings(searchResultsVPAdapter.count)
        view.fragment_search_results_vp_product.adapter = searchResultsVPAdapter
        view.fragment_search_results_rv_pages_display.layoutManager = horizontalLayoutManager

        pageNumbersAdapter = PageNumbersAdapter(pageNumbers) {
            position -> switchToPage(position)
        }

        view.fragment_search_results_rv_pages_display.adapter = pageNumbersAdapter

        setupGraphClient()
        queryForProductItems()

        return view
    }

    //Grabs the all of the Products that match the passed searchText, alongside their title, description, images, and price
    private fun queryForProductItems() {
        var query = Storefront.query{
            it.shop{
                it.products({args -> args.query(searchText)}) {
                    it.edges {
                        it.node {
                            it.title()
                            it.description()
                            it.images {
                                it.edges {
                                    it.node { it.src() }
                                }
                            }
                            it.variants({args -> args.first(1)}) {
                                it.edges {
                                    it.node {
                                        id
                                        it.price()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        var call = graphClient.queryGraph(query)
        call.enqueue(object : GraphCall.Callback<Storefront.QueryRoot> {
            override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {
                if(response.data()?.shop?.products != null) {
                    val responseConstant = response

                    responseConstant.data()?.shop?.products?.edges?.forEach {
                        var currNode = it.node
                        var productTitle = currNode.title
                        var productDesc = currNode.description
                        var firstImage = true
                        var mainImageUrl = ""
                        var listOfThumbnails = ArrayList<ThumbnailImageItem>()
                        var price = 0.00
                        var variantId = ""

                        currNode.images.edges.forEach {
                            var imageNode = it.node
                            if(firstImage) {
                                mainImageUrl = imageNode.src
                                firstImage = false
                            } else {
                                var currThumbnailImage = ThumbnailImageItem(imageNode.src)
                                listOfThumbnails.add(currThumbnailImage)
                            }
                        }

                        currNode.variants.edges.forEach {
                            var variantsNode = it.node
                            price = variantsNode.price.toDouble()
                            variantId = variantsNode.id.toString()
                        }

                        var productItem = ProductItem(mainImageUrl, listOfThumbnails, productTitle,
                                price, 0.0, ArrayList<ReviewItem>(), productDesc, variantId)

                        productsList.add(productItem)
                    }

                    searchResultsVPAdapter.notifyDataSetChanged()
                    pageNumbers = createListOfPageStrings(searchResultsVPAdapter.count)
                    pageNumbersAdapter.notifyDataSetChanged()

                } else {
                    var toastText = "Failed to find items"
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG)
                }
            }

            override fun onFailure(error: GraphError) {
                var toastText = "Failed to find items, $error"
                Toast.makeText(context, toastText, Toast.LENGTH_LONG)
            }
        })
    }

    private fun setupGraphClient() {
        graphClient = GraphClient.builder(context!!)
                .shopDomain(ShopifyTokens.DOMAIN)
                .accessToken(ShopifyTokens.ACCESS_TOKEN)
                .httpCache(File(context?.cacheDir, "/http"), 10 * 1024 * 1024)
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES))
                .build()
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