package com.sanjog.lanatest.adapter

/**
 * Created by sanjogstha on 2019-12-16.
 * sanjogshrestha.nepal@gmail.com
 */
interface ShopCartItemListener<T> {
    /**
     * This is triggered when user taps the "ADD TO CART" button in the list
     * @param item The selected product from the list
     * @param position The selected product position from the list
     *
     * @see ProductListAdapter*/
    fun onAddCounterItem(item: T, position: Int)
    /**
     * This is triggered when user taps the add(+) button in the list
     * @param item The selected product from the list
     * @param position The selected product position from the list
     * @see ProductListAdapter*/
    fun onIncCounterItem(item: T, position: Int)

    /**
     * This is triggered when user taps the sub(-) button in the list
     * @param item The selected product from the list
     * @param position The selected product position from the list
     * @see ProductListAdapter*/
    fun onDecCounterItem(item: T, position: Int)
}