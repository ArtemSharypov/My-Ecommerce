package com.artem.myecommerce.domain

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class ReviewItem (var reviewersName: String, var rating: Long, var date: Date, var reviewDescription: String, var reviewTitle: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readLong(),
            parcel.readSerializable() as Date,
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(reviewersName)
        parcel.writeLong(rating)
        parcel.writeSerializable(date)
        parcel.writeString(reviewDescription)
        parcel.writeString(reviewTitle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ReviewItem> {
        override fun createFromParcel(parcel: Parcel): ReviewItem {
            return ReviewItem(parcel)
        }

        override fun newArray(size: Int): Array<ReviewItem?> {
            return arrayOfNulls(size)
        }
    }

}