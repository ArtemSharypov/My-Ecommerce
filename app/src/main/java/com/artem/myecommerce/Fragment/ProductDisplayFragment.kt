package com.artem.myecommerce

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.artem.myecommerce.`interface`.CartDetailsInterface
import com.artem.myecommerce.adapter.ReviewsListAdapter
import com.artem.myecommerce.adapter.ThumbnailsAdapter
import com.artem.myecommerce.domain.ProductItem
import com.artem.myecommerce.domain.ReviewItem
import com.artem.myecommerce.domain.ThumbnailImageItem
import com.artem.myecommerce.utility.CalculatorForTotals
import com.bumptech.glide.Glide
import com.shopify.buy3.*
import com.shopify.graphql.support.ID
import com.shopify.graphql.support.Input
import kotlinx.android.synthetic.main.fragment_product_display.*
import kotlinx.android.synthetic.main.fragment_product_display.view.*
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

class ProductDisplayFragment : Fragment() {
    private lateinit var reviewsListAdapter: ReviewsListAdapter
    private lateinit var thumbnailsAdapter: ThumbnailsAdapter
    private var reviewsList = ArrayList<ReviewItem>()
    private lateinit var currentProduct: ProductItem
    private var calculator = CalculatorForTotals()
    private lateinit var cartDetailCallback: CartDetailsInterface
    private lateinit var graphClient: GraphClient

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
            currentProduct = ProductItem("", ArrayList<ThumbnailImageItem>(), "", 0.0, 0.0, ArrayList<ReviewItem>(), "", "")
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
            addToCheckout()
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

        setupGraphClient()

        return view
    }

    private fun addToCheckout() {
        var currCheckoutId = cartDetailCallback.getCartId()
        var quantity = fragment_product_display_et_quantity.text.toString().toInt()

        if(currCheckoutId == null) {
            addToNewCheckout(quantity)
        } else {
            addToExistingCheckout(currCheckoutId, quantity)
        }
    }

    //Creates a new Checkout and adds the ProductItem to it
    private fun addToNewCheckout(quantity: Int) {
        var cartInput = Storefront.CheckoutCreateInput()
                .setLineItemsInput(Input.value(Arrays.asList(
                        Storefront.CheckoutLineItemInput(quantity, ID(currentProduct.variantId))
                )))

        var query = Storefront.mutation{
            it.checkoutCreate(cartInput, {
                it.checkout{
                    it.webUrl()
                }
                it.userErrors{
                    it.field()
                    it.message()
                }
            })
        }

        graphClient.mutateGraph(query).enqueue(object : GraphCall.Callback<Storefront.Mutation> {
            override fun onResponse(response: GraphResponse<Storefront.Mutation>) {
                if(response.data()?.checkoutCreate?.userErrors?.isEmpty()!!) {
                    val responseConstant = response
                    cartDetailCallback.setCartId(responseConstant.data()?.checkoutCreate?.checkout?.id!!)
                    val checkout = responseConstant.data()?.checkoutCreate?.checkout
                    cartDetailCallback.setCheckout(checkout!!)
                } else {
                    var toastText = "Failed to login"
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG)
                }
            }

            override fun onFailure(error: GraphError) {
                var toastText = "Failed to create a Cart, $error"
                Toast.makeText(context, toastText, Toast.LENGTH_LONG)
            }
        })
    }

    //Adds the ProductItem to the already existing Checkout
    private fun addToExistingCheckout(checkoutId: ID, quantity: Int) {
        var cartInput = Storefront.CheckoutLineItemInput(quantity, ID(currentProduct.variantId))

        var query = Storefront.mutation{
            it.checkoutLineItemsAdd(Arrays.asList(cartInput), checkoutId) {
                it.checkout {
                    it.webUrl()
                }
                it.userErrors{
                    it.field()
                    it.message()
                }
            }
        }

        graphClient.mutateGraph(query).enqueue(object : GraphCall.Callback<Storefront.Mutation> {
            override fun onResponse(response: GraphResponse<Storefront.Mutation>) {
                if(response.data()?.checkoutCreate?.userErrors?.isEmpty()!!) {
                    var toastText = "Added the item to your Cart"
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG)
                } else {
                    var toastText = "Failed to add the Product to your Cart"
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG)
                }
            }

            override fun onFailure(error: GraphError) {
                var toastText = "Failed to add the Product to your Cart, $error"
                Toast.makeText(context, toastText, Toast.LENGTH_LONG)
            }
        })
    }

    //Switches the main ImageView display to the Image that was clicked
    private fun imageClickAtPos(position: Int) {
        var imageClickedURL = currentProduct.thumbnailImages[position]
        Glide.with(context!!).load(imageClickedURL).into(fragment_product_display_iv_displayed_image)
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
            cartDetailCallback = context as CartDetailsInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString() + " must implement the necessary Interface")
        }
    }
}