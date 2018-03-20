package com.artem.myecommerce

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

class SignUpFragment : Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_sign_up, null)

        view.fragment_sign_up_btn_sign_up.setOnClickListener {
            signUp()
        }

        view.fragment_sign_up_tv_have_account.setOnClickListener {
            switchToSignIn()
        }

        return view
    }

    private fun signUp() {

    }

    private fun switchToSignIn() {

    }
}