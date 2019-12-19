package com.sanjog.lanatest.data

import androidx.lifecycle.LiveData
import com.sanjog.lanatest.LanaApplication
import com.sanjog.lanatest.data.dao.CheckoutDao
import com.sanjog.lanatest.data.entities.ProductCheckoutEntity
import com.sanjog.lanatest.data.model.ProductDto
import io.reactivex.Observable

/**
 * Created by sanjogstha on 2019-12-18.
 * sanjogshrestha.nepal@gmail.com
 */
class ProductRepository {
    var productDao: CheckoutDao

    init {
        val database: AppDatabase = AppDatabase.getInstance(LanaApplication.getApplicationContext())
        productDao = database.productDao()
    }

    val allItemsInCheckout: LiveData<List<ProductCheckoutEntity>> = productDao.loadAllCheckout()

    val getTotalOrdersInCheckout : Observable<Int> = productDao.getTotalItemsInCart()

    val getTotalAmountInCheckout : Observable<Double> = productDao.getTotalAmountInCart()

    suspend fun deleteAllItemsInCheckout(){
        productDao.deleteAll()
    }

    suspend fun insert(item: ProductDto) {
        val productCheckoutEntity = getProductCheckoutEntity(item)
        productDao.insertProduct(productCheckoutEntity)
    }

    private fun getProductCheckoutEntity(item: ProductDto): ProductCheckoutEntity {
        val pair = checkConditionForTotal(item)
        return ProductCheckoutEntity(item.code, item.name, pair.first, item.count,
            item.count * item.price, pair.second)
    }

    private fun checkConditionForTotal(item: ProductDto): Pair<Double, Double> {
        var price = item.price
        val count = item.count
        val total: Double
        when (item.code) {
            "VOUCHER" -> {
                val remainder = count % 2
                total = if (remainder == 0) {
                    count / 2 * price
                } else {
                    (count / 2 * price) + remainder * price
                }
            }
            "TSHIRT" -> {
                if (count >= 3) {
                    price -= 1
                }
                total = count * price
            }
            else -> total = count * price

        }
        return Pair(price, total)
    }

    suspend fun update(item: ProductDto){
        val productCheckoutEntity = getProductCheckoutEntity(item)
        productDao.updatedCheckout(productCheckoutEntity)
    }

    suspend fun deleteProduct(item: ProductDto) {
        val productCheckoutEntity = getProductCheckoutEntity(item)
        productDao.deleteProduct(productCheckoutEntity)
    }
}