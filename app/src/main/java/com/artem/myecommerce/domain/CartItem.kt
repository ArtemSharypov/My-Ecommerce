package com.artem.myecommerce.domain

import android.os.Parcel
import android.os.Parcelable

class CartItem (var productItem: ProductItem, var quantity: Int, var cartItemId: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readParcelable(ProductItem::class.java.classLoader),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(productItem, flags)
        parcel.writeInt(quantity)
        parcel.writeString(cartItemId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CartItem> {
        override fun createFromParcel(parcel: Parcel): CartItem {
            return CartItem(parcel)
        }

        override fun newArray(size: Int): Array<CartItem?> {
            return arrayOfNulls(size)
        }
    }

}