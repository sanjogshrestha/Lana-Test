package com.sanjog.lanatest.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.sanjog.lanatest.R
import com.sanjog.lanatest.adapter.ProductListAdapter
import com.sanjog.lanatest.adapter.ShopCartItemListener
import com.sanjog.lanatest.data.model.ProductDto
import com.sanjog.lanatest.databinding.FragmentListBinding
import com.sanjog.lanatest.viewmodel.ProductViewModel


/**
 * Created by sanjogstha on 2019-12-16.
 * sanjogshrestha.nepal@gmail.com
 */
class ListFragment : Fragment(), ShopCartItemListener<ProductDto> {
    private lateinit var viewModel : ProductViewModel
    private lateinit var adapter : ProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentListBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_list, container, false)
        viewModel = ViewModelProviders.of(this).get(ProductViewModel::class.java)

        setHasOptionsMenu(false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.clickListener = this
        binding.shopCartToolbar?.run {
           setUpToolbar(activity!!)
        }

        binding.checkoutImageView.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_listFragment_to_checkoutFragment)
        }

        adapter = ProductListAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter

        viewModel.productList.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.selectedProduct.observe(this, Observer {
            adapter.notifyItemChanged(it.position, it.productDTO)
        })

        viewModel.networkState.observe(this, Observer {
            if(it.msg != -1){
                binding.progressBar.visibility = View.GONE
                binding.emptyLayout.emptyTitle.text = getString(it.msg)
            }
        })

        return binding.root
    }

    private fun Toolbar.setUpToolbar(context : Activity) {
        (context as AppCompatActivity).setSupportActionBar(this)
        context.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }

    override fun onAddCounterItem(item: ProductDto, position: Int) {
        viewModel.onAddCounterItem(item, position)
    }

    override fun onIncCounterItem(item: ProductDto, position: Int) {
        viewModel.onIncCounterItem(item, position)
    }

    override fun onDecCounterItem(item: ProductDto, position: Int) {
        viewModel.onDecCounterItem(item, position)
    }
}