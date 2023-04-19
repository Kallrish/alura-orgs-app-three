package br.com.alura.orgs_two.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import br.com.alura.orgs_two.databinding.ImageFormBinding
import br.com.alura.orgs_two.extensions.imageLoadAttempt

class ImageDialogForm(private val context: Context) {

    fun show(
        defaultUrl: String? = null,
        whenImageLoaded: (url: String) -> Unit
    ) {
        ImageFormBinding
            .inflate(LayoutInflater.from(context)).apply {
                defaultUrl?.let {
                    imageFormIvChooseImage.imageLoadAttempt(it)
                    imageFormTietUrl.setText(it)
                }

                imageFormBtnLoadImage.setOnClickListener {
                    val url = imageFormTietUrl.text.toString()
                    imageFormIvChooseImage.imageLoadAttempt(url)
                }
                AlertDialog.Builder(context)
                    .setView(root)
                    .setPositiveButton("Confirma") { _, _ ->
                        val url = imageFormTietUrl.text.toString()
                        whenImageLoaded(url)
                    }
                    .setNegativeButton("Cancela") { _, _ ->

                    }
                    .show()
            }


    }
}