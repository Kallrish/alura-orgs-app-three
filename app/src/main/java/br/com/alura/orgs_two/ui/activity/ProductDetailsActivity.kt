package br.com.alura.orgs_two.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs_two.R
import br.com.alura.orgs_two.database.AppDatabase
import br.com.alura.orgs_two.databinding.ActivityProductDetailsBinding
import br.com.alura.orgs_two.extensions.formatBrazilCurrency
import br.com.alura.orgs_two.extensions.imageLoadAttempt
import br.com.alura.orgs_two.model.Product
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ProductDetailsActivity : AppCompatActivity() {

    private var productId: Long = 0L
    private var product: Product? = null
    private val binding by lazy {
        ActivityProductDetailsBinding.inflate(layoutInflater)
    }
    private val productDao by lazy {
        AppDatabase.createDbInstance(this).productDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        tryLoadingProduct()
        searchProducts()
    }

    private fun searchProducts() {
        lifecycleScope.launch {
            productDao.searchById(productId).collect() {productFound ->
                product = productFound
                product?.let {
                    fillFields(it)
                } ?: finish()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.products_details_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_products_details_edit -> {
                Intent(this, ProductFormActivity::class.java).apply {
                    putExtra(PRODUCT_KEY_ID, productId)
                    startActivity(this)
                }
            }
            R.id.menu_products_details_remove -> {
                lifecycleScope.launch {
                    product?.let {
                        productDao.remove(it)
                    }
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun tryLoadingProduct() {
        productId = intent.getLongExtra(PRODUCT_KEY_ID, 0L)
    }

    private fun fillFields(loadedProduct: Product) {
        with(binding) {
            activityProductDetailImage.imageLoadAttempt(loadedProduct.image)
            activityProductDetailTvName.text = loadedProduct.name
            activityProductDetailTvDescription.text = loadedProduct.description
            activityProductDetailTvValue.text =
                loadedProduct.value.formatBrazilCurrency()
        }
    }
}