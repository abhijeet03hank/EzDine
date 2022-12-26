package com.demo.ezdine.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.demo.ezdine.R
import com.demo.ezdine.common.Constants
import com.demo.ezdine.data.model.Food
import com.demo.ezdine.databinding.RowFoodTypeListViewBinding

class FoodTypeListAdapter(context: Context, foodTypeList: ArrayList<String>,private val onClicked : (String) -> Unit): androidx.recyclerview.widget.RecyclerView.Adapter<FoodTypeListAdapter.MyViewHolder>() {
    val TAG = "FoodTypeListAdapter"
    private var foodTypeList = ArrayList<String>()
    private var context: Context

    init {
        this.foodTypeList = foodTypeList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowFoodTypeListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            val foodType = foodTypeList[position]
            foodType.let {
                holder.bind(it)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    inner class MyViewHolder(private val binding: RowFoodTypeListViewBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root){
        fun bind(foodType : String){
            getFoodTypeText(foodType)?.let { binding.tvFoodType.text = it }
            getFoodTypeDrawable(foodType)?.let { binding.ivFoodType.setBackgroundResource(it) }
            binding.root.setOnClickListener {
                onClicked(foodType)
            }
        }
    }

    override fun getItemCount(): Int {
        return foodTypeList.size
    }

    fun getFoodTypeDrawable(foodType : String): Int? = when (foodType) {
        Constants.BURGER -> R.drawable.ic_menu_burger
        Constants.COLD_DRINKS -> R.drawable.ic_menu_cold_drinks
        Constants.HOT_DRINKS -> R.drawable.ic_menu_hot_coffee
        Constants.CHIPS -> R.drawable.ic_menu_chips
        Constants.PANCAKE -> R.drawable.ic_menu_pancake
        Constants.ICE_CREAM -> R.drawable.ic_menu_icecream
        else -> null
    }

    fun getFoodTypeText(foodType : String): String? = when (foodType) {
        Constants.BURGER -> "Burger"
        Constants.COLD_DRINKS -> "Cold drinks"
        Constants.HOT_DRINKS -> "Hot drinks"
        Constants.CHIPS -> "Chips"
        Constants.PANCAKE -> "Pancake"
        Constants.ICE_CREAM -> "Ice-cream"
        else -> null
    }
}
