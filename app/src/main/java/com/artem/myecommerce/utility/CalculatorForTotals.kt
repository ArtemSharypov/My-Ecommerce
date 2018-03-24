package com.artem.myecommerce.utility

import com.artem.myecommerce.domain.CartItem

class CalculatorForTotals {
    private val PST_PERCENT = 8
    private val GST_PERCENT = 5

    fun calculatePST(subtotal: Long, shipping: Long) : Long {
        var pstAmount: Long = 0
        var totalAmount = subtotal + shipping

        if(totalAmount > 0) {
            pstAmount = totalAmount * PST_PERCENT
        }

        return pstAmount
    }

    fun calculatePST(subtotal: Long) : Long {
        var pstAmount: Long = 0
        var totalAmount = subtotal

        if(totalAmount > 0) {
            pstAmount = totalAmount * PST_PERCENT
        }

        return pstAmount
    }

    fun calculateGST(subtotal: Long, shipping: Long) : Long {
        var gstAmount: Long = 0
        var totalAmount = subtotal + shipping

        if(totalAmount > 0) {
            gstAmount = totalAmount * GST_PERCENT
        }

        return gstAmount
    }

    fun calculateGST(subtotal: Long) : Long {
        var gstAmount: Long = 0
        var totalAmount = subtotal

        if(totalAmount > 0) {
            gstAmount = totalAmount * GST_PERCENT
        }

        return gstAmount
    }

    fun calculateSubtotalCartItems(orderItemsList: ArrayList<CartItem>) : Long {
        var subtotal: Long = 0

        orderItemsList.forEach {
            subtotal += (it.price * it.quantity)
        }

        return subtotal
    }

    fun calculateSubtotal(price: Long, quantity: Int) : Long {
        var total: Long = 0

        if(quantity != null && quantity >= 0) {
            total = price * quantity
        }

        return total
    }

    fun calculateTotal(pstAmount: Long, gstAmount: Long, subtotal: Long, shipping: Long) : Long{
        var totalAmount = pstAmount + gstAmount + subtotal + shipping

        return totalAmount
    }

    fun calculateTotal(pstAmount: Long, gstAmount: Long, subtotal: Long) : Long{
        var totalAmount = pstAmount + gstAmount + subtotal

        return totalAmount
    }
}