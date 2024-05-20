package com.example.m12_mvvm

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.graphics.toColor
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.m12_mvvm.databinding.FragmentMainBinding


class MainFragment : Fragment() {
    companion object{
        fun newInstance() = MainFragment()
    }
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // условие проверки нажатия кнопки
        binding.editText.addTextChangedListener { it ->
            if (it.isNullOrEmpty() || it.length < 3)
                binding.buttonSearch.isEnabled = false
            else
                binding.buttonSearch.isEnabled = true


        }
        // активация поиска
        binding.buttonSearch.setOnClickListener {
            val searchString = binding.editText.text.toString()
            viewModel.onSignInClick(searchString)
        }
        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.state
                    .collect { state ->
                        when (state) {
                            State.Start -> {
                                binding.editText.isEnabled = true
                                binding.buttonSearch.isEnabled = false
                                binding.textView.text = getString(R.string.search_result)
                                binding.progress.isVisible = false

                            }

                            State.Loading -> {
                                binding.editText.isEnabled = false
                                binding.buttonSearch.isEnabled = false
                                binding.textView.text = getString(R.string.search_continue)
                                binding.progress.isVisible = true
                            }

                            State.Success -> {
                                binding.editText.isEnabled = true
                                binding.buttonSearch.isEnabled = true
                                binding.textView.text = ("По вашему запросу ${binding.editText.text.toString()} ничего не найдено")
                                binding.progress.isVisible = false
                            }

                        }
                    }
            }

    }

}