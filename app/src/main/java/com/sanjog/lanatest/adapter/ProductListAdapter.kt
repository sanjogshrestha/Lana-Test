package com.sanjog.lanatest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sanjog.lanatest.data.model.ProductDto
import com.sanjog.lanatest.databinding.LayoutProductItemBinding

/**
 * Created by sanjogstha on 2019-12-16.
 * sanjogshrestha.nepal@gmail.com
 */
class ProductListAdapter(var listener: ShopCartItemListener<ProductDto>)
    : ListAdapter<ProductDto, RecyclerView.ViewHolder>(ProductDiffCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ProductViewHolder(LayoutProductItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val product = getItem(position)
        (holder as ProductViewHolder).bind(product, listener, position)
    }

    class ProductViewHolder(
        private val binding: LayoutProductItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: ProductDto,
            listener: ShopCartItemListener<ProductDto>,
            itemPos: Int
        ) {
            binding.apply {
                product = item
                clickListener = listener
                itemPosition = itemPos
                executePendingBindings()
            }
        }
    }
}

class ProductDiffCallBack : DiffUtil.ItemCallback<ProductDto>() {
    override fun areItemsTheSame(oldItem: ProductDto, newItem: ProductDto): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: ProductDto, newItem: ProductDto): Boolean {
        return oldItem == newItem
    }
}

