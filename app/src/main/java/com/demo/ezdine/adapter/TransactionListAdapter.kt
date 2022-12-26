package com.demo.ezdine.adapter

import android.content.Context
import android.icu.text.DecimalFormat
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.demo.ezdine.data.model.Transaction
import com.demo.ezdine.databinding.RowTransactionListViewBinding
import java.util.Calendar

class TransactionListAdapter(context: Context, transactionItemList: ArrayList<Transaction>): androidx.recyclerview.widget.RecyclerView.Adapter<TransactionListAdapter.MyViewHolder>() {
    val TAG = "TransactionListAdapter"
    private var transactionItemList = ArrayList<Transaction>()
    private var context: Context

    init {
        this.transactionItemList = transactionItemList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewItem: Int): MyViewHolder {
        val binding = RowTransactionListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            val transactionItem = transactionItemList[position]
            transactionItem.let {
                holder.bind(it)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    inner class MyViewHolder(private val binding: RowTransactionListViewBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root){
        fun bind(transactionItem : Transaction){

            binding.tvTransactionID.text = transactionItem.id.toString().padStart(4,'0')
            binding.tvTransactionPrice.text = "$"+transactionItem.total_price
            if(!transactionItem.first_name.isNullOrEmpty()) {
                binding.tvTransactionCustomerName.text =
                    transactionItem.first_name + " " + transactionItem.last_name
            }
            var fm = SimpleDateFormat("HH:mm, dd MMM")
            var cal = Calendar.getInstance()
            transactionItem.time_in_milis?.let{
                cal.timeInMillis = it
            }
            binding.tvTransactionTime.text =  fm.format(cal.time)
            if(!transactionItem.table_no.isNullOrEmpty()) {
                binding.tvTransactionTableNumber.text = transactionItem.table_no
            }
        }
    }

    override fun getItemCount(): Int {
        return transactionItemList.size
    }

    private fun getTotal(price : String,quantity : Int) : String{
        var originalPrice = price.toDouble()
        var total = originalPrice * quantity

        var ft = DecimalFormat("0.00");
        var newTotal = ft.format(total)
        return newTotal
    }

}

