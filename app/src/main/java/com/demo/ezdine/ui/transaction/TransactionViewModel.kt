package com.demo.ezdine.ui.transaction

import android.app.Application
import androidx.lifecycle.*
import com.demo.ezdine.data.model.Order
import com.demo.ezdine.data.model.Transaction
import com.demo.ezdine.data.repository.FoodRepository
import com.demo.ezdine.data.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionViewModel (
    private val appContext: Application,
    private val transactionRepository: TransactionRepository
) :   AndroidViewModel(appContext)  {
    private val TAG ="TransactionViewModel"

    private var _transactionItemList : MutableLiveData<List<Transaction>?> = MutableLiveData()
    val transactionItemList : LiveData<List<Transaction>?> = _transactionItemList

    fun fetchTransactionList(){
        viewModelScope.launch(Dispatchers.IO) {
            var transactionList = transactionRepository.getTransactionList()
            _transactionItemList.postValue(transactionList)
        }
    }

}