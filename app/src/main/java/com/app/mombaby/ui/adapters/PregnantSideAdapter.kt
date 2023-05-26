package com.app.mombaby.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.mombaby.databinding.PregnantSideItemBinding
import com.app.mombaby.ui.adapters.listeners.OnItemClickListener

class PregnantSideAdapter() :
    RecyclerView.Adapter<PregnantSideAdapter.ViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null
    lateinit var binding: PregnantSideItemBinding

    fun onItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            PregnantSideItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return 36
    }

    class ViewHolder(val binding: PregnantSideItemBinding) : RecyclerView.ViewHolder(binding.root) {
    }
}
