package com.sanjog.lanatest.webservice

/**
 * Created by sanjogstha on 2019-12-12.
 * sanjogshrestha.nepal@gmail.com
 */
enum class Status {
    RUNNING,
    SUCCESS,
    FAILED
}

data class NetworkState(
    val status: Status,
    val msg: Int = -1) {
    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)
        fun error(msg: Int) = NetworkState(Status.FAILED, msg)
    }
}