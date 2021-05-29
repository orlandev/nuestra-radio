package com.inmersoft.trinidadpatrimonial.core.imageloader

import android.widget.ImageView

interface ImageLoader {
    fun loadImage(imageUrl: String?, imageView: ImageView)
    fun loadImage(imageUrl: String?, imageView: ImageView, placeHolderResource: Int)
    fun loadImage(
        imageUrl: String?,
        imageView: ImageView,
        placeHolderResource: Int,
        errorResource: Int
    )

}