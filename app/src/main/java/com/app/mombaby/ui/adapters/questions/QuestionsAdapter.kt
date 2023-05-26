package com.app.mombaby.ui.adapters.questions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.mombaby.databinding.QuestionItemBinding
import com.app.mombaby.models.adapterModels.Question
import com.app.mombaby.ui.adapters.listeners.OnItemClickListener

class QuestionsAdapter(val questions: ArrayList<Question>) :
    RecyclerView.Adapter<QuestionsAdapter.ViewHolder>() {

    lateinit var binding: QuestionItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = QuestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(questions)
    }

    override fun getItemCount(): Int {
        return questions.size
    }

    class ViewHolder(val binding: QuestionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(questions: ArrayList<Question>) {

            binding.question = questions[adapterPosition]

            val optionAdapter = OptionAdapter(questions[adapterPosition].optionList)
            binding.questionItemAnswerRecycler.apply {
                layoutManager =
                    LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
                adapter = optionAdapter
            }
            optionAdapter.onItemClickListener(object : OnItemClickListener {
                override fun onItemClick(position: Int) {
                    questions[adapterPosition].optionList.mapIndexed { index, option ->
                        option.isSelected = index == position
                    }
                    optionAdapter.notifyDataSetChanged()
                }
            })
            binding.executePendingBindings()
        }
    }

}
