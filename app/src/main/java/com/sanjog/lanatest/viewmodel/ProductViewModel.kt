package com.sanjog.lanatest.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanjog.lanatest.R
import com.sanjog.lanatest.data.ProductCheckoutRepository
import com.sanjog.lanatest.data.ProductRepository
import com.sanjog.lanatest.data.model.ProductDto
import com.sanjog.lanatest.data.model.ProductResultsDto
import com.sanjog.lanatest.data.model.SelectedProductDTO
import com.sanjog.lanatest.webservice.NetworkState
import com.sanjog.lanatest.webservice.RetrofitService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLHandshakeException


/**
 * Created by sanjogstha on 2019-12-16.
 * sanjogshrestha.nepal@gmail.com
 */
class ProductViewModel  @Inject constructor(private val retrofit: Retrofit): ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val productList = MutableLiveData<List<ProductDto>>()
    val networkState = MutableLiveData<NetworkState>()
    var selectedProduct = MutableLiveData<SelectedProductDTO>()
    var cartTotalCount = MutableLiveData<Int>()
    var productCheckoutRepository = ProductCheckoutRepository()
    var productRepository = ProductRepository()

    /**
     * This deletes all the previous details for checkout.
     * Reason : Every time the app launches, it is expected to always add new/fresh items */
    private fun deletePreviousCheckoutHistory() {
        viewModelScope.launch {
            productCheckoutRepository.deleteAllItemsInCheckout()
        }
    }

    /**
     * Concept :
     * If products are found in records, we display the items from the database (loaded initially from webservice).
     * else we fetch the list of items from the webservice only if the last update is greater than a day
     * @see handleApiError() for error handling
     * @see NetworkState for different states of the webservice call*/
    @SuppressLint("CheckResult")
    fun getProducts() {
        deletePreviousCheckoutHistory()
        networkState.value = NetworkState.LOADING
        compositeDisposable.add(productRepository.getAllProducts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
                productList.value = it
                if(it.isNotEmpty()) {
                    networkState.value = NetworkState.LOADED
                }
            })

        Thread {
            val hasExpired : Boolean = productRepository.productDao.hasExpired(FRESH_TIMEOUT,
                System.currentTimeMillis())
            val isProductEmpty : Boolean = productRepository.productDao.isEmpty() == 0
            if(hasExpired || isProductEmpty) {
                compositeDisposable.add(
                    retrofit.create(RetrofitService::class.java)
                        .getProducts()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe({ result: ProductResultsDto ->
                            viewModelScope.launch {
                                productRepository.insertProduct(result)
                            }
                            productList.value = result.products
                            networkState.value = NetworkState.LOADED
                        }, { t: Throwable? ->
                            networkState.value = NetworkState.LOADED
                            if (t != null) {
                                this.handleApiError(t)
                            } else {
                                networkState.value = NetworkState.error(R.string.exception_generic)
                            }
                        })
                )
            }
        }.start()
    }

    /**
     * This handles the api failure.
     * We assign the different scenario to the condition based on error instance
     * @param error Thrown by Retrofit*/
    private fun handleApiError(error: Throwable) {
        if (error is HttpException) {
            networkState.value = when (error.code()) {
                HttpsURLConnection.HTTP_INTERNAL_ERROR -> NetworkState.error(R.string.exception_internal_server_error)
                HttpsURLConnection.HTTP_BAD_REQUEST ->  NetworkState.error(R.string.exception_bad_request)
                else ->  NetworkState.error(R.string.exception_generic)
            }
            return
        }

        if (error is IOException || error is UnknownHostException
            || error is SSLHandshakeException || error is SocketTimeoutException) {
            networkState.value = NetworkState.error(R.string.exception_internet_connection)
            return
        }

        networkState.value = NetworkState.error(R.string.exception_generic)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    /**
     * This is triggered when user taps the "ADD TO CART" button in the list
     * @param item The selected product from the list
     * @param position The selected product position from the list
     *
     * The count value is assigned to 1 as this will be the first entry.
     * We then insert the product to the cart record*/
    fun onAddCounterItem(item: ProductDto, position: Int){
        item.count = 1
        viewModelScope.launch{
            productCheckoutRepository.insert(item)
        }
        selectedProduct.value = SelectedProductDTO(item, position)
        checkOrderCountInCart()
    }

    /**
     * This is triggered when user taps the add(+) button in the list
     * @param item The selected product from the list
     * @param position The selected product position from the list
     *
     * The count value is incremented by 1 and then update the product to the cart record*/
    fun onIncCounterItem(item: ProductDto, position: Int){
        item.count++
        updateDB(item, position)
    }

    /**
     * This is triggered when user taps the sub(-) button in the list
     * @param item The selected product from the list
     * @param position The selected product position from the list
     *
     * The count value is decremented by 1 and then update the product to the cart record
     * We delete the item from the record if the count is 0. This is done so that when we list
     * all the items from record, we don't want any items with order count as 0. So to prevent this,
     * we delete item from the record*/
    fun onDecCounterItem(item: ProductDto, position: Int) {
        item.count--
        if(item.count == 0){
            viewModelScope.launch{
                productCheckoutRepository.deleteProduct(item)
            }
        }
        updateDB(item, position)
    }

    private fun updateDB(item: ProductDto, position: Int){
        viewModelScope.launch{
            productCheckoutRepository.update(item)
        }
        selectedProduct.value = SelectedProductDTO(item, position)
        checkOrderCountInCart()
    }

    private fun checkOrderCountInCart(){
        compositeDisposable.add(productCheckoutRepository.getTotalOrdersInCheckout
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { count ->
                cartTotalCount.value = count
            })

        compositeDisposable.add(productCheckoutRepository.productDao.getCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { count ->
                if(count == 0) {
                    cartTotalCount.value = 0
                }
            })
    }

    companion object {
        val FRESH_TIMEOUT = TimeUnit.DAYS.toMillis(1)
    }
}