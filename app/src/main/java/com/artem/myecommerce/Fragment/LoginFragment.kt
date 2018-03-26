package com.artem.myecommerce

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.artem.myecommerce.`interface`.ReplaceFragmentInterface
import com.artem.myecommerce.`interface`.SignUpAndLoginInterface
import com.shopify.buy3.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.view.*
import java.io.File
import java.util.concurrent.TimeUnit

class LoginFragment : Fragment(){
    private var replaceFragmentCallback: ReplaceFragmentInterface? = null
    private var activityCallback: SignUpAndLoginInterface? = null
    private lateinit var graphClient: GraphClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_login, null)

        view.fragment_login_btn_sign_in.setOnClickListener {
            login()
        }

        view.fragment_login_tv_no_account.setOnClickListener {
            switchToSignUp()
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

    private fun login() {
        var email = fragment_login_et_email.text.toString()
        var password = fragment_login_et_password.text.toString()
        var input = Storefront.CustomerAccessTokenCreateInput(email, password)

        var mutationQuery = Storefront.mutation({ mutation -> mutation
                .customerAccessTokenCreate(input, { query -> query
                        .customerAccessToken({ customerAccessToken -> customerAccessToken
                                    .accessToken()
                                    .expiresAt()
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
                if(response.data()?.customerAccessTokenCreate?.userErrors?.isEmpty()!!) {
                    val responseConstant = response
                    var accessToken = responseConstant.data()?.customerAccessTokenCreate?.customerAccessToken?.accessToken
                    activityCallback?.finishedLogin(accessToken!!)
                } else {
                    var toastText = "Failed to login"
                    Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(error: GraphError) {
                var toastText = "Failed to login, $error"
                Toast.makeText(context, toastText, Toast.LENGTH_LONG).show()
            }
        })
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