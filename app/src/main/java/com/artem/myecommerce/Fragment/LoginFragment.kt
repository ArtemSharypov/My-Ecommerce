package com.artem.myecommerce

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.`interface`.ReplaceFragmentInterface
import com.artem.myecommerce.`interface`.SignUpAndLoginInterface
import kotlinx.android.synthetic.main.fragment_login.view.*

class LoginFragment : Fragment(){
    private var replaceFragmentCallback: ReplaceFragmentInterface? = null
    private var activityCallback: SignUpAndLoginInterface? = null

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
        //todo add logic for signing a user in

        activityCallback?.finishedLogin()
    }

    private fun switchToSignUp() {
        var signupFragment = SignUpFragment()
        replaceFragmentCallback?.replaceWithFragment(signupFragment)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            replaceFragmentCallback = context as ReplaceFragmentInterface
            activityCallback = context as SignUpAndLoginInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString() + " must implement the necessary Interface")
        }
    }
}