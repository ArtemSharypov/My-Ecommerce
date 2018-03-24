package com.artem.myecommerce.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.KeyEvent
import android.view.View
import com.artem.myecommerce.*
import com.artem.myecommerce.`interface`.ReplaceFragmentInterface
import com.artem.myecommerce.`interface`.SignUpAndLoginInterface
import kotlinx.android.synthetic.main.activity_main.*
import layout.CartFragment

class MainActivity : AppCompatActivity(), ReplaceFragmentInterface, SignUpAndLoginInterface {
    private var signedIn = false

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

        var homeFragment = HomeFragment()

        supportFragmentManager.beginTransaction()
                .add(R.id.activity_main_fl_fragment_holder, homeFragment)
                .commit()
    }

    override fun replaceWithFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_fl_fragment_holder, fragment)
                .commit()
    }

    //Removes the last 2 fragments
    override fun finishedSignUp() {
        signedIn = true
        updateSignInAndOutButton()

        //todo find a better way to handle the signup navigation back
        onBackPressed()
        onBackPressed()
    }

    override fun finishedLogin() {
        signedIn = true
        updateSignInAndOutButton()

        //todo find a better way to handle the login navigation back
        onBackPressed()
    }

    //Changes the Sign In / Sign Out Button to the updated text, depending on if a user is signed in or out
    private fun updateSignInAndOutButton() {
        if(signedIn) {
            activity_main_btn_sign_in_and_out.text = resources.getString(R.string.sign_out)
        } else {
            activity_main_btn_sign_in_and_out.text = resources.getString(R.string.sign_in)
        }
    }

    private fun switchToCart() {
        var cartFragment = CartFragment()
        replaceWithFragment(cartFragment)
    }

    private fun handleSignInOrOut() {
        if(signedIn) {
            //handle logic for signing out

            signedIn = false
            updateSignInAndOutButton()
        } else {
            var loginFragment = LoginFragment()
            replaceWithFragment(loginFragment)
        }
    }

    private fun switchToUserAccount() {
        var userInfoFragment = UserInfoFragment()
        replaceWithFragment(userInfoFragment)
    }

    private fun searchForItem() {
        var searchResultsFragment = SearchResultsFragment.newInstance(activity_main_cet_search.text.toString())
        replaceWithFragment(searchResultsFragment)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0)
        {
            supportFragmentManager.popBackStack()
        }
        else
        {
            super.onBackPressed()
        }
    }
}
