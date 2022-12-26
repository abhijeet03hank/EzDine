package com.demo.ezdine.adapter

import android.content.Context
import android.icu.text.DecimalFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.demo.ezdine.common.GlideApp
import com.demo.ezdine.data.model.Order
import com.demo.ezdine.databinding.RowOrderListViewBinding

class OrderListAdapter(context: Context, orderItemList: ArrayList<Order>, private val onClicked : (Order) -> Unit): androidx.recyclerview.widget.RecyclerView.Adapter<OrderListAdapter.MyViewHolder>() {
    val TAG = "OrderListAdapter"
    private var orderItemList = ArrayList<Order>()
    private var context: Context

    init {
        this.orderItemList = orderItemList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewItem: Int): MyViewHolder {
        val binding = RowOrderListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            val orderItem = orderItemList[position]
            orderItem.let {
                holder.bind(it)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    inner class MyViewHolder(private val binding: RowOrderListViewBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root){
        fun bind(orderItem : Order){
            GlideApp.with(context)
//                .load(foodItem.img_url)
                .load("https://s3.amazonaws.com/koya-dev-videos/kindness/8da807aa-1e1e-413d-bf9b-5bb084646593/medialibrary/9456621508/videos/1eb78337-d569-41bd-95ad-153d9098de03.png")
                .centerCrop()
                .into(binding.ivAddItemImage)
            binding.tvOrderFoodName.text = orderItem.food.name
            binding.tvOrderFoodPrice.text = "$"+orderItem.price
            binding.tvTotalValue.text = getTotal(orderItem.price,orderItem.quantity)
            binding.tvAddItemQuantity.text = orderItem.quantity.toString()

            binding.root.setOnClickListener {
                onClicked(orderItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return orderItemList.size
    }

    fun updateFoodItemList(newOrderItemList : ArrayList<Order>){
        this.orderItemList = newOrderItemList
        notifyDataSetChanged()
    }

    private fun getTotal(price : String,quantity : Int) : String{
        var originalPrice = price.toDouble()
        var total = originalPrice * quantity

        var ft = DecimalFormat("0.00");
        var newTotal = ft.format(total)
        return newTotal
    }

}
