package com.app.mombaby.ui.fragments.loginPages.password

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.app.mombaby.R
import com.app.mombaby.databinding.ForgetPasswordVerifyBinding
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
class ForgetPasswordVerify : Fragment() {

    lateinit var binding: ForgetPasswordVerifyBinding

    @Inject
    lateinit var appProgressBar: AppProgressBar

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel: PasswordViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.forget_password_verify, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.forgetPasswordVerifySubmitButton.setOnClickListener {
            if (!ViewUtils.isEmptyOrNull(binding.verifyCodeEditText)) {
                viewModel.setStateEvent(PasswordEvent.ForgetPasswordCheck(binding.verifyCodeEditText.text.toString()))
                viewModel.forgetPasswordCheckDataState.observe(viewLifecycleOwner, { dataState ->
                    when (dataState) {
                        is DataState.Error -> {
                            appProgressBar.hide()
                        }
                        DataState.Loading -> {
                            appProgressBar.show()
                        }
                        is DataState.RetrofitError -> {
                            appProgressBar.hide()
                            Log.i(
                                "TAG", "onViewCreated: ${
                                    dataState.errorBody
                                }"
                            )
                            if (!dataState.errorBody.isNullOrEmpty())
                                binding.root.makeSnackBar("کد تایید اشتباه می باشد")
                        }
                        is DataState.Success -> {
                            appProgressBar.hide()
                            if (dataState.data?.data == "done") {
                                sharedPreferences.edit()
                                    .putBoolean(CacheKeys.passwordEnabled, false).apply()
                                findNavController().navigate(R.id.action_forgetPasswordVerify_to_appMainPage)
                            }
                        }
                    }
                })
            } else
                binding.root.makeSnackBar("لطفا کد ارسال شده را وارد کنید")
        }
    }
}