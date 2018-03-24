package com.artem.myecommerce.domain

import android.os.Parcel
import android.os.Parcelable

class ProductItem (var mainImageURL: String, var thumbnailImages: ArrayList<ThumbnailImageItem>, var productName: String, var price: Long,
                   var reviewRating: Long, var reviews: ArrayList<ReviewItem>, var description: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.createTypedArrayList(ThumbnailImageItem.CREATOR),
            parcel.readString(),
            parcel.readLong(),
            parcel.readLong(),
            parcel.createTypedArrayList(ReviewItem.CREATOR),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(mainImageURL)
        parcel.writeTypedList(thumbnailImages)
        parcel.writeString(productName)
        parcel.writeLong(price)
        parcel.writeLong(reviewRating)
        parcel.writeTypedList(reviews)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductItem> {
        override fun createFromParcel(parcel: Parcel): ProductItem {
            return ProductItem(parcel)
        }

        override fun newArray(size: Int): Array<ProductItem?> {
            return arrayOfNulls(size)
        }
    }

}