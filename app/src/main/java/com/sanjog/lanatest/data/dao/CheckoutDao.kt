package com.sanjog.lanatest.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sanjog.lanatest.data.entities.ProductCheckoutEntity
import io.reactivex.Observable

/**
 * Created by sanjogstha on 2019-12-17.
 * sanjogshrestha.nepal@gmail.com
 */
@Dao
interface CheckoutDao {
    @Query("SELECT * FROM product_checkout")
    fun loadAllCheckout(): LiveData<List<ProductCheckoutEntity>>

    @Query("SELECT * FROM product_checkout where code = :productCode")
    fun getProduct(productCode: String): Observable<ProductCheckoutEntity>

    @Update
    suspend fun updatedCheckout(productCheckoutEntity: ProductCheckoutEntity) : Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(productCheckoutEntity: ProductCheckoutEntity) : Long

    @Delete
    suspend fun deleteProduct(product: ProductCheckoutEntity): Int

    @Query("DELETE FROM product_checkout")
    suspend fun deleteAll()

    @Query("SELECT COUNT(code) FROM product_checkout")
    fun getCount(): Observable<Int>

    @Query("SELECT SUM(count) FROM product_checkout")
    fun getTotalItemsInCart() : Observable<Int>

    @Query("SELECT SUM(total) FROM product_checkout")
    fun getTotalAmountInCart() : Observable<Double>
}