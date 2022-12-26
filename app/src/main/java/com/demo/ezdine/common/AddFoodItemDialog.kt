package com.demo.ezdine.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.DecimalFormat
import android.os.Bundle
import android.view.ViewGroup
import com.demo.ezdine.data.model.Food
import com.demo.ezdine.data.model.Order
import com.demo.ezdine.databinding.AddItemToCartDialogBinding

class AddFoodItemDialog(context: Context,val  food: Food,val onAddClick:(Order)->Unit) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = AddItemToCartDialogBinding.inflate(layoutInflater)

        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        setCancelable(true)
        var baseQuantity = 1

        binding.tvAddItemName.text = food.name
        binding.tvAddItemPrice.text = "$"+food.price

            GlideApp.with(context)
//                .load(food.img_url)
                .load("https://s3.amazonaws.com/koya-dev-videos/kindness/8da807aa-1e1e-413d-bf9b-5bb084646593/medialibrary/9456621508/videos/1eb78337-d569-41bd-95ad-153d9098de03.png")
                .centerCrop()
                .into(binding.ivAddItemImage)


        binding.ivPlusQuantity.setOnClickListener {
            baseQuantity++
                val total = getTotal(baseQuantity)
                binding.tvTotalValue.text = "$"+total.toString()
                binding.tvAddItemQuantity.text = baseQuantity.toString()
        }

        binding.ivMinusQuantity.setOnClickListener {
            if(baseQuantity>1) {
                baseQuantity--
                val total = getTotal(baseQuantity)
                binding.tvTotalValue.text = "$ "+total
                binding.tvAddItemQuantity.text = baseQuantity.toString()
            }
        }

        binding.tvAddItemCancel.setOnClickListener {
            dismiss()
        }

        binding.tvAddItemSubmit.setOnClickListener {
            val order = Order(food = food, quantity = baseQuantity, price = food.price)
            onAddClick(order)
            dismiss()
        }
    }

    private fun getTotal(quantity : Int) : String{
        var originalPrice = food.price.toDouble()
        var total = originalPrice * quantity

        var ft = DecimalFormat("0.00");
        var newTotal = ft.format(total)
        return newTotal
    }

}
