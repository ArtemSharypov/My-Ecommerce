package layout

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.R
import com.artem.myecommerce.`interface`.ReplaceFragmentInterface
import com.artem.myecommerce.adapter.CartItemsListAdapter
import com.artem.myecommerce.domain.CartItem
import com.artem.myecommerce.fragment.OrderFragment
import com.artem.myecommerce.utility.CalculatorForTotals
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.view.*

class CartFragment : Fragment() {
    private var replaceFragmentCallback: ReplaceFragmentInterface? = null
    private lateinit var adapter: CartItemsListAdapter
    private var cartItemsList = ArrayList<CartItem>()
    private var calculator = CalculatorForTotals()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_cart, null)

        //todo add way to populate cartItemsList

        adapter = CartItemsListAdapter(context!!, cartItemsList) {
            calculateTotals()
        }
        view.fragment_cart_lv_items.adapter = adapter

        view.fragment_cart_btn_proceed_checkout.setOnClickListener {
            switchToCheckout()
        }

        calculateTotals()

        return view
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

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            replaceFragmentCallback = context as ReplaceFragmentInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString() + " must implement the necessary Interface")
        }
    }
}