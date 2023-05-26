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
import com.app.mombaby.databinding.BloodTestMainBinding
import com.app.mombaby.databinding.BloodTestQuestionsBinding
import com.app.mombaby.databinding.BloodTestResultBinding
import com.app.mombaby.ui.adapters.listeners.OnItemClickListener
import com.app.mombaby.ui.adapters.questions.OptionAdapter
import com.app.mombaby.utils.animations.AnimUtils.animatedVisibility
import com.app.mombaby.utils.utilities.ViewUtils.makeSnackBar

class BloodTestQuestions : Fragment() {

    private lateinit var binding: BloodTestMainBinding

    private lateinit var bloodTestQuestionsBinding: BloodTestQuestionsBinding
    private lateinit var bloodTestResultBinding: BloodTestResultBinding

    lateinit var optionAdapter: OptionAdapter
    private val bloodTestOptions = PregnancyTestData.bloodTestOptions

    private var answer = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.blood_test_main, container, false)
        bloodTestQuestionsBinding = binding.bloodQuestionsLayout
        bloodTestResultBinding = binding.bloodResultLayout
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        answer = -1
        bloodTestOptions.forEach {
            it.isSelected = false
        }
        if (this::optionAdapter.isInitialized)
            optionAdapter.notifyDataSetChanged()

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        optionAdapter = OptionAdapter(bloodTestOptions)
        bloodTestQuestionsBinding.bloodTestOptions.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        optionAdapter.onItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                bloodTestOptions.mapIndexed { index, option ->
                    option.isSelected = index == position
                }
                answer = position
                optionAdapter.notifyDataSetChanged()
            }
        })
        bloodTestQuestionsBinding.bloodTestOptions.adapter = optionAdapter

        bloodTestQuestionsBinding.bloodTestButton.setOnClickListener {
            when (answer) {
                0 -> "جواب منفی است. شما باردار نیستید"
                1 -> "مشکوک . مجدد باید تست انجام شود"
                2 -> "تبریک میگم. شما باردار هستید"
                else -> null
            }?.let { res ->
                layoutVisibility(false)
                bloodTestResultBinding.bloodResultText.text = res
            } ?: run {
                binding.root.makeSnackBar("لطفا گزینه مورد نظر خود را انتخاب کنید")
            }
        }

        bloodTestResultBinding.bloodResultTestAgain.setOnClickListener {
            layoutVisibility(true)
            bloodTestOptions.map { it.isSelected = false }
            optionAdapter.notifyDataSetChanged()
            answer = -1
        }

    }

    private fun layoutVisibility(questionLayoutVisibility: Boolean) {
        binding.bloodQuestionsLayout.root.animatedVisibility({
            binding.bloodQuestionsLayout.root.visibility =
                if (questionLayoutVisibility) View.VISIBLE else View.GONE
        })
        binding.bloodResultLayout.root.animatedVisibility({
            binding.bloodResultLayout.root.visibility =
                if (questionLayoutVisibility) View.GONE else View.VISIBLE
        })
    }
}