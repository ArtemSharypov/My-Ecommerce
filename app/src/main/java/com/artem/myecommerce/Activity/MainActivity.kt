package com.artem.myecommerce.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import com.artem.myecommerce.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        activity_main_cet_search.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
                if(keyEvent?.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    searchForItem()

                    return true
                }

                return false
            }
        })

        activity_main_iv_search.setOnClickListener {
            searchForItem()
        }

        activity_main_btn_sign_in_and_out.setOnClickListener {
            handleSignInOrOut()
        }

        activity_main_iv_user_acc.setOnClickListener {
            switchToUserAccount()
        }

        activity_main_iv_cart.setOnClickListener {
            switchToCart()
        }
    }

    private fun switchToCart() {

    }

    private fun handleSignInOrOut() {

    }

    private fun switchToUserAccount() {

    }

    private fun searchForItem() {

    }
}
