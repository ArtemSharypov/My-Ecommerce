package layout

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.artem.myecommerce.R
import com.artem.myecommerce.ShopifyTokens
import com.artem.myecommerce.`interface`.CartDetailsInterface
import com.artem.myecommerce.`interface`.ReplaceFragmentInterface
import com.artem.myecommerce.adapter.CartItemsListAdapter
import com.artem.myecommerce.domain.CartItem
import com.artem.myecommerce.domain.ProductItem
import com.artem.myecommerce.fragment.OrderFragment
import com.artem.myecommerce.utility.CalculatorForTotals
import com.shopify.buy3.*
import com.shopify.graphql.support.ID
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.view.*
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class CartFragment : Fragment() {
    private var replaceFragmentCallback: ReplaceFragmentInterface? = null
    private lateinit var adapter: CartItemsListAdapter
    private var cartItemsList = ArrayList<CartItem>()
    private var calculator = CalculatorForTotals()
    private lateinit var cartDetailCallback: CartDetailsInterface
    private lateinit var graphClient: GraphClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_cart, null)

        setupGraphClient()
        populateCartItemsListFromCheckout()

        adapter = CartItemsListAdapter(context!!, cartItemsList, {cartItem, pos -> updateQuantityOfItem(cartItem, pos) },  { removeItemAtPos(it) })
        view.fragment_cart_lv_items.adapter = adapter

        view.fragment_cart_btn_proceed_checkout.setOnClickListener {
            switchToCheckout()
        }

        calculateTotals()

        return view
    }

    //Grabs all of the LineItems from the Checkout and populates it for display
    private fun populateCartItemsListFromCheckout() {
        var currCheckout = cartDetailCallback.getCheckout()

        if(currCheckout != null) {
            val lineItemsEdge = currCheckout.lineItems.edges

            lineItemsEdge.forEach {
                var currNode = it.node
                var lineItemId = currNode.id
                var quantity = currNode.quantity
                var productTitle = currNode.title
                var variantId = currNode.variant
                var customAtts = currNode.customAttributes
                var mainImageUrl = ""
                var price = 0.00

                customAtts.forEach {
                    if(it.key == "mainImageUrl") {
                        mainImageUrl = it.value
                    } else if(it.key == "price") {
                        price = it.value.toDouble()
                    }
                }

                var currProductItem = ProductItem(mainImageUrl, ArrayList(), productTitle, price, 0.0, ArrayList(), "", variantId.toString())
                var currCartItem = CartItem(currProductItem, quantity, lineItemId.toString())

                cartItemsList.add(currCartItem)
            }
        }
    }

    //Updates the amount of Quantity of the CartItem in the Cart
    private fun updateQuantityOfItem(cartItem: CartItem, quantity: Int) {
        var currCheckoutId = cartDetailCallback.getCartId()

        if(quantity >= 0) {
            cartItem.quantity = quantity
            calculateTotals()

            var cartUpdate = Storefront.CheckoutLineItemUpdateInput()
            cartUpdate.quantity = quantity
            cartUpdate.variantId = ID(cartItem.productItem.variantId)
            cartUpdate.id = ID(cartItem.cartItemId)

            var query = Storefront.mutation{
                it.checkoutLineItemsUpdate(currCheckoutId, Arrays.asList(cartUpdate)) {
                    it.checkout {
                        it.webUrl()
                    }
                    it.userErrors {
                        it.field()
                        it.message()
                    }
                }
            }

            graphClient.mutateGraph(query).enqueue(object : GraphCall.Callback<Storefront.Mutation> {
                override fun onResponse(response: GraphResponse<Storefront.Mutation>) {
                    if(response.data()?.checkoutCreate?.userErrors?.isEmpty()!!) {
                        val checkout = response.data()?.checkoutCreate?.checkout
                        cartDetailCallback.setCheckout(checkout!!)

                        var toastText = "Updated the Product in your cart"
                        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
                    } else {
                        var toastText = "Failed to update the Product from your Cart"
                        Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(error: GraphError) {
                    var toastText = "Failed to update the Product from your Cart, $error"
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
                }
            })

        }
    }

    //Removes the CartItem from the List, updates the ListView and the Totals for GST/PST/Subtotal/Total
    private fun removeItemAtPos(position: Int) {
        cartItemsList.removeAt(position)
        adapter.notifyDataSetChanged()

        calculateTotals()

        var currItem = cartItemsList[position]
        var currCheckoutId = cartDetailCallback.getCartId()
        var query = Storefront.mutation{
            it.checkoutLineItemsRemove(currCheckoutId, Arrays.asList(ID(currItem.cartItemId))) {
                it.checkout {
                    it.webUrl()
                }
                it.userErrors {
                    it.field()
                    it.message()
                }
            }
        }

        graphClient.mutateGraph(query).enqueue(object : GraphCall.Callback<Storefront.Mutation> {
            override fun onResponse(response: GraphResponse<Storefront.Mutation>) {
                if(response.data()?.checkoutCreate?.userErrors?.isEmpty()!!) {
                    val checkout = response.data()?.checkoutCreate?.checkout
                    cartDetailCallback.setCheckout(checkout!!)

                    var toastText = "Removed the Product from your cart"
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
                } else {
                    var toastText = "Failed to removed the Product from your Cart"
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(error: GraphError) {
                var toastText = "Failed to remove the Product from your Cart, $error"
                Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
            }
        })
    }

    //Calculates the Totals based on all of the Items in a Cart, and updates all of the Taxes & Totals for them
    private fun calculateTotals(){
        var subtotal = calculator.calculateSubtotalCartItems(cartItemsList)
        var pstAmount = calculator.calculatePST(subtotal)
        var gstAmount = calculator.calculateGST(subtotal)
        var total = calculator.calculateTotal(pstAmount, gstAmount, subtotal)

        fragment_cart_tv_subtotal_amount.text = subtotal.toString()
        fragment_cart_tv_pst_amount.text = pstAmount.toString()
        fragment_cart_tv_gst_amount.text = gstAmount.toString()
        fragment_cart_tv_total_amount.text = total.toString()
    }

    private fun switchToCheckout() {
        var orderFragment = OrderFragment()
        replaceFragmentCallback?.replaceWithFragment(orderFragment)
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

        try {
            cartDetailCallback = context as CartDetailsInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() + " must implement the necessary Interface")
        }
    }
}