package com.sanjog.lanatest.data

import com.sanjog.lanatest.LanaApplication
import com.sanjog.lanatest.data.dao.ProductDao
import com.sanjog.lanatest.data.entities.ProductEntity
import com.sanjog.lanatest.data.model.ProductDto
import com.sanjog.lanatest.data.model.ProductResultsDto
import io.reactivex.Single
import java.util.*

/**
 * Created by sanjogstha on 2019-12-20.
 * sanjogshrestha.nepal@gmail.com
 */
class ProductRepository {
    var productDao: ProductDao

    init {
        val database: AppDatabase = AppDatabase.getInstance(LanaApplication.getApplicationContext())
        productDao = database.productDao()
    }

    private val loadAllProduct: Single<List<ProductEntity>> = productDao.loadAllProduct()

    fun getAllProducts(): Single<List<ProductDto>>{
        return loadAllProduct
            .map { products ->
                val list = ArrayList<ProductDto>()
                if (products.isNotEmpty()) {
                    for (productEntity in products) {
                        list.add(ProductDto(productEntity.code, productEntity.name,
                            productEntity.price, 0))
                    }
                }
                list
            }
    }

    suspend fun insertProduct(result: ProductResultsDto){
        val products: List<ProductDto>? = result.products
        if (products != null) {
            for(product in products){
                productDao.insert(ProductEntity(product.code, product.name, product.price,
                    System.currentTimeMillis()))
            }
        }
    }
}