package com.app.mombaby.ui.fragments.mainPage.home

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.mombaby.R
import com.app.mombaby.data.PregnancyGuideTitle
import com.app.mombaby.databinding.PregnancyHomeBinding
import com.app.mombaby.ui.adapters.PregnancyGuideAdapter
import com.app.mombaby.ui.adapters.listeners.OnItemClickListener
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.AppAlertDialog
import com.app.mombaby.utils.utilities.CommonUtils.onBackPressed
import com.app.mombaby.utils.utilities.ViewUtils.showInternetWarningToast
import com.app.mombaby.viewModels.PregnancyHomeEvent
import com.app.mombaby.viewModels.PregnancyHomeViewModel
import com.app.mombaby.views.AppProgressBar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PregnancyHome : Fragment() {

    lateinit var binding: PregnancyHomeBinding
    val viewModel: PregnancyHomeViewModel by viewModels()

    @Inject
    lateinit var progressBar: AppProgressBar

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.pregnancy_home, container, false)
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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val sideAdapter = PregnantSideAdapter()
//        with(binding.pregnancySideHomeRecyclerView) {
//            layoutManager =
//                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
//            adapter = sideAdapter
//        }

        binding.pregnancyChangeStatus.setOnClickListener {
            findNavController().navigate(R.id.userProfile)
        }

        val pregnancyGuideAdapter =
            PregnancyGuideAdapter(PregnancyGuideTitle.pregnancyTitle, requireContext())
        binding.pregnancyHomeRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        pregnancyGuideAdapter.onItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                val bundle = bundleOf(
                    "title" to PregnancyGuideTitle.pregnancyTitle[position],
                    "week" to position + 1
                )
                findNavController().navigate(R.id.action_pregnancyHome_to_showBlogs, bundle)

            }

        })
        binding.pregnancyHomeRecyclerView.adapter = pregnancyGuideAdapter
        binding.pregnancyHomeRecyclerView.isNestedScrollingEnabled = false

        binding.hello.text = "وقت بخیر " + sharedPreferences.getString("userFirstName", "")

        viewModel.setStateEvent(PregnancyHomeEvent.PregnancyInfo)
        viewModel.dataState.observe(viewLifecycleOwner) {
            when (it) {
                is DataState.Error -> {
                    binding.root.showInternetWarningToast()
                    progressBar.hide()
                }
                DataState.Loading -> {
                    progressBar.show()
                }
                is DataState.RetrofitError -> {
                    binding.root.showInternetWarningToast()
                    progressBar.hide()
                }
                is DataState.Success -> {
                    progressBar.hide()
                    val data = it.data
                    Handler(Looper.getMainLooper()).post {
                        binding.pregnancyDialogBack.visibility = View.VISIBLE
                        binding.pregnancyChangeStatus.visibility = View.GONE
                        if (data?.childBirthDate != null) {
                            binding.babyBirthDay.visibility = View.VISIBLE
                            binding.pregnancyHomeRecyclerView.visibility = View.VISIBLE
                            binding.pregnancyHomeWeek.text = data.week

                            data.nini?.let { babyName ->
                                binding.pregnancyDialogBabyNameEditText.setText(babyName)
                                binding.babyBirthDay.text =
                                    data.childBirthDate.toString() + " هفته تا تولد " + babyName
                            } ?: run {
                                binding.babyBirthDay.text =
                                    data.childBirthDate.toString() + " هفته تا تولد" + " نی نی "
                            }
                            setBabyImage(40 - data?.childBirthDate)

                        } else {
                            binding.pregnancyDialogBack.visibility = View.GONE
                            binding.pregnancyShowDialog.performClick()
                        }
                    }

                }
            }
        }

        binding.pregnancyDialogSubmit.setOnClickListener {
            binding.pregnancyDialogCalendar.selectedDate?.let {
                if (it.isBeforeNow || it.isEqualNow) {
                    val date =
                        it.toDateTimeISO().year.toString() + "-" + it.toDateTimeISO().monthOfYear + "-" + it.toDateTimeISO().dayOfMonth
                    viewModel.setStateEvent(
                        PregnancyHomeEvent.UpdatePregnancyInfo(
                            date,
                            binding.pregnancyDialogBabyNameEditText.text.toString()
                        )
                    )
                    viewModel.dataStateUpdateInfo.observe(viewLifecycleOwner) { dataState ->
                        when (dataState) {
                            is DataState.Error -> {
                                binding.root.showInternetWarningToast()
                                progressBar.hide()
                            }
                            DataState.Loading -> {
                                progressBar.show()
                            }
                            is DataState.RetrofitError -> {
                                binding.root.showInternetWarningToast()
                                progressBar.hide()
                            }
                            is DataState.Success -> {
                                progressBar.hide()
                                if (dataState.data?.data == "done") {
                                    viewModel.setStateEvent(PregnancyHomeEvent.PregnancyInfo)
                                    binding.pregnancyHomeMotion.transitionToStart()
                                }
                            }
                        }
                    }
                } else
                    Toast.makeText(
                        requireContext(),
                        "لطفا تاریخ از امروز و قبل از آن را وارد کنید",
                        Toast.LENGTH_SHORT
                    ).show()
            } ?: run {
                Toast.makeText(
                    requireContext(),
                    "لطفا تاریخ را وارد کنید",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setBabyImage(week: Int) {
        with(binding.pregnantHomeBabyImage) {
            when {
                (0 <= week) && (week < 4) -> {
                    setImageDrawable(resources.getDrawable(R.drawable.ic_week1, null))
                }
                (4 <= week) && (week < 8) -> {
                    setImageDrawable(resources.getDrawable(R.drawable.ic_week2, null))
                }
                (8 <= week) && (week < 12) -> {
                    setImageDrawable(resources.getDrawable(R.drawable.ic_week3, null))
                }
                (12 <= week) && (week < 16) -> {
                    setImageDrawable(resources.getDrawable(R.drawable.ic_week4, null))
                }
                (16 <= week) && (week < 20) -> {
                    setImageDrawable(resources.getDrawable(R.drawable.ic_week5, null))
                }
                (20 <= week) && (week < 24) -> {
                    setImageDrawable(resources.getDrawable(R.drawable.ic_week6, null))
                }
                (24 <= week) && (week < 28) -> {
                    setImageDrawable(resources.getDrawable(R.drawable.ic_week7, null))
                }
                (28 <= week) && (week < 32) -> {
                    setImageDrawable(resources.getDrawable(R.drawable.ic_week8, null))
                }
                (32 <= week) && (week < 36) -> {
                    setImageDrawable(resources.getDrawable(R.drawable.ic_week9, null))
                }
                (36 <= week) && (week <= 40) -> {
                    setImageDrawable(resources.getDrawable(R.drawable.ic_week9, null))
                }
                (week > 40) -> {
                    setImageDrawable(resources.getDrawable(R.drawable.ic_after_pregnancy, null))
                    binding.babyBirthDay.visibility = View.GONE
                    binding.pregnancyHomeWeek.text = "قدم نو رسیده مبارک"
                    binding.pregnancyHomeRecyclerView.visibility = View.GONE
                    binding.pregnancyChangeStatus.visibility = View.VISIBLE

                }
            }
        }

    }
}