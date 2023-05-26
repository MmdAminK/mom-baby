package com.app.mombaby.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.mombaby.databinding.PregnancyGuideItemBinding
import com.app.mombaby.ui.adapters.listeners.OnItemClickListener
import com.bumptech.glide.Glide

class PregnancyGuideAdapter(private val titles: List<String>, val context: Context) :
    RecyclerView.Adapter<PregnancyGuideAdapter.ViewHolder>() {

    var onItemClickListener: OnItemClickListener? = null
    lateinit var binding: PregnancyGuideItemBinding

    companion object {
        private val drawableMap: HashMap<Int, Int> = HashMap()
    }

    init {
        for (i in 0..39) {
            val name = if (i < 4) "week_4" else "week_${i + 1}"
            drawableMap[i] = context.resources.getIdentifier(
                name, "drawable", context.packageName
            )
        }
    }

    fun onItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding =
            PregnancyGuideItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position in 1..3) {
            val params = binding.mainCardLayout.layoutParams
            params.height = 0
            binding.mainCardLayout.layoutParams = params
        }

        holder.bind(titles[position], onItemClickListener!!)
    }

    override fun getItemCount(): Int {
        return titles.size
    }

    class ViewHolder(val binding: PregnancyGuideItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(title: String, onItemClickListener: OnItemClickListener) {
            binding.apply {
                pregnancyGuideTitle.text = title
                Glide.with(binding.root).load(
                    binding.root.context.resources.getDrawable(
                        drawableMap[adapterPosition]!!, null
                    )
                ).into(image)
                root.setOnClickListener { onItemClickListener.onItemClick(adapterPosition) }
                executePendingBindings()
            }
        }

    }
}
