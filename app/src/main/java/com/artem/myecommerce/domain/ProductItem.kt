package com.artem.myecommerce.domain

class ProductItem (var mainImageURL: String, var thumbnailImages: ArrayList<ThumbnailImageItem>, var productName: String, var price: Long,
                   var reviewRating: Long, var reviews: ArrayList<ReviewItem>, var description: String){
}