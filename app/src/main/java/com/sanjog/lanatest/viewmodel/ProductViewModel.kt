package com.sanjog.lanatest.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sanjog.lanatest.R
import com.sanjog.lanatest.data.ProductRepository
import com.sanjog.lanatest.data.model.ProductDto
import com.sanjog.lanatest.data.model.ProductResultsDto
import com.sanjog.lanatest.data.model.SelectedProductDTO
import com.sanjog.lanatest.webservice.NetworkState
import com.sanjog.lanatest.webservice.RetrofitApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLHandshakeException


/**
 * Created by sanjogstha on 2019-12-16.
 * sanjogshrestha.nepal@gmail.com
 */
class ProductViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    val productList = MutableLiveData<ProductResultsDto>()
    val networkState = MutableLiveData<NetworkState>()
    var selectedProduct = MutableLiveData<SelectedProductDTO>()
    var cartTotalCount = MutableLiveData<Int>()
    var productRepository = ProductRepository()

    init {
        deletePreviousCheckoutHistory()
        getProducts()
    }

    /**
     * This deletes all the previous details for checkout.
     * Reason : Every time the app launches, it is expected to always add new/fresh items */
    private fun deletePreviousCheckoutHistory() {
        viewModelScope.launch {
            productRepository.deleteAllItemsInCheckout()
        }
    }

    /**
     * This fetches the list of items from the Webservice
     * @see handleApiError() for error handling
     * @see NetworkState for different states of the webservice call*/
    fun getProducts() {
        networkState.value = NetworkState.LOADING
        compositeDisposable.add(
            RetrofitApi.service.getProducts()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({ result: ProductResultsDto ->
                productList.value = result
                networkState.value = NetworkState.LOADED
            }, { t: Throwable? ->
                networkState.value = NetworkState.LOADED
                if (t != null) {
                    this.handleApiError(t)
                }else {
                    networkState.value = NetworkState.error(R.string.exception_generic)
                }
            }))
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
            productRepository.insert(item)
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
                productRepository.deleteProduct(item)
            }
        }
        updateDB(item, position)
    }

    private fun updateDB(item: ProductDto, position: Int){
        viewModelScope.launch{
            productRepository.update(item)
        }
        selectedProduct.value = SelectedProductDTO(item, position)
        checkOrderCountInCart()
    }

    private fun checkOrderCountInCart(){
        compositeDisposable.add(productRepository.getTotalOrdersInCheckout
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { count ->
                cartTotalCount.value = count
            })

        compositeDisposable.add(productRepository.productDao.getCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { count ->
                if(count == 0) {
                    cartTotalCount.value = 0
                }
            })
    }
}