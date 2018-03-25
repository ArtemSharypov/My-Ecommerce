package com.artem.myecommerce.utility

import com.artem.myecommerce.domain.CartItem

class CalculatorForTotals {
    private val PST_PERCENT = 8
    private val GST_PERCENT = 5

    fun calculatePST(subtotal: Double, shipping: Double) : Double {
        var pstAmount = 0.0
        var totalAmount = subtotal + shipping

        if(totalAmount > 0) {
            pstAmount = totalAmount * PST_PERCENT
        }

        return pstAmount
    }

    fun calculatePST(subtotal: Double) : Double {
        var pstAmount = 0.0
        var totalAmount = subtotal

        if(totalAmount > 0) {
            pstAmount = totalAmount * PST_PERCENT
        }

        return pstAmount
    }

    fun calculateGST(subtotal: Double, shipping: Double) : Double {
        var gstAmount = 0.0
        var totalAmount = subtotal + shipping

        if(totalAmount > 0) {
            gstAmount = totalAmount * GST_PERCENT
        }

        return gstAmount
    }

    fun calculateGST(subtotal: Double) : Double {
        var gstAmount= 0.0
        var totalAmount = subtotal

        if(totalAmount > 0) {
            gstAmount = totalAmount * GST_PERCENT
        }

        return gstAmount
    }

    fun calculateSubtotalCartItems(orderItemsList: ArrayList<CartItem>) : Double {
        var subtotal = 0.0

        orderItemsList.forEach {
            subtotal += (it.productItem.price * it.quantity)
        }

        return subtotal
    }

    fun calculateSubtotal(price: Double, quantity: Int) : Double {
        var total = 0.0

        if(quantity != null && quantity >= 0) {
            total = price * quantity
        }

        return total
    }

    fun calculateTotal(pstAmount: Double, gstAmount: Double, subtotal: Double, shipping: Double) : Double{
        var totalAmount = pstAmount + gstAmount + subtotal + shipping

        return totalAmount
    }

    fun calculateTotal(pstAmount: Double, gstAmount: Double, subtotal: Double) : Double{
        var totalAmount = pstAmount + gstAmount + subtotal

        return totalAmount
    }
}