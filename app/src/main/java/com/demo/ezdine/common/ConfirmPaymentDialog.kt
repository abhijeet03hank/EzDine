package com.demo.ezdine.common

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import com.demo.ezdine.data.model.Transaction
import com.demo.ezdine.databinding.ConfirmPaymentDialogBinding


class ConfirmPaymentDialog(context: Context,private val totalPrice :String , val onAddClick:(Transaction)->Unit) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ConfirmPaymentDialogBinding.inflate(layoutInflater)

        setContentView(binding.root)
        window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent);
        setCancelable(true)
        var baseQuantity = 1



        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnConfirm.setOnClickListener {
            val time = System.currentTimeMillis()
            var tableNo = binding.tiedTableNo.text
            val transaction = Transaction(
                first_name = "",
                last_name = "",
                total_price = totalPrice,
                status = TransactionStatus.CONFIRMED.toString(),
                time_in_milis = time, table_no = tableNo.toString())
            onAddClick(transaction)
            dismiss()
        }
    }



}
