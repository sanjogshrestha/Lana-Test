package com.sanjog.lanatest.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sanjog.lanatest.data.ProductRepository
import com.sanjog.lanatest.data.entities.ProductCheckoutEntity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Created by sanjogstha on 2019-12-19.
 * sanjogshrestha.nepal@gmail.com
 */
class CheckoutViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var allItemsData : LiveData<List<ProductCheckoutEntity>>
            = ProductRepository().allItemsInCheckout
    var totalAmountLiveData = MutableLiveData<Double>()

    init {
        compositeDisposable.add(ProductRepository().getTotalAmountInCheckout
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { count ->
                totalAmountLiveData.value = count
            })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}