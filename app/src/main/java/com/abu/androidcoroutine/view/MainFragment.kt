package com.abu.androidcoroutine.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer

import com.abu.androidcoroutine.R
import com.abu.androidcoroutine.databinding.MainFragmentBinding
import com.abu.androidcoroutine.viewmodel.MainViewModel

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            viewModel.uiText.observe(it, Observer { log ->
                binding.tvDisplay.text = binding.tvDisplay.text.toString() + "$log\n"
            })
        }
        init()
    }

    private fun init() {
        binding.btn1.setOnClickListener{ viewModel.run() }
        binding.btn2.setOnClickListener { viewModel.runRetry(retryTime = 3) }
        binding.btn3.setOnClickListener { viewModel.runRetryWithTimeout(retryTime = 3, timeout = 2, costTime = 4) }
        binding.btn4.setOnClickListener { viewModel.runMultiJobs() }
    }

    override fun onResume() {
        super.onResume()
        binding.tvDisplay.text = ""
    }
}
