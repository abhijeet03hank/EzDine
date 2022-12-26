package com.demo.ezdine.ui.sale

import android.app.Application
import androidx.lifecycle.*
import com.demo.ezdine.data.model.Food
import com.demo.ezdine.data.model.Order
import com.demo.ezdine.data.model.User
import com.demo.ezdine.data.repository.FoodRepository
import com.demo.ezdine.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SaleViewModel(
    private val appContext: Application,
    private val foodRepository: FoodRepository
) :   AndroidViewModel(appContext)  {

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
            _orderItemList.postValue(currentOrderList)
        }
    }


}