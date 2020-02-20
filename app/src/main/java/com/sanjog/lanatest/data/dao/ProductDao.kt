package com.sanjog.lanatest.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sanjog.lanatest.data.entities.ProductEntity
import io.reactivex.Single
import androidx.lifecycle.LiveData



/**
 * Created by sanjogstha on 2019-12-20.
 * sanjogshrestha.nepal@gmail.com
 */
@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun loadAllProduct(): Single<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: ProductEntity) : Long

    @Query("SELECT COUNT(*) FROM product WHERE  :currentTime - lastUpdate >= :timeout")
    fun hasExpired(timeout : Long, currentTime : Long): Boolean

    @Query("SELECT count(*) FROM product")
    fun isEmpty(): Int
}
