package com.demo.ezdine.ui.sale

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Query
import com.demo.ezdine.R
import com.demo.ezdine.activity.MainActivity
import com.demo.ezdine.adapter.FoodItemListAdapter
import com.demo.ezdine.adapter.FoodTypeListAdapter
import com.demo.ezdine.common.AddFoodItemDialog
import com.demo.ezdine.data.db.AppDatabase
import com.demo.ezdine.data.model.Food
import com.demo.ezdine.data.repository.FoodRepository
import com.demo.ezdine.databinding.FragmentSaleBinding
import com.demo.ezdine.viewmodel.LoginViewModel
import com.demo.ezdine.viewmodel.LoginViewModelFactory

class SaleFragment : Fragment() {
    private val TAG ="SaleFragment"

    private var _binding: FragmentSaleBinding? = null
    private lateinit var foodRepository: FoodRepository
    private lateinit var saleViewModel: SaleViewModel
    private lateinit var saleViewModelFactory: SaleViewModelFactory
    private lateinit var foodItemListAdapter : FoodItemListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSaleBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val database = AppDatabase.getDatabase(requireActivity())
        foodRepository = FoodRepository(database)
        saleViewModelFactory = SaleViewModelFactory(requireActivity().application, foodRepository)
        saleViewModel = ViewModelProvider(requireActivity(), saleViewModelFactory)[SaleViewModel::class.java]

        binding.btn1.setOnClickListener {
                binding.motionLayout.transitionToState(R.id.start)
        }

        binding.btn2.setOnClickListener {
            binding.motionLayout.transitionToState(R.id.end)
        }
        return root
    }


    private fun initObservers() {
        try {
            if (::saleViewModel.isInitialized) {
                saleViewModel.fetchFoodTypeList()
                saleViewModel.fetchFoodItemList()
                saleViewModel.foodTypeList.observe(viewLifecycleOwner, Observer { foodTypeList ->
                    if (!foodTypeList.isNullOrEmpty()) {
                        populateFoodCategory(foodTypeList as ArrayList<String>)
                    }
                })

                saleViewModel.foodItemList.observe(viewLifecycleOwner, Observer { foodItemList ->
                    if (!foodItemList.isNullOrEmpty()) {
                        populateFoodItems(foodItemList as ArrayList<Food>)
                        binding.tvNoRecordsFound.visibility = View.GONE
                    } else{
                        populateFoodItems(arrayListOf<Food>() )
                        binding.tvNoRecordsFound.visibility = View.VISIBLE
                    }
                })

                saleViewModel.orderItemList.observe(viewLifecycleOwner, Observer { orderItemList ->
                    if (!orderItemList.isNullOrEmpty()) {
                        binding.motionLayout.transitionToState(R.id.end)
//                        populateFoodItems(foodItemList as ArrayList<Food>)
                    } else{
//                        populateFoodItems(arrayListOf<Food>() )
                        binding.motionLayout.transitionToState(R.id.start)
                    }
                })
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    private fun populateFoodCategory(foodTypeList: ArrayList<String>) {
        try {
                binding.rvFoodType.layoutManager =
                    LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
                var foodTypeListAdapter = FoodTypeListAdapter(requireActivity(), foodTypeList) { foodType ->
                    saleViewModel.fetchFoodItemList(foodType as String?)
                }
            binding.rvFoodType.adapter = foodTypeListAdapter
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    private fun populateFoodItems(foodItemList: ArrayList<Food>) {
        try {
            if(::foodItemListAdapter.isInitialized) {
                foodItemListAdapter.updateFoodItemList(foodItemList)
            }else {
                binding.rvFoodItems.layoutManager =
                    object : GridLayoutManager(requireActivity(), 3, RecyclerView.VERTICAL, false) {
                        override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                            // force width of viewHolder to be a fraction of RecyclerViews
                            // this will override layout_width from xml
                            lp.width = width / spanCount
                            return true
                        }
                    }
                 foodItemListAdapter = FoodItemListAdapter(requireActivity(), foodItemList){ it ->
                     var addFoodItemDialog = AddFoodItemDialog(requireActivity(),it){ order ->
                         saleViewModel.updateOrderItem(order)
                     }
                     addFoodItemDialog.show()
                 }
                binding.rvFoodItems.adapter = foodItemListAdapter
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showAddFoodItemPopup(){

    }

}