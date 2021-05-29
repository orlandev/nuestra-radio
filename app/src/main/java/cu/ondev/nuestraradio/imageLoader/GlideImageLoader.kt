package com.inmersoft.trinidadpatrimonial.core.imageloader

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.inmersoft.trinidadpatrimonial.core.imageloader.ImageLoader

class GlideImageLoader(private val context: Context) : ImageLoader {

    override fun loadImage(imageUrl: String?, imageView: ImageView) {
        Glide.with(context)
            .load(imageUrl)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadImage(imageUrl: String?, imageView: ImageView, placeHolderResource: Int) {
        Glide.with(context)
            .load(imageUrl)
            .placeholder(placeHolderResource)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }

    override fun loadImage(
        imageUrl: String?,
        imageView: ImageView,
        placeHolderResource: Int,
        errorResource: Int
    ) {
        Glide.with(context)
            .load(imageUrl)
            .placeholder(placeHolderResource)
            .error(errorResource)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imageView)
    }
}