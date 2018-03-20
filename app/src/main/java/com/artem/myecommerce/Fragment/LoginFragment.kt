package com.artem.myecommerce

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_login, null)

        view.fragment_login_btn_sign_in.setOnClickListener {
            login()
        }

        view.fragment_login_tv_no_account.setOnClickListener {
            switchToSignUp()
        }

        return view
    }

    private fun login() {

    }

    private fun switchToSignUp() {

    }
}