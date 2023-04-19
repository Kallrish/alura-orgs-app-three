package br.com.alura.orgs_two.ui.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import br.com.alura.orgs_two.R
import br.com.alura.orgs_two.databinding.ProductItemBinding
import br.com.alura.orgs_two.extensions.formatBrazilCurrency
import br.com.alura.orgs_two.extensions.imageLoadAttempt
import br.com.alura.orgs_two.model.Product

class AdapterProductList(
    private val context: Context,
    products: List<Product> = emptyList(),
    var itemClick: (product: Product) -> Unit = {},
    var itemEditClick: (product: Product) -> Unit = {},
    var itemRemoveClick: (product: Product) -> Unit = {}

) : RecyclerView.Adapter<AdapterProductList.ViewHolder>() {

    private val products = products.toMutableList()

    inner class ViewHolder(private val binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root), PopupMenu.OnMenuItemClickListener {

        private val name = binding.productItemName
        private val description = binding.productItemDescription
        private val value = binding.productItemValue
        private val image = binding.productItemImage

        private lateinit var product: Product

        init {
            itemView.setOnClickListener {
                if (::product.isInitialized) {
                    itemClick(product)
                }
            }
            itemView.setOnLongClickListener {
                if (::product.isInitialized) {
                    showPopUp(it)
                }
                true
            }
        }

        fun showPopUp(view: View?) {
            PopupMenu(context, view).apply {
                inflate(R.menu.products_details_menu)
                setOnMenuItemClickListener(this@ViewHolder)
                show()
            }
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.menu_products_details_edit -> {
                    itemEditClick(product)
                    true
                }
                R.id.menu_products_details_remove -> {
                    itemRemoveClick(product)
                    true
                }
                else -> false
            }
        }

        fun link(product: Product) {
            this.product = product
            name.text = product.name
            description.text = product.description
            val formattedValue = product.value.formatBrazilCurrency()
            value.text = formattedValue

            val visibility = if (product.image != null) {
                View.VISIBLE
            } else {
                View.GONE
            }
            image.visibility = visibility

            image.imageLoadAttempt(product.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding = ProductItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.link(product)
    }

    override fun getItemCount(): Int = products.size

    fun update(products: List<Product>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged()
    }
}
