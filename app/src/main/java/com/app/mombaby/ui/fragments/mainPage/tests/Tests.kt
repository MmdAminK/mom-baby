package com.app.mombaby.ui.fragments.mainPage.tests

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.TestsBinding
import com.app.mombaby.utils.utilities.AppAlertDialog
import com.app.mombaby.utils.utilities.CommonUtils.onBackPressed

class Tests : Fragment() {

    lateinit var binding: TestsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.tests, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressed {
            val dialog = AppAlertDialog(requireContext())
            dialog.setMessage("آیا می خواهید از برنامه خارج شوید؟")
            dialog.setYesButtonAction {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            dialog.setNoButtonAction { }
            dialog.showDialog()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.testsPregnant.setOnClickListener {
            findNavController().navigate(R.id.action_tests_to_pregnancyTestQuestions)
        }
        binding.testsDepressed.setOnClickListener {
            findNavController().navigate(R.id.action_tests_to_depressionTestDescriptions)
        }
        binding.testsStress.setOnClickListener {
            findNavController().navigate(R.id.action_tests_to_anxietyTest)
        }
    }
}