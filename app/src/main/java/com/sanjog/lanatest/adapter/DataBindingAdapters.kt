package com.sanjog.lanatest.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.sanjog.lanatest.R


/**
 * Created by sanjogstha on 2019-12-20.
 * sanjogshrestha.nepal@gmail.com
 */
@BindingAdapter("imageSrc")
fun setImageUri(view: ImageView, code: String) {
    val icVoucher: Int
    when (code) {
        "VOUCHER" -> {
            icVoucher = R.drawable.ic_voucher
        }
        "TSHIRT" -> {
            icVoucher = R.drawable.ic_shirt
        }
        else ->  {
            icVoucher = R.drawable.ic_mug
        }

    }
    view.setImageResource(icVoucher)
}
class DataBindingAdapters