package br.com.alura.orgs_two.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs_two.R
import br.com.alura.orgs_two.database.AppDatabase
import br.com.alura.orgs_two.databinding.ActivityProductFormBinding
import br.com.alura.orgs_two.extensions.imageLoadAttempt
import br.com.alura.orgs_two.model.Product
import br.com.alura.orgs_two.ui.dialog.ImageDialogForm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal

class ProductFormActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityProductFormBinding.inflate(layoutInflater)
    }
    private val productDao by lazy {
        AppDatabase.createDbInstance(this).productDao()
    }

    private var url: String? = null
    private var productId = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        title = getString(R.string.add_product)
        setupSaveButton()
        binding.activityProductFormImage.setOnClickListener {
            ImageDialogForm(this)
                .show(url) { image ->
                    url = image
                    binding.activityProductFormImage.imageLoadAttempt(url)
                }
        }
        tryLoadProducts()
        trySearchProduct()
    }

    private fun trySearchProduct() {
        lifecycleScope.launch {
            productDao.searchById(productId).collect() {productFound ->
                productFound?.let {
                    title = getString(R.string.update_product)
                    fillFields(it)
                }
            }
        }
    }

    private fun tryLoadProducts() {
        productId = intent.getLongExtra(PRODUCT_KEY_ID, 0L)
    }

    private fun fillFields(products: Product) {
        url = products.image
        binding.activityProductFormImage
            .imageLoadAttempt(products.image)
        binding.activityProductFormTietName
            .setText(products.name)
        binding.activityProductFormTietDescription
            .setText(products.description)
        binding.activityProductFormTietValue.setText(products.value.toPlainString())
    }

    private fun setupSaveButton() {
        val buttonSave = binding.activityProductFormBtnSave
        buttonSave.setOnClickListener {
            val newProduct = createProduct()
            lifecycleScope.launch {
                productDao.save(newProduct)
                finish()
            }
        }
    }

    private fun createProduct(): Product {
        val fieldName = binding.activityProductFormTietName
        val name = fieldName.text.toString()
        val fieldDescription = binding.activityProductFormTietDescription
        val description = fieldDescription.text.toString()
        val fieldValue = binding.activityProductFormTietValue
        val textValue = fieldValue.text.toString()
        val value = if (textValue.isBlank()) {
            BigDecimal.ZERO
        } else {
            BigDecimal(textValue)
        }

        return Product(
            id = productId,
            name = name,
            description = description,
            value = value,
            image = url
        )
    }
}