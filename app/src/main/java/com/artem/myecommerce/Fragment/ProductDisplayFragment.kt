package com.artem.myecommerce

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.adapter.ReviewsListAdapter
import com.artem.myecommerce.adapter.ThumbnailsAdapter
import com.artem.myecommerce.domain.ProductItem
import com.artem.myecommerce.domain.ReviewItem
import com.artem.myecommerce.domain.ThumbnailImageItem
import com.bumptech.glide.Glide
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

        view.fragment_product_display_tv_product_name.text = currentProduct.productName
        view.fragment_product_display_tv_review_rating.text = currentProduct.reviewRating.toString()
        view.fragment_product_display_tv_description.text = currentProduct.description
        view.fragment_product_display_tv_price.text = "$" + currentProduct.price

        Glide.with(context!!).load(currentProduct.mainImageURL).into(view.fragment_product_display_iv_displayed_image)

        //todo add way to populate reviewsList
        reviewsListAdapter = ReviewsListAdapter(context!!, reviewsList)
        view.fragment_product_display_lv_reviews.adapter = reviewsListAdapter

        var horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view.fragment_product_display_rv_thumbnails.layoutManager = horizontalLayoutManager

        thumbnailsAdapter = ThumbnailsAdapter(currentProduct.thumbnailImages, context!!)
        view.fragment_product_display_rv_thumbnails.adapter = thumbnailsAdapter

        view.fragment_product_display_btn_add_to_cart.setOnClickListener {
            addToCart()
        }

        view.fragment_product_display_et_quantity.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                var subtotal = calculateSubtotal(currentProduct.price, text.toString().toInt())
                view.fragment_product_display_tv_subtotal_amount.text = "$" + subtotal.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        return view
    }

    private fun addToCart() {

    }

    private fun calculateSubtotal(price: Long, quantity: Int) : Long {
        var total: Long = 0

        if(quantity != null && quantity >= 0) {
            total = price * quantity
        }

        return total
    }
}