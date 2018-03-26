package com.artem.myecommerce

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.artem.myecommerce.`interface`.SignUpAndLoginInterface
import com.shopify.buy3.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*
import java.io.File
import java.util.concurrent.TimeUnit

class SignUpFragment : Fragment(){
    private var activityCallback: SignUpAndLoginInterface? = null
    private lateinit var graphClient: GraphClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_sign_up, null)

        view.fragment_sign_up_btn_sign_up.setOnClickListener {
            signUp()
        }

        view.fragment_sign_up_tv_have_account.setOnClickListener {
            switchToSignIn()
        }

        setupGraphClient()

        return view
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
            activityCallback = context as SignUpAndLoginInterface
        } catch (e: ClassCastException) {
            throw ClassCastException(context?.toString() + " must implement ReplaceFragmentInterface")
        }
    }

    private fun signUp() {
        var email = fragment_sign_up_et_email.text.toString()
        var password = fragment_sign_up_et_password.text.toString()
        var name = fragment_sign_up_et_name.text.toString()
        var splitName = name.split(" ")
        var firstName = ""
        var lastName = ""

        if(splitName.isNotEmpty()) {
            firstName = splitName[0]
            if(splitName.size > 1) {
                lastName = splitName[1]
            }
        }

        var input = Storefront.CustomerCreateInput(email, password)
                .setFirstName(firstName)
                .setLastName(lastName)

        var mutationQuery = Storefront.mutation({ mutation -> mutation
                .customerCreate(input, { query -> query
                        .customer({ customer -> customer
                                .id()
                                .email()
                                .firstName()
                                .lastName()
                        })
                        .userErrors({ userError -> userError
                                .field()
                                .message()
                        })
                })
        })

        var call = graphClient.mutateGraph(mutationQuery)
        call.enqueue(object : GraphCall.Callback<Storefront.Mutation> {
            override fun onResponse(response: GraphResponse<Storefront.Mutation>) {
                if(response.data()?.customerCreate?.userErrors?.isEmpty()!!) {
                    //todo add something to say that the signup was a success
                    activityCallback?.finishedSignUp()
                } else {
                    var toastText = "Failed to create an account"
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(error: GraphError) {
                var toastText = "Failed to create an account, $error"
                Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun switchToSignIn() {
        activity?.onBackPressed()
    }
}