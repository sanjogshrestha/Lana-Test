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
import androidx.recyclerview.widget.LinearLayoutManager
import com.sanjog.lanatest.R
import com.sanjog.lanatest.adapter.CheckOutListAdapter
import com.sanjog.lanatest.databinding.FragmentCheckoutBinding
import com.sanjog.lanatest.viewmodel.CheckoutViewModel

/**
 * Created by sanjogstha on 2019-12-16.
 * sanjogshrestha.nepal@gmail.com
 */
class CheckoutFragment : Fragment() {
    private lateinit var adapter : CheckOutListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding : FragmentCheckoutBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_checkout, container, false)
        val checkoutViewModel = ViewModelProviders.of(this)
            .get(CheckoutViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = checkoutViewModel
        binding.shopCartToolbar?.run {
            setUpToolbar(activity!!)
        }
        adapter = CheckOutListAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter

        checkoutViewModel.allItemsData.observe(this, Observer {
            adapter.submitList(it)
        })
        return binding.root
    }

    private fun Toolbar.setUpToolbar(context : Activity) {
        (context as AppCompatActivity).setSupportActionBar(this)
        context.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}