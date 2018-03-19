package layout

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.R
import com.artem.myecommerce.adapter.CartItemsListAdapter
import com.artem.myecommerce.domain.CartItem
import kotlinx.android.synthetic.main.fragment_cart.view.*

class CartFragment : Fragment() {
    private lateinit var adapter: CartItemsListAdapter
    private var cartItemsList = ArrayList<CartItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_cart, null)

        //todo add way to populate cartItemsList

        adapter = CartItemsListAdapter(context!!, cartItemsList)
        view.fragment_cart_lv_items.adapter = adapter

        return view
    }
}