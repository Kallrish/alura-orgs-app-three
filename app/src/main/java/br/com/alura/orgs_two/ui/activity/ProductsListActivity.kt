package br.com.alura.orgs_two.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.alura.orgs_two.R
import br.com.alura.orgs_two.database.AppDatabase
import br.com.alura.orgs_two.databinding.ActivityProductsListBinding
import br.com.alura.orgs_two.model.Product
import br.com.alura.orgs_two.ui.recyclerview.adapter.AdapterProductList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductsListActivity : AppCompatActivity() {

    private val adapter = AdapterProductList(context = this)
    private val binding by lazy {
        ActivityProductsListBinding.inflate(layoutInflater)
    }
    private val productDao by lazy {
        AppDatabase.createDbInstance(this).productDao()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()
        setupFab()
        lifecycleScope.launch {
            productDao.searchAll().collect() { products ->
                adapter.update(products)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.product_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.submenu_sort_name_desc -> {
                lifecycleScope.launch {
                    adapter.update(productDao.orderNameDesc())
                }
            }
            R.id.submenu_sort_name_asc -> {
                lifecycleScope.launch {
                    adapter.update(productDao.orderNameAsc())
                }
            }
            R.id.submenu_sort_description_desc -> {
                lifecycleScope.launch {
                    adapter.update(productDao.orderDescriptionDesc())
                }
            }
            R.id.submenu_sort_description_asc -> {
                lifecycleScope.launch {
                    adapter.update(productDao.orderDescriptionAsc())
                }
            }
            R.id.submenu_sort_value_desc -> {
                lifecycleScope.launch {
                    adapter.update(productDao.orderValueDesc())
                }
            }
            R.id.submenu_sort_value_asc -> {
                lifecycleScope.launch {
                    adapter.update(productDao.orderValueAsc())
                }
            }
            R.id.submenu_sort_no_sort -> {
                lifecycleScope.launch {
                    productDao.searchAll().collect() { products ->
                        adapter.update(products)
                    }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupFab() {
        val fabAddProduct = binding.activityProductListEfabAddProduct
        fabAddProduct.setOnClickListener {
            goToProductForm()
        }
    }

    private fun goToProductForm() {
        val intent = Intent(this, ProductFormActivity::class.java)
        startActivity(intent)
    }

    private fun setupRecyclerView() {
        val rvProductList = binding.activityProductListProductList
        rvProductList.adapter = adapter
        adapter.itemClick = {
            val intent = Intent(
                this,
                ProductDetailsActivity::class.java
            ).apply {
                putExtra(PRODUCT_KEY_ID, it.id)
            }
            startActivity(intent)
        }
        adapter.itemEditClick = {
            Intent(this, ProductFormActivity::class.java).apply {
                putExtra(PRODUCT_KEY_ID, it.id)
                startActivity(this)
            }
        }
        adapter.itemRemoveClick = {
            lifecycleScope.launch {
                productDao.remove(it)
                productDao.searchAll().collect() { products ->
                    adapter.update(products)
                }
            }
        }
    }

}