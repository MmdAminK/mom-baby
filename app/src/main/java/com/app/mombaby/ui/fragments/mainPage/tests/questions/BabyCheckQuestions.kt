package com.app.mombaby.ui.fragments.mainPage.tests.questions

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.mombaby.R
import com.app.mombaby.data.PregnancyTestData
import com.app.mombaby.databinding.BabyCheckMainBinding
import com.app.mombaby.databinding.BabyCheckQuestionsBinding
import com.app.mombaby.databinding.BabyCheckResultBinding
import com.app.mombaby.ui.adapters.listeners.OnItemClickListener
import com.app.mombaby.ui.adapters.questions.OptionAdapter
import com.app.mombaby.utils.animations.AnimUtils.animatedVisibility
import com.app.mombaby.utils.utilities.ViewUtils.makeSnackBar

class BabyCheckQuestions : Fragment() {

    private lateinit var babyCheckQuestionsBinding: BabyCheckQuestionsBinding
    private lateinit var babyCheckResultsBinding: BabyCheckResultBinding
    private lateinit var binding: BabyCheckMainBinding
    lateinit var optionAdapter: OptionAdapter
    private val babyCheckOptionList = PregnancyTestData.babyCheckOptions
    private var answer = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.baby_check_main, container, false)
        babyCheckQuestionsBinding = binding.babyCheckQuestionsLayout
        babyCheckResultsBinding = binding.babyCheckResultLayout

        layoutVisibility(true)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        answer = -1
        babyCheckOptionList.forEach {
            it.isSelected = false
        }
        if (this::optionAdapter.isInitialized)
            optionAdapter.notifyDataSetChanged()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        optionAdapter = OptionAdapter(babyCheckOptionList)
        babyCheckQuestionsBinding.apply {
            with(babyCheckOptions) {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                optionAdapter.onItemClickListener(object : OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        babyCheckOptionList.mapIndexed { index, option ->
                            option.isSelected = index == position
                        }
                        answer = position
                        optionAdapter.notifyDataSetChanged()
                    }
                })
                adapter = optionAdapter
            }
            babyCheckButton.setOnClickListener {
                when (answer) {
                    0 -> "تبریک میگم، شما باردار هستید"
                    1 -> "جواب منفی است، شما باردار نیستید"
                    2 -> " احتمال بارداری وجود دارد، تست باید تکرار شود"
                    3 -> "نتیجه نامعتبر، تست باید تکرار شود"
                    else -> null
                }?.let { res ->
                    layoutVisibility(false)
                    babyCheckResultsBinding.babyCheckResultText.text = res
                } ?: run {
                    binding.root.makeSnackBar("لطفا گزینه مورد نظر خود را انتخاب کنید")
                }
            }
        }

        babyCheckResultsBinding.babyCheckResultTestAgain.setOnClickListener {
            layoutVisibility(true)
            babyCheckOptionList.map { it.isSelected = false }
            optionAdapter.notifyDataSetChanged()
            answer = -1
        }


    }

    private fun layoutVisibility(questionLayoutVisibility: Boolean) {
        binding.babyCheckQuestionsLayout.root.animatedVisibility({
            binding.babyCheckQuestionsLayout.root.visibility =
                if (questionLayoutVisibility) View.VISIBLE else View.GONE
        })
        binding.babyCheckResultLayout.root.animatedVisibility({
            binding.babyCheckResultLayout.root.visibility =
                if (questionLayoutVisibility) View.GONE else View.VISIBLE
        })
    }
}