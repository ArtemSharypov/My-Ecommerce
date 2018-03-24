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
import com.artem.myecommerce.utility.CalculatorForTotals
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_product_display.*
import kotlinx.android.synthetic.main.fragment_product_display.view.*

class ProductDisplayFragment : Fragment() {
    private lateinit var reviewsListAdapter: ReviewsListAdapter
    private lateinit var thumbnailsAdapter: ThumbnailsAdapter
    private var reviewsList = ArrayList<ReviewItem>()
    private lateinit var currentProduct: ProductItem
    private var calculator = CalculatorForTotals()

    companion object {
        private val PRODUCT_ITEM = "productItem"

        fun newInstance(productItem: ProductItem) : ProductDisplayFragment {
            val args = Bundle()
            args.putParcelable(PRODUCT_ITEM, productItem)

            val productDisplayFragment = ProductDisplayFragment()
            productDisplayFragment.arguments = args

            return productDisplayFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_product_display, null)
        val args = arguments
        var horizontalLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        if(args != null) {
            currentProduct = args.getParcelable(PRODUCT_ITEM)
        } else {
            currentProduct = ProductItem("", ArrayList<ThumbnailImageItem>(), "", 0, 0, ArrayList<ReviewItem>(), "")
        }

        view.fragment_product_display_tv_product_name.text = currentProduct.productName
        view.fragment_product_display_tv_review_rating.text = currentProduct.reviewRating.toString()
        view.fragment_product_display_tv_description.text = currentProduct.description
        view.fragment_product_display_tv_price.text = "$" + currentProduct.price

        Glide.with(context!!).load(currentProduct.mainImageURL).into(view.fragment_product_display_iv_displayed_image)

        //todo add way to populate reviewsList
        reviewsListAdapter = ReviewsListAdapter(context!!, reviewsList)

        thumbnailsAdapter = ThumbnailsAdapter(currentProduct.thumbnailImages, context!!) {
            position -> imageClickAtPos(position)
        }

        view.fragment_product_display_lv_reviews.adapter = reviewsListAdapter
        view.fragment_product_display_rv_thumbnails.layoutManager = horizontalLayoutManager
        view.fragment_product_display_rv_thumbnails.adapter = thumbnailsAdapter
        view.fragment_product_display_btn_add_to_cart.setOnClickListener {
            addToCart()
        }

        //Updates the Subtotal displayed as a User changes the Quantity amount
        view.fragment_product_display_et_quantity.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                var subtotal = calculator.calculateSubtotal(currentProduct.price, text.toString().toInt())
                view.fragment_product_display_tv_subtotal_amount.text = "$" + subtotal.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        return view
    }

    private fun addToCart() {
        //todo add a way to add the item to a cart
    }

    //Switches the main ImageView display to the Image that was clicked
    private fun imageClickAtPos(position: Int) {
        var imageClickedURL = currentProduct.thumbnailImages[position]
        Glide.with(context!!).load(imageClickedURL).into(fragment_product_display_iv_displayed_image)
    }
}