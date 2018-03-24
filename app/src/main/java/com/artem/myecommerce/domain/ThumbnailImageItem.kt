package com.artem.myecommerce.domain

import android.os.Parcel
import android.os.Parcelable

class ThumbnailImageItem (var imageURL: String) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imageURL)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ThumbnailImageItem> {
        override fun createFromParcel(parcel: Parcel): ThumbnailImageItem {
            return ThumbnailImageItem(parcel)
        }

        override fun newArray(size: Int): Array<ThumbnailImageItem?> {
            return arrayOfNulls(size)
        }
    }
}