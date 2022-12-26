package com.demo.ezdine.ui.sale

import android.app.Application
import android.icu.text.DecimalFormat
import android.util.Log
import androidx.lifecycle.*
import com.demo.ezdine.data.model.Food
import com.demo.ezdine.data.model.Order
import com.demo.ezdine.data.model.User
import com.demo.ezdine.data.repository.FoodRepository
import com.demo.ezdine.data.repository.TransactionRepository
import com.demo.ezdine.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaleViewModel(
    private val appContext: Application,
    private val foodRepository: FoodRepository,
    private val transactionRepository: TransactionRepository
) :   AndroidViewModel(appContext)  {
    private val TAG ="SaleViewModel"

    private val _text = MutableLiveData<String>().apply {
        value = "This is Sale Fragment"
    }
    val text: LiveData<String> = _text

    private var _foodTypeList : MutableLiveData<List<String>?> = MutableLiveData()
    val foodTypeList : LiveData<List<String>?> = _foodTypeList

    private var _foodItemList : MutableLiveData<List<Food>?> = MutableLiveData()
    val foodItemList : LiveData<List<Food>?> = _foodItemList

    private var _orderItemList : MutableLiveData<List<Order>?> = MutableLiveData()
    val orderItemList : LiveData<List<Order>?> = _orderItemList


    private var _tax : MutableLiveData<String> = MutableLiveData()
    val tax : LiveData<String> = _tax

    private var _subTotal : MutableLiveData<String> = MutableLiveData()
    val subTotal : LiveData<String> = _subTotal

    private var _total : MutableLiveData<String> = MutableLiveData()
    val total : LiveData<String> = _total

    fun fetchFoodTypeList(){
        viewModelScope.launch(Dispatchers.IO) {
            var foodTypeList = foodRepository.getFoodTypeList()
            _foodTypeList.postValue(foodTypeList)
        }
    }

    fun fetchFoodItemList(query : String? = null){
        viewModelScope.launch(Dispatchers.IO) {
            var foodItemList = if (query.isNullOrEmpty()){
                foodRepository.getFoodList()
            }else{
                val searchQuery = "%$query%"
                foodRepository.getFoodListWithQuery(searchQuery,searchQuery)
            }
            _foodItemList.postValue(foodItemList)
        }
    }


    fun updateOrderItem(order: Order){
        var currentOrderList = if(null!=_orderItemList?.value)_orderItemList?.value as ArrayList else null
        viewModelScope.launch(Dispatchers.IO) {
            if (currentOrderList.isNullOrEmpty()) {
                currentOrderList = arrayListOf<Order>()
                currentOrderList?.add(order)
            } else {
                var currentOrder = currentOrderList?.firstOrNull { od ->
                    od.food.id == order.food.id
                }
                if (currentOrder == null) {
                    currentOrderList?.add(order)
                } else {
                    currentOrderList?.let {
                        var ind = it.indexOf(currentOrder)
                        it[ind] = order
                    }
                }
            }
            updateOrderTotal(currentOrderList)
            _orderItemList.postValue(currentOrderList)
        }
    }

    private fun updateOrderTotal(orderList: ArrayList<Order>?){

        var subTotal : Double = 0.0
        var tax = 0.0
        var finalTotal = 0.0
        var ft = DecimalFormat("0.00");

        orderList?.let {
            try {
                for (orderItem in orderList) {
                    var originalPrice = orderItem.price.toDouble()
                    var total = originalPrice * orderItem.quantity
                    subTotal += total
                }
                tax = subTotal * (8.25 / 100)
                finalTotal = subTotal - tax

            } catch (e: Exception) {
                Log.d(TAG, e.toString())
            }
        }
        var subTotalStr = ft.format(subTotal)
        var taxStr = ft.format(tax)
        var finalTotalStr = ft.format(finalTotal)

        _tax.postValue(taxStr)
        _subTotal.postValue(subTotalStr)
        _total.postValue(finalTotalStr)

    }

    fun clearOrderItem(){
        viewModelScope.launch(Dispatchers.IO) {
            _orderItemList.postValue(arrayListOf<Order>())
            updateOrderTotal(arrayListOf<Order>())

        }
    }


}