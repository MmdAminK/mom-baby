package com.app.mombaby.data

import com.app.mombaby.models.adapterModels.Option

object PregnancyTestData {
    val babyCheckOptions = arrayListOf(
        Option(" خط " + "T" + " و " + "C" + " پر رنگ شده است"),
        Option("فقط خط" + " C " + "پر رنگ شده است"),
        Option(" خط" + " T " + "کم رنگ و خط" + " C " + "پر رنگ شده است"),
        Option("فقط خط" + " T " + "پر رنگ شده است")
    )

    val bloodTestOptions = arrayListOf(
        Option("کمتر از 5"),
        Option("بین 5 تا 25"),
        Option("بیشتر از 25"),
    )
}