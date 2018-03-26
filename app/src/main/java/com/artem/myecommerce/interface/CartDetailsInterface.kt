package com.artem.myecommerce.`interface`

import com.shopify.buy3.Storefront
import com.shopify.graphql.support.ID

interface CartDetailsInterface {
    fun setCartId(id: ID)
    fun getCartId(): ID?
    fun setCheckout(checkout: Storefront.Checkout?)
    fun getCheckout(): Storefront.Checkout?
}