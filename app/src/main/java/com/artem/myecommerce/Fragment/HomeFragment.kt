package com.artem.myecommerce

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.adapter.HomeProductRecyclerAdapter
import com.artem.myecommerce.domain.ProductItem
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var popularAdapter: HomeProductRecyclerAdapter
    private lateinit var newAdapter: HomeProductRecyclerAdapter
    private lateinit var trendingAdapter: HomeProductRecyclerAdapter
    private lateinit var highlyRatedAdapter: HomeProductRecyclerAdapter
    private var popularItemsList = ArrayList<ProductItem>()
    private var newItemsList = ArrayList<ProductItem>()
    private var trendingItemsList = ArrayList<ProductItem>()
    private var highlyRatedItemsList = ArrayList<ProductItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_home, null)
        var horizontalLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        view.fragment_home_rv_popular.layoutManager = horizontalLayoutManager
        view.fragment_home_rv_new.layoutManager = horizontalLayoutManager
        view.fragment_home_rv_trending.layoutManager = horizontalLayoutManager
        view.fragment_home_rv_highly_rated.layoutManager = horizontalLayoutManager

        //todo populate all of the lists for the adapters

        popularAdapter = HomeProductRecyclerAdapter(popularItemsList)
        newAdapter = HomeProductRecyclerAdapter(newItemsList)
        trendingAdapter = HomeProductRecyclerAdapter(trendingItemsList)
        highlyRatedAdapter = HomeProductRecyclerAdapter(highlyRatedItemsList)

        view.fragment_home_rv_popular.adapter = popularAdapter
        view.fragment_home_rv_new.adapter = newAdapter
        view.fragment_home_rv_trending.adapter = trendingAdapter
        view.fragment_home_rv_highly_rated.adapter = highlyRatedAdapter

        return view
    }
}