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
import com.app.mombaby.databinding.PasswordLoginBinding
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
class PasswordLogin : Fragment() {

    lateinit var binding: PasswordLoginBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.password_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences.edit().putBoolean(CacheKeys.passwordEnabled, true).apply()
        
        binding.passwordLoginForget.setOnClickListener {
            findNavController().navigate(R.id.action_passwordLogin_to_forgetPassword)
        }

        binding.passwordLoginSubmitButton.setOnClickListener {
            if (!ViewUtils.isEmptyOrNull(binding.password)) {
                viewModel.setStateEvent(PasswordEvent.CheckPassword(binding.password.text.toString()))
                viewModel.checkPasswordDataState.observe(viewLifecycleOwner, { dataState ->
                    when (dataState) {
                        is DataState.Error -> {
                            appProgressBar.hide()
                        }
                        DataState.Loading -> {
                            appProgressBar.show()
                        }
                        is DataState.RetrofitError -> {
                            binding.root.makeSnackBar("رمز عبور اشتباه می باشد")
                            appProgressBar.hide()
                        }
                        is DataState.Success -> {
                            appProgressBar.hide()
                            if (dataState.data?.data == "done")
                                findNavController().navigate(R.id.action_passwordLogin_to_appMainPage)
                        }
                    }
                })
            } else
                binding.root.makeSnackBar("لطفا رمز عبور را وارد کنید")
        }
    }
}