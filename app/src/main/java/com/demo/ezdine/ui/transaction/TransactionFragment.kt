package com.demo.ezdine.ui.transaction

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.ezdine.adapter.FoodTypeListAdapter
import com.demo.ezdine.adapter.TransactionListAdapter
import com.demo.ezdine.data.db.AppDatabase
import com.demo.ezdine.data.model.Transaction
import com.demo.ezdine.data.repository.TransactionRepository
import com.demo.ezdine.databinding.FragmentOrderBinding
import com.demo.ezdine.ui.sale.SaleViewModel
import com.demo.ezdine.ui.sale.SaleViewModelFactory

class TransactionFragment : Fragment() {
    private val TAG ="TransactionFragment"

    private var _binding: FragmentOrderBinding? = null
    lateinit var transactionRepository: TransactionRepository
    lateinit var transactionViewModel: TransactionViewModel
    lateinit var transactionViewModelFactory: TransactionViewModelFactory

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val database = AppDatabase.getDatabase(requireActivity())
        transactionRepository = TransactionRepository(database)
        transactionViewModelFactory = TransactionViewModelFactory(requireActivity().application,transactionRepository)
        transactionViewModel = ViewModelProvider(requireActivity(), transactionViewModelFactory)[TransactionViewModel::class.java]
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    private fun initObservers() {
        try {
            if (::transactionViewModel.isInitialized) {
                transactionViewModel.fetchTransactionList()
                transactionViewModel.transactionItemList.observe(viewLifecycleOwner, Observer { transactionList ->
                    if (!transactionList.isNullOrEmpty()) {
                        populateTransaction(transactionList as ArrayList<Transaction>)
                    }
                })
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    private fun populateTransaction(transactionList: ArrayList<Transaction>) {
        try {
            binding.rvTransactionView.layoutManager = GridLayoutManager (requireActivity(),4, RecyclerView.VERTICAL, false)
            var transactionListAdapter = TransactionListAdapter(requireActivity(), transactionList)
            binding.rvTransactionView.adapter = transactionListAdapter
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}