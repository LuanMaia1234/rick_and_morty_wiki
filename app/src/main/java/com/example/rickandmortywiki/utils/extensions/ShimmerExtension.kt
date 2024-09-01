package com.example.rickandmortywiki.utils.extensions

import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable

val Shimmer.AlphaHighlightBuilder.drawable: ShimmerDrawable
    get() {
        val shimmer = this
            .setDuration(1800)
            .setBaseAlpha(0.7f)
            .setHighlightAlpha(0.6f)
            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
            .setAutoStart(true)
            .build()
        return ShimmerDrawable().apply {
            setShimmer(shimmer)
        }
    }