package com.app.mombaby.ui.fragments.loginPages.password

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.SetPasswordBinding
import com.app.mombaby.utils.models.CacheKeys
import com.app.mombaby.utils.models.DataState
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
class SetPassword : Fragment() {

    private val viewModel: PasswordViewModel by viewModels()

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var appProgressBar: AppProgressBar

    lateinit var binding: SetPasswordBinding

    var name: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        name = requireArguments().getBoolean("name")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.set_password, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setPasswordSkip.setOnClickListener {
            sharedPreferences.edit().putBoolean(CacheKeys.passwordEnabled, false).apply()
            findNavController().navigate(
                if (name) R.id.action_setPassword_to_signUp
                else R.id.action_setPassword_to_pregnancyStatus
            )
        }

        binding.setPasswordSubmitButton.setOnClickListener {
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
                                    sharedPreferences.edit()
                                        .putBoolean(CacheKeys.passwordEnabled, true).apply()

                                    findNavController().navigate(
                                        if (name) R.id.action_setPassword_to_signUp
                                        else R.id.action_setPassword_to_pregnancyStatus
                                    )

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