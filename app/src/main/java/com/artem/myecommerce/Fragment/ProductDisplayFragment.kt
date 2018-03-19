package com.artem.myecommerce

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.adapter.ThumbnailsAdapter
import com.artem.myecommerce.adapter.ReviewsListAdapter
import com.artem.myecommerce.domain.ProductItem
import com.artem.myecommerce.domain.ReviewItem
import com.artem.myecommerce.domain.ThumbnailImageItem
import kotlinx.android.synthetic.main.fragment_product_display.view.*

class ProductDisplayFragment : Fragment() {
    private lateinit var reviewsListAdapter: ReviewsListAdapter
    private lateinit var thumbnailsAdapter: ThumbnailsAdapter
    private var reviewsList = ArrayList<ReviewItem>()
    private lateinit var currentProduct: ProductItem

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_product_display, null)

        //todo need to have a proper product item
        currentProduct = ProductItem("", ArrayList<ThumbnailImageItem>(), "", 0, 0, ArrayList<ReviewItem>(), "")

        //todo add way to populate reviewsList
        reviewsListAdapter = ReviewsListAdapter(context!!, reviewsList)
        view.fragment_product_display_lv_reviews.adapter = reviewsListAdapter

        var horizontalLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        view.fragment_product_display_rv_thumbnails.layoutManager = horizontalLayoutManager

        thumbnailsAdapter = ThumbnailsAdapter(currentProduct.thumbnailImages)
        view.fragment_product_display_rv_thumbnails.adapter = thumbnailsAdapter

        return view
    }
}