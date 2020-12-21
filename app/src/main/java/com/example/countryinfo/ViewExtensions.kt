package com.example.countryinfo

import android.content.Context
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.net.toUri
import com.github.twocoffeesoneteam.glidetovectoryou.GlideApp

fun ImageView.loadSvgImage(countryUri: Uri) {
    val requestBuilder = GlideApp.with(this)
        .`as`(PictureDrawable::class.java)
        .listener(SvgSoftwareLayerSetter())
    requestBuilder.load(countryUri).into(this)
}