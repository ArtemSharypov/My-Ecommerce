package com.artem.myecommerce

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artem.myecommerce.`interface`.SignUpAndLoginInterface
import kotlinx.android.synthetic.main.fragment_sign_up.view.*

class SignUpFragment : Fragment(){
    private var activityCallback: SignUpAndLoginInterface? = null

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

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            activityCallback = context as SignUpAndLoginInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString() + " must implement ReplaceFragmentInterface")
        }
    }

    private fun signUp() {
        //todo add logic for signing up a user, on success use the activityCallback below

        activityCallback?.finishedSignUp()
    }

    private fun switchToSignIn() {
        activity?.onBackPressed()
    }
}