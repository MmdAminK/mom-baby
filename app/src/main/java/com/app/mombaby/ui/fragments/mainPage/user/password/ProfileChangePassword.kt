package com.app.mombaby.ui.fragments.mainPage.user.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.ProfileChangePasswordBinding
import com.app.mombaby.utils.models.DataState
import com.app.mombaby.utils.utilities.CommonUtils.backToPreviousDestination
import com.app.mombaby.utils.utilities.ViewUtils
import com.app.mombaby.utils.utilities.ViewUtils.makeSnackBar
import com.app.mombaby.viewModels.PasswordEvent
import com.app.mombaby.viewModels.PasswordViewModel
import com.app.mombaby.views.AppProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProfileChangePassword : Fragment() {

    lateinit var binding: ProfileChangePasswordBinding
    private val viewModel: PasswordViewModel by viewModels()

    @Inject
    lateinit var appProgressBar: AppProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.profile_change_password, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().backToPreviousDestination(findNavController())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.profileChangePasswordActionBar.onBackClickListener {
            findNavController().popBackStack()
        }

        binding.changePassProfileSubmitButton.setOnClickListener {
            if (!ViewUtils.isEmptyOrNull(binding.password, binding.repeatPasswordEditText)) {
                if (binding.password.text.toString() == binding.repeatPasswordEditText.text.toString()) {
                    viewModel.setStateEvent(PasswordEvent.AddPassword(binding.password.text.toString()))
                    viewModel.addPasswordDataState.observe(viewLifecycleOwner, { dataState ->
                        when (dataState) {
                            is DataState.Error -> {
                                appProgressBar.hide()
                            }
                            DataState.Loading -> {
                                appProgressBar.show()
                            }
                            is DataState.RetrofitError -> {
                                appProgressBar.hide()
                            }
                            is DataState.Success -> {
                                appProgressBar.hide()
                                if (dataState.data?.data == "done") {
                                    Toast.makeText(
                                        requireContext(),
                                        "رمز عبور با موفقیت تغییر کرد",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    findNavController().navigate(R.id.action_profileChangePassword_to_userProfile)
                                }
                            }
                        }
                    })
                } else
                    binding.root.makeSnackBar("تکرار رمز عبور اشتباه می باشد")

            } else
                binding.root.makeSnackBar("لطفا تمامی موارد را کامل کنید")
        }
    }
}