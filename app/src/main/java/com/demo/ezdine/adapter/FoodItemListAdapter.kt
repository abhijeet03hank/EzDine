package com.demo.ezdine.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.demo.ezdine.common.GlideApp
import com.demo.ezdine.common.MyGlideApp
import com.demo.ezdine.data.model.Food
import com.demo.ezdine.databinding.RowFoodItemListViewBinding

class FoodItemListAdapter(context: Context, foodItemList: ArrayList<Food>,private val onClicked : (Food) -> Unit): androidx.recyclerview.widget.RecyclerView.Adapter<FoodItemListAdapter.MyViewHolder>() {
    val TAG = "FoodItemListAdapter"
    private var foodItemList = ArrayList<Food>()
    private var context: Context

    init {
        this.foodItemList = foodItemList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewItem: Int): MyViewHolder {
        val binding = RowFoodItemListViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        try {
            val foodItem = foodItemList[position]
            foodItem.let {
                holder.bind(it)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
        }
    }

    inner class MyViewHolder(private val binding: RowFoodItemListViewBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root){
        fun bind(foodItem : Food){
            GlideApp.with(context)
                .load(foodItem.img_url)
//                .load("https://www.shutterstock.com/shutterstock/photos/1911762766/display_1500/stock-photo-mojito-or-virgin-mojito-long-rum-drink-with-fresh-mint-lime-juice-cane-sugar-and-soda-1911762766.jpg")
//                .load("https://s3.amazonaws.com/koya-dev-videos/kindness/8da807aa-1e1e-413d-bf9b-5bb084646593/medialibrary/9456621508/videos/1eb78337-d569-41bd-95ad-153d9098de03.png")
                .centerCrop()
                .into(binding.ivFoodItemImage)
            binding.tvFoodItemName.text = foodItem.name
            binding.tvFoodItemPrice.text = "$"+foodItem.price

            binding.root.setOnClickListener {
                onClicked(foodItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return foodItemList.size
    }

    fun updateFoodItemList(newFoodItemList : ArrayList<Food>){
        this.foodItemList = newFoodItemList
        notifyDataSetChanged()
    }
}
