package br.com.alura.orgs_two.extensions

import android.widget.ImageView
import br.com.alura.orgs_two.R
import coil.load

fun ImageView.imageLoadAttempt(
    url: String? = null,
    fallback: Int = R.drawable.default_image
) {
    load(url) {
        fallback(fallback)
        error(R.drawable.error)
        placeholder(R.drawable.loading_image)
    }
}