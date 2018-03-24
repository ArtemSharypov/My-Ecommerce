package com.artem.myecommerce.domain

import android.os.Parcel
import android.os.Parcelable

class CartItem (var mainImageURL: String, var productName: String, var price: Long, var quantity: Int, var total: Long) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mainImageURL)
        parcel.writeString(productName)
        parcel.writeLong(price)
        parcel.writeInt(quantity)
        parcel.writeLong(total)
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