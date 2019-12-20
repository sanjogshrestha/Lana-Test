package com.sanjog.lanatest.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sanjog.lanatest.R
import com.sanjog.lanatest.data.entities.ProductCheckoutEntity
import com.sanjog.lanatest.databinding.LayoutCheckoutItemBinding

/**
 * Created by sanjogstha on 2019-12-19.
 * sanjogshrestha.nepal@gmail.com
 */
class CheckOutListAdapter() : ListAdapter<ProductCheckoutEntity,
        RecyclerView.ViewHolder>(CheckoutProductDiffCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(
            LayoutCheckoutItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = getItem(position)
        (holder as ProductViewHolder).bind(product)
    }

    class ProductViewHolder(
        private val binding: LayoutCheckoutItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ProductCheckoutEntity
        ) {
            binding.apply {
                product = item
                when (item.code) {
                    "VOUCHER" -> {
                        if(item.count > 1) {
                            shopCartItemExpectedTotal.visibility = View.VISIBLE
                            shopCartItemOffer.visibility = View.VISIBLE
                            shopCartItemOffer.text = binding.root.context.getString(R.string.offer_voucher)
                        } else {
                            shopCartItemOffer.visibility = View.GONE
                            shopCartItemExpectedTotal.visibility = View.GONE
                        }
                    }
                    "TSHIRT" -> {
                        if(item.count > 2) {
                            shopCartItemOffer.visibility = View.VISIBLE
                            shopCartItemOffer.text = binding.root.context.getString(R.string.offer_tshirt)
                            shopCartItemExpectedTotal.visibility = View.VISIBLE
                        } else {
                            shopCartItemExpectedTotal.visibility = View.GONE
                            shopCartItemOffer.visibility = View.GONE
                        }
                    }
                    else ->  {
                        shopCartItemExpectedTotal.visibility = View.GONE
                        shopCartItemOffer.visibility = View.GONE
                    }

                }
                shopCartItemExpectedTotal.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                executePendingBindings()
            }
        }
    }
}

class CheckoutProductDiffCallBack : DiffUtil.ItemCallback<ProductCheckoutEntity>() {
    override fun areItemsTheSame(oldItem: ProductCheckoutEntity,
                                 newItem: ProductCheckoutEntity): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: ProductCheckoutEntity,
                                    newItem: ProductCheckoutEntity): Boolean {
        return oldItem == newItem
    }
}

