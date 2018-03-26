package com.artem.myecommerce

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.artem.myecommerce.`interface`.ReplaceFragmentInterface
import com.artem.myecommerce.adapter.HomeProductRecyclerAdapter
import com.artem.myecommerce.domain.ProductItem
import com.artem.myecommerce.domain.ReviewItem
import com.artem.myecommerce.domain.ThumbnailImageItem
import com.shopify.buy3.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.io.File
import java.util.concurrent.TimeUnit

class HomeFragment : Fragment() {
    private var replaceFragmentCallback: ReplaceFragmentInterface? = null
    private lateinit var popularAdapter: HomeProductRecyclerAdapter
    private lateinit var newAdapter: HomeProductRecyclerAdapter
    private lateinit var trendingAdapter: HomeProductRecyclerAdapter
    private lateinit var highlyRatedAdapter: HomeProductRecyclerAdapter
    private var popularItemsList = ArrayList<ProductItem>()
    private var newItemsList = ArrayList<ProductItem>()
    private var trendingItemsList = ArrayList<ProductItem>()
    private var highlyRatedItemsList = ArrayList<ProductItem>()
    private lateinit var graphClient: GraphClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_home, null)
        var horizontalLayoutManagerPopular = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        var horizontalLayoutManagerNew = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        var horizontalLayoutManagerTrending = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        var horizontalLayoutManagerRated = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        view.fragment_home_rv_popular.layoutManager = horizontalLayoutManagerPopular
        view.fragment_home_rv_new.layoutManager = horizontalLayoutManagerNew
        view.fragment_home_rv_trending.layoutManager = horizontalLayoutManagerTrending
        view.fragment_home_rv_highly_rated.layoutManager = horizontalLayoutManagerRated

        popularAdapter = HomeProductRecyclerAdapter(popularItemsList, context!!) { position, listOfProductItems ->
            productItemClicked(position, listOfProductItems)
        }

        newAdapter = HomeProductRecyclerAdapter(newItemsList, context!!) { position, listOfProductItems ->
            productItemClicked(position, listOfProductItems)
        }

        trendingAdapter = HomeProductRecyclerAdapter(trendingItemsList, context!!) { position, listOfProductItems ->
            productItemClicked(position, listOfProductItems)
        }

        highlyRatedAdapter = HomeProductRecyclerAdapter(highlyRatedItemsList, context!!) { position, listOfProductItems ->
            productItemClicked(position, listOfProductItems)
        }

        view.fragment_home_rv_popular.adapter = popularAdapter
        view.fragment_home_rv_new.adapter = newAdapter
        view.fragment_home_rv_trending.adapter = trendingAdapter
        view.fragment_home_rv_highly_rated.adapter = highlyRatedAdapter

        setupGraphClient()
        queryForProductItems()

        return view
    }

    //Grabs the first 15 Products, alongside their title, description, images, and price
    private fun queryForProductItems() {
        var query = Storefront.query{
            it.shop{
                it.products({args -> args.first(15)}) {
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
                                        it.price()
                                        id
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

                        popularItemsList.add(productItem)
                        newItemsList.add(productItem)
                        trendingItemsList.add(productItem)
                        highlyRatedItemsList.add(productItem)
                    }

                    popularAdapter.notifyDataSetChanged()
                    newAdapter.notifyDataSetChanged()
                    trendingAdapter.notifyDataSetChanged()
                    highlyRatedAdapter.notifyDataSetChanged()
                } else {
                    var toastText = "Failed to find items"
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(error: GraphError) {
                var toastText = "Failed to find items, $error"
                Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun productItemClicked(positionInList: Int, listOfProducts: ArrayList<ProductItem>) {
        var currentProductFragment = CurrentProductFragment.newInstance(positionInList, listOfProducts)
        replaceFragmentCallback?.replaceWithFragment(currentProductFragment)
    }

    private fun setupGraphClient() {
        graphClient = GraphClient.builder(context!!)
                .shopDomain(ShopifyTokens.DOMAIN)
                .accessToken(ShopifyTokens.ACCESS_TOKEN)
                .httpCache(File(context?.cacheDir, "/http"), 10 * 1024 * 1024)
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES))
                .build()
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