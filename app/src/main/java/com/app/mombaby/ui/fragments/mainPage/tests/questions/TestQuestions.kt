package com.app.mombaby.ui.fragments.mainPage.tests.questions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.app.mombaby.R
import com.app.mombaby.databinding.TestQuestionsBinding
import com.app.mombaby.models.adapterModels.Question
import com.app.mombaby.repositories.tests.TestRepositoryImpl
import com.app.mombaby.ui.adapters.questions.QuestionsAdapter
import com.app.mombaby.utils.utilities.FaNum
import com.app.mombaby.utils.utilities.ViewUtils.makeSnackBar
import com.app.mombaby.utils.utilities.ViewUtils.setCurrentItemSmoothly
import com.app.mombaby.utils.utilities.ViewUtils.setLayoutOffset


class TestQuestions : Fragment() {
    lateinit var binding: TestQuestionsBinding
    lateinit var questionsAdapter: QuestionsAdapter
    private lateinit var testRepository: TestRepositoryImpl

    private var questionsSize: Int = 0
    var questions = ArrayList<Question>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testRepository = requireArguments().get("repository") as TestRepositoryImpl
        questions = testRepository.questions()
        questionsSize = testRepository.questionsSize()
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.test_questions, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.testQuestionsActionBar.setTitle(testRepository.name()!!)
        binding.testQuestionsActionBar.onBackClickListener { findNavController().popBackStack() }

        questionsAdapter = QuestionsAdapter(questions)
        binding.apply {
            initNextPreviousButton()

            testQuestionsViewPager.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int, positionOffset: Float, positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                    testQuestionsNumber.text =
                        String().plus("سوال ").plus(position + 1).plus(" از ").plus(questionsSize)
                    buttonVisibilityByPosition(position)
                }
            })
            testQuestionsNumber.text =
                testQuestionsViewPager.currentItem.toString()

            with(testQuestionsViewPager) {
                adapter = questionsAdapter
                setLayoutOffset()
            }
        }

        binding.testQuestionsResultButton.setOnClickListener {
            val notAnsweredQuestions = checkNotAnsweredQuestions()
            val questionsNumber = notAnsweredQuestions.joinToString(" ، ")
            if (notAnsweredQuestions.size != 0)
                binding.root.makeSnackBar(FaNum.convert("شما به سوال " + questionsNumber + "پاسخ نداده اید"))
            else {
                val bundle =
                    bundleOf("result" to testRepository.result(), "title" to testRepository.name())
                findNavController().navigate(
                    R.id.action_depressionTestQuestions_to_testResult,
                    bundle
                )
                for (question in questions)
                    for (option in question.optionList)
                        option.isSelected = false
                questionsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun checkNotAnsweredQuestions(): ArrayList<Int> {
        val notAnsweredQuestions = ArrayList<Int>()
        testRepository.questions().forEachIndexed { index, question ->
            var answered = false
            question.optionList.forEach { option ->
                Log.i("TAG", "checkNotAnsweredQuestions: ${option.isSelected}")
                answered = answered || option.isSelected
            }
            if (!answered) notAnsweredQuestions.add(index + 1)
        }
        return notAnsweredQuestions
    }

    private fun initNextPreviousButton() {
        binding.apply {
            with(testQuestionsViewPager) {
                testQuestionsNextButton.setOnClickListener {
                    setCurrentItemSmoothly(testQuestionsViewPager.currentItem + 1)
                }
                testQuestionsNextButton.setOnLongClickListener {
                    setCurrentItemSmoothly(questionsSize - 1)
                    true
                }
                testQuestionsPreviousButton.setOnClickListener {
                    setCurrentItemSmoothly(testQuestionsViewPager.currentItem - 1)
                }
                testQuestionsPreviousButton.setOnLongClickListener {
                    setCurrentItemSmoothly(0)
                    true
                }
            }
        }
    }

    private fun buttonVisibilityByPosition(position: Int) {
        binding.apply {
            if (position == questionsSize - 1) {
                testQuestionsNextPreviousGroup.visibility = View.INVISIBLE
                testQuestionsResultButton.visibility = View.VISIBLE
            } else {
                testQuestionsNextPreviousGroup.visibility = View.VISIBLE
                testQuestionsResultButton.visibility = View.GONE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.testQuestionsViewPager.setCurrentItemSmoothly(0)
    }
}
