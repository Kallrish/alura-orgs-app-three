package br.com.alura.orgs_two.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.alura.orgs_two.database.converter.Converters
import br.com.alura.orgs_two.database.dao.ProductDao
import br.com.alura.orgs_two.model.Product

@Database(entities = [Product::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {

        @Volatile
        private lateinit var db: AppDatabase

        fun createDbInstance(context: Context) : AppDatabase {
            if (::db.isInitialized) return db
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "orgs.db"
            ).build().also {
                    db = it
                }
        }

    }
}