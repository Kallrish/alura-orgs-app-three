package br.com.alura.orgs_two.database.dao

import androidx.room.*
import br.com.alura.orgs_two.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Query("SELECT * FROM product WHERE id = :id")
    fun searchById(id: Long) : Flow<Product?>

    @Query("SELECT * FROM product")
    fun searchAll() : Flow<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg product: Product)

    @Delete
    suspend fun remove(product: Product)

    @Query("SELECT * FROM product ORDER BY name DESC")
    suspend fun orderNameDesc() : List<Product>

    @Query("SELECT * FROM product ORDER BY name ASC")
    suspend fun orderNameAsc() : List<Product>

    @Query("SELECT * FROM product ORDER BY description DESC")
    suspend fun orderDescriptionDesc() : List<Product>

    @Query("SELECT * FROM product ORDER BY description ASC")
    suspend fun orderDescriptionAsc() : List<Product>

    @Query("SELECT * FROM product ORDER BY value DESC")
    suspend fun orderValueDesc() : List<Product>

    @Query("SELECT * FROM product ORDER BY value ASC")
    suspend fun orderValueAsc() : List<Product>
}