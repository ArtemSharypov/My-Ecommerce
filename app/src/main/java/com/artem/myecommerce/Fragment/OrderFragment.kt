package com.artem.myecommerce.fragment

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
import com.artem.myecommerce.adapter.OrderItemsListAdapter
import com.artem.myecommerce.domain.CartItem
import com.artem.myecommerce.utility.CalculatorForTotals
import com.shopify.buy3.*
import com.shopify.graphql.support.ID
import kotlinx.android.synthetic.main.fragment_order.*
import kotlinx.android.synthetic.main.fragment_order.view.*
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

class OrderFragment : Fragment() {
    private var replaceFragmentCallback: ReplaceFragmentInterface? = null
    private lateinit var adapter: OrderItemsListAdapter
    private var orderItemsList = ArrayList<CartItem>()
    private var calculator = CalculatorForTotals()
    private lateinit var graphClient: GraphClient
    private lateinit var cardClient: CardClient
    private lateinit var cartDetailCallback: CartDetailsInterface

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_order, null)

        //todo add way to populate orderItemsList

        adapter = OrderItemsListAdapter(context!!, orderItemsList)
        view.fragment_order_lv_order_items.adapter = adapter

        view.fragment_order_btn_place_order.setOnClickListener {
            placeOrder()
        }

        view.fragment_order_cl_order_tab.setOnClickListener {
            showHideYourOrder()
        }

        view.fragment_order_cl_billing_info_tab.setOnClickListener {
            showHideBillingInfo()
        }

        view.fragment_order_cl_shipping_info.setOnClickListener {
            showHideShippingInfo()
        }

        calculateAllTotals()
        setupGraphClient()
        setupCardClient()

        return view
    }

    //Calculates all of the totals based on all of the Items in the current Order
    private fun calculateAllTotals() {
        var subtotal = calculator.calculateSubtotalCartItems(orderItemsList)
        var shipping = 0.0
        var pstAmount = calculator.calculatePST(subtotal, shipping)
        var gstAmount = calculator.calculateGST(subtotal, shipping)
        var total = calculator.calculateTotal(pstAmount, gstAmount, subtotal, shipping)

        fragment_order_tv_cart_total_amount.text = subtotal.toString()
        fragment_order_tv_subtotal_amount.text = subtotal.toString()
        fragment_order_tv_shipping_cost.text = shipping.toString()
        fragment_order_tv_pst_amount.text = pstAmount.toString()
        fragment_order_tv_gst_amount.text = gstAmount.toString()
        fragment_order_tv_total_amount.text = total.toString()
    }

    //Attempts to place a Order on the current Checkout
    private fun placeOrder() {
        var currCheckoutId = cartDetailCallback.getCartId()
        var checkout = cartDetailCallback.getCheckout()
        var creditCard = CreditCard.builder() //todo add actual input fields for cc info
                .firstName("first")
                .lastName("last")
                .number("1")
                .expireMonth("2")
                .expireYear("2018")
                .verificationCode("123")
                .build()

        //todo find where this magical .getVaultURL comes from that doesn't exist
        cardClient.vault(creditCard, checkout?.webUrl!!).enqueue(object : CreditCardVaultCall.Callback {
            override fun onResponse(token: String) {
                proceedCheckout(token, currCheckoutId!!)
            }

            override fun onFailure(error: IOException) {
                var toastText = "Error with the Credit Card Entered, $error"
                Toast.makeText(context, toastText, Toast.LENGTH_LONG)
            }
        })

    }

    //Continues with a Checkout once a CreditCard has received a Vault Token
    private fun proceedCheckout(creditCardVaultToken: String, currCheckoutId: ID) {
        var total = fragment_order_tv_total_amount.text.toString().toBigDecimal()
        var idempotencyKey = UUID.randomUUID().toString()
        var billingAddress = Storefront.MailingAddressInput() //todo add way for adding billing address info aka input fields

        var input = Storefront.CreditCardPaymentInput(total, idempotencyKey, billingAddress,
                creditCardVaultToken)

        var query = Storefront.mutation{
            it.checkoutCompleteWithCreditCard(currCheckoutId, input) {
                it.payment {
                    it.ready()
                    it.errorMessage()
                }
                it.checkout {
                    it.ready()
                }
                it.userErrors {
                    it.field()
                    it.message()
                }
            }
        }

        graphClient.mutateGraph(query).enqueue(object : GraphCall.Callback<Storefront.Mutation> {
            override fun onResponse(response: GraphResponse<Storefront.Mutation>) {
                if(response.data()?.checkoutCompleteWithCreditCard?.userErrors?.isEmpty()!!) {
                    val responseConstant = response
                    var checkoutReady = responseConstant.data()?.checkoutCompleteWithCreditCard?.checkout?.ready
                    var paymentReady = responseConstant.data()?.checkoutCompleteWithCreditCard?.payment?.ready

                    if(paymentReady != null && checkoutReady != null) {
                        var paymentId = responseConstant.data()?.checkoutCompleteWithCreditCard?.payment?.id
                        checkForCheckoutCompletion(paymentId!!)
                    }
                } else {
                    var toastText = "Failed to create your order"
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG)
                }
            }

            override fun onFailure(error: GraphError) {
                var toastText = "Failed with creating your Order, $error"
                Toast.makeText(context, toastText, Toast.LENGTH_LONG)
            }
        })
    }

    //Polls until the Checkout is complete (or until it polls 12 times)
    private fun checkForCheckoutCompletion(paymentID: ID) {
        var query = Storefront.query {
            it.node(paymentID) {
                it.onPayment {
                    it.checkout {
                        it.order {
                            it.processedAt()
                            it.orderNumber()
                            it.totalPrice()
                        }
                    }
                    it.errorMessage()
                    it.ready()
                }
            }
        }

        graphClient.queryGraph(query).enqueue(object : GraphCall.Callback<Storefront.QueryRoot> {
            override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {
                var payment = response.data()?.node as Storefront.Payment

                if(payment.errorMessage == null || payment.errorMessage.isEmpty()) {
                    var checkout = payment.checkout
                    var orderId = checkout.order.id.toString()

                    //todo add something to deal with when a order has successfully been placed
                } else {
                    var error = payment.errorMessage
                    var toastText = "Failed with the payment on checkout, $error"
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG)
                }
            }

            override fun onFailure(error: GraphError) {}
        }, null, RetryHandler.exponentialBackoff(500, TimeUnit.MILLISECONDS, 1.2f)
                .whenResponse<Storefront.QueryRoot> {
                    (it.data()?.node as Storefront.Payment).ready == false
                }
                .maxCount(12)
                .build()
        )
    }

    //Shows/Hides the Order Information section
    private fun showHideYourOrder() {
        if(fragment_order_cl_order_tab.visibility == View.VISIBLE) {
            fragment_order_cl_order_tab.visibility = View.INVISIBLE
            fragment_order_iv_show_hide_order.setImageResource(R.drawable.ic_expand_more)
        } else {
            fragment_order_cl_order_tab.visibility = View.VISIBLE
            fragment_order_iv_show_hide_order.setImageResource(R.drawable.ic_expand_less)
        }
    }

    //Shows/Hides the SHipping Information section
    private fun showHideShippingInfo() {
        if(fragment_order_cl_shipping_info.visibility == View.VISIBLE) {
            fragment_order_cl_shipping_info.visibility = View.INVISIBLE
            fragment_order_iv_show_hide_shipping.setImageResource(R.drawable.ic_expand_more)
        } else {
            fragment_order_cl_shipping_info.visibility = View.VISIBLE
            fragment_order_iv_show_hide_shipping.setImageResource(R.drawable.ic_expand_less)
        }
    }

    //Shows/Hides the Billing Information section
    private fun showHideBillingInfo() {
        if(fragment_order_cl_billing_info.visibility == View.VISIBLE) {
            fragment_order_cl_billing_info.visibility = View.INVISIBLE
            fragment_order_iv_show_hide_billing.setImageResource(R.drawable.ic_expand_more)
        } else {
            fragment_order_cl_billing_info.visibility = View.VISIBLE
            fragment_order_iv_show_hide_billing.setImageResource(R.drawable.ic_expand_less)
        }
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
            throw ClassCastException(context?.toString() + " must implement the necessary Interface")
        }
    }

    private fun setupGraphClient() {
        graphClient = GraphClient.builder(context!!)
                .shopDomain(ShopifyTokens.DOMAIN)
                .accessToken(ShopifyTokens.ACCESS_TOKEN)
                .httpCache(File(context?.cacheDir, "/http"), 10 * 1024 * 1024)
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST.expireAfter(5, TimeUnit.MINUTES))
                .build()
    }

    //todo finish this?
    private fun setupCardClient() {
        cardClient = CardClient()
    }
}
