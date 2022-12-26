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
import com.demo.ezdine.adapter.OrderListAdapter
import com.demo.ezdine.common.AddFoodItemDialog
import com.demo.ezdine.common.ConfirmPaymentDialog
import com.demo.ezdine.common.ToGoConfirmDialog
import com.demo.ezdine.data.db.AppDatabase
import com.demo.ezdine.data.model.Food
import com.demo.ezdine.data.model.Order
import com.demo.ezdine.data.repository.FoodRepository
import com.demo.ezdine.data.repository.TransactionRepository
import com.demo.ezdine.databinding.FragmentSaleBinding
import com.demo.ezdine.viewmodel.LoginViewModel
import com.demo.ezdine.viewmodel.LoginViewModelFactory

class SaleFragment : Fragment() {
    private val TAG ="SaleFragment"

    private var _binding: FragmentSaleBinding? = null
    private lateinit var foodRepository: FoodRepository
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var saleViewModel: SaleViewModel
    private lateinit var saleViewModelFactory: SaleViewModelFactory
    private lateinit var foodItemListAdapter : FoodItemListAdapter
    private lateinit var finalPrice : String

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
        transactionRepository = TransactionRepository(database)
        saleViewModelFactory = SaleViewModelFactory(requireActivity().application, foodRepository,transactionRepository)
        saleViewModel = ViewModelProvider(requireActivity(), saleViewModelFactory)[SaleViewModel::class.java]

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        initClickListeners()
    }

    override fun onResume() {
        super.onResume()
        if(!(requireActivity() as MainActivity).user.type.contentEquals("Manager")){
            binding.layoutCartScreen.tvToGoBtn.visibility = View.GONE
        }
    }


    private fun initClickListeners(){


        binding.layoutCartScreen.ivCloseCartView.setOnClickListener {
            binding.motionLayout.transitionToState(R.id.start)
            saleViewModel.clearOrderItem()
        }

        binding.layoutCartScreen.tvPayBtn.setOnClickListener {
            var confirmPaymentDialog = ConfirmPaymentDialog(requireActivity(),finalPrice){
                binding.motionLayout.transitionToState(R.id.start)
                saleViewModel.insertTransaction(it)
                saleViewModel.clearOrderItem()
            }
            confirmPaymentDialog.show()
        }

        binding.layoutCartScreen.tvToGoBtn.setOnClickListener {
            var toGoConfirmDialog = ToGoConfirmDialog(requireActivity(),finalPrice){
                binding.motionLayout.transitionToState(R.id.start)
                saleViewModel.insertTransaction(it)
                saleViewModel.clearOrderItem()
            }
            toGoConfirmDialog.show()
        }
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
                        populateOrderList(orderItemList as ArrayList<Order>)
                        binding.motionLayout.transitionToState(R.id.end)
                    } else{
                        populateOrderList(arrayListOf<Order>() )
                        binding.motionLayout.transitionToState(R.id.start)
                    }
                })

                saleViewModel.subTotal.observe(viewLifecycleOwner, Observer { it ->
                    binding.layoutCartScreen.tvSubTotalValue.text = "$ "+it
                })

                saleViewModel.tax.observe(viewLifecycleOwner, Observer { it ->
                    binding.layoutCartScreen.tvTaxValue.text = "$ "+it
                })

                saleViewModel.total.observe(viewLifecycleOwner, Observer { it ->
                    binding.layoutCartScreen.tvTotalValue.text = "$ "+it
                    finalPrice = it
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
                    object : GridLayoutManager(requireActivity(), 3, RecyclerView.VERTICAL, false)
                    {
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

    private fun populateOrderList(orderList: ArrayList<Order>) {
        try {
            binding.layoutCartScreen.rvOrderItems.layoutManager =
                LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL, false)
            var orderListAdapter = OrderListAdapter(requireActivity(), orderList) {

            }
            binding.layoutCartScreen.rvOrderItems.adapter = orderListAdapter
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showAddFoodItemPopup(){

    }

}