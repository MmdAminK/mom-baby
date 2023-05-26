package com.app.mombaby.ui.activities

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.app.mombaby.R
import com.app.mombaby.databinding.AppMainPageBinding
import com.app.mombaby.databinding.MenstruationHomeBinding
import com.app.mombaby.utils.models.CacheKeys.homeTapTarget
import com.app.mombaby.utils.models.CacheKeys.testsTapTarget
import com.app.mombaby.utils.models.CacheKeys.toolsTapTarget
import com.app.mombaby.utils.models.CacheKeys.userProfileTapTarget
import com.app.mombaby.utils.utilities.TapTargetBuilder.createTapTarget
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AppMainPage : AppCompatActivity() {
    lateinit var binding: AppMainPageBinding
    lateinit var navController: NavController

    companion object {
        @SuppressLint("StaticFieldLeak")
        @JvmStatic
        lateinit var targetSeq: TapTargetSequence
    }

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    lateinit var menstruationBinding: MenstruationHomeBinding


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.app_main_page)

        binding.apply {
            bottomNavigationView.itemIconTintList = null
            bottomNavigationView.selectedItemId = R.id.home

            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.main_navigation_host_fragment) as NavHostFragment
            navController = navHostFragment.navController
            NavigationUI.setupWithNavController(bottomNavigationView, navController)
        }
        visibilityNavElements(navController)

        sharedPreferences.let {
            targetSeq = TapTargetSequence(this).targets(
                this.createTapTarget(
                    1,
                    binding.bottomNavigationView.findViewById(R.id.home),
                    "خانه",
                    "اینجا طی دوران بارداری، میتونی اطلاعات هفته به هفته ی بارداری و اطلاعات کامل در مورد مادر و جنین رو داشته باشی و در دوران قاعدگی میتونی روزهای پریودت رو ثبت کنی و از زمان تخمک گذاری و زمان پریود بعدیت مطلع بشی."
                ),
                this.createTapTarget(
                    2,
                    binding.bottomNavigationView.findViewById(R.id.tools),
                    "ابزارها",
                    "این قسمت بهت کمک میکنه که روزهای تخمک گذاری، سن بارداری، زمان زایمان، میزان مجاز اضافه وزن و شاخص توده ی بدنیت رو به دست بیاری."
                ),
                this.createTapTarget(
                    3,
                    binding.bottomNavigationView.findViewById(R.id.tests),
                    "تست ها",
                    "اینجا میتونی به تست های اضطراب، افسردگی و تست بارداری دسترسی داشته باشی."
                ),
                this.createTapTarget(
                    4,
                    binding.bottomNavigationView.findViewById(R.id.userProfile),
                    "پروفایل",
                    "اینجا میتونی مشخص کنی که در حال حاضر باردار هستی یا نه و بر همون اساس، راهنمایی های مربوط به دوران بارداری یا قاعدگی رو دریافت کنی."
                )
            ).listener(object : TapTargetSequence.Listener {
                override fun onSequenceFinish() {
                }

                override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {
                    when (lastTarget?.id()) {
                        1 -> it.edit().putBoolean(homeTapTarget, true).apply()
                        2 -> it.edit().putBoolean(toolsTapTarget, true).apply()
                        3 -> it.edit().putBoolean(testsTapTarget, true).apply()
                        4 -> it.edit().putBoolean(userProfileTapTarget, true).apply()
                    }
                }

                override fun onSequenceCanceled(lastTarget: TapTarget?) {
                }

            })
        }


    }

    private fun visibilityNavElements(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.apply {
                when (destination.id) {
                    R.id.tests, R.id.tools, R.id.home, R.id.pregnancyHome,
                    R.id.userProfile, R.id.menstruationHome ->
                        bottomNavigationCardView.visibility = VISIBLE
                    else -> bottomNavigationCardView.visibility = GONE
                }
            }
        }
    }

}

