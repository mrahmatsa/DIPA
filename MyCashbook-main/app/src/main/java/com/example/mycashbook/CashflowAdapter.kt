package com.example.mycashbook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mycashbook.CashflowAdapter.*
import com.example.mycashbook.database.CashflowModel
import com.example.mycashbook.databinding.ItemCashflowBinding


class CashflowAdapter(cashflow: List<CashflowModel>) : RecyclerView.Adapter<CashflowAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemCashflowBinding) : RecyclerView.ViewHolder(binding.root) {
        private var binding : ItemCashflowBinding = binding

        fun bind(cashflow : CashflowModel){
            var status : String? = null
            if(cashflow.masukKeluar == "masuk"){
                status = "[+]"
                binding.masukkeluarItem.setBackgroundResource(R.drawable.increase)
            }
            else{
                status = "[-]"
                binding.masukkeluarItem.setBackgroundResource(R.drawable.decrease)
            }
            binding.nominalItem.text = status+" Rp. "+cashflow.nominal.toString()
            binding.keteranganItem.text = cashflow.keterangan
            binding.tanggalItem.text = cashflow.tanggal
            binding.executePendingBindings()
        }
    }

    private var mCashflow: List<CashflowModel> = cashflow

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context: Context = parent.getContext()
        val inflater = LayoutInflater.from(context)
        val binding = ItemCashflowBinding.inflate(inflater,parent,false)

        // Inflate the custom layout
        val cashflowView: View =
            inflater.inflate(R.layout.item_cashflow, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cashflow: CashflowModel = mCashflow.get(position)
            holder.bind(cashflow)
    }

    override fun getItemCount(): Int {
        return mCashflow.size
    }
}