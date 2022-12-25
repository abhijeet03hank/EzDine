package com.demo.ezdine.ui.sale

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.demo.ezdine.R
import com.demo.ezdine.databinding.FragmentSaleBinding

class SaleFragment : Fragment() {

    private var _binding: FragmentSaleBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(SaleViewModel::class.java)

        _binding = FragmentSaleBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        binding.btn1.setOnClickListener {
                binding.motionLayout.transitionToState(R.id.start)
        }

        binding.btn2.setOnClickListener {
            binding.motionLayout.transitionToState(R.id.end)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}